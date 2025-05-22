package com.ning.controller;

import com.ning.mqtt.MqttUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/openSource/mqtt")
@Slf4j
public class MqttController {

    @Autowired
    MqttClient client;

    @Autowired
    MqttUtil mqttUtil;

    @GetMapping("/send")
    public String send() {
        try {
            for (int i = 0; i < 3; i++) {
                mqttUtil.publish("test", "消息hello" + i);
                log.info("发送成功：{}", i);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }
}
