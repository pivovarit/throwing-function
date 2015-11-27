package touk.pl.function;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;

    static <T, E extends Exception> ThrowingSupplier<T, E> empty() {
        return () -> null;
    }
}
