package org.zepe.rpc.model;

import lombok.*;

import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/23 19:59
 * @description
 */
@Data
@NoArgsConstructor
public class RpcResponse implements Serializable {
    private Object data;
    private Class<?> dataType;
    private int statusCode;
    private String message;
    private Exception exception;

    public static RpcResponse success(Object data, Class<?> dataType) {
        return new RpcResponse(RpcStatusCode.SUCCESS, data, dataType);
    }

    public static RpcResponse success(Object data, Class<?> dataType, String message) {
        return new RpcResponse(RpcStatusCode.SUCCESS.getCode(), message, data, dataType);
    }

    public static RpcResponse failure(RpcStatusCode RpcStatusCode) {
        return new RpcResponse(RpcStatusCode);
    }

    public static RpcResponse failure(RpcStatusCode RpcStatusCode, Exception e) {
        RpcResponse rpcResponse = new RpcResponse(RpcStatusCode.getCode(), RpcStatusCode.getMessage(), null, null);
        rpcResponse.setException(e);
        return rpcResponse;
    }

    public static RpcResponse failure(RpcStatusCode RpcStatusCode, Exception e, String message) {
        RpcResponse rpcResponse = new RpcResponse(RpcStatusCode.getCode(), message, null, null);
        rpcResponse.setException(e);
        return rpcResponse;
    }

    public static RpcResponse failure(RpcStatusCode RpcStatusCode, String message) {
        return new RpcResponse(RpcStatusCode.getCode(), message, null, null);
    }

    private RpcResponse(RpcStatusCode RpcStatusCode) {
        this.statusCode = RpcStatusCode.getCode();
        this.message = RpcStatusCode.getMessage();
    }

    private RpcResponse(RpcStatusCode RpcStatusCode, Object data, Class<?> dataType) {
        this.statusCode = RpcStatusCode.getCode();
        this.message = RpcStatusCode.getMessage();
        this.data = data;
        this.dataType = dataType;
    }

    private RpcResponse(int code, String message, Object data, Class<?> dataType) {
        this.statusCode = code;
        this.message = message;
        this.data = data;
        this.dataType = dataType;
    }
}
