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

class ThrowingIntSupplierTest {

    @Test
    void shouldGet() throws Exception {
        ThrowingIntSupplier<Exception> supplier = () -> 42;
        assertThat(supplier.getAsInt()).isEqualTo(42);
    }

    @Test
    void shouldReturnValueWhenUncheckedAndNoException() {
        ThrowingIntSupplier<Exception> supplier = () -> 5;
        assertThat(ThrowingIntSupplier.unchecked(supplier).getAsInt()).isEqualTo(5);
    }

    @Test
    void shouldWrapInCheckedException() {
        ThrowingIntSupplier<Exception> supplier = () -> { throw new IOException("boom"); };
        assertThatThrownBy(ThrowingIntSupplier.unchecked(supplier)::getAsInt)
          .isInstanceOf(CheckedException.class)
          .hasMessage("boom");
    }

    @Test
    void shouldReturnValueWhenSneakyAndNoException() {
        ThrowingIntSupplier<Exception> supplier = () -> 5;
        assertThat(ThrowingIntSupplier.sneaky(supplier).getAsInt()).isEqualTo(5);
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("io");
        ThrowingIntSupplier<IOException> supplier = () -> { throw cause; };
        assertThatThrownBy(() -> ThrowingIntSupplier.sneaky(supplier).getAsInt())
          .isInstanceOf(IOException.class)
          .hasMessage("io")
          .hasNoCause();
    }

    @Test
    void shouldRecoverUsingException() {
        ThrowingIntSupplier<Exception> supplier = () -> { throw new Exception(); };
        assertThat(ThrowingIntSupplier.recover(supplier, e -> -1).getAsInt()).isEqualTo(-1);
    }

    @Test
    void shouldPassThroughOnSuccessfulRecover() {
        ThrowingIntSupplier<Exception> supplier = () -> 7;
        assertThat(ThrowingIntSupplier.recover(supplier, e -> -1).getAsInt()).isEqualTo(7);
    }

    @Test
    void shouldRejectNulls() {
        assertThatThrownBy(() -> ThrowingIntSupplier.unchecked(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ThrowingIntSupplier.sneaky(null)).isInstanceOf(NullPointerException.class);
        ThrowingIntSupplier<Exception> supplier = () -> 1;
        assertThatThrownBy(() -> ThrowingIntSupplier.recover(supplier, null)).isInstanceOf(NullPointerException.class);
    }
}
