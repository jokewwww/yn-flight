package com.higer.higerservice.configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 10:16
 * @Description:
 */
@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "flightDataSource")
    @Qualifier("flightDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.flight")
    public DataSource flightDataSource() {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.type(DruidDataSource.class);
        DruidDataSource druidDataSource = (DruidDataSource) dataSourceBuilder.build();
        initDBInfo(druidDataSource);
        return druidDataSource;
    }

    @Bean(name = "userDataSource")
    @Qualifier("userDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource userDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.type(DruidDataSource.class);
        DruidDataSource druidDataSource = (DruidDataSource) dataSourceBuilder.build();
        initDBInfo(druidDataSource);
        return druidDataSource;
    }

    private void initDBInfo(DruidDataSource druidDataSource) {
        druidDataSource.setTimeBetweenConnectErrorMillis(600000);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setValidationQuery("SELECT 1");
        druidDataSource.setInitialSize(20);
        druidDataSource.setMinIdle(10);
        druidDataSource.setMaxActive(50);
        druidDataSource.setMaxWait(60000);//获取链接的等待时间
        druidDataSource.setMinEvictableIdleTimeMillis(300000);//配置单个线程的最小生命周期
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestOnBorrow(true);
        druidDataSource.setTestOnReturn(false);
        try {
            List<Filter> filters = new ArrayList<Filter>();
            druidDataSource.setProxyFilters(filters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
