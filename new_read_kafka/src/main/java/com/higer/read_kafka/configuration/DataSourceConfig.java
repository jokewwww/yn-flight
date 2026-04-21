package com.higer.read_kafka.configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 10:16
 * @Description:
 */

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource flightDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.type(DruidDataSource.class);
        DruidDataSource druidDataSource = (DruidDataSource) dataSourceBuilder.build();
        initDBInfo(druidDataSource);
        return druidDataSource;
    }


    private void initDBInfo(DruidDataSource druidDataSource) {
        /*druidDataSource.setTimeBetweenConnectErrorMillis(300000);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setValidationQuery("SELECT 1");
        druidDataSource.setTestOnBorrow(true);
        //maxIdleTime#是否在获得连接后检测其可用性
        //spring.datasource.druid.test-on-borrow=false
        //#是否在连接放回连接池后检测其可用性
        //spring.datasource.druid.test-on-return=false
        druidDataSource.setInitialSize(10);
        druidDataSource.setMinIdle(5);
     */

        /*   druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(1800);*//*
        druidDataSource.setMaxActive(350);
        druidDataSource.setMaxWait(60000);//获取链接的等待时间
        druidDataSource.setMinEvictableIdleTimeMillis(25200000);//配置单个线程的最小生命周期
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setLogAbandoned(true);
        druidDataSource.setUseUnfairLock(true);*/
        druidDataSource.setTimeBetweenConnectErrorMillis(300000);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setValidationQuery("SELECT 1");
        druidDataSource.setTestOnBorrow(true);
        //maxIdleTime#是否在获得连接后检测其可用性
        //spring.datasource.druid.test-on-borrow=false
        //#是否在连接放回连接池后检测其可用性
        //spring.datasource.druid.test-on-return=false
        druidDataSource.setInitialSize(10);
        druidDataSource.setMinIdle(5);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(120);
        druidDataSource.setMaxActive(300);
        druidDataSource.setMaxWait(60000);//获取链接的等待时间
        druidDataSource.setMinEvictableIdleTimeMillis(300000);//配置单个线程的最小生命周期
        druidDataSource.setTestOnReturn(false);
        try {
            druidDataSource.setFilters("stat,wall,log4j");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //<!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
       // druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
       /* try {
            List<Filter> filters = new ArrayList<Filter>();
            druidDataSource.setProxyFilters(filters);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
