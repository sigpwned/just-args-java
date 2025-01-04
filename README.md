# Just Args for Java [![tests](https://github.com/sigpwned/just-args-java/actions/workflows/tests.yml/badge.svg)](https://github.com/sigpwned/just-args-java/actions/workflows/tests.yml) [![Maven Central Version](https://img.shields.io/maven-central/v/com.sigpwned/just-args)](https://central.sonatype.com/artifact/com.sigpwned/just-args) [![javadoc](https://javadoc.io/badge2/com.sigpwned/just-args/javadoc.svg)](https://javadoc.io/doc/com.sigpwned/just-args)

Just Args is a small, simple library for Java that provides command-line argument parsing support and nothing else.

## Goals

Just Args should...

* **Parse arguments**. The library parses valid command-line arguments into a structured and useful model.
* **Be very small**. The JAR file is currently less than 10KB compressed, under 25KB uncompressed.
* **Be very simple**. Users only need one method to parse arguments: `JustArgs.parseArgs`.
* **Be flexible**. Supports options, flags, and positional arguments, as well as advanced configurations.
* **Work out of the box**. Designed to handle common argument parsing use cases with minimal configuration.

## Non-Goals

Just Args should not...

* **Validate command-line usage**. The library is not a strict validator and assumes you know how your CLI should behave.
* **Provide advanced features**. The library intentionally avoids dependencies, complex argument validation, and advanced frameworks.

## Installation

Just Args is available in Maven Central. You can add it to your project using the following Maven dependency:

```xml
<dependency>
    <groupId>com.sigpwned</groupId>
    <artifactId>just-args</artifactId>
    <version>0.0.0</version>
</dependency>
```

Just Args is a single Java file with no dependencies. In a pinch, you can copy-paste it into your project.

## Quickstart

### Basic Usage

To parse a list of command-line arguments:

```java
import com.sigpwned.just.args.JustArgs;

List<String> args = List.of("--xray", "value1", "-f", "positional1");
int maxArgs = 1;

Map<Character, String> shortOptionNames = Map.of('x', "xray");
Map<String, String> longOptionNames = Map.of("xray", "xray");
Map<Character, String> shortPositiveFlagNames = Map.of('f', "flag");
Map<String, String> longPositiveFlagNames = Map.of();
Map<Character, String> shortNegativeFlagNames = Map.of();
Map<String, String> longNegativeFlagNames = Map.of();

JustArgs.ParsedArgs result = JustArgs.parseArgs(
    args, maxArgs, shortOptionNames, longOptionNames, 
    shortPositiveFlagNames, longPositiveFlagNames, 
    shortNegativeFlagNames, longNegativeFlagNames
);

System.out.println(result.getArgs()); // [positional1]
System.out.println(result.getOptions()); // {xray=[value1]}
System.out.println(result.getFlags()); // {flag=[true]}
```

### Features

Just Args supports:

* **Options**: Arguments with values, e.g., `-k value`, `--key value` or `--key=value`.
* **Flags**: Boolean arguments, e.g., `-f` or `--flag`.
* **Short Flag Batches**: Multiple short flags grouped together, e.g., `-abc` is equivialent to `-a -b -c`
* **Positional Arguments**: Unlabeled arguments.
* **Separator Token `--`**: Marks all subsequent arguments as positional.

---

## Advanced Usage

### Handling Syntax Errors

Just Args throws a `JustArgs.IllegalSyntaxException` when it encounters invalid syntax. This is a subclass of `IllegalArgumentException` for simplicity.

```java
try {
    JustArgs.parseArgs(...);
} catch (JustArgs.IllegalSyntaxException e) {
    System.err.println("Syntax error at index " + e.getIndex() + ": " + e.getMessage());
}
```

### Customizing Argument Names

You can configure short and long names for options and flags, and assign them to the same logical bucket in the result:

```java
Map<Character, String> shortOptionNames = Map.of('o', "output");
Map<String, String> longOptionNames = Map.of("output", "output");

Map<Character, String> shortPositiveFlagNames = Map.of('v', "verbose");
Map<String, String> longPositiveFlagNames = Map.of("verbose", "verbose");
```

---

## FAQ

### Why Another Argument Parsing Library?

Most libraries are either too large, too complex, or depend on external frameworks. Just Args is small, simple, and dependency-free—perfect for lightweight projects.

### What About Error Messages?

Just Args focuses on simplicity. Error messages are provided through exceptions, leaving full control to the user.

### Can You Add Feature X?

Feel free to ask, but probably not. Just Args is intentionally minimal. If you need advanced features, consider a more fully-featured library like Apache Commons CLI or Picocli.

---

## A Note on Development

Just Args was built with simplicity and clarity in mind. The library is intentionally small and avoids external dependencies to make it easy to embed in any project.

---
