package org.zepe.rpc.serializer;

import org.zepe.rpc.serializer.impl.JdkSerializer;
import org.zepe.rpc.spi.SpiLoader;

/**
 * @author zzpus
 * @datetime 2025/4/23 19:52
 * @description
 */
public class SerializerFactory {
    static {
        SpiLoader.load(Serializer.class);
    }

    private SerializerFactory() {
    }

    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static Serializer getSerializer(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }

}
