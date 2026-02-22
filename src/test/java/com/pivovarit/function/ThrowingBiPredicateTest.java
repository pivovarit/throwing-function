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

class ThrowingBiPredicateTest {

    @Test
    void shouldTest() throws Exception {
        // given
        ThrowingBiPredicate<Integer, Integer, Exception> p = (i, j) -> true;

        // when
        boolean result = p.test(42, 0);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingStandardUtilsFunctions() {
        Exception cause = new Exception("some message");

        // given
        ThrowingBiPredicate<Integer, Integer, Exception> predicate = (i, j) -> {
            throw cause;
        };

        // when
        assertThatThrownBy(() -> ThrowingBiPredicate.unchecked(predicate).test(42, 0))
          .isInstanceOf(RuntimeException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");

        // given
        ThrowingBiPredicate<Integer, Integer, IOException> predicate = (i, j) -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingBiPredicate.sneaky(predicate).test(42, 0))
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }
}
