package com.ning.jpa.impl;

import com.ning.entity.Messageinf;
import com.ning.jpa.MessageinfService;
import com.ning.repository.MessageinfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class MessageinfServiceImpl implements MessageinfService {

    @Autowired
    MessageinfDao messageinfDao;
    @Override
    public Messageinf query() {
        Optional<Messageinf> byId = messageinfDao.findById(1L);
        if (byId.isPresent()){
            return byId.get();
        }
        return null;
    }
}
