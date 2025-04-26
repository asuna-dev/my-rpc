package org.zepe.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zepe.rpc.constant.RpcConstant;

import java.util.Objects;

/**
 * @author zzpus
 * @datetime 2025/4/24 20:22
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceMetaInfo {
    private String serviceName;
    // 服务版本，一台机器的服务同属于一个分组，但不同服务有各自的版本
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;
    private String serviceGroup = RpcConstant.DEFAULT_SERVICE_GROUP;

    private String serviceHost;
    private Integer servicePort;

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

}
