# ThrowingFunction
Java 8 functional types supporting checked exceptions and some additional flavours.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/pl.touk/throwing-function/badge.svg)](https://maven-badges.herokuapp.com/maven-central/pl.touk/throwing-function)
[![Build Status](https://travis-ci.org/TouK/ThrowingFunction.svg?branch=master)](https://travis-ci.org/TouK/ThrowingFunction)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/pl.touk/throwing-function/badge.svg)](http://www.javadoc.io/doc/pl.touk/throwing-function)


## Provides shortcuts for solving Java 8 checked exceptions lambda repackaging hell.

### You can now define functions that throw checked exceptions
    ThrowingFunction<String, URI, URISyntaxException> toUri = URI::new;

### And use those functions seamlessly with native Java 8 classes by using a custom unchecked() adapter

    ...stream().map(ThrowingFunction.unchecked(URI::new)).forEach(System.out::println);

    ...stream().map(unchecked(URI::new)).forEach(System.out::println); //with a static import

### No more:

     ...stream().map(path -> {
                try {
                    return new URI(path);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }).forEach(System.out::println);


For Maven users:

    <dependency>
      <groupId>pl.touk</groupId>
      <artifactId>throwing-function</artifactId>
      <version>1.2</version>
    </dependency>


#### Additional features:

    default Function<T, R> unchecked() {...}
Transforms ThrowingFunction into a regular Function. Checked exception gets wrapped in a RuntimeException. 
Feature is available for all functional types. Comes both as a static and as an instance method.

    default Function<T, Optional<R>> returningOptional() {...}
Transforms ThrowingFunction into a regular Function returning result wrapped into an Optional instance. If exception 
is thrown, result will contain an empty Optional instance.

    default ThrowingFunction<T, Void, E> asFunction() {...}
Returns ThrowingPredicate/ThrowingSupplier/ThrowingConsumer instance as a new ThrowingFunction instance.
    

