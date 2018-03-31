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
    void shouldTestOR() throws Exception {
        // given
        ThrowingPredicate<Integer, Exception> pTrue = i -> true;
        ThrowingPredicate<Integer, Exception> pFalse = i -> false;

        // then
        assertThat(pTrue.or(pFalse).test(42)).isTrue();
        assertThat(pTrue.or(pTrue).test(42)).isTrue();
        assertThat(pFalse.or(pTrue).test(42)).isTrue();
        assertThat(pFalse.or(pFalse).test(42)).isFalse();
    }

    @Test
    void shouldTestXOR() throws Exception {
        // given
        ThrowingPredicate<Integer, Exception> pTrue = i -> true;
        ThrowingPredicate<Integer, Exception> pFalse = i -> false;

        // then
        assertThat(pTrue.xor(pFalse).test(42)).isTrue();
        assertThat(pTrue.xor(pTrue).test(42)).isFalse();
        assertThat(pFalse.xor(pTrue).test(42)).isTrue();
        assertThat(pFalse.xor(pFalse).test(42)).isFalse();
    }

    @Test
    void shouldTestAND() throws Exception {
        // given
        ThrowingPredicate<Integer, Exception> pTrue = i -> true;
        ThrowingPredicate<Integer, Exception> pFalse = i -> false;

        // then
        assertThat(pTrue.and(pFalse).test(42)).isFalse();
        assertThat(pTrue.and(pTrue).test(42)).isTrue();
        assertThat(pFalse.and(pTrue).test(42)).isFalse();
        assertThat(pFalse.and(pFalse).test(42)).isFalse();
    }

    @Test
    void shouldTestNegate() throws Exception {
        // given
        ThrowingPredicate<Integer, Exception> pTrue = i -> true;
        ThrowingPredicate<Integer, Exception> pFalse = i -> false;

        // then
        assertThat(pTrue.negate().test(42)).isFalse();
        assertThat(pFalse.negate().test(42)).isTrue();
    }

    @Test
    void shouldTestAsFunction() {
        // given
        ThrowingPredicate<Integer, Exception> p = i -> true;

        // then
        assertThat(p.asFunction())
          .isInstanceOf(ThrowingFunction.class);
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
    void shouldTestWhenUsingUncheck() {
        // given
        ThrowingPredicate<Integer, Exception> predicate = i -> true;

        // when
        Stream.of(42).anyMatch(predicate.uncheck());

        // then compiles
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUncheck() {
        Exception cause = new Exception("some message");

        // given
        ThrowingPredicate<Integer, Exception> predicate = i -> {
            throw cause;
        };

        // when
        assertThatThrownBy(() -> Stream.of(42).anyMatch(i -> predicate.uncheck().test(i)))
          .isInstanceOf(RuntimeException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }
}