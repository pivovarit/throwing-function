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

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RecoverTest {

    @Test
    void functionRecoversUsingInputAndException() {
        ThrowingFunction<String, String, Exception> f = s -> { throw new Exception("E"); };
        Function<String, String> recovered = ThrowingFunction.recover(f, (s, e) -> s + ":" + e.getMessage());
        assertThat(recovered.apply("in")).isEqualTo("in:E");
    }

    @Test
    void functionDoesNotInvokeHandlerOnSuccess() {
        AtomicReference<String> handlerCalled = new AtomicReference<>();
        Function<String, Integer> recovered = ThrowingFunction.recover(
          Integer::parseInt, (s, e) -> { handlerCalled.set(s); return -1; });
        assertThat(recovered.apply("42")).isEqualTo(42);
        assertThat(handlerCalled.get()).isNull();
    }

    @Test
    void unaryOperatorRecoversUsingInputAndException() {
        ThrowingUnaryOperator<String, Exception> op = s -> { throw new Exception("boom"); };
        UnaryOperator<String> recovered = ThrowingUnaryOperator.recover(op, (s, e) -> s.toUpperCase());
        assertThat(recovered.apply("x")).isEqualTo("X");
    }

    @Test
    void toLongFunctionRecoversUsingInputAndException() {
        ThrowingToLongFunction<String, Exception> f = s -> { throw new Exception(); };
        ToLongFunction<String> recovered = ThrowingToLongFunction.recover(f, (s, e) -> (long) s.length());
        assertThat(recovered.applyAsLong("abcd")).isEqualTo(4L);
    }

    @Test
    void predicateRecoversToFalse() {
        ThrowingPredicate<String, Exception> p = s -> { throw new Exception(); };
        Predicate<String> recovered = ThrowingPredicate.recover(p, (s, e) -> false);
        assertThat(recovered.test("x")).isFalse();
    }

    @Test
    void consumerRecoversUsingInputAndException() {
        AtomicReference<String> captured = new AtomicReference<>();
        ThrowingConsumer<String, Exception> c = s -> { throw new Exception(s); };
        Consumer<String> recovered = ThrowingConsumer.recover(c, (s, e) -> captured.set(s + ":" + e.getMessage()));
        recovered.accept("x");
        assertThat(captured.get()).isEqualTo("x:x");
    }

    @Test
    void runnableRecoversUsingException() {
        AtomicReference<String> captured = new AtomicReference<>();
        ThrowingRunnable<Exception> r = () -> { throw new Exception("failed"); };
        Runnable recovered = ThrowingRunnable.recover(r, e -> captured.set(e.getMessage()));
        recovered.run();
        assertThat(captured.get()).isEqualTo("failed");
    }

    @Test
    void supplierRecoversUsingException() {
        ThrowingSupplier<String, Exception> s = () -> { throw new Exception("oops"); };
        Supplier<String> recovered = ThrowingSupplier.recover(s, e -> "fallback:" + e.getMessage());
        assertThat(recovered.get()).isEqualTo("fallback:oops");
    }

    @Test
    void biFunctionRecoversUsingException() {
        ThrowingBiFunction<String, String, String, Exception> f = (a, b) -> { throw new Exception("x"); };
        BiFunction<String, String, String> recovered = ThrowingBiFunction.recover(f, e -> "fallback");
        assertThat(recovered.apply("a", "b")).isEqualTo("fallback");
    }

    @Test
    void binaryOperatorRecoversUsingException() {
        ThrowingBinaryOperator<String, Exception> op = (a, b) -> { throw new Exception(); };
        BinaryOperator<String> recovered = ThrowingBinaryOperator.recover(op, e -> "default");
        assertThat(recovered.apply("a", "b")).isEqualTo("default");
    }

    @Test
    void biConsumerRecoversUsingException() {
        AtomicReference<String> captured = new AtomicReference<>();
        ThrowingBiConsumer<String, String, Exception> c = (a, b) -> { throw new Exception("e"); };
        BiConsumer<String, String> recovered = ThrowingBiConsumer.recover(c, e -> captured.set(e.getMessage()));
        recovered.accept("a", "b");
        assertThat(captured.get()).isEqualTo("e");
    }

    @Test
    void biPredicateRecoversToFalse() {
        ThrowingBiPredicate<String, String, Exception> p = (a, b) -> { throw new Exception(); };
        BiPredicate<String, String> recovered = ThrowingBiPredicate.recover(p, e -> false);
        assertThat(recovered.test("a", "b")).isFalse();
    }

    @Test
    void unaryOperatorPassesThroughOnSuccess() {
        UnaryOperator<String> recovered = ThrowingUnaryOperator.recover(s -> s + "!", (s, e) -> "X");
        assertThat(recovered.apply("hi")).isEqualTo("hi!");
    }

    @Test
    void toIntFunctionPassesThroughOnSuccess() {
        ToIntFunction<String> recovered = ThrowingToIntFunction.recover(s -> 7, (s, e) -> -1);
        assertThat(recovered.applyAsInt("x")).isEqualTo(7);
    }

    @Test
    void toLongFunctionPassesThroughOnSuccess() {
        ToLongFunction<String> recovered = ThrowingToLongFunction.recover(s -> 7L, (s, e) -> -1L);
        assertThat(recovered.applyAsLong("x")).isEqualTo(7L);
    }

    @Test
    void toDoubleFunctionPassesThroughOnSuccess() {
        ToDoubleFunction<String> recovered = ThrowingToDoubleFunction.recover(s -> 7.0, (s, e) -> -1.0);
        assertThat(recovered.applyAsDouble("x")).isEqualTo(7.0);
    }

    @Test
    void predicatePassesThroughOnSuccess() {
        Predicate<String> recovered = ThrowingPredicate.recover(s -> true, (s, e) -> false);
        assertThat(recovered.test("x")).isTrue();
    }

    @Test
    void predicateRecoversToTrue() {
        ThrowingPredicate<String, Exception> p = s -> { throw new Exception(); };
        Predicate<String> recovered = ThrowingPredicate.recover(p, (s, e) -> true);
        assertThat(recovered.test("x")).isTrue();
    }

    @Test
    void supplierPassesThroughOnSuccess() {
        Supplier<String> recovered = ThrowingSupplier.recover(() -> "ok", e -> "fallback");
        assertThat(recovered.get()).isEqualTo("ok");
    }

    @Test
    void consumerDoesNotInvokeHandlerOnSuccess() {
        AtomicReference<String> sideEffect = new AtomicReference<>();
        AtomicReference<String> handled = new AtomicReference<>();
        Consumer<String> recovered = ThrowingConsumer.recover(sideEffect::set, (s, e) -> handled.set(s));
        recovered.accept("hello");
        assertThat(sideEffect.get()).isEqualTo("hello");
        assertThat(handled.get()).isNull();
    }

    @Test
    void runnableDoesNotInvokeHandlerOnSuccess() {
        AtomicReference<String> handled = new AtomicReference<>();
        Runnable recovered = ThrowingRunnable.recover(() -> {}, e -> handled.set("x"));
        recovered.run();
        assertThat(handled.get()).isNull();
    }

    @Test
    void biFunctionPassesThroughOnSuccess() {
        BiFunction<String, String, String> recovered = ThrowingBiFunction.recover((a, b) -> a + b, e -> "fallback");
        assertThat(recovered.apply("a", "b")).isEqualTo("ab");
    }

    @Test
    void binaryOperatorPassesThroughOnSuccess() {
        BinaryOperator<String> recovered = ThrowingBinaryOperator.recover((a, b) -> a + b, e -> "default");
        assertThat(recovered.apply("a", "b")).isEqualTo("ab");
    }

    @Test
    void biConsumerDoesNotInvokeHandlerOnSuccess() {
        AtomicReference<String> sideEffect = new AtomicReference<>();
        AtomicReference<String> handled = new AtomicReference<>();
        BiConsumer<String, String> recovered = ThrowingBiConsumer.recover((a, b) -> sideEffect.set(a + b), e -> handled.set("x"));
        recovered.accept("a", "b");
        assertThat(sideEffect.get()).isEqualTo("ab");
        assertThat(handled.get()).isNull();
    }

    @Test
    void biPredicatePassesThroughOnSuccess() {
        BiPredicate<String, String> recovered = ThrowingBiPredicate.recover((a, b) -> true, e -> false);
        assertThat(recovered.test("a", "b")).isTrue();
    }

    @Test
    void predicatePassesFalseThroughOnSuccess() {
        Predicate<String> recovered = ThrowingPredicate.recover(s -> false, (s, e) -> true);
        assertThat(recovered.test("x")).isFalse();
    }

    @Test
    void biPredicatePassesFalseThroughOnSuccess() {
        BiPredicate<String, String> recovered = ThrowingBiPredicate.recover((a, b) -> false, e -> true);
        assertThat(recovered.test("a", "b")).isFalse();
    }

    @Test
    void biPredicateRecoversToTrue() {
        ThrowingBiPredicate<String, String, Exception> p = (a, b) -> { throw new Exception(); };
        BiPredicate<String, String> recovered = ThrowingBiPredicate.recover(p, e -> true);
        assertThat(recovered.test("a", "b")).isTrue();
    }

    @Test
    void rejectsNullArguments() {
        ThrowingFunction<String, String, Exception> f = s -> s;
        assertThatThrownBy(() -> ThrowingFunction.recover(f, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ThrowingFunction.recover(null, (s, e) -> s)).isInstanceOf(NullPointerException.class);

        ThrowingSupplier<String, Exception> s = () -> "x";
        assertThatThrownBy(() -> ThrowingSupplier.recover(s, null)).isInstanceOf(NullPointerException.class);

        ThrowingBiFunction<String, String, String, Exception> bf = (a, b) -> a;
        assertThatThrownBy(() -> ThrowingBiFunction.recover(bf, null)).isInstanceOf(NullPointerException.class);

        ThrowingRunnable<Exception> r = () -> {};
        assertThatThrownBy(() -> ThrowingRunnable.recover(r, null)).isInstanceOf(NullPointerException.class);
    }
}
