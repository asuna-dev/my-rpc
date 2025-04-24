package org.zepe.rpc.serializer;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.zepe.rpc.config.RpcConfig;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author zzpus
 * @datetime 2025/4/24 16:48
 * @description
 */
@Slf4j
public class SerializerFactoryTest {
    RpcConfig obj = new RpcConfig();

    @Test
    public void getSerializer() throws IOException {
        log.info("before: {}", obj);
        Serializer serializer = SerializerFactory.getSerializer(SerializerKeys.JDK);
        byte[] bytes = serializer.serialize(obj);
        RpcConfig deObj = serializer.deserialize(bytes, RpcConfig.class);
        log.info("after jdk: {}", deObj);
        Assert.assertEquals(obj, deObj);

        serializer = SerializerFactory.getSerializer(SerializerKeys.KRYO);
        bytes = serializer.serialize(obj);
        deObj = serializer.deserialize(bytes, RpcConfig.class);
        log.info("after kryo: {}", deObj);
        Assert.assertEquals(obj, deObj);
    }
}