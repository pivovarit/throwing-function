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

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a function that accepts zero arguments and returns some value.
 * Function might throw a checked exception instance.
 *
 * @param <T> the type of the output to the function
 * @param <E> the type of the thrown checked exception
 * @author Grzegorz Piwowarek
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    /**
     * Gets a result.
     *
     * @return a result
     * @throws E the checked exception type
     */
    T get() throws E;

    /**
     * Returns a new Supplier instance which wraps the thrown checked exception instance into a {@link CheckedException}
     *
     * @param <T>      the type of results supplied by this supplier
     * @param supplier the ThrowingSupplier to wrap
     * @return Supplier instance that wraps the checked exception into a {@link CheckedException}
     */
    static <T> Supplier<T> unchecked(ThrowingSupplier<? extends T, ?> supplier) {
        requireNonNull(supplier);
        return () -> {
            try {
                return supplier.get();
            } catch (final Exception e) {
                throw new CheckedException(e);
            }
        };
    }

    /**
     * Returns a new Supplier instance which returns the result as an Optional, or an empty Optional in case of a thrown exception
     *
     * @param <T>      the type of results supplied by this supplier
     * @param supplier the ThrowingSupplier to wrap
     * @return Supplier instance that returns the result as an Optional, or empty Optional in case of a thrown exception
     */
    static <T> Supplier<Optional<T>> optional(ThrowingSupplier<? extends T, ?> supplier) {
        requireNonNull(supplier);
        return () -> {
            try {
                return Optional.ofNullable(supplier.get());
            } catch (final Exception e) {
                return Optional.empty();
            }
        };
    }

    /**
     * Returns a new Supplier instance which rethrows the checked exception using the Sneaky Throws pattern
     *
     * @param <T>      the type of results supplied by this supplier
     * @param supplier the ThrowingSupplier to wrap
     * @return Supplier instance that rethrows the checked exception using the Sneaky Throws pattern
     */
    static <T> Supplier<T> sneaky(ThrowingSupplier<? extends T, ?> supplier) {
        requireNonNull(supplier);
        return () -> {
            try {
                return supplier.get();
            } catch (final Exception ex) {
                return SneakyThrowUtil.sneakyThrow(ex);
            }
        };
    }
}
