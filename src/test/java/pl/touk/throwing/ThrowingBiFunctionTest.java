package pl.touk.throwing;

import org.junit.Test;
import pl.touk.throwing.exception.WrappedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ThrowingBiFunctionTest {

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
    public void shouldReturnOptional() throws Exception {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i * j;

        // when
        final Optional<Integer> result = f1.lift().apply(2, 2);

        // then
        assertThat(result).isPresent();
    }

    @Test
    public void shouldReturnEmptyOptional() throws Exception {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw new Exception(); };

        // when
        final Optional<Integer> result = f1.lift().apply(42, 42);

        // then
        assertThat(result).isEmpty();
    }

    @Test(expected = Exception.class)
    public void shouldThrowEx() throws Exception {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw new Exception(); };

        // when
        f1.apply(42, 42);

        // then RuntimeException is thrown
    }

    @Test(expected = WrappedException.class)
    public void shouldWrapInWrappedEx() throws Exception {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw new Exception(); };

        // when
        f1.unchecked().apply(42, 42);

        // then RuntimeException is thrown
    }

    @Test(expected = WrappedException.class)
    public void shouldWrapInRuntimeExWhenUsingUnchecked() throws Exception {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw new Exception(); };

        // when
        ThrowingBiFunction.unchecked(f1).apply(42, 42);

        // then RuntimeException is thrown
    }

    @Test
    public void shouldApplyWhenNoExceptionThrown() throws Exception {
        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i + j;

        // when
        ThrowingBiFunction.unchecked(f1).apply(42, 0);

        // then no exception thrown
    }

    @Test
    public void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() throws Exception {

        // given
        final ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i + j;

        // when
        final Optional<Integer> result = f1.lift().apply(2, 2);

        //then
        assertThat(result).isPresent();
    }
}