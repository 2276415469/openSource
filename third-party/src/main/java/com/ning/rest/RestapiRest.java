package com.ning.rest;


import com.ning.rest.entity.RestapiParam;
import com.ning.rest.entity.RestapiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "open-source")
public interface RestapiRest {

    /**
     * 正常应该是 把对方的api包拿过来 直接用
     */
    @GetMapping(value = "/openSource/restapi/query")
    RestapiResponse query(RestapiParam restapiParam);

    @PostMapping(value = "/openSource/restapi/submit")
    RestapiResponse submit(RestapiParam restapiParam);
}
