package org.zepe.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzpus
 * @datetime 2025/4/27 00:51
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceLocalRegisterInfo {
    private String serviceName;
    private Object implInstance;
    private String serviceVersion;
}
