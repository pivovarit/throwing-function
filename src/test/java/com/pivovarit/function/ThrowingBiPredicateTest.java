package com.pivovarit.function;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingBiPredicateTest {

    @Test
    void shouldTest() throws Exception {
        // given
        ThrowingBiPredicate<Integer, Integer, Exception> p = (i, j) -> true;

        // when
        boolean result = p.test(42, 0);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingStandardUtilsFunctions() {
        Exception cause = new Exception("some message");

        // given
        ThrowingBiPredicate<Integer, Integer, Exception> predicate = (i, j) -> {
            throw cause;
        };

        // when
        assertThatThrownBy(() -> ThrowingBiPredicate.unchecked(predicate).test(42, 0))
          .isInstanceOf(RuntimeException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }
}