# ThrowingFunction
Java 8 functional types supporting checked exceptions + some additional flavours.

[![Build Status](https://travis-ci.org/TouK/ThrowingFunction.svg?branch=master)](https://travis-ci.org/TouK/ThrowingFunction)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/pl.touk/throwing-function/badge.svg)](https://maven-badges.herokuapp.com/maven-central/pl.touk/throwing-function)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/pl.touk/throwing-function/badge.svg)](http://www.javadoc.io/doc/pl.touk/throwing-function)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

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
      <version>1.2.1</version>
    </dependency>
    
### Available types:

+ ThrowingBiConsumer
+ ThrowingBiFunction
+ ThrowingBiPredicate
+ ThrowingBinaryOperator
+ ThrowingConsumer
+ ThrowingFunction
+ ThrowingPredicate
+ ThrowingRunnable
+ ThrowingSupplier
+ ThrowingUnaryOperator


#### Additional features:

    default Function<T, R> unchecked() {...}
Transforms ThrowingFunction into a regular Function. Checked exception gets wrapped in a RuntimeException. 
Feature is available for all java.util.function types. Comes both as a static and as an instance method.

    static <T, E extends Throwable> T checked(Class<E> exceptionType, Supplier<T> supplier) throws E {...}
Additional static function allowing to catch wrapped checked exception, unwrap and rethrow. Comes in handy sometimes.

    default Function<T, Optional<R>> trying() {...}
Transforms ThrowingFunction into a regular Function returning result wrapped into an Optional instance. If exception 
is thrown, result will contain an empty Optional instance. Exception gets ignored. Comes as a static method. Equivalent instance method is called `returningOptional()`

    default ThrowingFunction<T, Void, E> asFunction() {...}
Returns ThrowingPredicate/ThrowingSupplier/ThrowingConsumer instance as a new ThrowingFunction instance.
    

