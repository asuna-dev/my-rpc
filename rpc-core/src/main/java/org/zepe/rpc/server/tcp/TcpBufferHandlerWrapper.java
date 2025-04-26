package org.zepe.rpc.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.protocol.ProtocolConstant;

/**
 * @author zzpus
 * @datetime 2025/4/26 16:03
 * @description 装饰器模式增强Buffer处理器能力，避免tcp粘包问题
 */
@Slf4j
public class TcpBufferHandlerWrapper implements Handler<Buffer> {
    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<>() {
            int size = -1;
            Buffer result = Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                if (-1 == size) {
                    // parser首次会一次读取17字节的header
                    size = buffer.getInt(13);
                    // parser变更读取body长度模式
                    parser.fixedSizeMode(size);
                    result.appendBuffer(buffer);
                } else {
                    result.appendBuffer(buffer);
                    // 处理完整的请求
                    bufferHandler.handle(result);
                    // 重置parser
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    result = Buffer.buffer();
                }
            }
        });

        return parser;
    }
}
