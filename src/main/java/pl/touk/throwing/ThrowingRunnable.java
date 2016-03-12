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

import java.util.Objects;

import pl.touk.throwing.exception.WrappedException;

/**
 * Represents an action that can be performed.
 * Function might throw a checked exception instance.
 *
 * @param <E> the type of the thrown checked exception
 */
@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {
    void run() throws E;

    static <E extends Exception> Runnable unchecked(ThrowingRunnable<E> runnable) {
        Objects.requireNonNull(runnable);

        return runnable.unchecked();
    }

    /**
     * @return a new Runnable instance which wraps thrown checked exception instance into a RuntimeException
     */
    default Runnable unchecked() {
        return () -> {
            try {
                run();
            } catch (final Throwable e) {
                throw new WrappedException(e, e.getClass());
            }
        };
    }
}
