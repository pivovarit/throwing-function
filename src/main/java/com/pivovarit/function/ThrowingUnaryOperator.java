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
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * Represents an operation on a single operand that produces a result of the
 * same type as its operand.  This is a specialization of {@code Function} for
 * the case where the operand and result are of the same type.
 * Function may throw a checked exception.
 *
 * @param <T> the type of the operand and result of the operator
 * @param <E> the type of the thrown checked exception
 *
 * @see ThrowingFunction
 *
 * @author Grzegorz Piwowarek
 */
@FunctionalInterface
public interface ThrowingUnaryOperator<T, E extends Exception> extends ThrowingFunction<T, T, E> {

    static <T> UnaryOperator<T> unchecked(ThrowingUnaryOperator<T, ?> operator) {
        return requireNonNull(operator).uncheck();
    }

    /**
     * Returns a new UnaryOperator instance which rethrows the checked exception using the Sneaky Throws pattern
     * @return UnaryOperator instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T> UnaryOperator<T> sneaky(ThrowingUnaryOperator<T, ?> operator) {
        Objects.requireNonNull(operator);
        return t -> {
            try {
                return operator.apply(t);
            } catch (Exception e) {
                return SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }

    /**
     * Returns a new UnaryOperator instance which wraps thrown checked exception instance into a RuntimeException
     */
    @Override
    default UnaryOperator<T> uncheck() {
        return t -> {
            try {
                return apply(t);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }
}
