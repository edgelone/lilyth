package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity(name = "retailers")
public class Retailer extends BaseUser{
  private Integer id;
  private String avatarKey;
  private String avatarSwiftUrl;
}
