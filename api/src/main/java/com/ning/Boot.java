package com.ning;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication()
//@MapperScan("com.ning.dao")
//@MapperScan(basePackages = "com.ning.dao", sqlSessionFactoryRef = "maSqlSessionFactory")
@EnableEurekaServer
@EnableFeignClients
//@EnableApolloConfig
public class Boot {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(Boot.class, args);
    }
}
