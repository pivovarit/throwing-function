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
package pl.touk.throwing;

import pl.touk.throwing.exception.WrappedException;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result.  This is the two-arity specialization of {@link ThrowingConsumer}.
 * Unlike most other functional interfaces, {@code ThrowingBiConsumer}  is expected
 * to operate via side-effects.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <E> the type of the thrown checked exception
 *
 * @see ThrowingConsumer
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Throwable> {
    void accept(T t, U u) throws E;

    default ThrowingBiConsumer<T, U, E> andThenConsume(final ThrowingBiConsumer<? super T, ? super U, E> after) {
        Objects.requireNonNull(after);

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

    static <T, U, E extends Exception> BiConsumer<T, U> unchecked(ThrowingBiConsumer<T, U, E> consumer) {
        Objects.requireNonNull(consumer);

        return consumer.uncheck();
    }

    /**
     * Returns a new BiConsumer instance which wraps thrown checked exception instance into a RuntimeException
     * @return BiConsumer instance that packages checked exceptions into RuntimeException instances
     */
    default BiConsumer<T, U> uncheck() {
        return (arg1, arg2) -> {
            try {
                accept(arg1, arg2);
            } catch (final Throwable e) {
                throw new WrappedException(e);
            }
        };
    }
}
