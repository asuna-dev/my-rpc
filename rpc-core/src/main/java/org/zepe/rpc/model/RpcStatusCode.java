package org.zepe.rpc.model;

import lombok.Getter;

/**
 * @author zzpus
 * @datetime 2025/4/23 19:59
 * @description
 */
@Getter
public enum RpcStatusCode {
    SUCCESS(200, "成功"),

    BAD_REQUEST(400, "请求参数错误"),

    UNAUTHORIZED(401, "未认证或认证过期"),

    FORBIDDEN(403, "无权限访问"),

    NOT_FOUND(404, "资源不存在"),

    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    // 业务相关错误码（可以定义范围，比如1000-1999）
    USER_ALREADY_EXISTS(1001, "用户已存在"),

    USER_NOT_FOUND(1002, "用户不存在"),

    INVALID_PASSWORD(1003, "密码无效");

    private final int code;
    private final String message;

    RpcStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
