package pl.touk.throwing;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments.  This is
 * the two-arity specialization of {@link ThrowingPredicate}.
 * Function may throw a checked exception.
 *
 * @param <T> the type of the first argument to the predicate
 * @param <U> the type of the second argument to the predicate
 * @param <E> the type of the thrown checked exception
 */
@FunctionalInterface
public interface ThrowingBiPredicate<T, U, E extends Exception> {
    boolean test(T t, U u) throws E;

    default ThrowingBiPredicate<T, U, E> and(final ThrowingBiPredicate<? super T, ? super U, E> other) {
        Objects.requireNonNull(other);

        return (arg1, arg2) -> test(arg1, arg2) && other.test(arg1, arg2);
    }

    default ThrowingBiPredicate<T, U, E> or(final ThrowingBiPredicate<? super T, ? super U, E> other) {
        Objects.requireNonNull(other);

        return (arg1, arg2) -> test(arg1, arg2) || other.test(arg1, arg2);
    }

    default ThrowingBiPredicate<T, U, E> xor(final ThrowingBiPredicate<? super T, ? super U, E> other) {
        Objects.requireNonNull(other);

        return (arg1, arg2) -> test(arg1, arg2) ^ other.test(arg1, arg2);
    }

    default ThrowingBiPredicate<T, U, E> negate() {
        return (arg1, arg2) -> !test(arg1, arg2);
    }

    /**
     * Returns this ThrowingBiFunction instance as a ThrowingBiFunction
     * @return this action as a ThrowingBiFunction
     */
    default ThrowingBiFunction<T, U, Boolean, E> asFunction() {
        return this::test;
    }

    /**
     * Returns a new BiPredicate instance which wraps thrown checked exception instance into a RuntimeException
     */
    default BiPredicate<T, U> unchecked() {
        return (arg1, arg2) -> {
            try {
                return test(arg1, arg2);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
