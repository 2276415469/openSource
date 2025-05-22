package com.ning.controller;

import com.ning.dubboapi.NrpcService;
import com.ning.param.WebParam;
import com.ning.vo.WebVO;


import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/openSource/dubbo")
public class DubboController {
    private static final Logger logger = LoggerFactory.getLogger(DubboController.class);

    //这里是根据名字去找的 正常应也和其他外部引用一样 引apipom进来 我们这里就简单一些
    @DubboReference(version = "1",group = "rpc",loadbalance = "roundrobin",retries = 2)
    NrpcService nrpcService;
    @GetMapping(value = "/query")
    public String query(WebParam webParam) {
        final String info = nrpcService.getInfo();
        logger.info("rpc call");
        return info;
    }
}
