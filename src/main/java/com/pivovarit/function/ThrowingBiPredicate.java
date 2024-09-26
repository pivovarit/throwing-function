/*
 * Copyright 2024 the original author or authors.
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

import java.util.Objects;
import java.util.function.BiPredicate;

import static java.util.Objects.requireNonNull;

/**
 * Represents a predicate (boolean-valued function) of two arguments.  This is
 * the two-arity specialization of {@link ThrowingPredicate}.
 * Function may throw a checked exception.
 *
 * @param <T> the type of the first argument to the predicate
 * @param <U> the type of the second argument to the predicate
 * @param <E> the type of the thrown checked exception
 *
 * @author Grzegorz Piwowarek
 */
@FunctionalInterface
public interface ThrowingBiPredicate<T, U, E extends Exception> {
    boolean test(T t, U u) throws E;

    static <T, U> BiPredicate<T, U> unchecked(ThrowingBiPredicate<T, U, ?> predicate) {
        return requireNonNull(predicate).uncheck();
    }

    /**
     * Returns a new BiPredicate instance which rethrows the checked exception using the Sneaky Throws pattern
     *
     * @return BiPredicate instance that rethrows the checked exception using the Sneaky Throws pattern
     * @param predicate operation throwing checked exception
     * @param <T> type of the first argument to the predicate
     * @param <U> type of the second argument to the predicate
     */
    static <T, U> BiPredicate<T, U> sneaky(ThrowingBiPredicate<T, U, ?> predicate) {
        Objects.requireNonNull(predicate);
        return (t, u) -> {
            try {
                return predicate.test(t, u);
            } catch (Exception e) {
                return SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }

    default ThrowingBiPredicate<T, U, E> and(final ThrowingBiPredicate<? super T, ? super U, ? extends E> other) {
        requireNonNull(other);
        return (arg1, arg2) -> test(arg1, arg2) && other.test(arg1, arg2);
    }

    default ThrowingBiPredicate<T, U, E> or(final ThrowingBiPredicate<? super T, ? super U, ? extends E> other) {
        requireNonNull(other);
        return (arg1, arg2) -> test(arg1, arg2) || other.test(arg1, arg2);
    }

    default ThrowingBiPredicate<T, U, E> xor(final ThrowingBiPredicate<? super T, ? super U, ? extends E> other) {
        requireNonNull(other);
        return (arg1, arg2) -> test(arg1, arg2) ^ other.test(arg1, arg2);
    }

    default ThrowingBiPredicate<T, U, E> negate() {
        return (arg1, arg2) -> !test(arg1, arg2);
    }

    /**
     * @return this ThrowingBiFunction instance as a ThrowingBiFunction
     */
    default ThrowingBiFunction<T, U, Boolean, E> asFunction() {
        return this::test;
    }

    /**
     * @return a new BiPredicate instance which wraps thrown checked exception instance into a RuntimeException
     */
    default BiPredicate<T, U> uncheck() {
        return (arg1, arg2) -> {
            try {
                return test(arg1, arg2);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }
}
