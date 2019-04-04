package com.tujia.os.lilyth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author lk
 * @date 2019/4/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseUser {
  private String avatarKey;
  private String avatarSwiftUrl;

  public BaseUser of() {
    return new BaseUser(avatarKey, avatarSwiftUrl);
  }
}
