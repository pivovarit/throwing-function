package touk.pl.function;

import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Exception> {
    void accept(T t, U u) throws E;

    default ThrowingBiConsumer<T, U, E> andThenConsume(final ThrowingBiConsumer<? super T, ? super U, E> after) {
        Objects.requireNonNull(after);

        return (arg1, arg2) -> {
            accept(arg1, arg2);
            after.accept(arg1, arg2);
        };
    }

    default ThrowingBiFunction<T, U, Void, E> asFunction() {
        return (arg1, arg2) -> {
            accept(arg1, arg2);
            return null;
        };
    }

    default BiConsumer<T, U> wrappedWithRuntimeException() {
        return (arg1, arg2) -> {
            try {
                accept(arg1, arg2);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
