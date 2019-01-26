# throwing-function
Checked Exceptions-enabled Java 8+ functional interfaces + adapters

[![Build Status](https://travis-ci.org/pivovarit/throwing-function.svg?branch=master)](https://travis-ci.org/pivovarit/throwing-function)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# Easing the checked exceptions lambda repackaging pain.

## Rationale
Functional Interfaces from `java.util.function` package are not exception-friendly due to the absence of `throws` clause in all of them - which results in tedious and verbose necessity of `try-catch`ing every single checked exception potentially being thrown inside a lambda expression body.

So, something as simple as:
```
path -> new URI(path)
```
becomes:

```
path -> {
    try {
        return new URI(path);
    } catch (URISyntaxException e) {
        throw new RuntimeException(e);
    }
}
```    

### With throwing-function, you can define functions that throw checked exceptions:
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
      <groupId>com.pivovarit</groupId>
      <artifactId>throwing-function</artifactId>
      <version>1.4</version>
    </dependency>
    
For Gradle users:

    compile 'com.pivovarit:throwing-function:1.4'
    

### Available types:

+ [ThrowingFunction](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingFunction.java)
+ [ThrowingBiConsumer](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingBiConsumer.java)
+ [ThrowingBiFunction](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingBiFunction.java)
+ [ThrowingBiPredicate](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingBiPredicate.java)
+ [ThrowingBinaryOperator](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingBinaryOperator.java)
+ [ThrowingConsumer](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingConsumer.java)
+ [ThrowingPredicate](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingPredicate.java)
+ [ThrowingRunnable](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingRunnable.java)
+ [ThrowingSupplier](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingSupplier.java)
+ [ThrowingUnaryOperator](https://github.com/pivovarit/throwing-function/blob/master/src/main/java/com/pivovarit/function/ThrowingUnaryOperator.java)


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
