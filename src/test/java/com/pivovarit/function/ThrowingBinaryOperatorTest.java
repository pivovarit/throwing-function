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

class ThrowingBinaryOperatorTest {

    @Test
    void shouldThrowEx() {
        // given
        ThrowingBinaryOperator<Integer, Exception> f1 =
          (i, j) -> { throw new Exception("some message"); };

        // when
        assertThatThrownBy(() -> f1.apply(42, 42))
          .isInstanceOf(Exception.class)
          .hasMessage("some message");
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        Exception cause = new Exception("some message");

        // given
        ThrowingBinaryOperator<Integer, Exception> f1 =
          (i, j) -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingBinaryOperator.unchecked(f1).apply(42, 42))
          .isInstanceOf(CheckedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldApplyWhenNoExceptionThrown() {
        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = Integer::sum;

        // when
        ThrowingBinaryOperator.unchecked(f1).apply(42, 0);

        // then no exception thrown
    }

    @Test
    void shouldWrapInOptionalWhenUsingLifted() {

        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = Integer::sum;

        // when
        Optional<Integer> result =
          ThrowingBiFunction.optional(f1).apply(2, 2);

        //then
        assertThat(result).isPresent();
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");

        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = (i, j) -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingBinaryOperator.sneaky(f1).apply(42, 42))
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }
}
