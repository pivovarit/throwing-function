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
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingToIntFunctionTest {

    @Test
    void mapToIntTest() {
        int result = Stream.of("1", "2", "3")
          .mapToInt(ThrowingToIntFunction.<String>unchecked(Integer::parseInt))
          .sum();
        assertThat(result).isEqualTo(6);
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        assertThatThrownBy(() -> Stream.of(". .")
          .mapToInt(ThrowingToIntFunction.<String>unchecked(Integer::parseInt))
          .sum())
          .isInstanceOf(CheckedException.class)
          .hasMessage("For input string: \". .\"")
          .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");
        ThrowingToIntFunction<String, IOException> function = s -> { throw cause; };

        assertThatThrownBy(() -> ThrowingToIntFunction.sneaky(function).applyAsInt("test"))
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }

    @Test
    void shouldRecoverWithInputAndException() {
        ThrowingToIntFunction<String, Exception> function = s -> { throw new Exception(s); };
        ToIntFunction<String> recovered = ThrowingToIntFunction.recover(function, (s, e) -> s.length());
        assertThat(recovered.applyAsInt("boom")).isEqualTo(4);
    }

    @Test
    void shouldRejectNullFunction() {
        assertThatThrownBy(() -> ThrowingToIntFunction.unchecked(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ThrowingToIntFunction.sneaky(null)).isInstanceOf(NullPointerException.class);
        ThrowingToIntFunction<String, Exception> function = s -> 1;
        assertThatThrownBy(() -> ThrowingToIntFunction.recover(function, null)).isInstanceOf(NullPointerException.class);
    }
}
