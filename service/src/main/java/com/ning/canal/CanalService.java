package com.ning.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

//@Service
public class CanalService {
//    @Autowired
    private CanalConnector canalConnector;
    private static final Logger logger = LoggerFactory.getLogger(CanalService.class);

    public void listen() {
        // 连接到 Canal Server
        canalConnector.connect();
        // 订阅数据库表（支持正则表达式）
        canalConnector.subscribe("bd\\.test");

        while (true) {
            // 获取消息（每次最多获取 1000 条）
            Message message = canalConnector.getWithoutAck(1000);
            long batchId = message.getId();
            int size = message.getEntries().size();

            if (batchId == -1 || size == 0) {
                // 没有新数据，继续等待
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // 处理数据
                for (CanalEntry.Entry entry : message.getEntries()) {
//                    logger.info(entry.toString());
                }

                processData(message.getEntries());
            }

            // 确认消息已处理
            canalConnector.ack(batchId);
        }
    }

    public void processData(List<CanalEntry.Entry> entries) {
        if (CollectionUtils.isEmpty(entries)) {
            return;
        }
        for (CanalEntry.Entry entry : entries) {
            // 基本上就是监听数据变化 事务相关和其他都不管
            if (entry.getEntryType().equals(CanalEntry.EntryType.ROWDATA)) {
                CanalEntry.RowChange rowChange;
                try {
                    rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    throw new RuntimeException("解析 RowChange 失败", e);
                }

                CanalEntry.EventType eventType = rowChange.getEventType();
                // 一条sql会修改多条数据
                //还可以监听到 索引增加删除 类型修改等基本都不处理
                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                    if (eventType == CanalEntry.EventType.INSERT) {
                        logger.info("更新前数据：");
                        printColumn(rowData.getBeforeColumnsList());
                        logger.info("更新后数据：");
                        printColumn(rowData.getAfterColumnsList());
//                      一般新增都是使能某种功能
                    }

                    if (eventType == CanalEntry.EventType.UPDATE) {
                        logger.info("更新前数据：");
                        printColumn(rowData.getBeforeColumnsList());
                        logger.info("更新后数据：");
                        printColumn(rowData.getAfterColumnsList());
//                      一般需要判断修改了那些字段 应该是每个字段有自己的处理逻辑
                    }

                    if (eventType == CanalEntry.EventType.DELETE) {
                        logger.info("删除前数据：");
                        printColumn(rowData.getBeforeColumnsList());
                    }

                    //最后统一发到mq中
                }

            }
        }
    }

    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            logger.info(column.getName() + " : " + column.getValue());
        }
    }
}
