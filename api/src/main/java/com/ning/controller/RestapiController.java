package com.ning.controller;

import com.ning.rest.RestapiRest;
import com.ning.rest.entity.RestapiParam;
import com.ning.rest.entity.RestapiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 对外提供的restApi
 */
@RestController
@RequestMapping(value = "/openSource/restapi")
public class RestapiController {

    private static final Logger logger = LoggerFactory.getLogger(RestapiController.class);

    @Autowired
    private RestapiRest restapiRest;

    @GetMapping(value = "/query")
    public RestapiResponse<String> query(@RequestParam("id") Long id) {
        RestapiResponse restapiResponse = RestapiResponse.buildSuccess();
        restapiResponse.setData("query");
        return restapiResponse;
    }

    @PostMapping(value = "/submit")
    public RestapiResponse<String> submit(@RequestBody RestapiParam restapiParam) {

            Long id = restapiParam.getId();
        if (id == 2){
            return RestapiResponse.buildFail();
        }
        id++;
        restapiParam.setId(id);
        logger.info("id:-----------------{}--------------------",id);
        RestapiResponse submit = restapiRest.submit(restapiParam);
        return RestapiResponse.buildSuccess();
    }
}
