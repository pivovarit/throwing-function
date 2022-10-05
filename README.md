# Checked-Exceptions-enabled Java 8+ Functional Interfaces
and adapters

[![Build Status](https://travis-ci.org/pivovarit/throwing-function.svg?branch=master)](https://travis-ci.org/pivovarit/throwing-function)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pivovarit/throwing-function/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.pivovarit/throwing-function)

## Rationale

Standard `java.util.function` Functional Interfaces aren't checked-exception-friendly due to the absence of `throws ...` clause which results in tedious and verbose necessity of handling them by adding `try-catch` boilerplate.

Which makes one-liners like this:
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

By applying `com.pivovarit.function` functional interfaces, it's possible to regain clarity and readability:

    ThrowingFunction<String, URI, URISyntaxException> toUri = URI::new;

and use them seamlessly with native `java.util.function` classes by using custom `ThrowingFunction#unchecked` adapters:

    ...stream()
      .map(unchecked(URI::new)) // static import of ThrowingFunction#unchecked
      .forEach(System.out::println);

which avoids ending up with:

     ...stream().map(path -> {
         try {
             return new URI(path);
         } catch (URISyntaxException e) {
             throw new RuntimeException(e);
         }}).forEach(System.out::println);

### Basic API

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
        <version>1.5.1</version>
    </dependency>
    
##### Gradle

    compile 'com.pivovarit:throwing-function:1.5.1'

### Dependencies

None - the library is implemented using core Java libraries.

## Version history

### [1.5.1 (06-05-2020)](https://github.com/pivovarit/throwing-function/releases/tag/1.5.1)

* Fixed visibility issues with `ThrowingIntFunction`

### [1.5.0 (26-01-2019)](https://github.com/pivovarit/throwing-function/releases/tag/1.5.0)

* Introduced proper [Semantic Versioning](https://semver.org)
* Introduced `ThrowingIntFunction`
* Moved interfaces to `com.pivovarit.function`
* Removed controversial `unwrap()` functionality
