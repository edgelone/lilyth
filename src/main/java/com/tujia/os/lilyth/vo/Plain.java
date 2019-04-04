package com.tujia.os.lilyth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Plain {
    private String fileKey;
    private String bucket;
    private String account;
}
