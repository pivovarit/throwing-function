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
import java.util.concurrent.atomic.LongAdder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ThrowingBiConsumerTest {

    @Test
    void shouldConsume() throws Exception {
        LongAdder input = new LongAdder();

        ThrowingBiConsumer<Integer, Integer, Exception> consumer = (i, j) -> input.increment();

        consumer.accept(2, 3);

        assertThat(input.sum()).isEqualTo(1);
    }

    @Test
    void shouldConsumeAndThrowUnchecked() {
        IOException cause = new IOException("some message");

        ThrowingBiConsumer<Integer, Integer, IOException> consumer = (i, j) -> { throw cause; };

        assertThatThrownBy(() -> ThrowingBiConsumer.unchecked(consumer).accept(3, 3))
          .hasMessage(cause.getMessage())
          .isInstanceOf(CheckedException.class)
          .hasCauseInstanceOf(cause.getClass());
    }

    @Test
    void shouldConsumeUnchecked() {
        ThrowingBiConsumer<Integer, Integer, IOException> consumer = (i, j) -> {};

        ThrowingBiConsumer.unchecked(consumer).accept(3, 4);
    }

    @Test
    void shouldConsumeAndSneakyThrow() {
        IOException cause = new IOException("some message");

        ThrowingBiConsumer<Integer, Integer, IOException> consumer = (i, j) -> { throw cause; };

        assertThatThrownBy(() -> ThrowingBiConsumer.sneaky(consumer).accept(3, 3))
          .hasMessage(cause.getMessage())
          .isInstanceOf(IOException.class)
          .hasNoCause();
    }
}
