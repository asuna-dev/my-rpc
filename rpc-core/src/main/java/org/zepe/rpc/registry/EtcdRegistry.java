package org.zepe.rpc.registry;

import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.config.RegistryConfig;
import org.zepe.rpc.model.ServiceMetaInfo;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzpus
 * @datetime 2025/4/24 20:12
 * @description
 */
@Slf4j
public class EtcdRegistry implements Registry {
    private Client client;
    private KV kvClient;
    Lease leaseClient;

    private static final String ETCD_ROOT_PATH = "/rpc/";

    private static ByteSequence getEtcdKey(ServiceMetaInfo serviceMetaInfo) {
        return ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(), StandardCharsets.UTF_8);
    }

    @Override
    public void init(RegistryConfig registryConfig) {
        log.info("etcd registry init: {}", registryConfig);
        client = Client.builder().endpoints(registryConfig.getAddress())
            .connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
        leaseClient = client.getLeaseClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        log.info("register: {}", serviceMetaInfo.getServiceNodeKey());

        long leaseId = leaseClient.grant(300).get().getID();

        ByteSequence key = getEtcdKey(serviceMetaInfo);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) throws Exception {
        log.info("unregister: {}", serviceMetaInfo.getServiceNodeKey());
        ByteSequence key = getEtcdKey(serviceMetaInfo);
        kvClient.delete(key).get();
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        GetOption getOption = GetOption.builder().isPrefix(true).build();
        try {
            List<KeyValue> kvs =
                kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption).get().getKvs();
            return kvs.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("get service list error", e);
        }
    }

    @Override
    public void destroy() {
        log.info("registry node offline");
        if (leaseClient != null) {
            leaseClient.close();
        }
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

}
