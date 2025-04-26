package org.zepe.rpc.serializer.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.zepe.rpc.config.RpcConfig;
import org.zepe.rpc.serializer.Serializer;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/24 16:17
 * @description
 */
@Slf4j
public class SerializerTest {
    TS obj = new TS();

    @Data
    static final class TS implements Serializable {
        private String key = "123";
        private String value = "abc";
    }

    @Test
    public void testJdk() throws IOException {
        Serializer serializer = new JdkSerializer();

        log.info("before: {}", obj);
        byte[] bytes = serializer.serialize(obj);
        TS deserialize = serializer.deserialize(bytes, TS.class);
        log.info("after: {}", deserialize);
        Assert.assertEquals(obj.key, deserialize.key);
        Assert.assertEquals(obj.value, deserialize.value);

    }

    @Test
    public void testJson() throws IOException {
        Serializer serializer = new JdkSerializer();

        log.info("before: {}", obj);
        byte[] bytes = serializer.serialize(obj);
        TS deserialize = serializer.deserialize(bytes, TS.class);
        log.info("after: {}", deserialize);
        Assert.assertEquals(obj.key, deserialize.key);
        Assert.assertEquals(obj.value, deserialize.value);

    }

    @Test
    public void testKryo() throws IOException {
        Serializer serializer = new KryoSerializer();
        RpcConfig config = new RpcConfig();
        log.info("before: {}", config);
        byte[] bytes = serializer.serialize(config);
        RpcConfig deserialize = serializer.deserialize(bytes, RpcConfig.class);
        log.info("after: {}", deserialize);
        Assert.assertEquals(config.getVersion(), deserialize.getVersion());
    }

    @Test
    public void testHessian() throws IOException {
        Serializer serializer = new HessianSerializer();

        log.info("before: {}", obj);
        byte[] bytes = serializer.serialize(obj);
        TS deserialize = serializer.deserialize(bytes, TS.class);
        log.info("after: {}", deserialize);
        Assert.assertEquals(obj.key, deserialize.key);
        Assert.assertEquals(obj.value, deserialize.value);

    }

}