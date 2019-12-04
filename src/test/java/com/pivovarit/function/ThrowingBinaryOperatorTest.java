package com.pivovarit.function;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingBinaryOperatorTest {

    @Test
    void shouldThrowEx() {
        // given
        ThrowingBinaryOperator<Integer, Exception> f1 =
          (i, j) -> { throw new Exception("some message"); };

        // when
        assertThatThrownBy(() -> f1.apply(42, 42))
          .isInstanceOf(Exception.class)
          .hasMessage("some message");
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        Exception cause = new Exception("some message");

        // given
        ThrowingBinaryOperator<Integer, Exception> f1 =
          (i, j) -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingBinaryOperator.unchecked(f1).apply(42, 42))
          .isInstanceOf(CheckedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldApplyWhenNoExceptionThrown() {
        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = Integer::sum;

        // when
        ThrowingBinaryOperator.unchecked(f1).apply(42, 0);

        // then no exception thrown
    }

    @Test
    void shouldWrapInOptionalWhenUsingLifted() {

        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = Integer::sum;

        // when
        Optional<Integer> result =
          ThrowingBiFunction.optional(f1).apply(2, 2);

        //then
        assertThat(result).isPresent();
    }
}
