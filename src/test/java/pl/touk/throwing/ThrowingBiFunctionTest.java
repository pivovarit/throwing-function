package pl.touk.throwing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.touk.throwing.exception.WrappedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

public class ThrowingBiFunctionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldAndThen() throws Exception {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i * j;
        final ThrowingFunction<Integer, Integer, Exception> f2 = i -> i + 10;

        // when
        final Integer result = f1.andThen(f2).apply(2, 2);

        // then
        assertThat(result).isEqualTo(14);
    }

    @Test
    public void shouldReturnOptional() {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i * j;

        // when
        final Optional<Integer> result = f1.lift().apply(2, 2);

        // then
        assertThat(result).isPresent();
    }

    @Test
    public void shouldReturnEmptyOptional() {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw new Exception(); };

        // when
        final Optional<Integer> result = f1.lift().apply(42, 42);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldThrowEx() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("some message");

        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> {
            throw new Exception("some message");
        };

        // when
        f1.apply(42, 42);

        // then RuntimeException is thrown
        fail("exception expected");
    }

    @Test
    public void shouldWrapInWrappedEx() {
        final Exception cause = new Exception("some message");

        thrown.expect(WrappedException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw cause; };

        // when
        f1.unchecked().apply(42, 42);

        // then RuntimeException is thrown
        fail("exception expected");
    }

    @Test
    public void shouldWrapInRuntimeExWhenUsingUnchecked() {
        final Exception cause = new Exception("some message");

        thrown.expect(WrappedException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw cause; };

        // when
        ThrowingBiFunction.unchecked(f1).apply(42, 42);

        // then RuntimeException is thrown
        fail("exception expected");
    }

    @Test
    public void shouldApplyWhenNoExceptionThrown() {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i + j;

        // when
        ThrowingBiFunction.unchecked(f1).apply(42, 0);

        // then no exception thrown
    }

    @Test
    public void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() {

        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i + j;

        // when
        final Optional<Integer> result = f1.lift().apply(2, 2);

        //then
        assertThat(result).isPresent();
    }

    @Test
    public void shouldWrapInOptionalWhenUsingLifted() {

        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i + j;

        // when
        final Optional<Integer> result = ThrowingBiFunction.lifted(f1).apply(2, 2);

        //then
        assertThat(result).isPresent();
    }
}