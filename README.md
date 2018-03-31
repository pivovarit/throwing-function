# throwing-function
Checked Exceptions-enabled Java 8+ functional interfaces + adapters

[![Build Status](https://travis-ci.org/pivovarit/ThrowingFunction.svg?branch=master)](https://travis-ci.org/pivovarit/ThrowingFunction)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Provides shortcuts for solving Java 8 checked exceptions lambda repackaging hell.

### You can define functions that throw checked exceptions:
    ThrowingFunction<String, URI, URISyntaxException> toUri = URI::new;

### And use those functions seamlessly with native `java.util.function` classes by using a custom `ThrowingFunction#unchecked` adapter:

    ...stream()
      .map(unchecked(URI::new)) // static import of ThrowingFunction#unchecked
      .forEach(System.out::println);

### No more:

     ...stream().map(path -> {
         try {
             return new URI(path);
         } catch (URISyntaxException e) {
             throw new RuntimeException(e);
         }}).forEach(System.out::println);


For Maven users:

    <dependency>
        <groupId>pl.touk</groupId>
        <artifactId>throwing-function</artifactId>
        <version>1.3</version>
    </dependency>
    
For Gradle users:

    compile 'pl.touk:throwing-function:1.3'
    
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

    default Function<T, R> uncheck() {...}
Transforms a `ThrowingFunction` instance into a regular `Function`. Checked exceptions get wrapped in a `RuntimeException`. 
Feature is available for all java.util.function types. Comes both as a static and as an instance method.

    static Function<T, R> uncheck(ThrowingFunction<> f) {...}
Static version of the uncheck() method. Works nice when using with original java functional types.
    ...stream().map(unchecked(URI::new)).forEach(System.out::println);

    static Function<T, Optional<R>> lifted() {...}
Transforms ThrowingFunction into a regular Function returning result wrapped into an Optional instance. If exception 
is thrown, result will contain an empty Optional instance. Exception gets ignored. Comes as a static method. Equivalent instance method is called `lift()`

    default ThrowingFunction<T, Void, E> asFunction() {...}
Returns ThrowingPredicate/ThrowingSupplier/ThrowingConsumer instance as a new ThrowingFunction instance.

    Checker.checked()
Additional static function allowing catching wrapped checked exceptions, unwrap and rethrow them. Comes in handy sometimes.
