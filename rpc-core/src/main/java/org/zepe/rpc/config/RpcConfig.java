package org.zepe.rpc.config;

import lombok.Data;
import org.zepe.rpc.serializer.SerializerKeys;

import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/24 00:14
 * @description
 */
@Data
public class RpcConfig implements Serializable {
    boolean mock = false;
    private String serializer = SerializerKeys.JDK;
    private String name = "default";
    private String version = "0.0.1";
    private String serverHost = "127.0.0.1";
    private Integer serverPort = 9876;

    private RegistryConfig registryConfig = new RegistryConfig();
}
