package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity(name = "rate_uses")
public class RateUser extends BaseUser {
  private Integer id;
  private String avatarKey;
  private String avatarSwiftUrl;
}
