package touk.pl.function;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface ThrowingUnaryOperator<T, E extends Exception> extends ThrowingFunction<T, T, E> {

    static <T, E extends Exception> ThrowingUnaryOperator<T, E> identity() {
        return t -> t;
    }

    default UnaryOperator<T> wrappedWithRuntimeException() {
        return t -> {
            try {
                return apply(t);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
