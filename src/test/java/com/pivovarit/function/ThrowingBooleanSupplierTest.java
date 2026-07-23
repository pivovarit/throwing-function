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

class ThrowingBooleanSupplierTest {

    @Test
    void shouldGet() throws Exception {
        ThrowingBooleanSupplier<Exception> supplier = () -> true;
        assertThat(supplier.getAsBoolean()).isTrue();
    }

    @Test
    void shouldReturnValueWhenUncheckedAndNoException() {
        ThrowingBooleanSupplier<Exception> trueSupplier = () -> true;
        ThrowingBooleanSupplier<Exception> falseSupplier = () -> false;
        assertThat(ThrowingBooleanSupplier.unchecked(trueSupplier).getAsBoolean()).isTrue();
        assertThat(ThrowingBooleanSupplier.unchecked(falseSupplier).getAsBoolean()).isFalse();
    }

    @Test
    void shouldWrapInCheckedException() {
        ThrowingBooleanSupplier<Exception> supplier = () -> { throw new IOException("boom"); };
        assertThatThrownBy(ThrowingBooleanSupplier.unchecked(supplier)::getAsBoolean)
          .isInstanceOf(CheckedException.class)
          .hasMessage("boom");
    }

    @Test
    void shouldReturnValueWhenSneakyAndNoException() {
        ThrowingBooleanSupplier<Exception> trueSupplier = () -> true;
        ThrowingBooleanSupplier<Exception> falseSupplier = () -> false;
        assertThat(ThrowingBooleanSupplier.sneaky(trueSupplier).getAsBoolean()).isTrue();
        assertThat(ThrowingBooleanSupplier.sneaky(falseSupplier).getAsBoolean()).isFalse();
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("io");
        ThrowingBooleanSupplier<IOException> supplier = () -> { throw cause; };
        assertThatThrownBy(() -> ThrowingBooleanSupplier.sneaky(supplier).getAsBoolean())
          .isInstanceOf(IOException.class)
          .hasMessage("io")
          .hasNoCause();
    }

    @Test
    void shouldRecoverToFalse() {
        ThrowingBooleanSupplier<Exception> supplier = () -> { throw new Exception(); };
        assertThat(ThrowingBooleanSupplier.recover(supplier, e -> false).getAsBoolean()).isFalse();
    }

    @Test
    void shouldRecoverToTrue() {
        ThrowingBooleanSupplier<Exception> supplier = () -> { throw new Exception(); };
        assertThat(ThrowingBooleanSupplier.recover(supplier, e -> true).getAsBoolean()).isTrue();
    }

    @Test
    void shouldPassThroughOnSuccessfulRecover() {
        ThrowingBooleanSupplier<Exception> trueSupplier = () -> true;
        ThrowingBooleanSupplier<Exception> falseSupplier = () -> false;
        assertThat(ThrowingBooleanSupplier.recover(trueSupplier, e -> false).getAsBoolean()).isTrue();
        assertThat(ThrowingBooleanSupplier.recover(falseSupplier, e -> true).getAsBoolean()).isFalse();
    }

    @Test
    void shouldRejectNulls() {
        assertThatThrownBy(() -> ThrowingBooleanSupplier.unchecked(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ThrowingBooleanSupplier.sneaky(null)).isInstanceOf(NullPointerException.class);
        ThrowingBooleanSupplier<Exception> supplier = () -> true;
        assertThatThrownBy(() -> ThrowingBooleanSupplier.recover(supplier, null)).isInstanceOf(NullPointerException.class);
    }
}
