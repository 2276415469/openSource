package com.ning.controller;

import com.ning.entity.Messageinf;
import com.ning.jpa.MessageinfService;
import com.ning.param.WebParam;
import com.ning.vo.WebVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/openSource/jpa")
public class JpaController {
    @Autowired
    MessageinfService messageinfService;

    @GetMapping(value = "/query")
    public Messageinf query(WebParam webParam) {
        final Messageinf query = messageinfService.query();
        return query;
    }
}
