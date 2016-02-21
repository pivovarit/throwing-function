package pl.touk.throwing;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result.  This is the two-arity specialization of {@link ThrowingConsumer}.
 * Unlike most other functional interfaces, {@code ThrowingBiConsumer}  is expected
 * to operate via side-effects.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <E> the type of the thrown checked exception
 *
 * @see ThrowingConsumer
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Throwable> {
    void accept(T t, U u) throws E;

    default ThrowingBiConsumer<T, U, E> andThenConsume(final ThrowingBiConsumer<? super T, ? super U, E> after) {
        Objects.requireNonNull(after);

        return (arg1, arg2) -> {
            accept(arg1, arg2);
            after.accept(arg1, arg2);
        };
    }

    /**
     * Returns this ThrowingBiConsumer instance as a ThrowingBiFunction
     * @return this action as a ThrowingBiFunction
     */
    default ThrowingBiFunction<T, U, Void, E> asFunction() {
        return (arg1, arg2) -> {
            accept(arg1, arg2);
            return null;
        };
    }

    static <T, U, E extends Exception> BiConsumer<T, U> unchecked(ThrowingBiConsumer<T, U, E> consumer) {
        Objects.requireNonNull(consumer);

        return consumer.unchecked();
    }

    /**
     * Returns a new BiConsumer instance which wraps thrown checked exception instance into a RuntimeException
     * @return BiConsumer instance that packages checked exceptions into RuntimeException instances
     */
    default BiConsumer<T, U> unchecked() {
        return (arg1, arg2) -> {
            try {
                accept(arg1, arg2);
            } catch (final Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}
