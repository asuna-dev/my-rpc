package org.zepe.rpc.example.common.model;

import lombok.*;

import java.io.Serializable;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:12
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String name;
}
