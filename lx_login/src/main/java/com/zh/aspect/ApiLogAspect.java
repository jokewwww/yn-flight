package com.zh.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.zh.annotation.TaskLogs;
import com.zh.bean.flight.MyFlightTask;
import com.zh.bean.flight.MyFuelRecpt;
import com.zh.bean.flight.MyTask;
import com.zh.bean.flight.SysLog;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyStaffVehi;
import com.zh.exception.ApiLogContextHolder;
import com.zh.service.SysLogService;
import com.zh.thread.SysLogThread;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * API接口日志记录<br>
 * 第一层日志拦截
 */
@Order(1)
@Aspect
@Component
public class ApiLogAspect {

    private final static Logger log = LoggerFactory.getLogger(ApiLogAspect.class);

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private SysLogService sysLogService;

    @Pointcut(value = "@annotation(com.zh.annotation.TaskLogs)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        // 获取拦截的方法名
        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Class<? extends Object> class1 = target.getClass();
        Method currentMethod = class1.getMethod(msig.getName(),
                msig.getParameterTypes());
        // 方法名
        String methodName = currentMethod.getName();
        TaskLogs taskLogs = currentMethod.getAnnotation(TaskLogs.class);

        // 获取拦截方法的参数
        String className = class1.getSimpleName();
        Object[] params = point.getArgs();

        SysLog sysLog = new SysLog();

        Arrays.stream(params).forEach(obj -> {
            if (obj instanceof MyFlightTask) {
                MyFlightTask request = (MyFlightTask) obj;
                sysLog.setTaskId(request.getTaskId());
                sysLog.setFlNo(request.getTaskFuelRecptNo());
            }
            if (obj instanceof MyTask) {
                MyTask request = (MyTask) obj;
                sysLog.setTaskId(request.getTaskId());
                sysLog.setFlNo(request.getTaskFuelRecptNo());
            }
            if (obj instanceof MyFuelRecpt) {
                MyFuelRecpt request = (MyFuelRecpt) obj;
                sysLog.setTaskId(request.getTaskId());
                sysLog.setFlNo(request.getFlrcNo());
            }
            if (obj instanceof MyStaff) {
                MyStaff request = (MyStaff) obj;
                if (ObjectUtil.isNull(request.getLoginUserIn())) {
                    sysLog.setCreateBy(request.getStaffId());
                } else {
                    sysLog.setCreateBy(request.getLoginUserIn().getStaffId());
                }
            }
            if (obj instanceof MyStaffVehi) {
                MyStaffVehi request = (MyStaffVehi) obj;
                sysLog.setCreateBy(request.getSfvhStaffId());
            }
        });

        // 执行业务
        Object result = point.proceed();

        try {

            sysLog.setTitle(taskLogs.value());
            sysLog.setMethod(className + "." + methodName);
            sysLog.setIp(ServletUtil.getClientIP(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
            sysLog.setRequestBody(JSON.toJSONString(params));
            sysLog.setResponseBody(JSON.toJSONString(result));
            sysLog.setCreateDate(new Date());
            sysLog.setException(ApiLogContextHolder.getException());
            // 保存系统日志
            executorService.execute(new SysLogThread(sysLog, sysLogService));

        } catch (Exception e) {
            log.error("保存系统日志异常", e);
        } finally {
            ApiLogContextHolder.clearException();
        }

        return result;
    }

}