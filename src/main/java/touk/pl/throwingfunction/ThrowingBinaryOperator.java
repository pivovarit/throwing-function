package touk.pl.throwingfunction;

import java.util.Comparator;
import java.util.Objects;

/**
 * Represents an operation upon two operands of the same type, producing a result
 * of the same type as the operands.  This is a specialization of
 * {@link ThrowingBiFunction} for the case where the operands and the result are all of
 * the same type.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object)}.
 *
 * @param <T> the type of the operands and result of the operator
 * @param <E> the type of the thrown checked exception
 *
 * @see ThrowingBiFunction
 * @see ThrowingUnaryOperator
 */
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
