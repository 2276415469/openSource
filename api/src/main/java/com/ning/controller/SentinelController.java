package com.ning.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ning.param.WebParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/openSource/sentinel")
public class SentinelController {
    @SentinelResource(value = "sentinelQuery" ,fallback = "execptionProcess")
    @GetMapping(value = "/query")
    public String query(WebParam webParam) {
        throw new RuntimeException();
//        return "sentinel";
    }

    public String execptionProcess() {

        return "已降级处理";
    }
}
