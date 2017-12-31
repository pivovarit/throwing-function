package pl.touk.throwing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ThrowingBiConsumerTest {

    @Test
    void shouldConsume() throws Exception {
        // given
        Integer[] input = {0};

        ThrowingBiConsumer<Integer, Integer, Exception> consumer = (i, j) -> input[0] = 2;

        // when
        consumer.accept(2, 3);

        // then
        Assertions.assertThat(input[0]).isEqualTo(2);
    }

    @Test
    void shouldConsumeAfter() throws Exception {
        // given
        Integer[] input = {0};

        ThrowingBiConsumer<Integer, Integer, Exception> consumer = (i, j) -> input[0] = 2;
        ThrowingBiConsumer<Integer, Integer, Exception> after = (i, j) -> input[0] = 3;

        // when
        consumer.andThenConsume(after).accept(2, 3);

        // then
        Assertions.assertThat(input[0]).isEqualTo(3);
    }

    @Test
    void shouldConsumeAsFunction() throws Exception {
        // given
        Integer[] input = {0};

        ThrowingBiConsumer<Integer, Integer, Exception> consumer = (i, j) -> input[0] = 2;

        // when
        consumer.asFunction().apply(42, 0);

        // then
        Assertions.assertThat(input[0]).isEqualTo(2);
    }

    @Test
    void shouldConsumeAndThrowUnchecked() {
        IOException cause = new IOException("some message");

        // given
        ThrowingBiConsumer<Integer, Integer, IOException> consumer = (i, j) -> { throw cause; };

        // when
        assertThatThrownBy(() -> {
            ThrowingBiConsumer.unchecked(consumer).accept(3, 3);
        }).hasMessage(cause.getMessage())
          .isInstanceOf(WrappedException.class)
          .hasCauseInstanceOf(cause.getClass());
    }

    @Test
    void shouldConsumeUnchecked() {
        // given
        ThrowingBiConsumer<Integer, Integer, IOException> consumer = (i, j) -> {};

        // when
        ThrowingBiConsumer.unchecked(consumer).accept(3, 4);

        // then no exception thrown
    }
}