package org.zepe.rpc.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.zepe.rpc.registry.RegistryKeys;

import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/24 00:14
 * @description 注册中心配置
 */
@Data
public class RegistryConfig implements Serializable {

    private String registry = RegistryKeys.ETCD;

    private String host = "localhost";
    private Integer port = 2379;

    private String userName;
    private String password;
    // 超时时间/ms
    private Long timeout = 10000L;

    public String getAddress() {
        return StrUtil.format("http://{}:{}", host, port);
    }
}
