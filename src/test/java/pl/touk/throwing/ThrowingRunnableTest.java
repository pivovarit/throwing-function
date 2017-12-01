package pl.touk.throwing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pl.touk.throwing.exception.WrappedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import java.io.IOException;

public class ThrowingRunnableTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldRun() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("some message");
        
        // given
        ThrowingRunnable<Exception> runnable = () -> { throw new IOException("some message"); };

        // when
        runnable.run();

        // then exception thrown
        fail("exception expected");
    }

    @Test
    public void shouldRunUnchecked() {
        // given
        ThrowingRunnable<Exception> runnable = () -> { };

        // when
        runnable.unchecked().run();

        // then WrappedException thrown
    }

    @Test
    public void shouldRunUncheckedAndThrow() {
        final IOException cause = new IOException("some message");
        
        thrown.expect(WrappedException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        ThrowingRunnable<Exception> runnable = () -> { throw cause; };

        // when
        runnable.unchecked().run();

        // then WrappedException thrown
        fail("exception expected");
    }

    @Test
    public void shouldRunUncheckedAndThrowUsingUtilsMethod() {
        final IOException cause = new IOException("some message");
        
        thrown.expect(WrappedException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        ThrowingRunnable<Exception> runnable = () -> { throw cause; };

        // when
        ThrowingRunnable.unchecked(runnable).run();

        // then WrappedException thrown
        fail("exception expected");
    }

}
