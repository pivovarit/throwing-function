package pl.touk.throwing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

public class ThrowingSupplierTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldGet() throws Exception {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        final Integer result = supplier.get();

        // then
        assertThat(result).isEqualTo(42);
    }

    @Test
    public void shouldGetAsFunction() throws Exception {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        final ThrowingFunction<Void, Integer, IOException> result = supplier.asFunction();

        // then
        assertThat(result.apply(null)).isEqualTo(42);
    }

    @Test
    public void shouldLiftAndGet() {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        final Optional<Integer> result = supplier.lift().get();

        // then
        assertThat(result).isPresent();
    }

    @Test
    public void shouldLiftAndGetEmpty() {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> { throw new IOException(); };

        // when
        final Optional<Integer> result = supplier.lift().get();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldGetUnchecked() {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        final Integer result = supplier.uncheck().get();

        // then
        assertThat(result).isEqualTo(42);
    }

    @Test
    public void shouldGetUncheckedAndThrow() {
        final IOException cause = new IOException("some message");
        
        thrown.expect(WrappedException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> { throw cause; };

        // when
        supplier.uncheck().get();

        // then exception is thrown
        fail("exception expected");
    }

    @Test
    public void shouldGetUncheckedWithUtilsFunction() {
        final IOException cause = new IOException("some message");
        
        thrown.expect(WrappedException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> { throw cause; };

        // when
        ThrowingSupplier.unchecked(supplier).get();

        // then exception is thrown
        fail("exception expected");
    }

    @Test
    public void shouldLiftAndGetWithUtilsFunction() {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        final Optional<Integer> result = ThrowingSupplier.lifted(supplier).get();

        // then
        assertThat(result).isPresent();
    }
}
