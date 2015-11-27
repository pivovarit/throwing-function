package touk.pl.function;

@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;

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
