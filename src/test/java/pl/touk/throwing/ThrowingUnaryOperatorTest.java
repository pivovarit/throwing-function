package pl.touk.throwing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pl.touk.throwing.exception.WrappedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import java.io.IOException;

public class ThrowingUnaryOperatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldApply() throws Exception {
        // given
        ThrowingUnaryOperator<Integer, IOException> op = i -> i;

        // when
        op.apply(42);

        // then no exception thrown
    }

    @Test
    public void shouldApplyUnchecked() {
        // given
        ThrowingUnaryOperator<Integer, IOException> op = i -> i;

        // when
        ThrowingUnaryOperator.unchecked(op).apply(42);

        // then no exception thrown
    }

    @Test
    public void shouldApplyUncheckedAndThrow() {
        final IOException cause = new IOException("some message");
        
        thrown.expect(WrappedException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        ThrowingUnaryOperator<Integer, IOException> op = i -> { throw cause; };

        // when
        ThrowingUnaryOperator.unchecked(op).apply(42);

        // then an exception is thrown
        fail("exception expected");
    }
    
    @Test
    public void shouldApplyUncheckedAndThrowNPE() {
        thrown.expect(NullPointerException.class);

        // when
        ThrowingUnaryOperator.unchecked(null).apply(42);

        // then NPE is thrown
        fail("exception expected");
    }

}
