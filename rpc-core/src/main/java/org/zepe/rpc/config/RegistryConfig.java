package org.zepe.rpc.config;

import lombok.Data;
import org.zepe.rpc.registry.RegistryKeys;
import org.zepe.rpc.serializer.SerializerKeys;

import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/24 00:14
 * @description
 */
@Data
public class RegistryConfig implements Serializable {

    private String registry = RegistryKeys.ETCD;
    private String address = "http://localhost:2379";
    private String userName;
    private String password;
    // 超时时间/ms
    private Long timeout = 10000L;
}
