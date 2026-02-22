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

import java.util.function.BinaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * Represents an operation upon two operands of the same type, producing a result
 * of the same type as the operands.  This is a specialization of
 * {@link ThrowingBiFunction} for the case where the operands and the result are all of
 * the same type.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object)}.
 *
 * @param <T> the type of the operands and result of the operator
 * @param <E> the type of the thrown checked exception
 *
 * @author Grzegorz Piwowarek
 * @see ThrowingBiFunction
 * @see ThrowingUnaryOperator
 */
public interface ThrowingBinaryOperator<T, E extends Exception> extends ThrowingBiFunction<T, T, T, E> {

    /**
     * Returns a new BinaryOperator instance which wraps the thrown checked exception instance into a {@link CheckedException}
     *
     * @param <T>      the type of the operands and result of the operator
     * @param function the ThrowingBinaryOperator to wrap
     * @return BinaryOperator instance that wraps the checked exception into a {@link CheckedException}
     */
    static <T> BinaryOperator<T> unchecked(ThrowingBinaryOperator<T, ?> function) {
        requireNonNull(function);
        return (arg1, arg2) -> {
            try {
                return function.apply(arg1, arg2);
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }
}
