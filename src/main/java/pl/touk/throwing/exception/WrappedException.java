package pl.touk.throwing.exception;

public class WrappedException extends RuntimeException {
    public WrappedException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
