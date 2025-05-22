package com.ning.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

/**
 * consumer 消费者，对收到的消息进行处理
 */
//@Component
@Slf4j
public class MessageCallback implements MqttCallbackExtended {


    private MqttClient client;


    private String topic;


    private MqttConnectOptions mqttConnectOptions;


    public MessageCallback() {
    }

    public MessageCallback(MqttClient mqttClient, String topic, MqttConnectOptions mqttConnectOptions) {
        this.client = mqttClient;
        this.topic = topic;
        this.mqttConnectOptions = mqttConnectOptions;
    }

    // 在客户端连接断开时触发
    @Override
    public void connectionLost(Throwable throwable) {


        if (client != null && !client.isConnected()) {
            log.info("{}, 连接断开，正在reconnect....", client.getClientId());
            try {
                client.reconnect();
                // client.connect(this.mqttConnectOptions);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            log.info("未知异常，连接断开");
        }


    }
    // 在客户端与服务器连接成功时触发 上线以后才真正去订阅了topic
    @Override
    public void connectComplete(boolean b, String url) {
        log.info("{} 上线了{} {}", client.getClientId(), b, url);
        try {
            client.subscribe(this.topic, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }
    // 在客户端收到订阅的消息时触发
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("接收消息主题 : " + topic);
        log.info("接收消息内容 : " + new String(message.getPayload()));
        String msg = new String(message.getPayload());
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            String clientId = String.valueOf(jsonObject.get("clientid"));
            if (topic.endsWith("disconnected")) {
                log.info("设备{}已掉线", clientId);
            } else if (topic.endsWith("connected")) {
                log.info("设备{}已上线", clientId);
            } else {
                log.info("其他主题的消息");
            }
        } catch (JSONException e) {
            log.error("JSON Format Parsing Exception : {}", msg);
        }


    }
    // 在客户端发送消息至服务器成功时触发
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete---------" + token.isComplete());
    }
}
