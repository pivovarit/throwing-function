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

class ThrowingConsumerTest {

    @Test
    void shouldConsume() throws Exception {
        // given
        Integer[] input = {0};

        ThrowingConsumer<Integer, Exception> consumer = i -> input[0] = 2;

        // when
        consumer.accept(2);

        // then
        assertThat(input[0]).isEqualTo(2);
    }

    @Test
    void shouldConsumeAndThrowUnchecked() {
        IOException cause = new IOException("some message");

        // given
        ThrowingConsumer<Integer, IOException> consumer = i -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingConsumer.unchecked(consumer).accept(3))
          .isInstanceOf(CheckedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldConsumeUnchecked() {
        // given
        ThrowingConsumer<Integer, IOException> consumer = i -> {};

        // when
        ThrowingConsumer.unchecked(consumer).accept(3);

        // then no exception thrown
    }

    @Test
    void shouldConsumeAndSneakyThrow() {
        IOException cause = new IOException("some message");

        // given
        ThrowingConsumer<Integer, IOException> consumer = i -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingConsumer.sneaky(consumer).accept(3))
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }
}
