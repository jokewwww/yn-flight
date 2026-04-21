package com.higer.higerservice.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/5 22:12
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "obj")
@Data
public class ObjectProperties {
    private String myUrl;
    private String myProt;
}
