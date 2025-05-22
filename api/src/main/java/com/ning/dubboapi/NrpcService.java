package com.ning.dubboapi;

/**
 * rpc暴露出去的服务，所以应该在api层
 */
public interface NrpcService {
    String getInfo();
    String getInfo2();
}
