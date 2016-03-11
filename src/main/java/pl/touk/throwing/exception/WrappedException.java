package pl.touk.throwing.exception;

public class WrappedException extends RuntimeException {
    private final Class<? extends Throwable> klass;

    public WrappedException(Throwable cause, Class<? extends Throwable> klass) {
        super(cause.getMessage(), cause);
        this.klass = klass;
    }

    public Class<? extends Throwable> getKlass() {
        return klass;
    }
}
