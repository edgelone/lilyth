package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class GrootFile extends BaseModel{
    @Id
    @GeneratedValue
    private Integer id;
    private String fileKey;
    private String swiftUrl;
}
