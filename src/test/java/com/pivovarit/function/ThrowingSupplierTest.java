package com.pivovarit.function;

import com.pivovarit.function.exception.WrappedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingSupplierTest {

    @Test
    void shouldGet() throws Exception {
        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        Integer result = supplier.get();

        // then
        assertThat(result).isEqualTo(42);
    }

    @Test
    void shouldGetAsFunction() throws Exception {
        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        ThrowingFunction<Void, Integer, IOException> result = supplier.asFunction();

        // then
        assertThat(result.apply(null)).isEqualTo(42);
    }

    @Test
    void shouldLiftAndGet() {
        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        Optional<Integer> result = supplier.lift().get();

        // then
        assertThat(result).isPresent();
    }

    @Test
    void shouldLiftAndGetEmpty() {
        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> { throw new IOException(); };

        // when
        Optional<Integer> result = supplier.lift().get();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldLiftAndGetEmptyWhenNull() {
        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> null;

        // when
        Optional<Integer> result = supplier.lift().get();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldGetUnchecked() {
        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        Integer result = supplier.uncheck().get();

        // then
        assertThat(result).isEqualTo(42);
    }

    @Test
    void shouldGetUncheckedAndThrow() {
        IOException cause = new IOException("some message");

        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> { throw cause; };

        // when

        assertThatThrownBy(() -> supplier.uncheck().get())
          .isInstanceOf(WrappedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldGetUncheckedWithUtilsFunction() {
        IOException cause = new IOException("some message");

        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> { throw cause; };

        assertThatThrownBy(() -> ThrowingSupplier.unchecked(supplier).get())
          .isInstanceOf(WrappedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldLiftAndGetWithUtilsFunction() {
        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        Optional<Integer> result = ThrowingSupplier.lifted(supplier).get();

        // then
        assertThat(result).isPresent();
    }
}
