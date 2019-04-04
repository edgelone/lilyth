package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "contracts")
public class Contract extends BaseModel {
  @Id
  @GeneratedValue
  private String id;
  private String fileKey;
  private String swiftUrl;
}
