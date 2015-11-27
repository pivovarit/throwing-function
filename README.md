# ThrowingFunction
Java 8 functional types supporting checked exceptions

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
    
    
#### Additional features:

    default Function<T, R> wrappedWithRuntimeException() {...}
Transforms ThrowingFunction into regular Function. Checked exception is wrapped in a RuntimeException. Available for 
all functional types.

    
    default Function<T, Optional<R>> toOptionalFunction() {...}
Transforms ThrowingFunction into a regular Function returning result wrapped into an Optional instance. If exception 
is thrown, result will contain an empty Optional instance.
    

