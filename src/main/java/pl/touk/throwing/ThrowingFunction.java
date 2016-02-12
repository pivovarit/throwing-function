package pl.touk.throwing;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;


/**
 * Represents a function that accepts one argument and returns a value;
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the thrown checked exception
 *
 */
@FunctionalInterface
public interface ThrowingFunction<T,R,E extends Exception> {
    R apply(T arg) throws E;

    /**
     * @param <T> type
     * @param <E> checked exception
     * @return a function that accepts one argument and returns it as a value.
     */
    static <T, E extends Exception> ThrowingFunction<T, T, E> identity() {
        return t -> t;
    }

    static <T, R, E extends Exception> Function<T, R> unchecked(ThrowingFunction<T, R, E> function) {
        return function.unchecked();
    }

    default <V> ThrowingFunction<V, R, E> compose(final ThrowingFunction<? super V, ? extends T, E> before) {
        Objects.requireNonNull(before);

        return (V v) -> apply(before.apply(v));
    }

    default <V> ThrowingFunction<T, V, E> andThen(final ThrowingFunction<? super R, ? extends V, E> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * @return a Function that returns the result as an Optional instance. In case of a failure, empty Optional is
     * returned
     */
    default Function<T, Optional<R>> returningOptional() {
        return t -> {
            try {
                return Optional.of(apply(t));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }

    /**
     * @return a new Function instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Function<T, R> unchecked() {
        return t -> {
            try {
                return apply(t);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
