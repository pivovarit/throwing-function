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
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingToDoubleFunctionTest {

    @Test
    void mapToDoubleTest() {
        double result = Stream.of("1.5", "2.5", "3.0")
          .mapToDouble(ThrowingToDoubleFunction.<String>unchecked(Double::parseDouble))
          .sum();
        assertThat(result).isEqualTo(7.0);
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        assertThatThrownBy(() -> Stream.of(". .")
          .mapToDouble(ThrowingToDoubleFunction.<String>unchecked(Double::parseDouble))
          .sum())
          .isInstanceOf(CheckedException.class)
          .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");
        ThrowingToDoubleFunction<String, IOException> function = s -> { throw cause; };

        assertThatThrownBy(() -> ThrowingToDoubleFunction.sneaky(function).applyAsDouble("test"))
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }

    @Test
    void shouldRecoverWithInputAndException() {
        ThrowingToDoubleFunction<String, Exception> function = s -> { throw new Exception(s); };
        ToDoubleFunction<String> recovered = ThrowingToDoubleFunction.recover(function, (s, e) -> -1.0);
        assertThat(recovered.applyAsDouble("boom")).isEqualTo(-1.0);
    }

    @Test
    void shouldRejectNullFunction() {
        assertThatThrownBy(() -> ThrowingToDoubleFunction.unchecked(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ThrowingToDoubleFunction.sneaky(null)).isInstanceOf(NullPointerException.class);
        ThrowingToDoubleFunction<String, Exception> function = s -> 1.0;
        assertThatThrownBy(() -> ThrowingToDoubleFunction.recover(function, null)).isInstanceOf(NullPointerException.class);
    }
}
