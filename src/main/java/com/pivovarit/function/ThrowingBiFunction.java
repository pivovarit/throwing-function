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

import com.pivovarit.function.exception.WrappedException;

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
 * @author Grzegorz Piwowarek
 * @see ThrowingFunction
 */
@FunctionalInterface
public interface ThrowingBiFunction<T1, T2, R, E extends Exception> {
    R apply(T1 arg1, T2 arg2) throws E;

    static <T1, T2, R> BiFunction<T1, T2, R> unchecked(ThrowingBiFunction<T1, T2, R, ?> function) {
        return requireNonNull(function).unchecked();
    }

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

    static <T1, T2, R> BiFunction<T1, T2, Optional<R>> lifted(ThrowingBiFunction<T1, T2, R, ?> f) {
        return requireNonNull(f).lift();
    }

    /**
     * Performs provided action on the result of this ThrowingBiFunction instance
     *
     * @param after action that is supposed to be made on the result of apply()
     * @param <V>   after function's result type
     * @return combined function
     */
    default <V> ThrowingBiFunction<T1, T2, V, E> andThen(final ThrowingFunction<? super R, ? extends V, ? extends E> after) {
        requireNonNull(after);
        return (arg1, arg2) -> after.apply(apply(arg1, arg2));
    }

    default BiFunction<T1, T2, R> unchecked() {
        return (arg1, arg2) -> {
            try {
                return apply(arg1, arg2);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }

    default BiFunction<T1, T2, Optional<R>> lift() {
        return (arg1, arg2) -> {
            try {
                return Optional.ofNullable(apply(arg1, arg2));
            } catch (final Exception e) {
                return Optional.empty();
            }
        };
    }
}
