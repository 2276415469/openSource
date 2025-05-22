package com.ning.listen;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class TransactionListenerImpl implements TransactionListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionListenerImpl.class);

    //在这里执行你的本地事务,在消息发送后由mq来调用
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//        正常这里应该是DB操作
        String body = new String(msg.getBody(), StandardCharsets.UTF_8);
        if (Integer.parseInt(body) % 2 == 0){
            logger.info("提交:{}",body);
            return LocalTransactionState.COMMIT_MESSAGE;
        }else if (Integer.parseInt(body)>8){
            logger.info("未知状态:{}",body);
            return LocalTransactionState.UNKNOW;
        }else {
            logger.info("回滚:{}",body);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }
    // 检查本地事务的状态，如果你上面返回了未知，或者mq没有拿到你的本地事务结果 就会自己再查一遍 此时会调用此方法
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        // 获取消息中的标签
        String tags = msg.getTags();
        String body = new String(msg.getBody(), StandardCharsets.UTF_8);
        logger.info("状态回查:{}",body);
            return LocalTransactionState.COMMIT_MESSAGE;
    }
}

