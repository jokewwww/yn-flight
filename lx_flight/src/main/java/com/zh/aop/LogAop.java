//package com.zh.aop;
//
//import com.alibaba.fastjson.JSON;
//import com.zh.annotation.OperationLogs;
//import com.zh.bean.OperationLog;
//import com.zh.bean.login.MyStaff;
//import com.zh.bean.login.TStaff;
//import com.zh.service.LogOperationService;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//import java.lang.reflect.Method;
//import java.util.Date;
//import java.util.Optional;
//
/// *
//    xiuhongxin
// */
//@Aspect
//@Configuration
//public class LogAop {
//    private static Logger logger = LoggerFactory.getLogger(LogAop.class);
//
//    @Autowired
//    private LogOperationService logOperationService;
//
//    @Pointcut("@annotation( com.zh.annotation.OperationLogs)")
//    public void excudeSaveOperLog() {
//        System.out.println("AOPOperateLog");
//    }
//
//    @Around("excudeSaveOperLog()")
//    public Object saveOperLog(ProceedingJoinPoint joinPoint) throws Exception {
//        //保存日志
//        OperationLog operLog = new OperationLog();
//        Object result ;
//        try{
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            Class pointClass = joinPoint.getTarget().getClass();
//            Method pointMethod = signature.getMethod();
//            OperationLogs classCont = (OperationLogs) pointClass.getAnnotation(OperationLogs.class);
//            OperationLogs methodCont = pointMethod.getAnnotation(OperationLogs.class);
//            String content="";
//            Object[] args = joinPoint.getArgs();
//            for (Object obj : args) {
//                if (obj instanceof MyStaff) {
//                    MyStaff myStaff=(MyStaff)obj;
//                    operLog.setStaffId(myStaff.getLoginUserIn().getStaffId());
//                    operLog.setStaffName(myStaff.getLoginUserIn().getStaffName());
//                }
//                if(obj instanceof TStaff){
//                    TStaff tStaff=(TStaff)obj;
//                    operLog.setStaffId(tStaff.getLoginUserIn().getStaffId());
//                    operLog.setStaffName(tStaff.getLoginUserIn().getStaffName());
//                }
//                content+=JSON.toJSONString(obj);
//            }
//            operLog.setContent(content);
//            operLog.setModuleName(Optional.ofNullable(null != classCont?classCont.value():null).orElse(""));
//            operLog.setActionName(Optional.ofNullable(methodCont.toString()).orElse(""));
//            operLog.setCreateDate(new Date());
//        }catch (Exception e){
//            logger.info("操作日志错误 :" + e);
//        }
//        try {
//            result = joinPoint.proceed();
//            operLog.setSucceed(1);//成功
//            logOperationService.logOperationInsert(operLog);
//        } catch (Throwable e) {
//            operLog.setSucceed(0);//失败
//            operLog.setFailReason(String.valueOf(e.getMessage()));
//            logOperationService.logOperationInsert(operLog);
//            // 异常通知
//            logger.info("这个的异常是 :" + e);
//            throw new Exception(e);
//        }
//        return result;
//    }
//}
