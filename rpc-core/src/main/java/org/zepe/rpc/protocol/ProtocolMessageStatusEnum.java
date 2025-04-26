package org.zepe.rpc.protocol;

import lombok.Getter;

/**
 * @author zzpus
 * @datetime 2025/4/25 22:26
 * @description
 */
@Getter
public enum ProtocolMessageStatusEnum {
    OK("ok", 200),
    BAD_REQUEST("bad request", 400),
    BAD_RESPONSE("bad response", 500);

    private final String text;
    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static ProtocolMessageStatusEnum getEnumByValue(int value) throws RuntimeException {
        for (ProtocolMessageStatusEnum e : ProtocolMessageStatusEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new RuntimeException("undefined enum value");
    }
}
