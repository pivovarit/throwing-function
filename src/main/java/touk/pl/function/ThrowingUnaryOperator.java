package touk.pl.function;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface ThrowingUnaryOperator<T, E extends Exception> extends ThrowingFunction<T, T, E> {

    static <T, E extends Exception> ThrowingUnaryOperator<T, E> identity() {
        return t -> t;
    }

    default ThrowingUnaryOperator<T, E> applyBefore(final ThrowingUnaryOperator<T, E> operator) {
        return t -> operator.apply(apply(t));
    }

    default ThrowingUnaryOperator<T, E> applyAfter(final ThrowingUnaryOperator<T, E> operator) {
        return t -> apply(operator.apply(t));
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
