package com.tujia.os.lilyth.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author lk
 * @date 2019/4/4
 */
@Data
@Entity(name = "stash_houses")
public class StashHouse {
  @Id
  @GeneratedValue
  private Integer id;
  private String content;
}
