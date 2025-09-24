package com.pivovarit.function;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    void returnValueWhenExceptionOccurs() {
        Exception cause = new Exception("some message");

        // given
        ThrowingPredicate<Integer, Exception> predicate = i -> {
            throw cause;
        };

        // when
        assertThatCode(() -> Stream.of(42).anyMatch(i -> ThrowingPredicate.uncheckedFalse(predicate).test(i)))
                .doesNotThrowAnyException();
        assertThatCode(() -> Stream.of(42).anyMatch(i -> ThrowingPredicate.uncheckedTrue(predicate).test(i)))
                .doesNotThrowAnyException();

        assertThat(Stream.of(42).anyMatch(i -> ThrowingPredicate.uncheckedFalse(predicate).test(i))).isFalse();
        assertThat(Stream.of(42).anyMatch(i -> ThrowingPredicate.uncheckedTrue(predicate).test(i))).isTrue();
    }
}