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
import java.util.Optional;
import org.junit.jupiter.api.Test;

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

    @Test
    void shouldReturnEmptyOptionalWhenUsingOptionalAndExceptionThrown() {
        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> { throw new IOException("boom"); };

        // when
        Optional<Integer> result = ThrowingSupplier.optional(supplier).get();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");

        // given
        ThrowingSupplier<Integer, IOException> supplier = () -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingSupplier.sneaky(supplier).get())
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }
}
