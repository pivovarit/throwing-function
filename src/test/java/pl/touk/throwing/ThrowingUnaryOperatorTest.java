package pl.touk.throwing;

import org.junit.Test;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;

public class ThrowingUnaryOperatorTest {

    @Test
    public void shouldApply() throws Exception {
        // given
        ThrowingUnaryOperator<Integer, IOException> op = i -> i;

        // when
        op.apply(42);

        // then no exception thrown
    }

    @Test
    public void shouldApplyUnchecked() throws Exception {
        // given
        ThrowingUnaryOperator<Integer, IOException> op = i -> i;

        // when
        ThrowingUnaryOperator.unchecked(op).apply(42);

        // then no exception thrown
    }

    @Test(expected = WrappedException.class)
    public void shouldApplyUncheckedAndThrow() throws Exception {
        // given
        ThrowingUnaryOperator<Integer, IOException> op = i -> { throw new IOException(); };

        // when
        ThrowingUnaryOperator.unchecked(op).apply(42);

        // then an exception is thrown
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldApplyUncheckedAndThrowNPE() throws Exception {

        // when
        ThrowingUnaryOperator.unchecked(null).apply(42);

        // then NPE is thrown
    }

}
