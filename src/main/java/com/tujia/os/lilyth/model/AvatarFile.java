package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "avatar_files")
@Data
public class AvatarFile extends BaseModel {
    @Id
    @GeneratedValue
    private Integer Id;
    private String fileKey;
    private String swiftUrl;
}
