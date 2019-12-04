package com.pivovarit.function;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Stream;

import static com.pivovarit.function.TestCommons.givenThrowingFunction;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingFunctionTest {

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
    void shouldReturnEmptyOptional() {
        // given
        ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        Optional<Integer> result = f1.lift().apply(42);

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void shouldThrowEx() {
        ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction("custom exception message");

        assertThatThrownBy(() -> f1.apply(42))
          .isInstanceOf(Exception.class)
          .hasMessage("custom exception message");
    }

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
    void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() {

        // when
        Long result = Stream.of(". .")
          .map(ThrowingFunction.lifted(URI::new))
          .filter(Optional::isPresent)
          .count();

        //then
        assertThat(result).isZero();
    }
}