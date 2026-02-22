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

import com.tngtech.archunit.thirdparty.com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThrowingToLongFunctionTest {

    @Test
    void mapToLongTest() {
        List<Double> values = Lists.newArrayList(1.0, 2.0, 3.0, 4.0);
        long result = values.stream().mapToLong(ThrowingToLongFunction.<Double>unchecked(Double::longValue)).sum();
        assertThat(result).isEqualTo(10);
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        assertThatThrownBy(() -> Stream.of(". .")
                .mapToLong(ThrowingToLongFunction.unchecked(Long::parseLong))
                .sum())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("For input string: \". .\"")
                .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");

        // given
        ThrowingToLongFunction<String, IOException> function = s -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingToLongFunction.sneaky(function).applyAsLong("test"))
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }
}
