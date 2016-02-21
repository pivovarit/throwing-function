package pl.touk.throwing;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents a function that accepts one argument and returns a boolean value
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the input to the function
 * @param <E> the type of the thrown checked exception
 *
 */
@FunctionalInterface
public interface ThrowingPredicate<T, E extends Throwable> {
    boolean test(T t) throws E;

    default ThrowingPredicate<T, E> and(final ThrowingPredicate<? super T, E> other) {
        Objects.requireNonNull(other);

        return t -> test(t) && other.test(t);
    }

    default ThrowingPredicate<T, E> or(final ThrowingPredicate<? super T, E> other) {
        Objects.requireNonNull(other);

        return t -> test(t) || other.test(t);
    }

    default ThrowingPredicate<T, E> xor(final ThrowingPredicate<? super T, E> other) {
        Objects.requireNonNull(other);

        return t -> test(t) ^ other.test(t);
    }

    default ThrowingPredicate<T, E> negate() {
        return t -> !test(t);
    }

    default ThrowingFunction<T, Optional<Boolean>, E> returningOptional() {
        return t -> {
            try {
                return Optional.of(test(t));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }

    /**
     * @return this Predicate instance as a Function instance
     */
    default ThrowingFunction<T, Boolean, E> asFunction() {
        return this::test;
    }

    static <T, E extends Exception> Predicate<T> unchecked(ThrowingPredicate<T, E> predicate) {
        Objects.requireNonNull(predicate);

        return predicate.unchecked();
    }

    /**
     * @return a new Predicate instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Predicate<T> unchecked() {
        return t -> {
            try {
                return test(t);
            } catch (final Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}
