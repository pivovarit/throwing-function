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
    private ThrowingFunction<Path, Directory> createDirectory =  path -> FSDirectory.open(path);
