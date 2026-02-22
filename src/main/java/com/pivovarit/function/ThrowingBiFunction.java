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
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the two-arity specialization of {@link ThrowingFunction}.
 * Function may throw a checked exception.
 *
 * @param <T1> the type of the first argument to the function
 * @param <T2> the type of the second argument to the function
 * @param <R>  the type of the result of the function
 * @param <E>  the type of the thrown checked exception
 *
 * @author Grzegorz Piwowarek
 * @see ThrowingFunction
 */
@FunctionalInterface
public interface ThrowingBiFunction<T1, T2, R, E extends Exception> {
    /**
     * Applies this function to the given arguments.
     *
     * @param arg1 the first function argument
     * @param arg2 the second function argument
     * @return the function result
     * @throws E the checked exception type
     */
    R apply(T1 arg1, T2 arg2) throws E;

    /**
     * Returns a new BiFunction instance which wraps the thrown checked exception instance into a {@link CheckedException}
     *
     * @param <T1>     the type of the first argument to the function
     * @param <T2>     the type of the second argument to the function
     * @param <R>      the type of the result of the function
     * @param function the ThrowingBiFunction to wrap
     * @return BiFunction instance that wraps the checked exception into a {@link CheckedException}
     */
    static <T1, T2, R> BiFunction<T1, T2, R> unchecked(ThrowingBiFunction<? super T1, ? super T2, ? extends R, ?> function) {
        requireNonNull(function);

        return (arg1, arg2) -> {
            try {
                return function.apply(arg1, arg2);
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }

    /**
     * Returns a new BiFunction instance which rethrows the checked exception using the Sneaky Throws pattern
     *
     * @param <T1>     the type of the first argument to the function
     * @param <T2>     the type of the second argument to the function
     * @param <R>      the type of the result of the function
     * @param function the ThrowingBiFunction to wrap
     * @return BiFunction instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T1, T2, R> BiFunction<T1, T2, R> sneaky(ThrowingBiFunction<? super T1, ? super T2, ? extends R, ?> function) {
        requireNonNull(function);
        return (t1, t2) -> {
            try {
                return function.apply(t1, t2);
            } catch (final Exception ex) {
                return SneakyThrowUtil.sneakyThrow(ex);
            }
        };
    }

    /**
     * Returns a new BiFunction instance which returns the result as an Optional, or an empty Optional in case of a thrown exception
     *
     * @param <T1>     the type of the first argument to the function
     * @param <T2>     the type of the second argument to the function
     * @param <R>      the type of the result of the function
     * @param function the ThrowingBiFunction to wrap
     * @return BiFunction instance that returns the result as an Optional, or empty Optional in case of a thrown exception
     */
    static <T1, T2, R> BiFunction<T1, T2, Optional<R>> optional(ThrowingBiFunction<? super T1, ? super T2, ? extends R, ?> function) {
        requireNonNull(function);

        return (arg1, arg2) -> {
            try {
                return Optional.ofNullable(function.apply(arg1, arg2));
            } catch (final Exception e) {
                return Optional.empty();
            }
        };
    }
}
