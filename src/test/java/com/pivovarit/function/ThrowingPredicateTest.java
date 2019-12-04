package com.pivovarit.function;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingPredicateTest {

    @Test
    void shouldTest() throws Exception {
        // given
        ThrowingPredicate<Integer, Exception> p = i -> true;

        // when
        boolean result = p.test(42);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingStandardUtilsFunctions() {
        Exception cause = new Exception("some message");

        // given
        ThrowingPredicate<Integer, Exception> predicate = i -> {
            throw cause;
        };

        // when
        assertThatThrownBy(() -> Stream.of(42).anyMatch(i -> ThrowingPredicate.unchecked(predicate).test(i)))
          .isInstanceOf(RuntimeException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }
}