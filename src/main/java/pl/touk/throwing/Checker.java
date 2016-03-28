package pl.touk.throwing;

import pl.touk.throwing.exception.WrappedException;

import java.util.function.Supplier;

public final class Checker {
    private Checker() {
    }

    @SuppressWarnings("unchecked")
    public static <T, E extends Throwable> T checked(Class exceptionType, Supplier<T> supplier) throws E {
        try {
            return supplier.get();
        } catch (WrappedException ex) {
            if (exceptionType.isInstance(ex.getCause())) {
                throw (E) ex.getCause();
            } else {
                throw ex;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T checked(Supplier<T> supplier) throws Throwable {
        try {
            return supplier.get();
        } catch (WrappedException ex) {
            throw ex.getCause();
        }
    }
}
