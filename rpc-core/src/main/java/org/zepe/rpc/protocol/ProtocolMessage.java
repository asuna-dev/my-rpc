package org.zepe.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzpus
 * @datetime 2025/4/25 22:19
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    private Header header;
    private T body;

    @Data
    public static class Header {
        private byte magic = ProtocolConstant.PROTOCOL_MAGIC;
        private byte version = ProtocolConstant.PROTOCOL_VERSION;
        private byte serializer = (byte)ProtocolMessageSerializerEnum.JDK.getValue();
        private byte type;
        private byte status;
        private long requestId;
        private int bodyLength;
    }
}
