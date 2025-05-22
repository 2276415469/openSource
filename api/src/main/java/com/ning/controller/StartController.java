package com.ning.controller;

import com.ning.consistency.ConsistencyStarter;
import com.ning.param.WebParam;
import com.ning.vo.WebVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/openSource/start")
public class StartController {
    @Resource
    ConsistencyStarter consistencyStarter;
    @GetMapping(value = "/query")
    public String query(WebParam webParam) {
        final String s = consistencyStarter.healthCheck();
        return s;
    }
}
