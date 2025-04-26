package org.zepe.rpc.protocol;

import lombok.Getter;

/**
 * @author zzpus
 * @datetime 2025/4/25 22:39
 * @description
 */
@Getter
public enum ProtocolMessageTypeEnum {
    REQUEST(0),
    RESPONSE(1),
    HEARTBEAT(2),
    OTHERS(3);

    private final int key;

    ProtocolMessageTypeEnum(int k) {
        this.key = k;
    }

    public static ProtocolMessageTypeEnum getEnumByKey(int key) throws RuntimeException {
        for (ProtocolMessageTypeEnum e : ProtocolMessageTypeEnum.values()) {
            if (key == e.key) {
                return e;
            }
        }
        throw new RuntimeException("undefined enum value");
    }
}
