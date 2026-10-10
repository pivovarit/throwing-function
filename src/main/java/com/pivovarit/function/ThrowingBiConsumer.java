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
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result.  This is the two-arity specialization of {@link ThrowingConsumer}.
 * Unlike most other functional interfaces, {@code ThrowingBiConsumer}  is expected
 * to operate via side-effects.
 *
 * @param <T1> the type of the first argument to the operation
 * @param <T2> the type of the second argument to the operation
 * @param <E>  the type of the thrown checked exception
 *
 * @author Grzegorz Piwowarek
 * @see ThrowingConsumer
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T1, T2, E extends Exception> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t1 the first input argument
     * @param t2 the second input argument
     * @throws E the checked exception type
     */
    void accept(T1 t1, T2 t2) throws E;

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

    /**
     * Returns a new BiConsumer instance which, in case of a thrown checked exception, passes the thrown
     * exception to the supplied handler
     *
     * @param <T>      the type of the first argument to the operation
     * @param <U>      the type of the second argument to the operation
     * @param consumer the ThrowingBiConsumer to wrap
     * @param handler  the recovery handler invoked with the thrown exception
     * @return BiConsumer instance that recovers from a thrown checked exception using the supplied handler
     * @since 2.0.0
     */
    static <T, U> BiConsumer<T, U> recover(ThrowingBiConsumer<? super T, ? super U, ?> consumer, Consumer<? super Exception> handler) {
        requireNonNull(consumer);
        requireNonNull(handler);
        return (arg1, arg2) -> {
            try {
                consumer.accept(arg1, arg2);
            } catch (final Exception e) {
                handler.accept(e);
            }
        };
    }
}
