package com.zh.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/30 11:24
 * @Description:
 */
public class ExceptionUtils {

    /**
     * 以JSON格式输出
     * @param response
     */
    public static void responseOutWithJson(HttpServletResponse response,
                                       Object responseObject) {
        //将实体对象转换为JSON Object转换
        String responseJSONObject = JSON.toJSONString(responseObject);
        response.setStatus(401);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJSONObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
