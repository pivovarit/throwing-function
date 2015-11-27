package touk.pl.function;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@FunctionalInterface
public interface ThrowingPredicate<T, E extends Exception> {
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

    default ThrowingFunction<T, Optional<T>, E> toOptionalPredicate() {
        return arg -> test(arg) ? Optional.of(arg) : Optional.empty();
    }

    default ThrowingFunction<T, Boolean, E> asFunction() {
        return this::test;
    }

    default Predicate<T> wrappedWithRuntimeException() {
        return t -> {
            try {
                return test(t);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
