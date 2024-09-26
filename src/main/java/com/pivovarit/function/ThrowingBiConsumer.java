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
import com.pivovarit.function.exception.WrappedException;

import java.util.Objects;
import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result.  This is the two-arity specialization of {@link ThrowingConsumer}.
 * Unlike most other functional interfaces, {@code ThrowingBiConsumer}  is expected
 * to operate via side effects.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <E> the type of the thrown checked exception
 *
 * @see ThrowingConsumer
 *
 * @author Grzegorz Piwowarek
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Exception> {
    void accept(T t, U u) throws E;

    default ThrowingBiConsumer<T, U, E> andThenConsume(final ThrowingBiConsumer<? super T, ? super U, ? extends E> after) {
        requireNonNull(after);
        return (arg1, arg2) -> {
            accept(arg1, arg2);
            after.accept(arg1, arg2);
        };
    }

    /**
     * Returns this ThrowingBiConsumer instance as a ThrowingBiFunction
     * @return this action as a ThrowingBiFunction
     */
    default ThrowingBiFunction<T, U, Void, E> asFunction() {
        return (arg1, arg2) -> {
            accept(arg1, arg2);
            return null;
        };
    }

    static <T, U> BiConsumer<T, U> unchecked(ThrowingBiConsumer<T, U, ?> consumer) {
        return requireNonNull(consumer).uncheck();
    }

    /**
     * Returns a new BiConsumer instance which rethrows the checked exception using the Sneaky Throws pattern
     * @param consumer ThrowingBiConsumer operation throwing checked exception
     * @param <T> the type of the first argument to the operation
     * @param <U> the type of the second argument to the operation
     * @return BiConsumer instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T, U> BiConsumer<T, U> sneaky(ThrowingBiConsumer<T, U, ?> consumer) {
        Objects.requireNonNull(consumer);
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            } catch (Exception e) {
                SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }

    /**
     * Returns a new BiConsumer instance which wraps thrown checked exception instance into a RuntimeException
     * @return BiConsumer instance that packages checked exceptions into RuntimeException instances
     */
    default BiConsumer<T, U> uncheck() {
        return (arg1, arg2) -> {
            try {
                accept(arg1, arg2);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }
}
