package pl.touk.throwing;

import java.util.Objects;
import java.util.function.BiFunction;


/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the two-arity specialization of {@link ThrowingFunction}.
 * Function may throw a checked exception.
 *
 * @param <T1> the type of the first argument to the function
 * @param <T2> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the thrown checked exception
 *
 * @see ThrowingFunction
 */
@FunctionalInterface
public interface ThrowingBiFunction<T1, T2, R, E extends Exception> {
    R apply(T1 arg1, T2 arg2) throws E;

    /**
     * Performs provided action on the result of this ThrowingBiFunction instance
     */
    default <V> ThrowingBiFunction<T1, T2, V, E> andThen(final ThrowingFunction<? super R, ? extends V, E> after) {
        Objects.requireNonNull(after);

        return (arg1, arg2) -> after.apply(apply(arg1, arg2));
    }

    default BiFunction<T1, T2, R> unchecked() {
        return (arg1, arg2) -> {
            try {
                return apply(arg1, arg2);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
