package org.zepe.rpc.example.common.service;

import org.zepe.rpc.example.common.model.User;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:14
 * @description
 */
public interface UserService {
    User getUserByName(String name);
}
