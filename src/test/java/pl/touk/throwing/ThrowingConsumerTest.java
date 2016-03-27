package pl.touk.throwing;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;

public class ThrowingConsumerTest {

    @Test
    public void shouldConsume() throws Exception {
        // given
        final Integer[] input = {0};

        ThrowingConsumer<Integer, Exception> consumer = i -> input[0] = 2;

        // when
        consumer.accept(2);

        // then
        Assertions.assertThat(input[0]).isEqualTo(2);
    }

    @Test
    public void shouldConsumeAsFunction() throws Exception {
        // given
        final Integer[] input = {0};

        ThrowingConsumer<Integer, Exception> consumer = i -> input[0] = 2;

        // when
        consumer.asFunction().apply(42);

        // then
        Assertions.assertThat(input[0]).isEqualTo(2);
    }

    @Test(expected = WrappedException.class)
    public void shouldConsumeAndThrowUnchecked() throws Exception {
        // given
        ThrowingConsumer<Integer, IOException> consumer = i -> { throw new IOException(); };

        // when
        ThrowingConsumer.unchecked(consumer).accept(3);

        // then WrappedException was thrown
    }
}
