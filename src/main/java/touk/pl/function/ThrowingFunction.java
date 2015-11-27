package touk.pl.function;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public interface ThrowingFunction<T,R,E extends Exception> {
    R apply(T arg) throws E;

    default <V> ThrowingFunction<V, R, E> compose(final ThrowingFunction<? super V, ? extends T, E> before) {
        Objects.requireNonNull(before);

        return (V v) -> apply(before.apply(v));
    }

    default <V> ThrowingFunction<T, V, E> andThen(ThrowingFunction<? super R, ? extends V, E> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    default Function<T, Optional<R>> toOptionalFunction() {
        return t -> {
            try {
                return Optional.of(apply(t));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }


}
