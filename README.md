# Checked-Exceptions-enabled Java 8+ Functional Interfaces

[![Build against JDKs](https://github.com/pivovarit/throwing-function/actions/workflows/ci.yml/badge.svg)](https://github.com/pivovarit/throwing-function/actions/workflows/ci.yml)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![Maven Central Version](https://img.shields.io/maven-central/v/com.pivovarit/throwing-function)
[![libs.tech recommends](https://libs.tech/project/46967967/badge.svg)](https://libs.tech/project/46967967/throwing-function)


[![Stargazers over time](https://starchart.cc/pivovarit/throwing-function.svg?variant=adaptive)](https://starchart.cc/pivovarit/throwing-function)

## Overview

Java’s standard `java.util.function` interfaces are not compatible with checked exceptions. This leads to verbose and cluttered code, requiring manual try-catch blocks for exception handling, which makes one-liners like this:

```
path -> new URI(path)
```
become as verbose as:

```
path -> {
    try {
        return new URI(path);
    } catch (URISyntaxException e) {
        throw new RuntimeException(e);
    }
}
```    

This library introduces checked-exception-enabled functional interfaces, like `ThrowingFunction`, allowing cleaner, more concise code. You can now handle exceptions in functional pipelines without sacrificing readability:

    ThrowingFunction<String, URI, URISyntaxException> toUri = URI::new;

Using the `ThrowingFunction#unchecked` adapter, this can be seamlessly integrated into standard streams:

    ...stream()
      .map(unchecked(URI::new)) // static import of ThrowingFunction#unchecked
      .forEach(System.out::println);

This eliminates the need for bulky try-catch blocks within stream operations:

     ...stream().map(path -> {
         try {
             return new URI(path);
         } catch (URISyntaxException e) {
             throw new RuntimeException(e);
         }}).forEach(System.out::println);

### Key Features

- Functional Interfaces: Supports various functional types with checked exceptions.
- Adapters: Provides utility methods to convert `Throwing*` types into standard Java functional interfaces.
- Lightweight: No external dependencies, implemented using core Java libraries.

### Core API

#### Functional Interfaces

- [ThrowingFunction](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingFunction.java)
- [ThrowingIntFunction](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingIntFunction.java)
- [ThrowingToLongFunction](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingToLongFunction.java)
- [ThrowingBiConsumer](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingBiConsumer.java)
- [ThrowingBiFunction](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingBiFunction.java)
- [ThrowingBiPredicate](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingBiPredicate.java)
- [ThrowingBinaryOperator](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingBinaryOperator.java)
- [ThrowingConsumer](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingConsumer.java)
- [ThrowingPredicate](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingPredicate.java)
- [ThrowingRunnable](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingRunnable.java)
- [ThrowingSupplier](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingSupplier.java)
- [ThrowingUnaryOperator](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingUnaryOperator.java)


#### Adapters
+ `static Function<T, R> unchecked(ThrowingFunction<> f) {...}`

Transforms a `ThrowingFunction` instance into a standard `java.util.function.Function` by wrapping checked exceptions in a `RuntimeException` and rethrowing them. 

+ `static Function<T, Optional<R>> lifted() {...}`

Transforms a `ThrowingFunction` instance into a regular `Function` returning result wrapped in an `Optional` instance. 

+ `default ThrowingFunction<T, Void, E> asFunction() {...}`

Returns `Throwing(Predicate|Supplier|Consumer`) instance as a new `ThrowingFunction` instance.

### Maven Central

    <dependency>
        <groupId>com.pivovarit</groupId>
        <artifactId>throwing-function</artifactId>
        <version>1.6.1</version>
    </dependency>
    
##### Gradle

    compile 'com.pivovarit:throwing-function:1.6.1'

### Dependencies

None - the library is implemented using core Java libraries.

## Version history

## [1.6.1 (25-09-2024)](https://github.com/pivovarit/throwing-function/releases/tag/1.6.1)

* Explicit module configuration via a multi-release jar
* Improved Javadoc 

### [1.6.0 (24-09-2024)](https://github.com/pivovarit/throwing-function/releases/tag/1.6.0)

* Added `Automatic-Module-Name` to MANIFEST

### [1.5.1 (06-05-2020)](https://github.com/pivovarit/throwing-function/releases/tag/1.5.1)

* Fixed visibility issues with `ThrowingIntFunction`

### [1.5.0 (26-01-2019)](https://github.com/pivovarit/throwing-function/releases/tag/1.5.0)

* Introduced proper [Semantic Versioning](https://semver.org)
* Introduced `ThrowingIntFunction`
* Moved interfaces to `com.pivovarit.function`
* Removed controversial `unwrap()` functionality
