package org.zepe.rpc.registry;

import org.zepe.rpc.spi.SpiLoader;

/**
 * @author zzpus
 * @datetime 2025/4/24 21:41
 * @description
 */
public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    private RegistryFactory() {
    }

    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}
