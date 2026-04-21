package com.higer.flightinfo.component;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@AllArgsConstructor
@Data
public class TFlightFutureCallback<T> implements ListenableFutureCallback<T> {

    private T t;

    @Override
    public void onFailure(Throwable throwable) {
        log.error(String.format("保存失败：%s",JSONObject.toJSONString(t)),throwable);
    }

    @Override
    public void onSuccess(T t) {
        log.info("保存成功：{}",JSONObject.toJSONString(t));
    }
}
