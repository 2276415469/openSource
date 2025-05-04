package com.ning.controller;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping(value = "/openSource/rocketmq")
public class RocketMQController {



    private final String topic = "demo-topic";
    private final String tag = "demo-tag";
    private  boolean init = false;
    static DefaultMQProducer produceGroup2 = null;
    static int index=0;
        static {
        DefaultMQProducer produceGroup1 = new DefaultMQProducer("produceGroup1");
        produceGroup1.setNamesrvAddr("127.0.0.1:9876");
        try {
            produceGroup1.start();
            produceGroup2 = produceGroup1;
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/mqsend")
    public String sayHello() throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {

        for (int i = 0; i < 10; i++) {

            String m = index+"entry";
            Message message = new Message(topic, tag, m.getBytes(RemotingHelper.DEFAULT_CHARSET));
            index++;
//            改造为都放到一个队列里面  就可以顺序消费了
            SendResult sendResult = produceGroup2.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    return list.get((Integer)o);
                }
            },0);

            System.out.printf("发送消息:%s,结果:%s%n",m,sendResult.getSendStatus());
        }

        return "mq ok";
    }

    @GetMapping("/mqconsume")
    public String mqconsume() throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {

        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("consumeGroup1");
        defaultMQPushConsumer.setNamesrvAddr("127.0.0.1:9876");
        defaultMQPushConsumer.subscribe(topic,"*");

        defaultMQPushConsumer.registerMessageListener(
                new MessageListenerOrderly() {
                    @Override
                    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {

                        for (MessageExt messageExt : list) {
                            try {
                                String s = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
                                final String consumerGroup = defaultMQPushConsumer.getConsumerGroup();
                                System.out.printf("我是消费者:%s,我消费了%s%n",consumerGroup,s);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        return ConsumeOrderlyStatus.SUCCESS;
                    }
                }
        );
        defaultMQPushConsumer.start();



        return "consume ok";
    }
}
