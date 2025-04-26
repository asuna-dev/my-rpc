package org.zepe.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.config.RegistryConfig;
import org.zepe.rpc.model.ServiceMetaInfo;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
    Watch watchClient;

    // 提供者: 本地注册的服务缓存，定时续期
    private final Set<ServiceMetaInfo> localRpcServices = new ConcurrentHashSet<>();

    // 消费者: 服务提供者的本地列表缓存
    private final RegistryServiceCache serviceCache = new RegistryServiceCache();
    // 消费者: watch机制监听服务注册时间，维护本地缓存
    private final Set<String> watchingKeys = new ConcurrentHashSet<>();

    private static final String ETCD_ROOT_PATH = "/rpc/";

    private static String getEtcdKey(ServiceMetaInfo serviceMetaInfo) {
        return ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
    }

    private static String getEtcdKey(String svcNodeKey) {
        return ETCD_ROOT_PATH + svcNodeKey;
    }

    private void registerToEtcd(String svcNodeKey, String svcMetaInfo, int timeout)
        throws ExecutionException, InterruptedException {
        log.info("service register: {}", svcNodeKey);

        long leaseId = leaseClient.grant(timeout).get().getID();

        ByteSequence key = ByteSequence.from(getEtcdKey(svcNodeKey), StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(svcMetaInfo, StandardCharsets.UTF_8);

        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
    }

    private void unregister(String svcNodeKey) throws ExecutionException, InterruptedException {
        log.info("service unregister: {}", svcNodeKey);

        ByteSequence key = ByteSequence.from(getEtcdKey(svcNodeKey), StandardCharsets.UTF_8);
        kvClient.delete(key).get();
    }

    @Override
    public void init(RegistryConfig registryConfig) {
        log.info("etcd registry init: {}", registryConfig);

        client = Client.builder().endpoints(registryConfig.getAddress())
            .connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
        leaseClient = client.getLeaseClient();
        watchClient = client.getWatchClient();
        // 定期续约本地服务
        heartbeat(10);
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        localRpcServices.add(serviceMetaInfo);
        registerToEtcd(serviceMetaInfo.getServiceNodeKey(), JSONUtil.toJsonStr(serviceMetaInfo), 30);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) throws Exception {
        localRpcServices.remove(serviceMetaInfo);
        unregister(serviceMetaInfo.getServiceNodeKey());
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        List<ServiceMetaInfo> serviceMetaInfos = serviceCache.read(serviceKey);
        if (!CollUtil.isEmpty(serviceMetaInfos)) {
            return serviceMetaInfos;
        }

        try {
            // 前缀查找
            String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
            GetOption getOption = GetOption.builder().isPrefix(true).build();

            List<KeyValue> kvs =
                kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption).get().getKvs();

            if (CollUtil.isEmpty(kvs)) {
                return CollUtil.newArrayList();
            }

            serviceMetaInfos = kvs.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);

                serviceCache.write(serviceMetaInfo);
                return serviceMetaInfo;
            }).toList();

            // 进行前缀监听，维护消费者本地服务缓存列表
            watch(serviceKey);

            log.info("service discovery: {} {}", serviceKey, serviceMetaInfos.size());
            return serviceMetaInfos;

        } catch (Exception e) {
            throw new RuntimeException("service discovery error: " + serviceKey, e);
        }
    }

    @Override
    public void destroy() {
        log.info("registry node offline");

        //        serviceCache.clearAll();
        for (ServiceMetaInfo svc : localRpcServices) {
            try {
                unregister(svc);
            } catch (Exception e) {
                log.error("service unregister error: {}", svc.getServiceNodeKey(), e);
            }
        }

        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartbeat(int second) {
        String pattern = StrUtil.format("*/{} * * * * *", second);
        CronUtil.schedule(pattern, (Task)() -> {
            for (ServiceMetaInfo svc : localRpcServices) {
                try {
                    register(svc);
                } catch (Exception e) {
                    log.warn("service keep error", e);
                }
            }
        });

        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceKey) {
        boolean newAdd = watchingKeys.add(serviceKey);
        if (!newAdd) {
            return;
        }

        log.info("watch: {}", serviceKey);

        WatchOption watchOption = WatchOption.builder().isPrefix(true).build();
        watchClient.watch(ByteSequence.from(ETCD_ROOT_PATH + serviceKey, StandardCharsets.UTF_8), watchOption,
            watchResponse -> {
                try {
                    for (WatchEvent event : watchResponse.getEvents()) {
                        log.info("watch event: {}", event.getEventType());
                        ServiceMetaInfo serviceMetaInfo;
                        switch (event.getEventType()) {
                            case PUT:
                                // 更新缓存
                                serviceMetaInfo =
                                    JSONUtil.toBean(event.getKeyValue().getValue().toString(StandardCharsets.UTF_8),
                                        ServiceMetaInfo.class);
                                serviceCache.write(serviceMetaInfo);
                                log.info("service cache update: {}", serviceMetaInfo.getServiceNodeKey());
                                break;
                            case DELETE:
                                // 删除缓存
                                String etcdKey = event.getKeyValue().getKey().toString(StandardCharsets.UTF_8);
                                String svcNodeKey = StrUtil.replace(etcdKey, ETCD_ROOT_PATH, "");
                                serviceCache.remove(svcNodeKey);
                                log.info("service cache remove: {}", svcNodeKey);
                                break;
                            default:
                                log.info("unknown event type");
                                break;
                        }
                    }
                } catch (Exception e) {
                    log.error("watch error", e);
                }
            });
    }

}
