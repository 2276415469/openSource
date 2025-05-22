package com.ning.controller;

import com.ning.param.WebParam;
import com.ning.vo.WebVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping(value = "/openSource/nacos")
public class NacosController {
    private static final Logger logger = LoggerFactory.getLogger(NacosController.class);
    @Value("${test.one}")
    private String config;
    @GetMapping(value = "/query")
    public String query() {
        logger.info("配置内容{}",config);

        return config;
    }
}
