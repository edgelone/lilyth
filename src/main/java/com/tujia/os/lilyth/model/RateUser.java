package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "rate_users")
public class RateUser extends BaseUser {
  @Id
  @GeneratedValue
  private Integer id;
  private String avatarKey;
  private String avatarSwiftUrl;
}
