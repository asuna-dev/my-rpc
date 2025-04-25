package org.zepe.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.util.Objects;
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

    static public String getSvcKeyFromSvcNodeKey(String svcNodeKey) {
        return StrUtil.subBefore(svcNodeKey, '/', true);
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        ServiceMetaInfo that = (ServiceMetaInfo)o;
        return Objects.equals(serviceName, that.serviceName) && Objects.equals(serviceVersion,
            that.serviceVersion) && Objects.equals(serviceHost, that.serviceHost) && Objects.equals(servicePort,
            that.servicePort) && Objects.equals(serviceGroup, that.serviceGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, serviceVersion, serviceHost, servicePort, serviceGroup);
    }
}
