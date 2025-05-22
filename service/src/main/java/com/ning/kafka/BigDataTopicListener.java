package com.ning.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BigDataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(BigDataTopicListener.class);


//    @KafkaListener(topics = {"quickstart-events"})
    public void consumer(ConsumerRecord<?, ?> consumerRecord) {
        log.info("收到bigData推送的数据'{}'", consumerRecord.toString());
        //...
        //db.save(consumerRecord);//插入或者更新数据
    }

}

