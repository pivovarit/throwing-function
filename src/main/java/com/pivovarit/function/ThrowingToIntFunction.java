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

import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

import static java.util.Objects.requireNonNull;

/**
 * Represents a function that accepts one argument and produces an int-valued result;
 * the function might throw a checked exception instance.
 *
 * @param <T> the type of the input to the function
 * @param <E> the type of the thrown checked exception
 * @author Grzegorz Piwowarek
 * @since 2.0.0
 */
@FunctionalInterface
public interface ThrowingToIntFunction<T, E extends Exception> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     * @throws E the checked exception type
     */
    int applyAsInt(T t) throws E;

    /**
     * Returns a new ToIntFunction instance which wraps the thrown checked exception instance into a {@link CheckedException}
     *
     * @param <T>      the type of the input to the function
     * @param function the ThrowingToIntFunction to wrap
     * @return ToIntFunction instance that wraps the checked exception into a {@link CheckedException}
     */
    static <T> ToIntFunction<T> unchecked(ThrowingToIntFunction<? super T, ?> function) {
        requireNonNull(function);
        return t -> {
            try {
                return function.applyAsInt(t);
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }

    /**
     * Returns a new ToIntFunction instance which rethrows the checked exception using the Sneaky Throws pattern
     *
     * @param <T>      the type of the input to the function
     * @param function the ThrowingToIntFunction to wrap
     * @return ToIntFunction instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T> ToIntFunction<T> sneaky(ThrowingToIntFunction<? super T, ?> function) {
        requireNonNull(function);
        return t -> {
            try {
                return function.applyAsInt(t);
            } catch (final Exception e) {
                return SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }

    /**
     * Returns a new ToIntFunction instance which, in case of a thrown checked exception, returns the result
     * produced by the supplied handler applied to the input and the thrown exception
     *
     * @param <T>      the type of the input to the function
     * @param function the ThrowingToIntFunction to wrap
     * @param handler  the recovery handler invoked with the input and the thrown exception
     * @return ToIntFunction instance that recovers from a thrown checked exception using the supplied handler
     * @since 2.0.0
     */
    static <T> ToIntFunction<T> recover(ThrowingToIntFunction<? super T, ?> function, ToIntBiFunction<? super T, ? super Exception> handler) {
        requireNonNull(function);
        requireNonNull(handler);
        return t -> {
            try {
                return function.applyAsInt(t);
            } catch (final Exception e) {
                return handler.applyAsInt(t, e);
            }
        };
    }
}
