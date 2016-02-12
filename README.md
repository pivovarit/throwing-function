# ThrowingFunction
Java 8 functional types supporting checked exceptions

[![Build Status](https://travis-ci.org/TouK/ThrowingFunction.svg?branch=master)](https://travis-ci.org/TouK/ThrowingFunction)

## Provides shortcuts for solving Java 8 checked exceptions lambda repackaging hell.

### You can now define functions that throw checked exceptions
    ThrowingFunction<String, URI, URISyntaxException> toUri = URI::new;

### And use those functions seamlessly with native Java 8 classes by using a custom adapter

    ...stream().map(ThrowingFunction.unchecked(URI::new)).forEach(System.out::println);

    ...stream().map(unchecked(URI::new)).forEach(System.out::println); //with a static import

### Instead of:

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
      <version>1.1</version>
    </dependency>

For Gradle users:
    'pl.touk:throwing-function:1.1'
    
    
#### Additional features:

    default Function<T, R> unchecked() {...}
Transforms ThrowingFunction into regular Function. Checked exception gets wrapped in a RuntimeException. Available for
all functional types.

    default Function<T, Optional<R>> returningOptional() {...}
Transforms ThrowingFunction into a regular Function returning result wrapped into an Optional instance. If exception 
is thrown, result will contain an empty Optional instance.

    default ThrowingFunction<T, Void, E> asFunction() {...}
Returns ThrowingPredicate/ThrowingSupplier/ThrowingConsumer instance as a new ThrowingFunction instance.
    

