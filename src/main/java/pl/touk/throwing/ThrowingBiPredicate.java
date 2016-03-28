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
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments.  This is
 * the two-arity specialization of {@link ThrowingPredicate}.
 * Function may throw a checked exception.
 *
 * @param <T> the type of the first argument to the predicate
 * @param <U> the type of the second argument to the predicate
 * @param <E> the type of the thrown checked exception
 */
@FunctionalInterface
public interface ThrowingBiPredicate<T, U, E extends Throwable> {
    boolean test(T t, U u) throws E;

    static <T, U, E extends Exception> BiPredicate<T, U> unchecked(ThrowingBiPredicate<T, U, E> predicate) {
        Objects.requireNonNull(predicate);

        return predicate.uncheck();
    }

    default ThrowingBiPredicate<T, U, E> and(final ThrowingBiPredicate<? super T, ? super U, E> other) {
        Objects.requireNonNull(other);

        return (arg1, arg2) -> test(arg1, arg2) && other.test(arg1, arg2);
    }

    default ThrowingBiPredicate<T, U, E> or(final ThrowingBiPredicate<? super T, ? super U, E> other) {
        Objects.requireNonNull(other);

        return (arg1, arg2) -> test(arg1, arg2) || other.test(arg1, arg2);
    }

    default ThrowingBiPredicate<T, U, E> xor(final ThrowingBiPredicate<? super T, ? super U, E> other) {
        Objects.requireNonNull(other);

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
            } catch (final Throwable e) {
                throw new WrappedException(e);
            }
        };
    }
}
