package com.tujia.os.lilyth.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class StreamUtil {

  /**
   * Open stream of list.
   *
   * @param list may be {@literal null}.
   * @return Stream<T>
   */
  public static <T> Stream<T> open(List<T> list) {
    return Optional.ofNullable(list)
        .map(Collection::stream)
        .orElse(Stream.empty());
  }

  /**
   * Open stream of array.
   *
   * @param array may be {@literal null}.
   * @return Stream<T>
   */
  public static <T> Stream<T> open(T[] array) {
    return Optional.ofNullable(array)
        .map(Arrays::stream)
        .orElse(Stream.empty());
  }

  /**
   * Get first element of list.
   *
   * @param list         may be {@literal null}.
   * @param defaultValue may be {@literal null}.
   * @return T
   */
  public static <T> T first(List<T> list, T defaultValue) {
    return open(list).findFirst().orElse(defaultValue);
  }

  /**
   * Get first element of list, default null.
   *
   * @param list may be {@literal null}.
   * @return T
   */
  public static <T> T first(List<T> list) {
    return first(list, null);
  }

  /**
   * Concat several streams into one stream.
   *
   * @param streams
   * @param <T>
   * @return Stream<T>
   */
  public static <T> Stream<T> concat(Stream<T>... streams) {
    return Stream.of(streams)
        .filter(Objects::nonNull)
        .flatMap(Function.identity());
  }

  /**
   * Concat several lists into one stream.
   *
   * @param lists
   * @param <T>
   * @return Stream<T>
   */
  public static <T> Stream<T> concat(List<T>... lists) {
    return Arrays.stream(lists)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream);
  }

  private StreamUtil() {
  }

}
