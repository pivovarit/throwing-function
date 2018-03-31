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
package com.pivovarit.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import com.pivovarit.function.exception.WrappedException;

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
 * @see ThrowingBiFunction
 * @see ThrowingUnaryOperator
 *
 * @author Grzegorz Piwowarek
 */
public interface ThrowingBinaryOperator<T, E extends Exception> extends ThrowingBiFunction<T, T, T, E> {

    static <T, E extends Exception> BiFunction<T, T, T> unchecked(ThrowingBinaryOperator<T, E> function) {
        Objects.requireNonNull(function);

        return function.unchecked();
    }
    
    @Override
    default BinaryOperator<T> unchecked() {
        return (arg1, arg2) -> {
            try {
                return apply(arg1, arg2);
            } catch (final Exception e) {
                throw new WrappedException(e);
            }
        };
    }
}
