package com.pivovarit.function;

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
    void shouldGetUncheckedWithUtilsFunction() {
        IOException cause = new IOException("some message");

        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> { throw cause; };

        assertThatThrownBy(() -> ThrowingSupplier.unchecked(supplier).get())
          .isInstanceOf(CheckedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldLiftAndGetWithUtilsFunction() {
        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> 42;

        // when
        Optional<Integer> result = ThrowingSupplier.optional(supplier).get();

        // then
        assertThat(result).isPresent();
    }
}
