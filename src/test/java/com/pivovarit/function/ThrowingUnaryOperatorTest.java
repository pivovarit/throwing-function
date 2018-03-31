package com.pivovarit.function;

import com.pivovarit.function.exception.WrappedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ThrowingUnaryOperatorTest {

    @Test
    void shouldApply() throws Exception {
        // given
        ThrowingUnaryOperator<Integer, IOException> op = i -> i;

        // when
        op.apply(42);

        // then no exception thrown
    }

    @Test
    void shouldApplyUnchecked() {
        // given
        ThrowingUnaryOperator<Integer, IOException> op = i -> i;

        // when
        ThrowingUnaryOperator.unchecked(op).apply(42);

        // then no exception thrown
    }

    @Test
    void shouldApplyUncheckedAndThrow() {
        final IOException cause = new IOException("some message");

        // given
        ThrowingUnaryOperator<Integer, IOException> op = i -> { throw cause; };

        assertThatThrownBy(() -> {
            ThrowingUnaryOperator.unchecked(op).apply(42);
        }).hasMessage(cause.getMessage())
          .isInstanceOf(WrappedException.class)
          .hasCauseInstanceOf(cause.getClass());
    }

    @Test
    void shouldApplyUncheckedAndThrowNPE() {
        // when
        assertThatThrownBy(() -> {
            ThrowingUnaryOperator.unchecked(null).apply(42);
        }).isInstanceOf(NullPointerException.class);
    }
}
