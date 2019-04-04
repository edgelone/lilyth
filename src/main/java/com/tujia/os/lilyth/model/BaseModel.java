package com.tujia.os.lilyth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel {
    private String fileKey;
    private String swiftUrl;

    public BaseModel of() {
        return new BaseModel(fileKey, swiftUrl);
    }
}
