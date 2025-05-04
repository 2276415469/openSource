package com.ning.serviceImpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ning.Do.WebDO;
import com.ning.bo.WebBO;
import com.ning.dao.WebDao;
import com.ning.service.WebService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WebServiceImpl implements WebService {

    @Resource
    private WebDao webDao;

    @DS("master")
    @Override
    public WebBO query(WebBO webBo) {

        QueryWrapper<WebDO> queryWrapper = new QueryWrapper<>();
        List<WebDO> webDOS = webDao.selectList(queryWrapper);
        WebBO result = WebBO.builder().build();
        BeanUtils.copyProperties(CollectionUtils.isEmpty(webDOS) ? null : webDOS.get(0), result);

        return result;
    }
}
