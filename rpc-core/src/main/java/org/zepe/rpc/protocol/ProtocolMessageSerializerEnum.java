package org.zepe.rpc.protocol;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * @author zzpus
 * @datetime 2025/4/25 22:39
 * @description
 */
@Getter
public enum ProtocolMessageSerializerEnum {
    JDK("jdk", 0),
    JSON("json", 1),
    KRYO("kryo", 2),
    HESSIAN("hessian", 3);

    private final String key;
    private final int value;

    ProtocolMessageSerializerEnum(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public static ProtocolMessageSerializerEnum getEnumByKey(String key) throws RuntimeException {
        for (ProtocolMessageSerializerEnum e : ProtocolMessageSerializerEnum.values()) {
            if (StrUtil.compareIgnoreCase(e.key, key, true) == 0) {
                return e;
            }
        }
        throw new RuntimeException("undefined enum key");
    }

    public static ProtocolMessageSerializerEnum getEnumByValue(int value) throws RuntimeException {
        for (ProtocolMessageSerializerEnum e : ProtocolMessageSerializerEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new RuntimeException("undefined enum value");
    }
}
