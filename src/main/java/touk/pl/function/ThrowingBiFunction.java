package touk.pl.function;

import java.util.Objects;

@FunctionalInterface
public interface ThrowingBiFunction<T1, T2, R, E extends Exception> {
    R apply(T1 arg1, T2 arg2) throws E;

    default <V> ThrowingBiFunction<T1, T2, V, E> andThen(final ThrowingFunction<? super R, ? extends V, E> after) {
        Objects.requireNonNull(after);

        return (arg1, arg2) -> after.apply(apply(arg1, arg2));
    }
}
