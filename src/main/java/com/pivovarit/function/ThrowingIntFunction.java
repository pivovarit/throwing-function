package com.pivovarit.function;

import com.pivovarit.function.exception.WrappedException;

import java.util.Optional;
import java.util.function.IntFunction;

import static com.pivovarit.function.SneakyThrowUtil.sneakyThrow;
import static java.util.Objects.requireNonNull;

/**
 * Represents a function that accepts an int-valued argument and returns a value;
 * Function might throw a checked exception instance.
 *
 * @param <R> the type of the result of the function
 * @param <E> the type of the thrown checked exception
 * @author Grzegorz Piwowarek
 */
@FunctionalInterface
public interface ThrowingIntFunction<R, E extends Exception> {

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
     * Returns a new Function instance which wraps a result of the given function in an Optional, returning an empty Optional in case of a checked exception
     * @param f operation throwing checked exception
     * @param <R> type of the result of the function
     * @return a Function that returns the result of the given function as an Optional instance.
     */
    static <R> IntFunction<Optional<R>> lifted(final ThrowingIntFunction<R, ?> f) {
        return requireNonNull(f).lift();
    }

    /**
     * Returns a new Function instance which rethrows the checked exception using the Sneaky Throws pattern
     * @param function operation throwing checked exception
     * @param <R> type of the result of the function
     * @return Function instance that rethrows the checked exception using the Sneaky Throws pattern
     */
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
