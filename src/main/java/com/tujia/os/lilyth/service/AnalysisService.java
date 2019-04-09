package com.tujia.os.lilyth.service;

import com.tujia.os.lilyth.dao.*;
import com.tujia.os.lilyth.model.*;
import com.tujia.os.lilyth.util.JacksonUtil;
import com.tujia.os.lilyth.util.StreamUtil;
import com.tujia.os.lilyth.util.Utils;
import com.tujia.os.lilyth.vo.File;
import com.tujia.os.lilyth.vo.HouseDetail;
import com.tujia.os.lilyth.vo.Plain;
import com.tujia.os.lilyth.vo.Room;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnalysisService {

  @Autowired
  private AvatarFileDao avatarFileDao;
  @Autowired
  private GrootFileDao grootFileDao;
  @Autowired
  private RetailerDao retailerDao;
  @Autowired
  private RateUserDao rateUserDao;
  @Autowired
  private RateDao rateDao;
  @Autowired
  private ContractDao contractDao;
  @Autowired
  private StashHouseDao stashHouseDao;
  @Autowired
  private AntmanFileDao antmanFileDao;

  @Async
  public void analysisAvatarFile() {

    List<BaseModel> avatarFiles = avatarFileDao.findAll()
        .stream()
        .filter(a -> !StringUtils.isEmpty(a.getFileKey()))
        .map(AvatarFile::of)
        .collect(Collectors.toList());
    List<Plain> result = getPlains(avatarFiles);
    log.info("analysis avatar file count:{}", avatarFiles.size());
    appendToFile(result, "avatar_files");
    log.info("analysis avatar file done:{}", avatarFiles.size());

  }

  @Async
  public void analysisGrootFile() {
    List<BaseModel> grootFiles = grootFileDao.findAll()
        .stream()
        .filter(a -> !StringUtils.isEmpty(a.getFileKey()))
        .map(GrootFile::of).collect(Collectors.toList());
    log.info("analysis groot file count:{}", grootFiles.size());

    List<Plain> result = getPlains(grootFiles);

    appendToFile(result, "groot_files");
    log.info("analysis groot file done:{}", grootFiles.size());

  }

  @Async
  public void analysisContract() {

    List<BaseModel> contracts = contractDao.findAll()
        .stream()
        .filter(a -> !StringUtils.isEmpty(a.getFileKey()))
        .map(Contract::of)
        .collect(Collectors.toList());
    log.info("analysis contract file count:{}", contracts.size());

    List<Plain> result = getPlains(contracts);

    appendToFile(result, "contracts");
    log.info("analysis contract file done:{}", contracts.size());

  }

  @Async
  public void analysisRetailer() {
    List<BaseUser> retailers =
        retailerDao.findAll().stream().filter(a -> !StringUtils.isEmpty(a.getAvatarKey())).map(Retailer::of).collect(Collectors.toList());
    log.info("analysis retailer file count:{}", retailers.size());

    List<Plain> result = getPlains4User(retailers);
    appendToFile(result, "retailer_files");
    log.info("analysis retailer file done:{}", retailers.size());

  }

  @Async
  public void analysisRateUser() {
    List<BaseUser> rateUsers =
        rateUserDao.findAll().stream().filter(a -> !StringUtils.isEmpty(a.getAvatarKey())).map(RateUser::of).collect(Collectors.toList());
    log.info("analysis rate user file count:{}", rateUsers.size());

    List<Plain> result = getPlains4User(rateUsers);
    appendToFile(result, "rate_user_files");
    log.info("analysis rate user file done:{}", rateUsers.size());

  }

  @Async
  public void analysisRate() {
    List<Rate> rates = rateDao.findAll();
    List<BaseModel> models = rates.stream()
        .filter(r -> StringUtils.isEmpty(r.getSwiftUrls()))
        .filter(r -> StringUtils.isEmpty(r.getFileKeys()))
        .map(Rate::getFileKeys)
        .map(f -> f.split(","))
        .map(Arrays::asList)
        .flatMap(Collection::stream)
        .map(f -> new BaseModel(f, ""))
        .collect(Collectors.toList());
    log.info("analysis rate file count:{}", models.size());


    List<Plain> result = getPlains(models);
    appendToFile(result, "rates");
    log.info("analysis rate file done:{}", models.size());
  }

  public void analysisStashHouse() {
    List<StashHouse> stashHouses = stashHouseDao.findAll();
    List<BaseModel> models = stashHouses.stream()
        .map(StashHouse::getContent)
        .map(s -> JacksonUtil.readValue(s, HouseDetail.class))
        .filter(Objects::nonNull)
        .map(s -> {
          List<File> files = new ArrayList<>();
          StreamUtil.open(s.getImageInfos())
              .filter(Objects::nonNull)
              .filter(i -> !StringUtils.isEmpty(i.getFileKey()))
              .filter(i -> StringUtils.isEmpty(i.getSwiftUrl()))
              .forEach(files::add);
          StreamUtil.open(s.getRooms())
              .map(Room::getImageInfos)
              .filter(Objects::nonNull)
              .flatMap(Collection::stream)
              .filter(Objects::nonNull)
              .filter(i -> !StringUtils.isEmpty(i.getFileKey()))
              .filter(i -> StringUtils.isEmpty(i.getSwiftUrl()))
              .forEach(files::add);
          return files;
        })
        .flatMap(Collection::stream)
        .map(f -> new BaseModel(f.getFileKey(), f.getSwiftUrl()))
        .collect(Collectors.toList());
    List<Plain> result = getPlains(models);
    appendToFile(result, "stash_houses");
    log.info("analysis stash house file done:{}", models.size());

  }

  private List<Plain> getPlains(List<BaseModel> files) {
    List<List<BaseModel>> partList = ListUtils.partition(files, 5000);
    return partList.parallelStream().map(p -> {
      List<Plain> plains = new ArrayList<>();
      Set<String> fileKeys = p.stream()
          .filter(a -> StringUtils.isEmpty(a.getSwiftUrl()))
          .filter(a -> !StringUtils.isEmpty(a.getFileKey()))
          .map(BaseModel::getFileKey)
          .collect(Collectors.toSet());
      List<AntmanFile> antmanFiles = antmanFileDao.findByFileKeyIn(fileKeys).stream().filter(a -> a.getSize() != null && a.getSize() > 1).collect(Collectors.toList());
      Map<String, String> fileKeyBucketMap = antmanFiles.stream().collect(Collectors.toMap(AntmanFile::getFileKey, AntmanFile::getBucket, (x1, x2) -> x1));
      p.forEach(f -> plains.add(new Plain(f.getFileKey(), fileKeyBucketMap.getOrDefault(f.getFileKey(), ""), "ostenement")));
      return plains;
    }).flatMap(Collection::stream).filter(Objects::nonNull).collect(Collectors.toList());
  }

  private List<Plain> getPlains4User(List<BaseUser> users) {
    List<List<BaseUser>> partList = ListUtils.partition(users, 5000);
    return partList.parallelStream().map(p -> {
      List<Plain> plains = new ArrayList<>();
      Set<String> fileKeys = p.stream()
          .filter(a -> StringUtils.isEmpty(a.getAvatarSwiftUrl()))
          .filter(a -> !StringUtils.isEmpty(a.getAvatarKey()))
          .map(BaseUser::getAvatarKey)
          .collect(Collectors.toSet());
      List<AntmanFile> antmanFiles = antmanFileDao.findByFileKeyIn(fileKeys).stream().filter(a -> a.getSize() != null && a.getSize() > 1).collect(Collectors.toList());
      Map<String, String> fileKeyBucketMap = antmanFiles.stream().collect(Collectors.toMap(AntmanFile::getFileKey, AntmanFile::getBucket, (x1, x2) -> x1));
      p.forEach(f -> plains.add(new Plain(f.getAvatarKey(), fileKeyBucketMap.getOrDefault(f.getAvatarKey(), ""), "osportrait")));
      return plains;
    }).flatMap(Collection::stream).filter(Objects::nonNull).collect(Collectors.toList());
  }


  private void appendToFile(List<Plain> result, String fileName) {
    StringBuffer sb = new StringBuffer("");
    result.forEach(r -> sb.append(String.format("\"%s\",\"%s\",\"%s\"\n", r.getFileKey(), r.getBucket(), r.getAccount())));
    try {
      Utils.writeToFile(sb.toString(), fileName);
    } catch (Exception e) {
      log.error("write to file error", e);
    }
  }
}
