package touk.pl.function;

import java.util.Objects;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;

    default ThrowingConsumer<T, E> andThenConsume(final ThrowingConsumer<? super T, E> after) {
        Objects.requireNonNull(after);

        return t -> {
            accept(t);
            after.accept(t);
        };
    }
}
