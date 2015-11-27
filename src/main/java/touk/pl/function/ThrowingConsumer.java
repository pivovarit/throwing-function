package touk.pl.function;

import java.util.Objects;
import java.util.function.Consumer;

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

    default Consumer<T> wrappedWithRuntimeException() {
        return t -> {
            try {
                accept(t);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
