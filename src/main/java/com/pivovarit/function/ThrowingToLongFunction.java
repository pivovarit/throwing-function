/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pivovarit.function;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import static java.util.Objects.requireNonNull;

/**
 * Represents a ToLongFunction that accepts one argument and returns a long value;
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the input to the function
 * @param <E> the type of the thrown checked exception
 * @author Andreas Zöllner
 */
@FunctionalInterface
public interface ThrowingToLongFunction<T, E extends Exception> {
    long applyAsLong(T arg) throws E;

    static <T> ToLongFunction<T> unchecked(final ThrowingToLongFunction<? super T, ?> function) {
        requireNonNull(function);
        return t -> {
            try {
                return function.applyAsLong(t);
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }

    static <T1> ToLongFunction<T1> sneaky(ThrowingToLongFunction<? super T1, ?> function) {
        requireNonNull(function);
        return t -> {
            try {
                return function.applyAsLong(t);
            } catch (final Exception ex) {
                return SneakyThrowUtil.sneakyThrow(ex);
            }
        };
    }
}
