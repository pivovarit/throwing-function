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

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Represents a function that accepts one argument and returns a boolean value
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the input to the function
 * @param <E> the type of the thrown checked exception
 *
 * @author Grzegorz Piwowarek
 */
@FunctionalInterface
public interface ThrowingPredicate<T, E extends Exception> {
    boolean test(T t) throws E;

    static <T> Predicate<T> unchecked(ThrowingPredicate<T, ?> predicate) {
        return requireNonNull(predicate).uncheck();
    }

    /**
     * @return Predicate instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T> Predicate<T> sneaky(ThrowingPredicate<T, ?> predicate) {
        Objects.requireNonNull(predicate);
        return t -> {
            try {
                return predicate.test(t);
            } catch (Exception e) {
                return SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }

    default ThrowingPredicate<T, E> and(final ThrowingPredicate<? super T, ? extends E> other) {
        requireNonNull(other);
        return t -> test(t) && other.test(t);
    }

    default ThrowingPredicate<T, E> or(final ThrowingPredicate<? super T, ? extends E> other) {
        requireNonNull(other);
        return t -> test(t) || other.test(t);
    }

    default ThrowingPredicate<T, E> xor(final ThrowingPredicate<? super T, ? extends E> other) {
        requireNonNull(other);
        return t -> test(t) ^ other.test(t);
    }

    default ThrowingPredicate<T, E> negate() {
        return t -> !test(t);
    }

    /**
     * @return this Predicate instance as a Function instance
     */
    default ThrowingFunction<T, Boolean, E> asFunction() {
        return this::test;
    }

    /**
     * @return a new Predicate instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Predicate<T> uncheck() {
        return t -> {
            try {
                return test(t);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }
}
