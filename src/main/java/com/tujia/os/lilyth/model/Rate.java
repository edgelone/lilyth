package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "rates")
public class Rate {
  @Id
  @GeneratedValue
  private Integer id;
  private String fileKeys;
  private String swiftUrls;
}
