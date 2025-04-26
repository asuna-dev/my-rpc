package org.zepe.rpc.protocol;

import io.vertx.core.buffer.Buffer;
import org.zepe.rpc.model.RpcRequest;
import org.zepe.rpc.model.RpcResponse;
import org.zepe.rpc.serializer.Serializer;
import org.zepe.rpc.serializer.SerializerFactory;

/**
 * @author zzpus
 * @datetime 2025/4/25 22:19
 * @description
 */
public class ProtocolMessageDecoder {
    public static ProtocolMessage<?> decode(Buffer buffer) throws Exception {
        ProtocolMessage.Header header = new ProtocolMessage.Header();

        byte magic = buffer.getByte(0);
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("invalid magic number");
        }

        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));

        byte[] bodyBytes = buffer.getBytes(ProtocolConstant.MESSAGE_HEADER_LENGTH,
            ProtocolConstant.MESSAGE_HEADER_LENGTH + header.getBodyLength());

        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());

        ProtocolMessageSerializerEnum serializerEnum =
            ProtocolMessageSerializerEnum.getEnumByValue(header.getSerializer());

        Serializer serializer = SerializerFactory.getSerializer(serializerEnum.getKey());

        switch (messageTypeEnum) {
            case REQUEST -> {
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, rpcRequest);
            }

            case RESPONSE -> {
                RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, rpcResponse);
            }
            case HEARTBEAT, OTHERS -> throw new RuntimeException("unsupported message type");
        }

        return null;
    }
}
