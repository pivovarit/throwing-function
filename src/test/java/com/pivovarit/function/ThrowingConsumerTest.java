package com.pivovarit.function;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingConsumerTest {

    @Test
    void shouldConsume() throws Exception {
        // given
        Integer[] input = {0};

        ThrowingConsumer<Integer, Exception> consumer = i -> input[0] = 2;

        // when
        consumer.accept(2);

        // then
        assertThat(input[0]).isEqualTo(2);
    }


    @Test
    void shouldConsumeAndThrowUnchecked() {
        IOException cause = new IOException("some message");

        // given
        ThrowingConsumer<Integer, IOException> consumer = i -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingConsumer.unchecked(consumer).accept(3))
          .isInstanceOf(CheckedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldConsumeUnchecked() {
        // given
        ThrowingConsumer<Integer, IOException> consumer = i -> {};

        // when
        ThrowingConsumer.unchecked(consumer).accept(3);

        // then no exception thrown
    }
}
