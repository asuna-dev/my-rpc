package org.zepe.rpc.serializer;

import java.io.IOException;

/**
 * @author zzpus
 * @datetime 2025/4/23 19:52
 * @description
 */
public interface Serializer {
    <T> byte[] serialize(T object) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
