package com.tujia.os.lilyth.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lk
 * @date 2019/4/8
 */
@Data
public class HouseDetail {
  private List<File> imageInfos;
  private List<Room> rooms;
}
