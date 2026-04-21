package com.zh.aspect;

import com.zh.bean.ReturnMsg;
import com.zh.constant.Constant;
import com.zh.exception.ApiLogContextHolder;
import com.zh.exception.CustomException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * API接口应用异常拦截<br>
 * 第二层异常拦截
 */
@Order(2)
@Aspect
@Component
public class ApiExceptionAspect {

    @Pointcut(value = "@annotation(com.zh.annotation.TaskLogs)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        ReturnMsg msg = ReturnMsg.getInstance(Constant.CODE_ERR, "请联系管理员！", null);
        // 执行业务
        Object result = null;

        try {
            result = point.proceed();
        } catch (CustomException e) {
            saveErrorLog(e);
            Field returnMsg = CustomException.class.getDeclaredField("returnMsg");
            returnMsg.setAccessible(true);
            ReturnMsg retMsg = (ReturnMsg) returnMsg.get(e);
            msg.setData(retMsg.getData());
            msg.setErrInfo(retMsg.getErrInfo());
            msg.setCode(retMsg.getCode());
            msg.setErrorCode(retMsg.getErrorCode());
            return msg;
        } catch (IllegalArgumentException e) {
            saveErrorLog(e);
            msg = ReturnMsg.getInstanceNGz(e.getMessage(), null);
            return msg;
        } catch (Exception e) {
            saveErrorLog(e);
            e.printStackTrace();
            return msg;
        }

        return result;

    }

    private void saveErrorLog(Exception e) {
        ApiLogContextHolder.setException(e.getMessage());
    }

}