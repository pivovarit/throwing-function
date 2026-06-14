# throwing-function

[![ci](https://github.com/pivovarit/throwing-function/actions/workflows/ci.yml/badge.svg)](https://github.com/pivovarit/throwing-function/actions/workflows/ci.yml)
[![pitest](https://github.com/pivovarit/throwing-function/actions/workflows/pitest.yml/badge.svg?branch=main)](http://pivovarit.github.io/throwing-function/pitest)
[![Maven Central Version](https://img.shields.io/maven-central/v/com.pivovarit/throwing-function)](https://central.sonatype.com/artifact/com.pivovarit/throwing-function/versions)
[![javadoc](https://javadoc.io/badge2/com.pivovarit/throwing-function/javadoc.svg)](https://javadoc.io/doc/com.pivovarit/throwing-function)
[![libs.tech recommends](https://libs.tech/project/46967967/badge.svg)](https://libs.tech/project/46967967/throwing-function)

[![Stargazers over time](https://starchart.cc/pivovarit/throwing-function.svg?variant=adaptive)](https://starchart.cc/pivovarit/throwing-function)

Checked-exception-enabled Java functional interfaces — a zero-dependency drop-in for `java.util.function`.

## The Problem

Java's `java.util.function` interfaces don't support checked exceptions, turning clean one-liners into verbose try-catch blocks:

```java
// what you want
.map(path -> new URI(path))

// what Java forces you to write
.map(path -> {
    try {
        return new URI(path);
    } catch (URISyntaxException e) {
        throw new RuntimeException(e);
    }
})
```

## The Solution

`throwing-function` provides checked-exception-aware variants of all standard functional interfaces:

```java
ThrowingFunction<String, URI, URISyntaxException> toUri = URI::new;
```

And adapters to integrate them into standard Java APIs:

```java
stream.map(ThrowingFunction.unchecked(URI::new))
      .forEach(System.out::println);
```

## Functional Interfaces

| Interface | Functional method |
|---|---|
| `ThrowingFunction<T, R, E>` | `R apply(T t) throws E` |
| `ThrowingBiFunction<T1, T2, R, E>` | `R apply(T1 t1, T2 t2) throws E` |
| `ThrowingUnaryOperator<T, E>` | `T apply(T t) throws E` |
| `ThrowingBinaryOperator<T, E>` | `T apply(T t1, T t2) throws E` |
| `ThrowingConsumer<T, E>` | `void accept(T t) throws E` |
| `ThrowingBiConsumer<T, U, E>` | `void accept(T t, U u) throws E` |
| `ThrowingSupplier<T, E>` | `T get() throws E` |
| `ThrowingPredicate<T, E>` | `boolean test(T t) throws E` |
| `ThrowingBiPredicate<T, U, E>` | `boolean test(T t, U u) throws E` |
| `ThrowingRunnable<E>` | `void run() throws E` |
| `ThrowingToIntFunction<T, E>` | `int applyAsInt(T t) throws E` |
| `ThrowingToLongFunction<T, E>` | `long applyAsLong(T t) throws E` |
| `ThrowingToDoubleFunction<T, E>` | `double applyAsDouble(T t) throws E` |
| `ThrowingIntSupplier<E>` | `int getAsInt() throws E` |
| `ThrowingLongSupplier<E>` | `long getAsLong() throws E` |
| `ThrowingDoubleSupplier<E>` | `double getAsDouble() throws E` |
| `ThrowingBooleanSupplier<E>` | `boolean getAsBoolean() throws E` |

## Adapters

Each interface provides static adapter methods to bridge `Throwing*` instances into standard `java.util.function` types.

| Adapter | On a checked exception it… | Returns |
|---|---|---|
| `unchecked` | wraps it in a `CheckedException` (a `RuntimeException`) and rethrows | the matching JDK type |
| `sneaky` | rethrows the original checked exception as-is | the matching JDK type |
| `optional` | swallows it and yields `Optional.empty()` | an `Optional`-returning type |
| `recover` | calls your handler to produce a fallback value | the matching JDK type |

### `unchecked` — wrap in `CheckedException`

Wraps the checked exception in a `CheckedException` (a `RuntimeException` subclass) and rethrows it. Available on all interfaces.

```java
stream.map(ThrowingFunction.unchecked(URI::new))
      .forEach(System.out::println);
```

### `sneaky` — rethrow without wrapping

Rethrows the checked exception as-is, bypassing the compiler's checked-exception enforcement via type erasure. Available on all interfaces.

```java
stream.map(ThrowingFunction.sneaky(URI::new))
      .forEach(System.out::println);
```

### `optional` — return `Optional`

Returns the result wrapped in an `Optional`, or `Optional.empty()` on exception. No exception is propagated.

- `ThrowingFunction.optional(f)` → `Function<T, Optional<R>>`
- `ThrowingBiFunction.optional(f)` → `BiFunction<T1, T2, Optional<R>>`
- `ThrowingSupplier.optional(s)` → `Supplier<Optional<T>>`

```java
stream.map(ThrowingFunction.optional(URI::new))  // Stream<Optional<URI>>
      .forEach(System.out::println);
```

### `recover` — fall back to a handler

Runs the throwing lambda and, on a checked exception, invokes a handler that produces a fallback value (or handles the side effect) instead of wrapping or rethrowing. For single-argument interfaces the handler receives the input and the exception; for two-argument interfaces it receives the exception.

```java
stream.map(ThrowingFunction.recover(URI::new, (path, e) -> URI.create("about:blank")))
      .forEach(System.out::println);
```

It also expresses the "default to a value on failure" case for predicates:

```java
stream.filter(ThrowingPredicate.recover(Files::isHidden, (path, e) -> false));
```

## Installation

### Maven

```xml
<dependency>
    <groupId>com.pivovarit</groupId>
    <artifactId>throwing-function</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.pivovarit:throwing-function:2.0.0'
```

## Dependencies

None. Implemented using core Java libraries only.

## Requirements

- Java 8 or newer
- Java module name `com.pivovarit.function` (an explicit `module-info` ships in a multi-release jar)
- Zero runtime dependencies

## Cookbook

Map into a primitive stream:

```java
int totalBytes = paths.stream()
    .mapToInt(ThrowingToIntFunction.unchecked(p -> Files.readAllBytes(p).length))
    .sum();
```

Wrap a checked call for `CompletableFuture`:

```java
CompletableFuture<byte[]> contents = CompletableFuture.supplyAsync(
    ThrowingSupplier.unchecked(() -> Files.readAllBytes(path)));
```

Generate a primitive stream from a throwing source:

```java
IntStream ints = IntStream.generate(ThrowingIntSupplier.unchecked(dataInput::readInt));
```

## Migrating from 1.x

2.0 contains small, mechanical breaking changes — the exception wrapper was renamed, `lifted`/`lift` became `optional`, and the primitive interfaces changed. See [MIGRATION.md](MIGRATION.md).
