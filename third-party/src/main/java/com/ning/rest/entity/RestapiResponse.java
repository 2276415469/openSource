package com.ning.rest.entity;

import lombok.Data;

@Data
public class RestapiResponse<T> {
    private Integer status;
    private String message;
    private T data;

    public static RestapiResponse buildSuccess(){
        RestapiResponse restapiResponse = new RestapiResponse();
        restapiResponse.setStatus(200);
        restapiResponse.setMessage("success");
        return restapiResponse;
    }
    public static RestapiResponse buildFail(){
        RestapiResponse restapiResponse = new RestapiResponse();
        restapiResponse.setStatus(-1);
        restapiResponse.setMessage("fail");
        return restapiResponse;
    }
}
