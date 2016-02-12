# ThrowingFunction
Java 8 functional types supporting checked exceptions

[![Build Status](https://travis-ci.org/TouK/ThrowingFunction.svg?branch=master)](https://travis-ci.org/TouK/ThrowingFunction)

## Provides shortcut to solving checked exceptions lambda repackaging hell:

### From this:

    private Function<Path, Directory> createDirectory =  path -> {
        try{
            return FSDirectory.open(path);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    };

### To this:
    private ThrowingFunction<Path, Directory, Exception> createDirectory =  path -> FSDirectory.open(path);
    
    

    <dependency>
      <groupId>pl.touk</groupId>
      <artifactId>throwing-function</artifactId>
      <version>1.1</version>
    </dependency>

    
    
#### Additional features:

    default Function<T, R> unchecked() {...}
Transforms ThrowingFunction into regular Function. Checked exception is wrapped in a RuntimeException. Available for 
all functional types.
    
    default Function<T, Optional<R>> returningOptional() {...}
Transforms ThrowingFunction into a regular Function returning result wrapped into an Optional instance. If exception 
is thrown, result will contain an empty Optional instance.

    default ThrowingFunction<T, Void, E> asFunction() {...}
Returns ThrowingPredicate/ThrowingSupplier/ThrowingConsumer instance as a new ThrowingFunction instance.
    

