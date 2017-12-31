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
import java.util.Optional;
import java.util.function.Function;


/**
 * Represents a function that accepts one argument and returns a value;
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the thrown checked exception
 *
 */
@FunctionalInterface
public interface ThrowingFunction<T,R,E extends Exception> {
    R apply(T arg) throws E;

    /**
     * @param <T> type
     * @param <E> checked exception
     * @return a function that accepts one argument and returns it as a value.
     */
    static <T, E extends Exception> ThrowingFunction<T, T, E> identity() {
        return t -> t;
    }

    /**
     * @return a Function that returns the result of the given function as an Optional instance.
     * In case of a failure, empty Optional is returned
     */
    static <T, R, E extends Exception> Function<T, Optional<R>> lifted(ThrowingFunction<T, R, E> f) {
        Objects.requireNonNull(f);

        return f.lift();
    }

    static <T, R, E extends Exception> Function<T, R> unchecked(ThrowingFunction<T, R, E> f) {
        Objects.requireNonNull(f);

        return f.uncheck();
    }

    default <V> ThrowingFunction<V, R, E> compose(final ThrowingFunction<? super V, ? extends T, E> before) {
        Objects.requireNonNull(before);

        return (V v) -> apply(before.apply(v));
    }

    default <V> ThrowingFunction<T, V, E> andThen(final ThrowingFunction<? super R, ? extends V, E> after) {
        Objects.requireNonNull(after);

        return (T t) -> after.apply(apply(t));
    }

    /**
     * @return a Function that returns the result as an Optional instance. In case of a failure, empty Optional is
     * returned
     */
    default Function<T, Optional<R>> lift() {
        return t -> {
            try {
                return Optional.of(apply(t));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }

    /**
     * @return a new Function instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Function<T, R> uncheck() {
        return t -> {
            try {
                return apply(t);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }
}
