package touk.pl.function;

import java.util.Objects;
import java.util.function.BiPredicate;

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

    default BiPredicate<T, U> wrappedWithRuntimeException() {
        return (arg1, arg2) -> {
            try {
                return test(arg1, arg2);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
