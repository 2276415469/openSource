package com.ning.dubboapi.impl;

import com.ning.dubboapi.NrpcService;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

@DubboService(version = "1",group = "rpc",timeout = 0)
@Component
public class NrpcServiceImpl implements NrpcService {

    @Override
    public String getInfo() {
        return "这里是注册服务返回的信息";
    }

    @Override
    public String getInfo2() {
        return "这里是注册服务返回的信息2";
    }


}
