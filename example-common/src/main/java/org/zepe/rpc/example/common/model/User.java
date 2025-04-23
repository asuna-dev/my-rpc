package org.zepe.rpc.example.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:12
 * @description
 */
@Data
@Builder
@AllArgsConstructor
public class User implements Serializable {
    private String name;
}
