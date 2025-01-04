/*-
 * =================================LICENSE_START==================================
 * just-args
 * ====================================SECTION=====================================
 * Copyright (C) 2025 Andy Boothe
 * ====================================SECTION=====================================
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <https://unlicense.org/>
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.just.args;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class JustArgsTest {
  @Test
  public void givenSimpleValidCliArgs_whenParse_thenGetExpectedResults() {
    List<String> args = Arrays.asList("--option1", "value1", "-f", "positional1");
    int maxArgs = 1;

    Map<Character, String> shortOptionNames = mapOf();
    Map<String, String> longOptionNames = mapOf("option1", "opt1");
    Map<Character, String> shortPositiveFlagNames = mapOf('f', "flag1");
    Map<String, String> longPositiveFlagNames = mapOf();
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();

    JustArgs.ParsedArgs result =
        JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames, shortPositiveFlagNames,
            longPositiveFlagNames, shortNegativeFlagNames, longNegativeFlagNames);

    assertEquals(listOf("positional1"), result.getArgs());
    assertEquals(listOf(), result.getVarargs());
    assertEquals(mapOf("opt1", listOf("value1")), result.getOptions());
    assertEquals(mapOf("flag1", listOf(true)), result.getFlags());
  }

  @Test
  public void givenUnrecognizedSwitchInCliArgs_whenParse_thenIllegalSyntaxException() {
    List<String> args = Arrays.asList("--invalidOption");
    int maxArgs = 0;

    Map<Character, String> shortOptionNames = mapOf();
    Map<String, String> longOptionNames = mapOf();
    Map<Character, String> shortPositiveFlagNames = mapOf();
    Map<String, String> longPositiveFlagNames = mapOf();
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();

    assertThrows(JustArgs.IllegalSyntaxException.class,
        () -> JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames,
            shortPositiveFlagNames, longPositiveFlagNames, shortNegativeFlagNames,
            longNegativeFlagNames));
  }

  @Test
  public void givenLongFlagWithAttachedValue_whenParse_thenIllegalSyntaxException() {
    List<String> args = Arrays.asList("--longFlag=value");
    int maxArgs = 0;

    Map<Character, String> shortOptionNames = mapOf();
    Map<String, String> longOptionNames = mapOf();
    Map<Character, String> shortPositiveFlagNames = mapOf();
    Map<String, String> longPositiveFlagNames = mapOf("longFlag", "longFlag");
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();

    assertThrows(JustArgs.IllegalSyntaxException.class,
        () -> JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames,
            shortPositiveFlagNames, longPositiveFlagNames, shortNegativeFlagNames,
            longNegativeFlagNames));
  }

  @Test
  public void givenLongOptionWithAttachedValue_whenParse_thenGetExpectedResults() {
    List<String> args = Arrays.asList("--longOption=value");
    int maxArgs = 0;

    Map<Character, String> shortOptionNames = mapOf();
    Map<String, String> longOptionNames = mapOf("longOption", "longOption");
    Map<Character, String> shortPositiveFlagNames = mapOf();
    Map<String, String> longPositiveFlagNames = mapOf();
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();

    JustArgs.ParsedArgs result =
        JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames, shortPositiveFlagNames,
            longPositiveFlagNames, shortNegativeFlagNames, longNegativeFlagNames);

    assertEquals(mapOf("longOption", listOf("value")), result.getOptions());
  }

  @Test
  public void givenLongOptionAtEndOfArgs_whenParse_thenIllegalSyntaxException() {
    List<String> args = Arrays.asList("--longOption");
    int maxArgs = 0;

    Map<Character, String> shortOptionNames = mapOf();
    Map<String, String> longOptionNames = mapOf("longOption", "longOption");
    Map<Character, String> shortPositiveFlagNames = mapOf();
    Map<String, String> longPositiveFlagNames = mapOf();
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();

    assertThrows(JustArgs.IllegalSyntaxException.class,
        () -> JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames,
            shortPositiveFlagNames, longPositiveFlagNames, shortNegativeFlagNames,
            longNegativeFlagNames));
  }

  @Test
  public void givenShortOptionAtEndOfArgs_whenParse_thenIllegalSyntaxException() {
    List<String> args = Arrays.asList("-x");
    int maxArgs = 0;

    Map<Character, String> shortOptionNames = mapOf('x', "x");
    Map<String, String> longOptionNames = mapOf();
    Map<Character, String> shortPositiveFlagNames = mapOf();
    Map<String, String> longPositiveFlagNames = mapOf();
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();

    assertThrows(JustArgs.IllegalSyntaxException.class,
        () -> JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames,
            shortPositiveFlagNames, longPositiveFlagNames, shortNegativeFlagNames,
            longNegativeFlagNames));
  }

  @Test
  public void givenShortOptionInMiddleOfBatch_whenParse_thenIllegalSyntaxException() {
    List<String> args = Arrays.asList("-xyz");
    int maxArgs = 0;

    Map<Character, String> shortOptionNames = mapOf('y', "y");
    Map<String, String> longOptionNames = mapOf();
    Map<Character, String> shortPositiveFlagNames = mapOf('x', "x", 'z', "z");
    Map<String, String> longPositiveFlagNames = mapOf();
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();

    assertThrows(JustArgs.IllegalSyntaxException.class,
        () -> JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames,
            shortPositiveFlagNames, longPositiveFlagNames, shortNegativeFlagNames,
            longNegativeFlagNames));
  }

  @Test
  public void givenShortOptionAtEndOfBatchAtEndOfArgs_whenParse_thenIllegalSyntaxException() {
    List<String> args = Arrays.asList("-xzy");
    int maxArgs = 0;

    Map<Character, String> shortOptionNames = mapOf('y', "y");
    Map<String, String> longOptionNames = mapOf();
    Map<Character, String> shortPositiveFlagNames = mapOf('x', "x", 'z', "z");
    Map<String, String> longPositiveFlagNames = mapOf();
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();

    assertThrows(JustArgs.IllegalSyntaxException.class,
        () -> JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames,
            shortPositiveFlagNames, longPositiveFlagNames, shortNegativeFlagNames,
            longNegativeFlagNames));
  }

  @Test
  public void givenShortOptionAtEndOfBatchWithArg_whenParse_thenIllegalSyntaxException() {
    List<String> args = Arrays.asList("-xzy", "foo");
    int maxArgs = 0;

    Map<Character, String> shortOptionNames = mapOf('y', "y");
    Map<String, String> longOptionNames = mapOf();
    Map<Character, String> shortPositiveFlagNames = mapOf('x', "x", 'z', "z");
    Map<String, String> longPositiveFlagNames = mapOf();
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();


    JustArgs.ParsedArgs result =
        JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames, shortPositiveFlagNames,
            longPositiveFlagNames, shortNegativeFlagNames, longNegativeFlagNames);

    assertEquals(mapOf("x", listOf(true), "z", listOf(true)), result.getFlags());
    assertEquals(mapOf("y", listOf("foo")), result.getOptions());
  }

  @Test
  public void givenMorePositionalArgsThanMaxArgs_whenParse_thenGetSomeVarArgs() {
    List<String> args = Arrays.asList("positional1", "positional2", "positional3");
    int maxArgs = 2;

    Map<Character, String> shortOptionNames = mapOf();
    Map<String, String> longOptionNames = mapOf();
    Map<Character, String> shortPositiveFlagNames = mapOf();
    Map<String, String> longPositiveFlagNames = mapOf();
    Map<Character, String> shortNegativeFlagNames = mapOf();
    Map<String, String> longNegativeFlagNames = mapOf();

    JustArgs.ParsedArgs result =
        JustArgs.parseArgs(args, maxArgs, shortOptionNames, longOptionNames, shortPositiveFlagNames,
            longPositiveFlagNames, shortNegativeFlagNames, longNegativeFlagNames);

    assertEquals(listOf("positional1", "positional2"), result.getArgs());
    assertEquals(listOf("positional3"), result.getVarargs());
  }

  private static <K, V> Map<K, V> mapOf() {
    return emptyMap();
  }

  private static <K, V> Map<K, V> mapOf(K k, V v) {
    return singletonMap(k, v);
  }

  private static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
    final Map<K, V> result = new LinkedHashMap<>(2);
    result.put(k1, v1);
    result.put(k2, v2);
    return unmodifiableMap(result);
  }

  private static <T> List<T> listOf() {
    return emptyList();
  }

  private static <T> List<T> listOf(T firstElement) {
    return singletonList(firstElement);
  }

  @SuppressWarnings("unchecked")
  private static <T> List<T> listOf(T firstElement, T... moreElements) {
    final List<T> result = new ArrayList<>(1 + moreElements.length);
    result.add(firstElement);
    for (T element : moreElements) {
      result.add(element);
    }
    return unmodifiableList(result);
  }
}
