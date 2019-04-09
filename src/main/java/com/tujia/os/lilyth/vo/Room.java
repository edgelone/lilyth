package com.tujia.os.lilyth.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lk
 * @date 2019/4/8
 */
@Data
public class Room {
  private String uuid;
  private List<File> imageInfos;
}
