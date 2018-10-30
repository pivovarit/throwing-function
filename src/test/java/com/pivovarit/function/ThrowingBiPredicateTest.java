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
    void shouldTestOR() throws Exception {
        // given
        ThrowingBiPredicate<Integer, Integer, Exception> pTrue = (i, j) -> true;
        ThrowingBiPredicate<Integer, Integer, Exception> pFalse = (i, j) -> false;

        // then
        assertThat(pTrue.or(pFalse).test(42, 0)).isTrue();
        assertThat(pTrue.or(pTrue).test(42, 0)).isTrue();
        assertThat(pFalse.or(pTrue).test(42, 0)).isTrue();
        assertThat(pFalse.or(pFalse).test(42, 0)).isFalse();
    }

    @Test
    void shouldTestXOR() throws Exception {
        // given
        ThrowingBiPredicate<Integer, Integer, Exception> pTrue = (i, j) -> true;
        ThrowingBiPredicate<Integer, Integer, Exception> pFalse = (i, j) -> false;

        // then
        assertThat(pTrue.xor(pFalse).test(42, 0)).isTrue();
        assertThat(pTrue.xor(pTrue).test(42, 0)).isFalse();
        assertThat(pFalse.xor(pTrue).test(42, 0)).isTrue();
        assertThat(pFalse.xor(pFalse).test(42, 0)).isFalse();
    }

    @Test
    void shouldTestAND() throws Exception {
        // given
        ThrowingBiPredicate<Integer, Integer, Exception> pTrue = (i, j) -> true;
        ThrowingBiPredicate<Integer, Integer, Exception> pFalse = (i, j) -> false;

        // then
        assertThat(pTrue.and(pFalse).test(42, 0)).isFalse();
        assertThat(pTrue.and(pTrue).test(42, 0)).isTrue();
        assertThat(pFalse.and(pTrue).test(42, 0)).isFalse();
        assertThat(pFalse.and(pFalse).test(42, 0)).isFalse();
    }

    @Test
    void shouldTestNegate() throws Exception {
        // given
        ThrowingBiPredicate<Integer, Integer, Exception> pTrue = (i, j) -> true;
        ThrowingBiPredicate<Integer, Integer, Exception> pFalse = (i, j) -> false;

        // then
        assertThat(pTrue.negate().test(42, 0)).isFalse();
        assertThat(pFalse.negate().test(42, 0)).isTrue();
    }

    @Test
    void shouldTestAsFunction() throws Exception {
        // given
        ThrowingBiPredicate<Integer, Integer, Exception> p = (i, j) -> true;

        // then
        assertThat(p.asFunction().apply(1, 2)).isTrue();
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

    @Test
    void shouldTestWhenUsingUncheck() {
        // given
        ThrowingBiPredicate<Integer, Integer, Exception> predicate = (i, j) -> true;

        // when
        predicate.uncheck().test(1, 2);

        // then no exception is thrown
    }
}