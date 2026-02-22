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

class ThrowingRunnableTest {

    @Test
    void shouldRun() {

        // given
        ThrowingRunnable<Exception> runnable = () -> { throw new IOException("some message"); };

        // when
        assertThatThrownBy(runnable::run)
          .isInstanceOf(IOException.class)
          .hasMessage("some message");
    }

    @Test
    void shouldRunUncheckedAndThrowUsingUtilsMethod() {
        IOException cause = new IOException("some message");

        // given
        ThrowingRunnable<Exception> runnable = () -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingRunnable.unchecked(runnable).run())
          .isInstanceOf(CheckedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldRunWhenNoExceptionThrownWithUnchecked() {
        // given
        int[] counter = {0};
        ThrowingRunnable<Exception> runnable = () -> counter[0]++;

        // when
        ThrowingRunnable.unchecked(runnable).run();

        // then
        assertThat(counter[0]).isEqualTo(1);
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");

        // given
        ThrowingRunnable<IOException> runnable = () -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingRunnable.sneaky(runnable).run())
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }
}
