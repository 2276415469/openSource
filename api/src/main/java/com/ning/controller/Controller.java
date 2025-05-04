package com.ning.controller;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.ning.arrange.WebArrange;
import com.ning.healthcheck.Register;
import com.ning.param.WebParam;
import com.ning.vo.WebVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

@RestController
@RequestMapping(value = "/openSource/")
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    @Autowired
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String applicationName;
//    @Value("${fugai}")
    private String myProperty;
    @Resource
    private WebArrange webArrange;
    @GetMapping(value = "/query")
    public WebVO query(WebParam webParam) {
        logger.info("call query");
        WebVO query = webArrange.query(webParam);
        return query;
    }
    @GetMapping(value = "/query2")
    public String query2(WebParam webParam) {
        logger.info("call query");
        return "";
    }

    @GetMapping(value = "/health")
    public String health() {
        logger.info("call health");
        // 获取当前实例的注册信息
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka(applicationName, false);
        if (instanceInfo != null && instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP) {
            Register.get().put("instanceInfo",instanceInfo);
            return  "Successfully registered with Eureka";
        } else {
            return "Failed to register with Eureka";
        }
    }

    @GetMapping(value = "/down")
    public String down() {
        eurekaClient.shutdown();
        return "down";
    }

    @GetMapping(value = "/getconfig")
    public String getconfig() {
        Config appConfig = ConfigService.getAppConfig();
        Set<String> propertyNames = appConfig.getPropertyNames();
        for (String propertyName : propertyNames) {

            logger.info("已加载配置:{},{}",propertyName,appConfig.getProperty(propertyName,"weikong"));
        }

        appConfig = ConfigService.getConfig("TEST1.common");
        propertyNames = appConfig.getPropertyNames();
        for (String propertyName : propertyNames) {

            logger.info("已加载配置:{},{}",propertyName,appConfig.getProperty(propertyName,"weikong"));
        }

        logger.info(myProperty);


        return myProperty;
    }
}
