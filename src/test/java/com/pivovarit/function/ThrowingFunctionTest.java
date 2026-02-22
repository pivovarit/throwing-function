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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static com.pivovarit.function.TestCommons.givenThrowingFunction;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingFunctionTest {

    @Test
    void shouldReturnOptional() {
        // given
        ThrowingFunction<Integer, Integer, Exception> f1 = i -> i;

        // when
        Optional<Integer> result = f1.lift().apply(42);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void shouldReturnEmptyOptional() {
        // given
        ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        Optional<Integer> result = f1.lift().apply(42);

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void shouldThrowEx() {
        ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction("custom exception message");

        assertThatThrownBy(() -> f1.apply(42))
          .isInstanceOf(Exception.class)
          .hasMessage("custom exception message");
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        assertThatThrownBy(() -> Stream.of(". .")
          .map(ThrowingFunction.unchecked(URI::new))
          .collect(toList()))
          .isInstanceOf(RuntimeException.class)
          .hasMessage("Illegal character in path at index 1: . .")
          .hasCauseInstanceOf(URISyntaxException.class);
    }

    @Test
    void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() {

        // when
        Long result = Stream.of(". .")
          .map(ThrowingFunction.lifted(URI::new))
          .filter(Optional::isPresent)
          .count();

        //then
        assertThat(result).isZero();
    }

    @Test
    void shouldSneakyThrow() {
        IOException cause = new IOException("some message");

        // given
        ThrowingFunction<Integer, Integer, IOException> f = i -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingFunction.sneaky(f).apply(42))
          .isInstanceOf(IOException.class)
          .hasMessage(cause.getMessage())
          .hasNoCause();
    }
}
