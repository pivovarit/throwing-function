package com.pivovarit.function;

import static com.pivovarit.function.TestCommons.givenThrowingFunction;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ThrowingFunctionTest {

    @Test
    void shouldCompose() throws Exception {
        // given
        ThrowingFunction<Integer, Integer, Exception> f1 = i -> 2 * i;
        ThrowingFunction<Integer, Integer, Exception> f2 = i -> i + 10;

        // when
        Integer result = f1.compose(f2).apply(3);

        // then
        assertThat(result).isEqualTo(26);
    }

    @Test
    void shouldAndThen() throws Exception {
        // given
        ThrowingFunction<Integer, Integer, Exception> f1 = i -> 2 * i;
        ThrowingFunction<Integer, Integer, Exception> f2 = i -> i + 10;

        // when
        Integer result = f1.andThen(f2).apply(3);

        // then
        assertThat(result).isEqualTo(16);
    }

    @Test
    void shouldReturnOptional() {
        // given
        ThrowingFunction<Integer, Integer, Exception> f1 = i -> i;

        // when
        Optional<Integer> result = f1.lift().apply(42);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void shouldReturnEmptyOptional() throws Exception {
        // given
        ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        Optional<Integer> result = f1.lift().apply(42);

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void shouldThrowEx() throws Exception {
        ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction("custom exception message");

        assertThatThrownBy(() -> f1.apply(42))
          .isInstanceOf(Exception.class)
          .hasMessage("custom exception message");
    }

    @Test
    void shouldWrapInRuntimeEx() throws Exception {
        ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction("custom exception message");

        // when
        assertThatThrownBy(() -> f1.uncheck().apply(42))
          .isInstanceOf(Exception.class)
          .hasMessage("custom exception message");
    }

    /* Disabled test method as it will contradict with the sneaky throws approach implemented in ThrowingFunction#uncheck */
    @Disabled
    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        assertThatThrownBy(() -> Stream.of(". .")
          .map(ThrowingFunction.unchecked(URI::new))
          .collect(toList()))
          .isInstanceOf(RuntimeException.class)
          .hasMessage("Illegal character in path at index 1: . .")
          .hasCauseInstanceOf(URISyntaxException.class);
    }

    @Test
    void shouldApplyWhenNoExceptionThrown() {
        //given
        ThrowingFunction<String, String, Exception> f = String::toUpperCase;

        // when
        Stream.of(". .").map(f.uncheck()).collect(toList());

        // then no exception thrown
    }

    @Test
    void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() {

        // when
        Long result = Stream.of(". .")
          .map(ThrowingFunction.lifted(URI::new))
          .filter(Optional::isPresent)
          .count();

        //then
        assertThat(result).isZero();
    }
    
    @Test
    void shouldSneakyThrowsException() {
      assertThatThrownBy(() -> ThrowingFunction.unchecked((String uri) -> new URI(uri)).apply(". ."))
        .isInstanceOf(URISyntaxException.class);
    }
}