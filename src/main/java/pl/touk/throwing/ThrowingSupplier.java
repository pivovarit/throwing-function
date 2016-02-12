package pl.touk.throwing;

import java.util.function.Supplier;

/**
 * Represents a function that accepts zero arguments and returns some value.
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the output to the function
 * @param <E> the type of the thrown checked exception
 *
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;

    /**
     *
     * @param <T> type
     * @param <E> checked exception
     * @return a new Supplier instance returning null
     */
    static <T, E extends Exception> ThrowingSupplier<T, E> nullSupplier() {
        return () -> null;
    }

    /**
     * @return this Consumer instance as a new Function instance
     */
    default ThrowingFunction<Void, T, E> asFunction() {
        return arg -> get();
    }

    /**
     * @return a new Supplier instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Supplier<T> unchecked() {
        return () -> {
            try {
                return get();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
