package com.pivovarit.function;

import com.pivovarit.function.exception.WrappedException;

import java.util.Optional;
import java.util.function.DoubleFunction;

import static com.pivovarit.function.SneakyThrowUtil.sneakyThrow;
import static java.util.Objects.requireNonNull;

/**
 * Functional interface mirroring {@link DoubleFunction<R>} for primitive doubles from the java.util.function package.
 * This allows wrapping a exceptions in a runtime exception: {@link WrappedException}
 * <p>
 * As this adheres to the {@link DoubleFunction} interface, compose and andThen (as you might be familiar with from
 * {@link java.util.function.Function} are not available.
 *
 * @param <R> the type of the result of the function
 * @param <E> the type of the thrown checked exception
 * @since 1.5.0
 */
@FunctionalInterface
public interface ThrowingDoubleFunction<R, E extends Exception> {

    R apply(double d) throws E;

    default DoubleFunction<R> uncheck() {
        return t -> {
            try {
                return apply(t);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }

    static <R> DoubleFunction<R> unchecked(final ThrowingDoubleFunction<R, ?> f) {
        return requireNonNull(f).uncheck();
    }

    /**
     * @return a Function that returns the result of the given function as an Optional instance.
     * In case of a failure, empty Optional is returned
     */
    static <R> DoubleFunction<Optional<R>> lifted(final ThrowingDoubleFunction<R, ?> f) {
        return requireNonNull(f).lift();
    }

    static <R> DoubleFunction<R> sneaky(ThrowingDoubleFunction<? extends R, ?> function) {
        requireNonNull(function);
        return t -> {
            try {
                return function.apply(t);
            } catch (final Exception ex) {
                return sneakyThrow(ex);
            }
        };
    }

    default DoubleFunction<Optional<R>> lift() {
        return t -> {
            try {
                return Optional.ofNullable(apply(t));
            } catch (final Exception e) {
                return Optional.empty();
            }
        };
    }
}
