package com.ning.mqtt;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties("mqtt")
@Data
public class MqttConfig {

    String host;
    String clientId;
    String topic;
    String username;
    String password;
    Integer timeout;
    Integer keepalive;

    // MQTT客户端的配置类，可以设置mqtt服务器的账号和密码,具体连接到哪里是client决定的
    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        // 设置是否自动重连
        options.setAutomaticReconnect(true);
        // false 保持会话不被清理自动重连后才能收到订阅的主题消息（包括离线时发布的消息）
        options.setCleanSession(true);
        options.setConnectionTimeout(timeout);
        options.setKeepAliveInterval(keepalive);
        return options;
    }


    // MqttClient 类，MQTT的客户端类，可以去连接MQTT服务器
    @Bean
    public MqttClient mqttClient(MqttConnectOptions mqttConnectOptions) {
        try {
            MqttClient client = new MqttClient(host, clientId);


            // 回调对象，监听消息的获取，采用的接口回调，可以获取对应订阅到的消息
            client.setCallback(new MessageCallback(client, this.topic, mqttConnectOptions));
            // 连接
            client.connect(mqttConnectOptions());


            return client;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("mqtt 连接异常");
        }
    }
}