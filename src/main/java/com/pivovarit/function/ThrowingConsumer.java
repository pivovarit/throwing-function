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

import java.util.Objects;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Represents a function that accepts one argument and does not return any value;
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the input to the function
 * @param <E> the type of the thrown checked exception
 *
 * @author Grzegorz Piwowarek
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws E the checked exception type
     */
    void accept(T t) throws E;

    /**
     * Returns a new Consumer instance which wraps the thrown checked exception instance into a {@link CheckedException}
     *
     * @param <T>      the type of the input to the operation
     * @param consumer the ThrowingConsumer to wrap
     * @return Consumer instance that wraps the checked exception into a {@link CheckedException}
     */
    static <T> Consumer<T> unchecked(ThrowingConsumer<? super T, ?> consumer) {
        requireNonNull(consumer);
        return t -> {
            try {
                consumer.accept(t);
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }

    /**
     * Returns a new Consumer instance which rethrows the checked exception using the Sneaky Throws pattern
     *
     * @param <T>      the type of the input to the operation
     * @param consumer the ThrowingConsumer to wrap
     * @return Consumer instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T> Consumer<T> sneaky(ThrowingConsumer<? super T, ?> consumer) {
        Objects.requireNonNull(consumer);
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }
}
