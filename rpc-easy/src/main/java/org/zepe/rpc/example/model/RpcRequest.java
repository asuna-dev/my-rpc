package org.zepe.rpc.example.model;

import lombok.*;

import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/23 19:59
 * @description
 */
@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    private String serviceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
}
