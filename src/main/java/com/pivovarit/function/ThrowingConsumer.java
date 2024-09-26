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

    void accept(T t) throws E;

    static <T> Consumer<T> unchecked(ThrowingConsumer<T, ?> consumer) {
        return requireNonNull(consumer).uncheck();
    }

    /**
     * Returns a new BiConsumer instance which rethrows the checked exception using the Sneaky Throws pattern
     * @param consumer operation throwing checked exception
     * @param <T> type of the argument to the consumer
     * @return BiConsumer instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T> Consumer<T> sneaky(ThrowingConsumer<T, ?> consumer) {
        Objects.requireNonNull(consumer);
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }

    /**
     * Chains given ThrowingConsumer instance
     * @param after - consumer that is chained after this instance
     * @return chained Consumer instance
     */
    default ThrowingConsumer<T, E> andThenConsume(final ThrowingConsumer<? super T, ? extends E> after) {
        requireNonNull(after);
        return t -> {
            accept(t);
            after.accept(t);
        };
    }

    /**
     * @return this consumer instance as a Function instance
     */
    default ThrowingFunction<T, Void, E> asFunction() {
        return arg -> {
            accept(arg);
            return null;
        };
    }

    /**
     * @return a Consumer instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Consumer<T> uncheck() {
        return t -> {
            try {
                accept(t);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }
}
