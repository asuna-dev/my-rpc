//package org.zepe.rpc.registry;
//
//import cn.hutool.core.collection.CollUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.zepe.rpc.config.RegistryConfig;
//import org.zepe.rpc.model.ServiceMetaInfo;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.ScanParams;
//import redis.clients.jedis.ScanResult;
//
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//@Slf4j
//public class JedisRegistry implements Registry {
//    private Jedis jedis;
//    private ObjectMapper objectMapper = new ObjectMapper();
//    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//
//    // 提供者: 本地注册的服务缓存，定时续期
//    private final ConcurrentHashMap<String, ServiceMetaInfo> localRpcServices = new ConcurrentHashMap<>();
//
//    // 消费者: 服务提供者的本地列表缓存
//    private final Map<String, List<ServiceMetaInfo>> serviceCache = new HashMap<>();
//    // 消费者: 订阅机制监听服务注册事件，维护本地缓存
//    private final Set<String> subscribedKeys = ConcurrentHashMap.newKeySet();
//
//    private static final String REDIS_GROUP_PATH = "rpc:";
//
//    private static String getRedisGroupKey(ServiceMetaInfo serviceMetaInfo) {
//        return REDIS_GROUP_PATH + serviceMetaInfo.getServiceKey();
//    }
//
//    private void registerToRedis(String groupKey, String svcMetaInfo, int timeout) {
//        log.info("service register: {}", groupKey);
//
//        // 将服务元数据添加到Set中
//        jedis.sadd(groupKey, svcMetaInfo);
//
//        // 设置过期时间
//        jedis.expire(groupKey, timeout);
//    }
//
//    private void unregister(String groupKey, String svcMetaInfo) {
//        log.info("service unregister: {}", groupKey);
//
//        // 从Set中移除服务元数据
//        jedis.srem(groupKey, svcMetaInfo);
//    }
//
//    @Override
//    public void init(RegistryConfig registryConfig) {
//        log.info("redis registry init: {}", registryConfig);
//
//        jedis = new Jedis(registryConfig.getAddress(), registryConfig.getPort());
//        // 定期续约本地服务
//        heartbeat(10);
//    }
//
//    @Override
//    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
//        localRpcServices.put(serviceMetaInfo.getServiceNodeKey(), serviceMetaInfo);
//        String groupKey = getRedisGroupKey(serviceMetaInfo.getServiceName(), serviceMetaInfo.getVersion(),
//            serviceMetaInfo.getGroup());
//        String svcMetaInfoJson = objectMapper.writeValueAsString(serviceMetaInfo);
//        registerToRedis(groupKey, svcMetaInfoJson, 30);
//    }
//
//    @Override
//    public void unregister(ServiceMetaInfo serviceMetaInfo) throws Exception {
//        localRpcServices.remove(serviceMetaInfo.getServiceNodeKey());
//        String groupKey = getRedisGroupKey(serviceMetaInfo.getServiceName(), serviceMetaInfo.getVersion(),
//            serviceMetaInfo.getGroup());
//        String svcMetaInfoJson = objectMapper.writeValueAsString(serviceMetaInfo);
//        unregister(groupKey, svcMetaInfoJson);
//    }
//
//    @Override
//    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
//        return List.of();
//    }
//
//    @Override
//    public List<ServiceMetaInfo> serviceDiscovery(String serviceName, String version, String group) {
//        String cacheKey = serviceName + ":" + version + ":" + group;
//        List<ServiceMetaInfo> serviceMetaInfos = serviceCache.getOrDefault(cacheKey, CollUtil.newArrayList());
//        if (!CollUtil.isEmpty(serviceMetaInfos)) {
//            return serviceMetaInfos;
//        }
//
//        try {
//            // 获取服务分组中的所有服务元数据
//            String groupKey = getRedisGroupKey(serviceName, version, group);
//            Set<String> serviceMetaInfoJsons = jedis.smembers(groupKey);
//
//            serviceMetaInfos = serviceMetaInfoJsons.stream().map(json -> {
//                try {
//                    return objectMapper.readValue(json, ServiceMetaInfo.class);
//                } catch (Exception e) {
//                    log.error("Failed to parse service meta info JSON: {}", json, e);
//                    return null;
//                }
//            }).filter(metaInfo -> metaInfo != null).collect(Collectors.toList());
//
//            serviceCache.put(cacheKey, serviceMetaInfos);
//
//            // 进行前缀监听，维护消费者本地服务缓存列表
//            subscribe(serviceName, version, group);
//
//            log.info("service discovery: {} {} {} - {}", serviceName, version, group, serviceMetaInfos.size());
//            return serviceMetaInfos;
//
//        } catch (Exception e) {
//            throw new RuntimeException("service discovery error: " + serviceName + ":" + version + ":" + group, e);
//        }
//    }
//
//    @Override
//    public void destroy() {
//        log.info("registry node offline");
//
//        for (Map.Entry<String, ServiceMetaInfo> entry : localRpcServices.entrySet()) {
//            try {
//                String groupKey = getRedisGroupKey(entry.getValue().getServiceName(), entry.getValue().getVersion(),
//                    entry.getValue().getGroup());
//                String svcMetaInfoJson = objectMapper.writeValueAsString(entry.getValue());
//                unregister(groupKey, svcMetaInfoJson);
//            } catch (Exception e) {
//                log.error("service unregister error: {}", entry.getKey(), e);
//            }
//        }
//
//        if (jedis != null) {
//            jedis.close();
//        }
//
//        scheduler.shutdown();
//    }
//
//    @Override
//    public void heartbeat(int second) {
//        scheduler.scheduleAtFixedRate(() -> {
//            for (Map.Entry<String, ServiceMetaInfo> entry : localRpcServices.entrySet()) {
//                try {
//                    String groupKey = getRedisGroupKey(entry.getValue().getServiceName(), entry.getValue().getVersion(),
//                        entry.getValue().getGroup());
//                    String svcMetaInfoJson = objectMapper.writeValueAsString(entry.getValue());
//                    registerToRedis(groupKey, svcMetaInfoJson, 30);
//                } catch (Exception e) {
//                    log.warn("service keep error", e);
//                }
//            }
//        }, 0, second, TimeUnit.SECONDS);
//    }
//
//    @Override
//    public void watch(String serviceKey) {
//
//    }
//
//    @Override
//    public void watch(String serviceName, String version, String group) {
//        boolean newAdd = subscribedKeys.add(serviceName + ":" + version + ":" + group);
//        if (!newAdd) {
//            return;
//        }
//
//        log.info("watch: {}:{}:{}", serviceName, version, group);
//
//        jedis.psubscribe(new RedisPubSubListener(serviceName, version, group) {
//        }, "__keyevent@*" + jedis.getClient().getDb() + "__:*" + serviceName + ":" + version + ":" + group + "*");
//    }
//
//    private abstract class RedisPubSubListener extends redis.clients.jedis.JedisPubSub {
//        protected final String serviceName;
//        protected final String version;
//        protected final String group;
//
//        public RedisPubSubListener(String serviceName, String version, String group) {
//            this.serviceName = serviceName;
//            this.version = version;
//            this.group = group;
//        }
//
//        @Override
//        public void onMessage(String channel, String message) {
//        }
//
//        @Override
//        public void onPMessage(String pattern, String channel, String message) {
//            try {
//                String[] parts = channel.split(":");
//                String action = parts[parts.length - 2]; // set 或 del
//                String key = parts[parts.length - 1];
//
//                String groupKey = getRedisGroupKey(serviceName, version, group);
//
//                switch (action) {
//                    case "set": // 对应 sadd 命令
//                        String value = jedis.get(key);
//                        ServiceMetaInfo serviceMetaInfo = objectMapper.readValue(value, ServiceMetaInfo.class);
//                        serviceCache.computeIfAbsent(serviceName + ":" + version + ":" + group,
//                            k -> CollUtil.newArrayList()).add(serviceMetaInfo);
//                        log.debug("service cache update: {}", serviceMetaInfo.getServiceNodeKey());
//                        break;
//                    case "del": // 对应 srem 命令
//                        String fullKey = getRedisGroupKey(serviceName, version, group) + ":" + key;
//                        String removedValue = jedis.get(fullKey);
//                        if (removedValue != null) {
//                            ServiceMetaInfo removedServiceMetaInfo =
//                                objectMapper.readValue(removedValue, ServiceMetaInfo.class);
//                            serviceCache.getOrDefault(serviceName + ":" + version + ":" + group,
//                                CollUtil.newArrayList()).removeIf(
//                                s -> s.getServiceNodeKey().equals(removedServiceMetaInfo.getServiceNodeKey()));
//                            log.debug("service cache remove: {}", removedServiceMetaInfo.getServiceNodeKey());
//                        }
//                        break;
//                    default:
//                        log.warn("unknown event type");
//                        break;
//                }
//            } catch (Exception e) {
//                log.error("watch error", e);
//            }
//        }
//
//        @Override
//        public void onSubscribe(String channel, int subscribedChannels) {
//        }
//
//        @Override
//        public void onUnsubscribe(String channel, int subscribedChannels) {
//        }
//
//        @Override
//        public void onPSubscribe(String pattern, int subscribedPatterns) {
//        }
//
//        @Override
//        public void onPUnsubscribe(String pattern, int subscribedPatterns) {
//        }
//    }
//}
//
//
//
