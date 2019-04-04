package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity(name = "contracts")
public class Contract extends BaseModel {
  private String id;
  private String fileKey;
  private String swiftUrl;
}
