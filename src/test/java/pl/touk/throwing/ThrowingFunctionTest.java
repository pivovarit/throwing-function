package pl.touk.throwing;

import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.touk.throwing.ThrowingFunction.unchecked;

public class ThrowingFunctionTest {

    @Test
    public void shouldCompose() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = i -> 2 * i;
        final ThrowingFunction<Integer, Integer, Exception> f2 = i -> i + 10;

        // when
        final Integer result = f1.compose(f2).apply(3);

        // then
        assertThat(result).isEqualTo(26);
    }


    @Test
    public void shouldAndThen() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = i -> 2 * i;
        final ThrowingFunction<Integer, Integer, Exception> f2 = i -> i + 10;

        // when
        final Integer result = f1.andThen(f2).apply(3);

        // then
        assertThat(result).isEqualTo(16);
    }

    @Test
    public void shouldReturnOptional() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = ThrowingFunction.identity();

        // when
        final Optional<Integer> result = f1.returningOptional().apply(42);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void shouldReturnEmptyOptional() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        final Optional<Integer> result = f1.returningOptional().apply(42);

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test(expected = RuntimeException.class)
    public void shouldWrapInRuntimeEx() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        f1.unchecked().apply(42);

        // then RuntimeException is thrown
    }

    @Test(expected = RuntimeException.class)
    public void shouldWrapInRuntimeExWhenUsingStandardUtilsFunctions() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        Collections.singletonList(null).stream().forEach(arg -> unchecked(f1).apply(42));

        // then RuntimeException is thrown
    }

    private ThrowingFunction<Integer, Integer, Exception> givenThrowingFunction() throws Exception {
        return i -> {
            throw new Exception();
        };
    }
}