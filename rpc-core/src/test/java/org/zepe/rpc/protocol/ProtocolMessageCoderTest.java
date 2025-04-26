package org.zepe.rpc.protocol;

import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.zepe.rpc.model.RpcRequest;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author zzpus
 * @datetime 2025/4/26 00:39
 * @description
 */
@Slf4j
public class ProtocolMessageCoderTest {

    @Test
    public void test() throws Exception {
        ProtocolMessage<RpcRequest> request = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setSerializer((byte)ProtocolMessageSerializerEnum.JSON.getValue());
        header.setType((byte)ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setRequestId(123456789L);
        // header.setBodyLength(0);
        request.setHeader(header);

        log.info("before: {}", request);
        Buffer encode = ProtocolMessageEncoder.encode(request);

        ProtocolMessage<?> protocolMessage = ProtocolMessageDecoder.decode(encode);
        log.info("after: {}", protocolMessage);

        assertEquals(request.getHeader().getRequestId(), protocolMessage.getHeader().getRequestId());
    }
}