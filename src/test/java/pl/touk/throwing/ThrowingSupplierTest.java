package pl.touk.throwing;

import org.junit.Test;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ThrowingSupplierTest {

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
    public void shouldLiftAndGet() throws Exception {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        final Optional<Integer> result = supplier.lift().get();

        // then
        assertThat(result).isPresent();
    }

    @Test
    public void shouldLiftAndGetEmpty() throws Exception {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> { throw new IOException(); };

        // when
        final Optional<Integer> result = supplier.lift().get();

        // then
        assertThat(result).isEmpty();
    }

    @Test(expected = WrappedException.class)
    public void shouldGetUnchecked() throws Exception {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> { throw new IOException(); };

        // when
        supplier.unchecked().get();

        // then exception is thrown
    }

    @Test(expected = WrappedException.class)
    public void shouldGetUncheckedWithUtilsFunction() throws Exception {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> { throw new IOException(); };

        // when
        ThrowingSupplier.unchecked(supplier).get();

        // then exception is thrown
    }

    @Test
    public void shouldLiftAndGetWithUtilsFunction() throws Exception {
        // given
        final ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        final Optional<Integer> result = ThrowingSupplier.lifted(supplier).get();

        // then
        assertThat(result).isPresent();
    }
}
