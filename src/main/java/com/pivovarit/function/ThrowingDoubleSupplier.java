/*
 * Copyright 2014-2026 Grzegorz Piwowarek, https://4comprehension.com/
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

import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;

import static java.util.Objects.requireNonNull;

/**
 * Represents a supplier of double-valued results; the supplier might throw a checked exception instance.
 *
 * @param <E> the type of the thrown checked exception
 * @author Grzegorz Piwowarek
 * @since 2.0.0
 */
@FunctionalInterface
public interface ThrowingDoubleSupplier<E extends Exception> {

    /**
     * Gets a result.
     *
     * @return a result
     * @throws E the checked exception type
     */
    double getAsDouble() throws E;

    /**
     * Returns a new DoubleSupplier instance which wraps the thrown checked exception instance into a {@link CheckedException}
     *
     * @param supplier the ThrowingDoubleSupplier to wrap
     * @return DoubleSupplier instance that wraps the checked exception into a {@link CheckedException}
     */
    static DoubleSupplier unchecked(ThrowingDoubleSupplier<?> supplier) {
        requireNonNull(supplier);
        return () -> {
            try {
                return supplier.getAsDouble();
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }

    /**
     * Returns a new DoubleSupplier instance which rethrows the checked exception using the Sneaky Throws pattern
     *
     * @param supplier the ThrowingDoubleSupplier to wrap
     * @return DoubleSupplier instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static DoubleSupplier sneaky(ThrowingDoubleSupplier<?> supplier) {
        requireNonNull(supplier);
        return () -> {
            try {
                return supplier.getAsDouble();
            } catch (final Exception e) {
                return SneakyThrowUtil.sneakyThrow(e);
            }
        };
    }

    /**
     * Returns a new DoubleSupplier instance which, in case of a thrown checked exception, returns the result
     * produced by the supplied handler applied to the thrown exception
     *
     * @param supplier the ThrowingDoubleSupplier to wrap
     * @param handler  the recovery handler invoked with the thrown exception
     * @return DoubleSupplier instance that recovers from a thrown checked exception using the supplied handler
     * @since 2.0.0
     */
    static DoubleSupplier recover(ThrowingDoubleSupplier<?> supplier, ToDoubleFunction<? super Exception> handler) {
        requireNonNull(supplier);
        requireNonNull(handler);
        return () -> {
            try {
                return supplier.getAsDouble();
            } catch (final Exception e) {
                return handler.applyAsDouble(e);
            }
        };
    }
}
