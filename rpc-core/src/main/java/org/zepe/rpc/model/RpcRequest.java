package org.zepe.rpc.model;

import lombok.*;

import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/23 19:59
 * @description
 */
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
