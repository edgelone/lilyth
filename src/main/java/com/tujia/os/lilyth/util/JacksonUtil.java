package com.tujia.os.lilyth.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author tw
 * @author 小柯
 * @date 2017/5/26
 */
public class JacksonUtil {

  private static ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

  private JacksonUtil() {
  }

  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public static String writeValue(Object value) {
    try {
      if (value != null) {
        return objectMapper.writeValueAsString(value);
      }
    } catch (IOException e) {
      logger.error("Jackson write value error.", e);
    }
    return null;
  }

  public static <T> T readValue(String json, Class<T> valueClass) {
    try {
      if (StringUtils.isNotBlank(json)) {
        return objectMapper.readValue(json, valueClass);
      }
    } catch (IOException e) {
      logger.error("Jackson read value error.", e);
    }
    return null;
  }

  public static <T> List<T> readValueList(String json, Class<T> elementClass) {
    try {
      if (StringUtils.isNotBlank(json)) {
        return objectMapper.readValue(json, getCollectionType(elementClass));
      }
    } catch (IOException e) {
      logger.error("Jackson read value error.", e);
    }
    return null;
  }

  public static <K, V> Map<K, V> readValueMap(String json, Class<K> keyClass, Class<V> valueClass) {
    try {
      if (StringUtils.isNotBlank(json)) {
        return objectMapper.readValue(json, getMapType(keyClass, valueClass));
      }
    } catch (IOException e) {
      logger.error("Jackson read value error.", e);
    }
    return null;
  }

  public static <K, V> Map<K, List<V>> readValueListMap(String json, Class<K> keyClass, Class<V> valueClass) {
    try {
      if (StringUtils.isNotBlank(json)) {
        return objectMapper.readValue(json, getMapType(getType(keyClass), getCollectionType(valueClass)));
      }
    } catch (IOException e) {
      logger.error("Jackson read value error.", e);
    }
    return null;
  }

  public static <T> T convertValue(Object fromValue, Class<T> toValueClass) {
    try {
      if (fromValue != null) {
        return objectMapper.convertValue(fromValue, toValueClass);
      }
    } catch (IllegalArgumentException e) {
      logger.error("Jackson convert value error.", e);
    }
    return null;
  }

  /**
   * 获取泛型的 Collection Type
   *
   * @param elementClasses 元素类型
   * @return JavaType Java类型
   * @since 1.0
   */
  private static JavaType getCollectionType(Class<?>... elementClasses) {
    return objectMapper.getTypeFactory().constructParametricType(List.class, elementClasses);
  }

  /**
   * 获取泛型的 Map Type
   *
   * @param keyClass   键类型
   * @param valueClass 值类型
   * @return MapType
   */
  private static MapType getMapType(Class<?> keyClass, Class<?> valueClass) {
    return objectMapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
  }

  /**
   * 获取泛型的 Map Type
   *
   * @param keyType   键类型
   * @param valueType 值类型
   * @return MapType
   */
  private static MapType getMapType(JavaType keyType, JavaType valueType) {
    return objectMapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
  }

  /**
   * 获取泛型的 Java Type
   *
   * @param valueClass 值类型
   * @return JavaType
   */
  private static JavaType getType(Class<?> valueClass) {
    return objectMapper.getTypeFactory().constructType(valueClass);
  }

}
