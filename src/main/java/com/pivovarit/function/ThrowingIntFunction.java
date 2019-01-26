package com.pivovarit.function;

import com.pivovarit.function.exception.WrappedException;

import java.util.Optional;
import java.util.function.IntFunction;

import static com.pivovarit.function.SneakyThrowUtil.sneakyThrow;
import static java.util.Objects.requireNonNull;

/**
 * Functional interface mirroring {@link IntFunction<R>} for primitive ints from the java.util.function package.
 * This allows wrapping a exceptions in a runtime exception: {@link WrappedException}
 * <p>
 * As this adheres to the {@link IntFunction} interface, compose and andThen (as you might be familiar with from
 * {@link java.util.function.Function} are not available.
 *
 * @param <R> the type of the result of the function
 * @param <E> the type of the thrown checked exception
 * @since 1.5.0
 */
@FunctionalInterface
interface ThrowingIntFunction<R, E extends Exception> {

    R apply(int i) throws E;

    default IntFunction<R> uncheck() {
        return t -> {
            try {
                return apply(t);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }

    static <R> IntFunction<R> unchecked(final ThrowingIntFunction<R, ?> f) {
        return requireNonNull(f).uncheck();
    }

    /**
     * @return a Function that returns the result of the given function as an Optional instance.
     * In case of a failure, empty Optional is returned
     */
    static <R> IntFunction<Optional<R>> lifted(final ThrowingIntFunction<R, ?> f) {
        return requireNonNull(f).lift();
    }

    static <R> IntFunction<R> sneaky(ThrowingIntFunction<? extends R, ?> function) {
        requireNonNull(function);
        return t -> {
            try {
                return function.apply(t);
            } catch (final Exception ex) {
                return sneakyThrow(ex);
            }
        };
    }

    default IntFunction<Optional<R>> lift() {
        return t -> {
            try {
                return Optional.ofNullable(apply(t));
            } catch (final Exception e) {
                return Optional.empty();
            }
        };
    }
}
