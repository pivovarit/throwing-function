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

    static <T, U> BiPredicate<T, U> unchecked(ThrowingBiPredicate<? super T, ? super U, ?> predicate) {
        requireNonNull(predicate);
        return (arg1, arg2) -> {
            try {
                return predicate.test(arg1, arg2);
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }

    /**
     * @return BiPredicate instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T, U> BiPredicate<T, U> sneaky(ThrowingBiPredicate<? super T, ? super U, ?> predicate) {
        Objects.requireNonNull(predicate);
        return (t, u) -> {
            try {
                return predicate.test(t, u);
            } catch (Exception e) {
                return SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }
}
