package pl.touk.throwing;

import org.junit.Test;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;

public class ThrowingRunnableTest {

    @Test(expected = IOException.class)
    public void shouldRun() throws Exception {
        // given
        ThrowingRunnable<Exception> runnable = () -> { throw new IOException(); };

        // when
        runnable.run();

        // then exception thrown
    }

    @Test(expected = WrappedException.class)
    public void shouldRunUnchecked() throws Exception {
        // given
        ThrowingRunnable<Exception> runnable = () -> { };

        // when
        runnable.unchecked().run();

        // then WrappedException thrown
    }

    @Test(expected = WrappedException.class)
    public void shouldRunUncheckedAndThrow() throws Exception {
        // given
        ThrowingRunnable<Exception> runnable = () -> { throw new IOException(); };

        // when
        runnable.unchecked().run();

        // then WrappedException thrown
    }

    @Test(expected = WrappedException.class)
    public void shouldRunUncheckedAndThrowUsingUtilsMethod() throws Exception {
        // given
        ThrowingRunnable<Exception> runnable = () -> { throw new IOException(); };

        // when
        ThrowingRunnable.unchecked(runnable).run();

        // then WrappedException thrown
    }

}
