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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ThrowingUnaryOperatorTest {

    @Test
    void shouldApply() throws Exception {
        ThrowingUnaryOperator<Integer, IOException> op = i -> i;

        op.apply(42);
    }

    @Test
    void shouldApplyUnchecked() {
        ThrowingUnaryOperator<Integer, IOException> op = i -> i;

        ThrowingUnaryOperator.unchecked(op).apply(42);
    }

    @Test
    void shouldApplyUncheckedAndThrow() {
        final IOException cause = new IOException("some message");

        ThrowingUnaryOperator<Integer, IOException> op = i -> { throw cause; };

        assertThatThrownBy(() -> ThrowingUnaryOperator.unchecked(op).apply(42))
          .hasMessage(cause.getMessage())
          .isInstanceOf(CheckedException.class)
          .hasCauseInstanceOf(cause.getClass());
    }

    @Test
    void shouldApplyUncheckedAndThrowNPE() {
        assertThatThrownBy(() -> ThrowingUnaryOperator.unchecked(null).apply(42))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");

        ThrowingUnaryOperator<Integer, IOException> op = i -> { throw cause; };

        assertThatThrownBy(() -> ThrowingUnaryOperator.sneaky(op).apply(42))
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }
}
