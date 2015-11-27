package touk.pl.function;

import java.util.Objects;
import java.util.Optional;

public interface ThrowingPredicate<T, E extends Exception> {
    boolean test(T t) throws E;

    default ThrowingPredicate<T, E> and(ThrowingPredicate<? super T, E> other) {
        Objects.requireNonNull(other);

        return t -> test(t) && other.test(t);
    }

    default ThrowingPredicate<T, E> or(ThrowingPredicate<? super T, E> other) {
        Objects.requireNonNull(other);

        return t -> test(t) || other.test(t);
    }

    default ThrowingPredicate<T, E> xor(ThrowingPredicate<? super T, E> other) {
        Objects.requireNonNull(other);

        return t -> test(t) ^ other.test(t);
    }

    default ThrowingPredicate<T, E> negate() {
        return t -> !test(t);
    }

    default ThrowingFunction<T, Optional<T>, E> toOptionalPredicate() {
        return arg -> test(arg) ? Optional.of(arg) : Optional.empty();
    }
}
