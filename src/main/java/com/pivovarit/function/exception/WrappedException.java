package com.pivovarit.function.exception;

public class WrappedException extends RuntimeException {
    public WrappedException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
