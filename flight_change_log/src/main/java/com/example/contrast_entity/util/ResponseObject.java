package com.example.contrast_entity.util;

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
    private Object data;
    private String msg;

    public static ResponseObject success(Object data) {
        return new ResponseObject(0, data, "success");
    }

    public static ResponseObject error(String msg) {
        return new ResponseObject(1, null, msg);
    }
}
