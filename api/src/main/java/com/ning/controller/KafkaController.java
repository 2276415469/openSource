package com.ning.controller;

import com.ning.param.WebParam;
import com.ning.vo.WebVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/openSource/kafka")
public class KafkaController {
    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);
    @GetMapping(value = "/create")
    public String query() {


        return "success";
    }
}
