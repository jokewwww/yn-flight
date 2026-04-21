package com.higer.higerservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/18 15:29
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject {

    private Integer code;//0 成功  1 失败
    private byte[] data;
    private String str_data;
    private String msg;

    public static ResponseObject success(byte[] data, String str_data, String msg) {
        return new ResponseObject(0, data, str_data, "");
    }

    public static ResponseObject error(String msg, Integer code) {
        return new ResponseObject(code, null, null, msg);
    }
}
