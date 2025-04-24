package org.zepe.rpc.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzpus
 * @datetime 2025/4/24 00:14
 * @description
 */
@Data
public class RpcConfig {
    boolean mock = false;
    private String name = "ze-rpc";
    private String version = "0.0.1";
    private String serverHost = "127.0.0.1";
    private Integer serverPort = 8080;
}
