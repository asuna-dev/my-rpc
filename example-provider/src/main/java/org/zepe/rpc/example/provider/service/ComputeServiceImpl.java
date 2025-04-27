package org.zepe.rpc.example.provider.service;

import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.example.common.service.ComputeService;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:21
 * @description
 */
@Slf4j
public class ComputeServiceImpl implements ComputeService {

    @Override
    public int add(int a, int... x) {
        for (int i : x) {
            a += i;
        }
        return a;
    }
}
