package com.higer.higerservice.component;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * 针对内置tomcat进行优化
 */
@Component
public class MyTomcatFactory extends TomcatEmbeddedServletContainerFactory {
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers) {
        //设置端口
        this.setPort(30105);
        this.setUriEncoding(Charset.forName("UTF-8"));
        return super.getEmbeddedServletContainer(initializers);
    }

    protected void customizeConnector(Connector connector) {
        super.customizeConnector(connector);
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        //设置最大连接数
        protocol.setMaxConnections(2000);
        //设置最大线程数
        protocol.setMaxThreads(2000);
        protocol.setConnectionTimeout(30000);
        protocol.setMaxHttpHeaderSize(81920);
    }
}
