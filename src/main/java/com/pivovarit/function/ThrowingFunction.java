/*
 * Copyright 2014-2016 Grzegorz Piwowarek, https://4comprehension.com/
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

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Represents a function that accepts one argument and returns a value;
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the thrown checked exception
 * @author Grzegorz Piwowarek
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    /**
     * Applies this function to the given argument.
     *
     * @param arg the function argument
     * @return the function result
     * @throws E the checked exception type
     */
    R apply(T arg) throws E;

    /**
     * Returns a new Function instance which returns the result as an Optional, or an empty Optional in case of a thrown exception
     *
     * @param <T>      the type of the input to the function
     * @param <R>      the type of the result of the function
     * @param function the ThrowingFunction to wrap
     * @return a Function that returns the result of the given function as an Optional instance.
     * In case of a failure, empty Optional is returned
     */
    static <T, R> Function<T, Optional<R>> lifted(final ThrowingFunction<? super T, ? extends R, ?> function) {
        requireNonNull(function);

        return t -> {
            try {
                return Optional.ofNullable(function.apply(t));
            } catch (final Exception e) {
                return Optional.empty();
            }
        };
    }

    /**
     * Returns a new Function instance which wraps the thrown checked exception instance into a {@link CheckedException}
     *
     * @param <T>      the type of the input to the function
     * @param <R>      the type of the result of the function
     * @param function the ThrowingFunction to wrap
     * @return Function instance that wraps the checked exception into a {@link CheckedException}
     */
    static <T, R> Function<T, R> unchecked(final ThrowingFunction<? super T, ? extends R, ?> function) {
        requireNonNull(function);
        return t -> {
            try {
                return function.apply(t);
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }

    /**
     * Returns a new Function instance which rethrows the checked exception using the Sneaky Throws pattern
     *
     * @param <T1>     the type of the input to the function
     * @param <R>      the type of the result of the function
     * @param function the ThrowingFunction to wrap
     * @return Function instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T1, R> Function<T1, R> sneaky(ThrowingFunction<? super T1, ? extends R, ?> function) {
        requireNonNull(function);
        return t -> {
            try {
                return function.apply(t);
            } catch (final Exception ex) {
                return SneakyThrowUtil.sneakyThrow(ex);
            }
        };
    }

    /**
     * Returns a new Function instance which returns the result as an Optional, or an empty Optional in case of a thrown exception
     *
     * @return a Function that returns the result as an Optional instance, or an empty Optional in case of a failure
     */
    default Function<T, Optional<R>> lift() {
        return t -> {
            try {
                return Optional.ofNullable(apply(t));
            } catch (final Exception e) {
                return Optional.empty();
            }
        };
    }
}
