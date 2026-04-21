package com.zh.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import com.zh.bean.auto.TPermission;
import com.zh.bean.auto.TRolePermission;
import com.zh.bean.auto.TRoleUser;
import com.zh.bean.login.LoginUser;
import com.zh.bean.zuul.SysRequestLog;
import com.zh.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 处理请求参数filter
 */
@Component
public class SignFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(SignFilter.class);

    public static final String TOKEN_KEY = "token";
    public static final String ROLES = "roles:";
    public static final String URLS = "urls:";
    public static final String PERMISSION_UPDATE = "permission_key_update:";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TPermissionService tPermissionService;

    @Autowired
    private TRolePermissionService tRolePermissionService;

    @Autowired
    private TRoleUserService tRoleUserService;

    @Autowired
    private SysRequestLogService sysRequestLogService;

    @Autowired
    private StringRedisTemplate sRedis;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * run：过滤器的具体逻辑。
     */
    @Override
    public Object run() {
        // 获取到request
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setSendZuulResponse(true);
        ctx.setResponseStatusCode(200);
        HttpServletRequest request = ctx.getRequest();
        // 参数提前
        JSONObject json = new JSONObject();
        LoginUser loginUser = null;
        try {
            String method = request.getMethod();
            if ("OPTIONS".equals(method)) {
                return null;
            }
            log.debug("from url ==== " + request.getRequestURL());
            InputStream in = request.getInputStream();
            String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            if (StringUtils.isBlank(body)) {
                body = "{}";
            }
            log.debug("body ========================================== " + body);
            json = JSONObject.parseObject(body);

            String token = getToken(request);
            if ("/base/staffController/staffLogin".equals(request.getRequestURI())
                    || "/base/staffController/PCstaffLogin".equals(request.getRequestURI())
                    || "/base/staffController/PCManagestaffLogin".equals(request.getRequestURI())
                    // 中控机上传油单
                    || "/flight/recpt/upFuelIn".equals(request.getRequestURI())
                    || "/login222/login/do".equals(request.getRequestURI())) {
            } else if (StringUtils.isNotBlank(token) && !"null".equals(token) && !"undefined".equals(token)) {
                loginUser = tokenService.getLoginUser(token);
                if (loginUser != null) {
                    loginUser = checkLoginTime(loginUser);
                } else {
                    toLogin(ctx, HttpStatus.UNAUTHORIZED.value(), null);
                    return null;
                }
                // 刷新权限
            } else if ("/base/update/pem".equals(request.getRequestURI())) {
                updatePem();
                toLogin(ctx, 499, "刷新成功__" + new Date().toLocaleString());
                return null;
                // 登录
            } else {
                toLogin(ctx, HttpStatus.UNAUTHORIZED.value(), null);
                return null;
            }

            if ("GET".equals(method)) {
                request.getParameterMap();
                Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();
                if (requestQueryParams == null) {
                    requestQueryParams = new HashMap<>();
                }
                setLoginUserInValue(loginUser, requestQueryParams, LoginUser.class.getDeclaredFields());
                setLoginUserInValue(loginUser, requestQueryParams, LoginUser.class.getSuperclass().getDeclaredFields());
                ctx.setRequestQueryParams(requestQueryParams);

            } else if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {

                json.put("loginUserIn", loginUser);
                String newBody = json.toJSONString();

                log.debug("newBody ---------------------------------------------------- " + newBody);
                final byte[] reqBodyBytes = newBody.getBytes("utf-8");
                // 重写上下文的HttpServletRequestWrapper
                ctx.setRequest(new HttpServletRequestWrapper(request) {
                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        ServletInputStreamWrapper servletInputStreamWrapper = new ServletInputStreamWrapper(reqBodyBytes);
                        String requestURI = request.getRequestURL().toString();
                        log.debug("requestURI       " + requestURI);
                        return servletInputStreamWrapper;
                    }

                    @Override
                    public int getContentLength() {
                        return reqBodyBytes.length;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return reqBodyBytes.length;
                    }

                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                toLogin(ctx, HttpStatus.PAYMENT_REQUIRED.value(), null);
            } catch (IOException e1) {
                log.error(e1.toString(), e1);
            }
            ctx.set("errInfo", e.getMessage());
            ctx.set("code", "2");
            ctx.set("error.exception", e);
        }finally {
            SysRequestLog sysRequestLog = new SysRequestLog();
            sysRequestLog.setMethod(ctx.getRequest().getRequestURI());
            sysRequestLog.setStatus(ctx.getResponse().getStatus());
            sysRequestLog.setRequestBody(json.toJSONString());
            sysRequestLog.setCreateDate(new Date());
            Object errInfo = ctx.get("errInfo");
            if(null != errInfo){
                sysRequestLog.setException(errInfo.toString());
            }

            if(null != loginUser){
                sysRequestLog.setCreateBy(loginUser.getStaffId());
            }
           // sysRequestLogService.insertLog(sysRequestLog);
        }
              return null;
    }

    private void setLoginUserInValue(LoginUser loginUser, Map<String, List<String>> requestQueryParams,
                                     Field[] declaredFields) throws IllegalAccessException {

        List<String> arrayList = null;
        for (int i = 0; i < declaredFields.length; i++) {
            Field f = declaredFields[i];
            f.setAccessible(true);
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            Object object = f.get(loginUser);
            if (object == null) {
                continue;
            }
            arrayList = new ArrayList<>();
            arrayList.add(object.toString());
            requestQueryParams.put(f.getName(), arrayList);
        }
    }

    private void toLogin(RequestContext ctx, int status, String msg) throws IOException {
        HttpServletResponse response = ctx.getResponse();
        ctx.setResponseStatusCode(status);  // 401：重新登录；402：联系管理员；403：没权限。
        ctx.setSendZuulResponse(false);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setContentType("application/json;charset=UTF-8");
        if (msg != null) {
            ctx.setResponseBody("{\"code\":\"" + msg + "\"}");
        } else {
            ctx.setResponseBody("{\"code\":\"3\"}");
        }
    }

    /**
     * 校验时间<br>
     */
    private LoginUser checkLoginTime(LoginUser loginUser) {
        tokenService.refresh(loginUser);
        return loginUser;
    }

    /**
     * 根据参数或者header获取token
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getParameter(TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            token = request.getHeader(TOKEN_KEY);
        }
        return token;
    }

    /**
     * 是否有权限
     */
    private boolean doPem(HttpServletRequest request, LoginUser loginUser) {

        // 判断是否是管理员权限
        if (sRedis.hasKey("manager:" + loginUser.getStaffId())) {
            return true;
        }

        // 是否存在路径：
        if (sRedis.hasKey(URLS + request.getRequestURI())) {
            if (!sRedis.hasKey(ROLES + loginUser.getStaffId() + ":" + request.getRequestURI())) {
                return false;
            }
            return true;
        } else if ("GET".equals(request.getMethod())) {
            // 去缓存中查找
            String urlDb = findUrlInRedis(request, loginUser);
            if (urlDb == null) {
                return false;
            }
            if (!sRedis.hasKey(ROLES + loginUser.getStaffId() + ":" + urlDb)) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * 更新权限。
     */
    public void updatePem() {
        ValueOperations<String, String> opsForValue = sRedis.opsForValue();
        // 表示缓存了权限。
        opsForValue.set("hasPem:", "");

        Boolean flg = opsForValue.setIfAbsent(PERMISSION_UPDATE, "");
        if (flg) {
            opsForValue.set(PERMISSION_UPDATE, "", 60, TimeUnit.SECONDS);
            // clear
            try {
                Set<String> keys = sRedis.keys(URLS + "*");
                if (keys != null && keys.size() > 0) {
                    sRedis.delete(keys);
                }
                keys = sRedis.keys(ROLES + "*");
                if (keys != null && keys.size() > 0) {
                    sRedis.delete(keys);
                }
            } catch (Exception e) {
            }

            List<TRoleUser> roleUserList = tRoleUserService.selectAll();
            List<TRolePermission> rolePemList = tRolePermissionService.selectAll();
            List<TPermission> pemList = tPermissionService.selectAll();

            // 存入缓存
            Map<Integer, String> pemMap = new HashMap<>();
            for (TPermission pem : pemList) {
                pemMap.put(pem.getId(), pem.getUrl());
                opsForValue.set(URLS + pem.getUrl().toString(), "");
            }
            for (TRoleUser trUser : roleUserList) {
                for (TRolePermission rolePem : rolePemList) {
                    if (trUser.getRoleid().equals(rolePem.getRoleid())) {
                        opsForValue.set(ROLES + trUser.getUserid() + ":" + pemMap.get(rolePem.getPermissionid()), "");
                    }
                }
            }
            sRedis.delete(PERMISSION_UPDATE);
        }
    }

    /**
     * 去缓存中查找路径
     */
    private String findUrlInRedis(HttpServletRequest request, LoginUser loginUser) {
        String urlDb = null;
        String line = "/";
        String[] split = request.getRequestURI().split(line);
        String pre1 = line + split[1] + line + split[2];
        String pre = URLS + pre1;
        String subUri = request.getRequestURI().substring(pre1.length()).replace("/+", "/");
        int length = subUri.length() - subUri.replace("/", "").length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("/{}");
        }
        String subPath = pre + sb.toString();
        Set<String> keys = sRedis.keys(pre + "*");

        if (keys != null && keys.size() != 0) {

            for (String url : keys) {
                if (subPath.equals(url)) {
                    urlDb = pre1 + sb.toString();
                    break;
                }
            }
        }
        return urlDb;
    }
}