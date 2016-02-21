package pl.touk.throwing;

import java.util.Objects;

/**
 * Represents an action that can be performed.
 * Function might throw a checked exception instance.
 *
 * @param <E> the type of the thrown checked exception
 */
@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {
    void run() throws E;

    static <E extends Exception> Runnable unchecked(ThrowingRunnable<E> runnable) {
        Objects.requireNonNull(runnable);

        return runnable.unchecked();
    }

    /**
     * @return a new Runnable instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Runnable unchecked() {
        return () -> {
            try {
                run();
            } catch (final Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}
