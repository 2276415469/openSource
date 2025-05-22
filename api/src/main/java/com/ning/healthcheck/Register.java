package com.ning.healthcheck;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Register {
//    保存自己的注册信息
    public final static Map<String, Object> map = new ConcurrentHashMap<>();
    public static Map get() {
        return map;
    }
    /**
     * 没有内置安全认证 需要自己包一层
     */
}
