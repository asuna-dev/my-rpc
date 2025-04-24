package org.zepe.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.util.PrimitiveIterator;

/**
 * @author zzpus
 * @datetime 2025/4/24 20:22
 * @description
 */
@Data
public class ServiceMetaInfo {
    private String serviceName;
    private String serviceVersion = "0.1";
    private String serviceHost;
    private Integer servicePort;
    private String serviceGroup = "default";

    public String getServiceKey() {
        return StrUtil.format("{}/{}/{}", serviceName, serviceVersion, serviceGroup);
    }

    public String getServiceNodeKey() {
        return StrUtil.format("{}/{}:{}", getServiceKey(), serviceHost, servicePort);
    }

    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return StrUtil.format("http://{}:{}/rpc", serviceHost, servicePort);
        }
        return StrUtil.format("{}:{}/rpc", serviceHost, servicePort);
    }
}
