package pl.touk.throwing;

import org.junit.Test;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;

public class ThrowingBinaryOperatorTest {

    @Test
    public void shouldApply() throws Exception {
        // given
        ThrowingBinaryOperator<Integer, IOException> op = (i, j) -> i;

        // when
        op.apply(42, 0);

        // then no exception thrown
    }

    @Test
    public void shouldApplyUnchecked() throws Exception {
        // given
        ThrowingBinaryOperator<Integer, IOException> op = (i, j) -> i;

        // when
        ThrowingBiFunction.unchecked(op).apply(42, 0);

        // then no exception thrown
    }

    @Test(expected = WrappedException.class)
    public void shouldApplyUncheckedAndThrow() throws Exception {
        // given
        ThrowingBinaryOperator<Integer, IOException> op = i -> { throw new IOException(); };

        // when
        ThrowingBiFunction.unchecked(op).apply(42, 0);

        // then no exception thrown
    }
}
