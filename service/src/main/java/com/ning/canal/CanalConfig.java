package com.ning.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;

import java.net.InetSocketAddress;

//@Configuration
public class CanalConfig {
//    @Bean
    public CanalConnector canalConnector() {
        // 创建 Canal 连接器 一般canal都是监听所有表 然后由监听器再去分流监听哪些表
        return CanalConnectors.newSingleConnector(
                new InetSocketAddress("localhost", 11111), // Canal Server 地址
                "example",        // instance 名称
                "canal",          // 用户名
                "canal"           // 密码
        );
    }
}
