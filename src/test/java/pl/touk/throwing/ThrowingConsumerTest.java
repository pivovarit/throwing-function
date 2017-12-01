package pl.touk.throwing;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pl.touk.throwing.exception.WrappedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import java.io.IOException;

public class ThrowingConsumerTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
    public void shouldConsumeAfter() throws Exception {
        // given
        final Integer[] input = {0};

        ThrowingConsumer<Integer, Exception> consumer = i -> input[0] = 2;
        ThrowingConsumer<Integer, Exception> after = i -> input[0] = 3;

        // when
        consumer.andThenConsume(after).accept(2);

        // then
        Assertions.assertThat(input[0]).isEqualTo(3);
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

    @Test
    public void shouldConsumeAndThrowUnchecked() {
        final IOException cause = new IOException("some message");
        
        thrown.expect(WrappedException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        ThrowingConsumer<Integer, IOException> consumer = i -> { throw cause; };

        // when
        ThrowingConsumer.unchecked(consumer).accept(3);

        // then WrappedException was thrown
        fail("exception expected");
    }

    @Test
    public void shouldConsumeUnchecked() {
        // given
        ThrowingConsumer<Integer, IOException> consumer = i -> {};

        // when
        ThrowingConsumer.unchecked(consumer).accept(3);

        // then no exception thrown
    }
}
