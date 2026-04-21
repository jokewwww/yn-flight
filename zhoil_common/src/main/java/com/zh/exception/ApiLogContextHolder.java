package com.zh.exception;

/**
 *
 */
public class ApiLogContextHolder {

    private static final ThreadLocal<String> exceptionHolder = new ThreadLocal<String>();

    /**
     * 设置异常信息
     *
     * @param exception 异常信息
     */
    public static void setException(String exception) {
        exceptionHolder.set(exception);
    }

    /**
     * 获取异常信息
     */
    public static String getException() {
        return exceptionHolder.get();
    }

    /**
     * 清除异常信息
     */
    public static void clearException() {
        exceptionHolder.remove();
    }
}
