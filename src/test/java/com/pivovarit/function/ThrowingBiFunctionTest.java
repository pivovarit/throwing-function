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

import static com.pivovarit.function.ThrowingBiFunction.optional;
import static com.pivovarit.function.ThrowingBiFunction.sneaky;
import static com.pivovarit.function.ThrowingBiFunction.unchecked;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingBiFunctionTest {

    @Test
    void shouldThrowEx() throws Exception {
        // given
        Exception exception = new Exception("some message");

        ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> {
            throw exception;
        };

        // when
        assertThatThrownBy(() -> f1.apply(42, 42))
          .isInstanceOf(exception.getClass())
          .hasMessage(exception.getMessage());
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        Exception cause = new Exception("some message");

        // given
        ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw cause; };

        // when
        assertThatThrownBy(() -> unchecked(f1).apply(42, 42)).isInstanceOf(CheckedException.class)
          .hasMessage(cause.getMessage())
          .hasCauseInstanceOf(cause.getClass());
    }

    @Test
    void shouldApplyWhenNoExceptionThrown() {
        // given
        ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i + j;

        // when
        unchecked(f1).apply(42, 0);

        // then no exception thrown
    }

    @Test
    void shouldWrapInOptionalWhenUsingLifted() {

        // given
        ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> i + j;

        // when
        Optional<Integer> result = optional(f1).apply(2, 2);

        //then
        assertThat(result).isPresent();
    }

    @Test
    void shouldReturnEmptyOptionalWhenUsingOptionalAndExceptionThrown() {
        // given
        ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw new Exception("boom"); };

        // when
        Optional<Integer> result = optional(f1).apply(42, 42);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");

        // given
        ThrowingBiFunction<Integer, Integer, Integer, Exception> f1 = (i, j) -> { throw cause; };

        assertThatThrownBy(() -> sneaky(f1).apply(42, 42))
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage());
    }
}
