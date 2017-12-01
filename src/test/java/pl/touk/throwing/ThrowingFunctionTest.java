package pl.touk.throwing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.fail;
import static pl.touk.throwing.TestCommons.givenThrowingFunction;
import static pl.touk.throwing.ThrowingFunction.lifted;
import static pl.touk.throwing.ThrowingFunction.unchecked;

public class ThrowingFunctionTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
    public void shouldReturnOptional() {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = ThrowingFunction.identity();

        // when
        final Optional<Integer> result = f1.lift().apply(42);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void shouldReturnEmptyOptional() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        final Optional<Integer> result = f1.lift().apply(42);

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldThrowEx() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("custom exception message");
        
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction("custom exception message");

        // when
        f1.apply(42);

        // then RuntimeException is thrown
        fail("exception expected");
    }

    @Test
    public void shouldWrapInRuntimeEx() throws Exception {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("custom exception message");
        
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction("custom exception message");

        // when
        f1.uncheck().apply(42);

        // then RuntimeException is thrown
        fail("exception expected");
    }

    @Test
    public void shouldWrapInRuntimeExWhenUsingUnchecked() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Illegal character in path at index 1: . .");
        thrown.expectCause(isA(URISyntaxException.class));

        // when
        Stream.of(". .").map(unchecked(URI::new)).collect(toList());

        // then RuntimeException is thrown
        fail("exception expected");
    }

    @Test
    public void shouldApplyWhenNoExceptionThrown() {
        //given
        ThrowingFunction<String, String, Exception> f = String::toUpperCase;

        // when
        Stream.of(". .").map(f.uncheck()).collect(toList());

        // then no exception thrown
    }

    @Test
    public void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() {

        // when
        final Long result = Stream.of(". .")
                .map(lifted(URI::new))
                .filter(Optional::isPresent)
                .count();

        //then
        assertThat(result).isZero();
    }
}