package pl.touk.throwing;

/**
 * Represents an action that can be performed.
 * Function might throw a checked exception instance.
 *
 * @param <E> the type of the thrown checked exception
 */
@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;


    /**
     * Returns a new Runnable instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Runnable wrappedWithRuntimeException() {
        return () -> {
            try {
                run();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
