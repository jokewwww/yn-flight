package com.flight.config;

import com.flight.prop.Prop;
import com.flight.service.WebSocketService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.ServerEndpointConfig;

/**
 * 自定义配置类
 */
@Component
public class SpringContextHelper extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

    private static volatile BeanFactory context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebSocketService.staRedis = applicationContext.getBean(StringRedisTemplate.class);
        WebSocketService.restTemplate = applicationContext.getBean(RestTemplate.class);
        WebSocketService.prop = applicationContext.getBean(Prop.class);
        SpringContextHelper.context = applicationContext;
    }
}
