package org.zepe.rpc.protocol;

/**
 * @author zzpus
 * @datetime 2025/4/25 22:22
 * @description
 */
public interface ProtocolConstant {
    int MESSAGE_HEADER_LENGTH = 17;
    byte PROTOCOL_MAGIC = 0x7;
    byte PROTOCOL_VERSION = 0x1;
}
