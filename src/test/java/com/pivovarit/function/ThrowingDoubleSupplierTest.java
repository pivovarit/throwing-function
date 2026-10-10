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

class ThrowingDoubleSupplierTest {

    @Test
    void shouldGet() throws Exception {
        ThrowingDoubleSupplier<Exception> supplier = () -> 42.0;
        assertThat(supplier.getAsDouble()).isEqualTo(42.0);
    }

    @Test
    void shouldReturnValueWhenUncheckedAndNoException() {
        ThrowingDoubleSupplier<Exception> supplier = () -> 5.0;
        assertThat(ThrowingDoubleSupplier.unchecked(supplier).getAsDouble()).isEqualTo(5.0);
    }

    @Test
    void shouldWrapInCheckedException() {
        ThrowingDoubleSupplier<Exception> supplier = () -> { throw new IOException("boom"); };
        assertThatThrownBy(ThrowingDoubleSupplier.unchecked(supplier)::getAsDouble)
          .isInstanceOf(CheckedException.class)
          .hasMessage("boom");
    }

    @Test
    void shouldReturnValueWhenSneakyAndNoException() {
        ThrowingDoubleSupplier<Exception> supplier = () -> 5.0;
        assertThat(ThrowingDoubleSupplier.sneaky(supplier).getAsDouble()).isEqualTo(5.0);
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("io");
        ThrowingDoubleSupplier<IOException> supplier = () -> { throw cause; };
        assertThatThrownBy(() -> ThrowingDoubleSupplier.sneaky(supplier).getAsDouble())
          .isInstanceOf(IOException.class)
          .hasMessage("io")
          .hasNoCause();
    }

    @Test
    void shouldRecoverUsingException() {
        ThrowingDoubleSupplier<Exception> supplier = () -> { throw new Exception(); };
        assertThat(ThrowingDoubleSupplier.recover(supplier, e -> -1.0).getAsDouble()).isEqualTo(-1.0);
    }

    @Test
    void shouldPassThroughOnSuccessfulRecover() {
        ThrowingDoubleSupplier<Exception> supplier = () -> 7.0;
        assertThat(ThrowingDoubleSupplier.recover(supplier, e -> -1.0).getAsDouble()).isEqualTo(7.0);
    }

    @Test
    void shouldRejectNulls() {
        assertThatThrownBy(() -> ThrowingDoubleSupplier.unchecked(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ThrowingDoubleSupplier.sneaky(null)).isInstanceOf(NullPointerException.class);
        ThrowingDoubleSupplier<Exception> supplier = () -> 1.0;
        assertThatThrownBy(() -> ThrowingDoubleSupplier.recover(supplier, null)).isInstanceOf(NullPointerException.class);
    }
}
