package com.ning.arrange.impl;

import com.ning.arrange.WebArrange;
import com.ning.bo.WebBO;
import com.ning.param.WebParam;
import com.ning.service.WebService;
import com.ning.vo.WebVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WebArrangeImpl implements WebArrange {

    @Resource
    private WebService webService;
    @Override
    public WebVO query(WebParam webParam) {
        WebBO webBO = WebBO.builder().build();
        BeanUtils.copyProperties(webParam,webBO);
        WebBO query = webService.query(webBO);
        WebVO result = WebVO.builder().build();
        BeanUtils.copyProperties(query,result);

        return result;
    }
}
