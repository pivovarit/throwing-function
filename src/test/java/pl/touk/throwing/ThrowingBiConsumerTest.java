package pl.touk.throwing;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

public class ThrowingBiConsumerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldConsume() throws Exception {
        // given
        final Integer[] input = {0};

        ThrowingBiConsumer<Integer, Integer, Exception> consumer = (i, j) -> input[0] = 2;

        // when
        consumer.accept(2, 3);

        // then
        Assertions.assertThat(input[0]).isEqualTo(2);
    }

    @Test
    public void shouldConsumeAfter() throws Exception {
        // given
        final Integer[] input = {0};

        ThrowingBiConsumer<Integer, Integer, Exception> consumer = (i, j) -> input[0] = 2;
        ThrowingBiConsumer<Integer, Integer, Exception> after = (i, j) -> input[0] = 3;

        // when
        consumer.andThenConsume(after).accept(2, 3);

        // then
        Assertions.assertThat(input[0]).isEqualTo(3);
    }

    @Test
    public void shouldConsumeAsFunction() throws Exception {
        // given
        final Integer[] input = {0};

        ThrowingBiConsumer<Integer, Integer, Exception> consumer = (i, j) -> input[0] = 2;

        // when
        consumer.asFunction().apply(42, 0);

        // then
        Assertions.assertThat(input[0]).isEqualTo(2);
    }

    @Test
    public void shouldConsumeAndThrowUnchecked() {
        final IOException cause = new IOException("some message");

        thrown.expect(WrappedException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        ThrowingBiConsumer<Integer, Integer, IOException> consumer = (i, j) -> { throw cause; };

        // when
        ThrowingBiConsumer.unchecked(consumer).accept(3, 3);

        // then WrappedException was thrown
        fail("exception expected");
    }

    @Test
    public void shouldConsumeUnchecked() {
        // given
        ThrowingBiConsumer<Integer, Integer, IOException> consumer = (i, j) -> {};

        // when
        ThrowingBiConsumer.unchecked(consumer).accept(3, 4);

        // then no exception thrown
    }
}
