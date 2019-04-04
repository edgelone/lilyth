package com.tujia.os.lilyth.service;

import com.tujia.os.lilyth.dao.*;
import com.tujia.os.lilyth.model.*;
import com.tujia.os.lilyth.util.Utils;
import com.tujia.os.lilyth.vo.Plain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
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
  private ContractDao contractDao;

  @Autowired
  private AntmanFileDao antmanFileDao;

  public void analysisAvatarFile() {

    List<BaseModel> avatarFiles = avatarFileDao.findAll()
        .stream()
        .filter(a -> !StringUtils.isEmpty(a.getFileKey()))
        .map(AvatarFile::of)
        .collect(Collectors.toList());
    List<Plain> result = getPlains(avatarFiles);

    appendToFile(result, "avatar_files");
  }

  public void analysisGrootFile() {
    List<BaseModel> grootFiles = grootFileDao.findAll()
        .stream()
        .filter(a -> !StringUtils.isEmpty(a.getFileKey()))
        .map(GrootFile::of).collect(Collectors.toList());
    List<Plain> result = getPlains(grootFiles);

    appendToFile(result, "groot_files");
  }


  public void analysisContact() {

    List<BaseModel> contracts = contractDao.findAll()
        .stream()
        .filter(a -> !StringUtils.isEmpty(a.getFileKey()))
        .map(Contract::of)
        .collect(Collectors.toList());
    List<Plain> result = getPlains(contracts);

    appendToFile(result, "contracts");
  }

  private List<Plain> getPlains(List<BaseModel> files) {
    List<List<BaseModel>> partList = ListUtils.partition(files, 5000);
    return partList.parallelStream().map(p -> {
      List<Plain> plains = new ArrayList<>();
      Set<String> fileKeys = p.stream().filter(a -> StringUtils.isEmpty(a.getSwiftUrl()) && !StringUtils.isEmpty(a.getFileKey())).map(BaseModel::getFileKey).collect(Collectors.toSet());
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
      Set<String> fileKeys =
          p.stream().filter(a -> StringUtils.isEmpty(a.getAvatarSwiftUrl()) && !StringUtils.isEmpty(a.getAvatarKey())).map(BaseUser::getAvatarKey).collect(Collectors.toSet());
      List<AntmanFile> antmanFiles = antmanFileDao.findByFileKeyIn(fileKeys).stream().filter(a -> a.getSize() != null && a.getSize() > 1).collect(Collectors.toList());
      Map<String, String> fileKeyBucketMap = antmanFiles.stream().collect(Collectors.toMap(AntmanFile::getFileKey, AntmanFile::getBucket, (x1, x2) -> x1));
      p.forEach(f -> plains.add(new Plain(f.getAvatarKey(), fileKeyBucketMap.getOrDefault(f.getAvatarKey(), ""), "osportrait")));
      return plains;
    }).flatMap(Collection::stream).filter(Objects::nonNull).collect(Collectors.toList());
  }

  public void analysisRetailer() {
    List<BaseUser> retailers =
        retailerDao.findAll().stream().filter(a -> !StringUtils.isEmpty(a.getAvatarKey())).map(Retailer::of).collect(Collectors.toList());

    List<Plain> result = getPlains4User(retailers);
    appendToFile(result, "retailer_files");
  }

  public void analysisRateUser() {
    List<BaseUser> rateUsers =
        rateUserDao.findAll().stream().filter(a -> !StringUtils.isEmpty(a.getAvatarKey())).map(RateUser::of).collect(Collectors.toList());

    List<Plain> result = getPlains4User(rateUsers);
    appendToFile(result, "rate_user_files");
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
