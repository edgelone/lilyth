package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "antman_files")
public class AntmanFile {
  @Id
  @GeneratedValue
  private Integer id;
  private String fileKey;
  private String bucket;
  private Integer size;
  private String swiftUrl;
}
