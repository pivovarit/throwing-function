package touk.pl.function;

import java.util.Comparator;
import java.util.Objects;

public interface ThrowingBinaryOperator<T, E extends Exception> extends ThrowingBiFunction<T, T, T, E> {

    static <T, E extends Exception> ThrowingBinaryOperator<T, E> minBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
    }

    static <T, E extends Exception> ThrowingBinaryOperator<T, E> maxBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
    }
}
