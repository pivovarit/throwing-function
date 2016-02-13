package pl.touk.throwing;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents a function that accepts one argument and does not return any value;
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the input to the function
 * @param <E> the type of the thrown checked exception
 *
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

    void accept(T t) throws E;

    /**
     * Chains given ThrowingConsumer instance
     * @param after - consumer that is chained after this instance
     * @return chained Consumer instance
     */
    default ThrowingConsumer<T, E> andThenConsume(final ThrowingConsumer<? super T, E> after) {
        Objects.requireNonNull(after);

        return t -> {
            accept(t);
            after.accept(t);
        };
    }

    /**
     * @return this consumer instance as a Function instance
     */
    default ThrowingFunction<T, Void, E> asFunction() {
        return arg -> {
            this.accept(arg);
            return null;
        };
    }

    static <T, E extends Exception> Consumer<T> unchecked(ThrowingConsumer<T, E> consumer) {
        Objects.requireNonNull(consumer);

        return consumer.unchecked();
    }

    /**
     * @return a Consumer instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Consumer<T> unchecked() {
        return t -> {
            try {
                accept(t);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
