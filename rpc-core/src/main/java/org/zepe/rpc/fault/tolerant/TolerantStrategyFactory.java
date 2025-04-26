package org.zepe.rpc.fault.tolerant;

import org.zepe.rpc.fault.tolerant.impl.FailFastTolerantStrategy;
import org.zepe.rpc.spi.SpiLoader;

/**
 * @author zzpus
 * @datetime 2025/4/27 00:14
 * @description
 */
public class TolerantStrategyFactory {
    private static final TolerantStrategy DEFAULT = new FailFastTolerantStrategy();

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    private TolerantStrategyFactory() {
    }

    static public TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstanceOrDefault(TolerantStrategy.class, key, DEFAULT);
    }
}
