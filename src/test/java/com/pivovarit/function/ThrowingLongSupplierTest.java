/*
 * Copyright 2014-2026 Grzegorz Piwowarek, https://4comprehension.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pivovarit.function;

import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingLongSupplierTest {

    @Test
    void shouldGet() throws Exception {
        ThrowingLongSupplier<Exception> supplier = () -> 42L;
        assertThat(supplier.getAsLong()).isEqualTo(42L);
    }

    @Test
    void shouldReturnValueWhenUncheckedAndNoException() {
        ThrowingLongSupplier<Exception> supplier = () -> 5L;
        assertThat(ThrowingLongSupplier.unchecked(supplier).getAsLong()).isEqualTo(5L);
    }

    @Test
    void shouldWrapInCheckedException() {
        ThrowingLongSupplier<Exception> supplier = () -> { throw new IOException("boom"); };
        assertThatThrownBy(ThrowingLongSupplier.unchecked(supplier)::getAsLong)
          .isInstanceOf(CheckedException.class)
          .hasMessage("boom");
    }

    @Test
    void shouldReturnValueWhenSneakyAndNoException() {
        ThrowingLongSupplier<Exception> supplier = () -> 5L;
        assertThat(ThrowingLongSupplier.sneaky(supplier).getAsLong()).isEqualTo(5L);
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("io");
        ThrowingLongSupplier<IOException> supplier = () -> { throw cause; };
        assertThatThrownBy(() -> ThrowingLongSupplier.sneaky(supplier).getAsLong())
          .isInstanceOf(IOException.class)
          .hasMessage("io")
          .hasNoCause();
    }

    @Test
    void shouldRecoverUsingException() {
        ThrowingLongSupplier<Exception> supplier = () -> { throw new Exception(); };
        assertThat(ThrowingLongSupplier.recover(supplier, e -> -1L).getAsLong()).isEqualTo(-1L);
    }

    @Test
    void shouldPassThroughOnSuccessfulRecover() {
        ThrowingLongSupplier<Exception> supplier = () -> 7L;
        assertThat(ThrowingLongSupplier.recover(supplier, e -> -1L).getAsLong()).isEqualTo(7L);
    }

    @Test
    void shouldRejectNulls() {
        assertThatThrownBy(() -> ThrowingLongSupplier.unchecked(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ThrowingLongSupplier.sneaky(null)).isInstanceOf(NullPointerException.class);
        ThrowingLongSupplier<Exception> supplier = () -> 1L;
        assertThatThrownBy(() -> ThrowingLongSupplier.recover(supplier, null)).isInstanceOf(NullPointerException.class);
    }
}
