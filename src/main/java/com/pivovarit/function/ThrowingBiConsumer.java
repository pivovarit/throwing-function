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

import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result.  This is the two-arity specialization of {@link ThrowingConsumer}.
 * Unlike most other functional interfaces, {@code ThrowingBiConsumer}  is expected
 * to operate via side-effects.
 *
 * @param <T1> the type of the first argument to the operation
 * @param <T2> the type of the second argument to the operation
 * @param <EX> the type of the thrown checked exception
 *
 * @author Grzegorz Piwowarek
 * @see ThrowingConsumer
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T1, T2, EX extends Exception> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t  the first input argument
     * @param t2 the second input argument
     * @throws EX the checked exception type
     */
    void accept(T1 t, T2 t2) throws EX;

    /**
     * Returns a new BiConsumer instance which wraps the thrown checked exception instance into a {@link CheckedException}
     *
     * @param <T>      the type of the first argument to the operation
     * @param <U>      the type of the second argument to the operation
     * @param consumer the ThrowingBiConsumer to wrap
     * @return BiConsumer instance that wraps the checked exception into a {@link CheckedException}
     */
    static <T, U> BiConsumer<T, U> unchecked(ThrowingBiConsumer<? super T, ? super U, ?> consumer) {
        requireNonNull(consumer);
        return (arg1, arg2) -> {
            try {
                consumer.accept(arg1, arg2);
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }

    /**
     * Returns a new BiConsumer instance which rethrows the checked exception using the Sneaky Throws pattern
     *
     * @param <T>      the type of the first argument to the operation
     * @param <U>      the type of the second argument to the operation
     * @param consumer the ThrowingBiConsumer to wrap
     * @return BiConsumer instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T, U> BiConsumer<T, U> sneaky(ThrowingBiConsumer<? super T, ? super U, ?> consumer) {
        requireNonNull(consumer);
        return (arg1, arg2) -> {
            try {
                consumer.accept(arg1, arg2);
            } catch (final Exception e) {
                SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }
}
