# throwing-function

[![ci](https://github.com/pivovarit/throwing-function/actions/workflows/ci.yml/badge.svg)](https://github.com/pivovarit/throwing-function/actions/workflows/ci.yml)
[![pitest](https://github.com/pivovarit/parallel-collectors/actions/workflows/pitest.yml/badge.svg?branch=main)](http://pivovarit.github.io/parallel-collectors/pitest)

![Maven Central Version](https://img.shields.io/maven-central/v/com.pivovarit/throwing-function)
[![javadoc](https://javadoc.io/badge2/com.pivovarit/throwing-function/1.6.1/javadoc.svg)](https://javadoc.io/doc/com.pivovarit/throwing-function/1.6.1)

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
| `ThrowingToLongFunction<T, E>` | `long applyAsLong(T t) throws E` |

## Adapters

Each interface provides static adapter methods to bridge `Throwing*` instances into standard `java.util.function` types.

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

### `lifted` / `optional` — return `Optional`

Returns the result wrapped in an `Optional`, or `Optional.empty()` on exception. No exception is propagated.

- `ThrowingFunction.lifted(f)` → `Function<T, Optional<R>>`
- `ThrowingBiFunction.optional(f)` → `BiFunction<T1, T2, Optional<R>>`
- `ThrowingSupplier.optional(s)` → `Supplier<Optional<T>>`

```java
stream.map(ThrowingFunction.lifted(URI::new))  // Stream<Optional<URI>>
      .forEach(System.out::println);
```

## Installation

### Maven

```xml
<dependency>
    <groupId>com.pivovarit</groupId>
    <artifactId>throwing-function</artifactId>
    <version>1.6.1</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.pivovarit:throwing-function:1.6.1'
```

## Dependencies

None. Implemented using core Java libraries only.
