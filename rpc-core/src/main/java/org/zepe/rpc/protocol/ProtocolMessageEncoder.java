package org.zepe.rpc.protocol;

import io.vertx.core.buffer.Buffer;
import org.zepe.rpc.serializer.Serializer;
import org.zepe.rpc.serializer.SerializerFactory;

/**
 * @author zzpus
 * @datetime 2025/4/25 22:19
 * @description
 */
public class ProtocolMessageEncoder {
    public static Buffer encode(ProtocolMessage<?> message) throws Exception {
        if (message == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = message.getHeader();
        if (header == null) {
            throw new RuntimeException("message header is null");
        }
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        ProtocolMessageSerializerEnum serializerEnum =
            ProtocolMessageSerializerEnum.getEnumByValue(header.getSerializer());

        Serializer serializer = SerializerFactory.getSerializer(serializerEnum.getKey());
        byte[] bodyBytes = serializer.serialize(message.getBody());
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);

        return buffer;
    }
}
