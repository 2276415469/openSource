package com.ning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping(value = "/openSource/redis")
public class RedisController {
    final ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(10, 10, 1000, TimeUnit.DAYS,  new LinkedBlockingQueue<Runnable>());
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
    public static JedisPool jedisPool=null;
    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        // 设置最大连接数
        poolConfig.setMaxTotal(10);
        // 设置最大空闲连接数
        poolConfig.setMaxIdle(5);
        // 设置最小空闲连接数
        poolConfig.setMinIdle(1);
        // 设置等待可用连接的最大时间（毫秒），超时则抛出异常
        poolConfig.setMaxWaitMillis(3000);
        // 在获取连接的时候检查有效性, 默认false
        poolConfig.setTestOnBorrow(true);
        // 在将连接放回池中前检查有效性, 默认false
        poolConfig.setTestOnReturn(false);
        // 周期性地检查无效对象, 单位为毫秒
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        // 最小空闲时间，当达到最小空闲时间时会被释放
        poolConfig.setMinEvictableIdleTimeMillis(60000);

        // 创建JedisPool实例
        jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379, 2000, "123456");
    }

    @GetMapping("/test")
    public String sayHello() {



        // 使用try-with-resources确保资源正确关闭
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del("list");

            for (int i = 0; i < 1000; i++) {

                jedis.lpush("list",i+"");
            }

            threadPoolExecutor.execute(()->{
                Jedis jedis1 = jedisPool.getResource();
                for (int i = 0; i < 1000; i++) {
                    String list = jedis1.lpop("list");
                    if (list==null){
                        break;
                    }
                    System.out.println("one+:" + list);
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            threadPoolExecutor.execute(()->{
                Jedis jedis1 = jedisPool.getResource();
                for (int i = 0; i < 1000; i++) {
                    String list = jedis1.lpop("list");
                    if (list==null){
                        break;
                    }
                    System.out.println("two+:" + list);
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            threadPoolExecutor.execute(()->{
                Jedis jedis1 = jedisPool.getResource();
                for (int i = 0; i < 1000; i++) {
                    String list = jedis1.lpop("list");
                    if (list==null){
                        break;
                    }
                    System.out.println("three+:" + list);
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接池（通常在应用关闭时调用）
//            if (jedisPool != null) {
//                jedisPool.close();
//            }
        }






        return "sss";
    }

    @GetMapping("/exelua")
    public String exelua(){
        try (Jedis jedis = jedisPool.getResource()) {
            // 定义 Lua 脚本
            String luaScript =
                    "local val = redis.call('GET', KEYS[1]) " +
                            "if not val then return nil end " +
                            "val = tonumber(val) " +
                            "if not val then return nil end " +
                            "return redis.call('INCRBY', KEYS[1], ARGV[1])";

            // 执行 Lua 脚本
            Object result = jedis.eval(luaScript, 1, "mykey", "5");

            // 输出结果
            logger.info("Result of script execution: " + result);

            String sha1 = jedis.scriptLoad(luaScript);
            Object resultUsingSha = jedis.evalsha(sha1, 1, "mykey", "5");
            logger.info("Result using SHA1: " + resultUsingSha);
        }
        return "success";
    }
}
