package com.tujia.os.lilyth.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author tw
 * @author 小柯
 * @date 2017/6/22
 */
public class ZipStrUtil {

  private static final String ZIP_COMPRESS = "zip_compress==";
  private static final int DEFAULT_LENGTH_LIMIT = 4000;

  private static final Logger logger = LoggerFactory.getLogger(ZipStrUtil.class);

  /**
   * 字符串的压缩
   */
  public static String compress(String str, int length) {
    if (StringUtils.isEmpty(str) || str.length() < length) {
      return str;
    }
    try (
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ) {
      GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
      gzipOutputStream.write(str.getBytes());
      gzipOutputStream.close();
      return ZIP_COMPRESS + Base64.getEncoder().encodeToString(outputStream.toByteArray());
    } catch (IOException e) {
      logger.error("字符串压缩异常 ", e);
    }
    return null;
  }

  /**
   * 字符串的压缩，使用默认长度限制
   */
  public static String compress(String str) {
    return compress(str, DEFAULT_LENGTH_LIMIT);
  }

  /**
   * 字符串的解压
   */
  public static String uncompress(String str) {
    if (StringUtils.isEmpty(str) || !str.startsWith(ZIP_COMPRESS)) {
      return str;
    }
    try (
        GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(
            Base64.getDecoder().decode(str.substring(ZIP_COMPRESS.length()))));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
    ) {
      int offset;
      byte[] buffer = new byte[4096];
      while ((offset = inputStream.read(buffer)) >= 0) {
        outputStream.write(buffer, 0, offset);
      }
      return outputStream.toString("UTF-8");
    } catch (IOException e) {
      logger.error("字符串解压异常 ", e);
    }
    return null;
  }

}
