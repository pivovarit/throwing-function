package touk.pl.function;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;

    static <T, E extends Exception> ThrowingSupplier<T, E> empty() {
        return () -> null;
    }

    default Supplier<T> wrappedWithRuntimeException() {
        return () -> {
            try {
                return get();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
