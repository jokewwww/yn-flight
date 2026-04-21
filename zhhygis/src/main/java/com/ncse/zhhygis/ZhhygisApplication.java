package com.ncse.zhhygis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@ServletComponentScan
@EnableCaching//开启redis缓存
public class ZhhygisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhhygisApplication.class, args);
		/*ApplicationContext app = SpringApplication.run(ZhhygisApplication.class, args);
	    SpringContextUtil.setApplicationContext(app);*/
    }


}