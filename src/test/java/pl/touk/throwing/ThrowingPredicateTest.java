package pl.touk.throwing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;
import static pl.touk.throwing.ThrowingPredicate.unchecked;

public class ThrowingPredicateTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldTest() throws Exception {
        // given
        final ThrowingPredicate<Integer, Exception> p = i -> true;

        // when
        final boolean result = p.test(42);

        // then
        assertThat(result).isTrue();

    }

    @Test
    public void shouldTestOR() throws Exception {
        // given
        final ThrowingPredicate<Integer, Exception> pTrue = i -> true;
        final ThrowingPredicate<Integer, Exception> pFalse = i -> false;

        // then
        assertThat(pTrue.or(pFalse).test(42)).isTrue();
        assertThat(pTrue.or(pTrue).test(42)).isTrue();
        assertThat(pFalse.or(pTrue).test(42)).isTrue();
        assertThat(pFalse.or(pFalse).test(42)).isFalse();
    }

    @Test
    public void shouldTestXOR() throws Exception {
        // given
        final ThrowingPredicate<Integer, Exception> pTrue = i -> true;
        final ThrowingPredicate<Integer, Exception> pFalse = i -> false;

        // then
        assertThat(pTrue.xor(pFalse).test(42)).isTrue();
        assertThat(pTrue.xor(pTrue).test(42)).isFalse();
        assertThat(pFalse.xor(pTrue).test(42)).isTrue();
        assertThat(pFalse.xor(pFalse).test(42)).isFalse();
    }

    @Test
    public void shouldTestAND() throws Exception {
        // given
        final ThrowingPredicate<Integer, Exception> pTrue = i -> true;
        final ThrowingPredicate<Integer, Exception> pFalse = i -> false;

        // then
        assertThat(pTrue.and(pFalse).test(42)).isFalse();
        assertThat(pTrue.and(pTrue).test(42)).isTrue();
        assertThat(pFalse.and(pTrue).test(42)).isFalse();
        assertThat(pFalse.and(pFalse).test(42)).isFalse();
    }

    @Test
    public void shouldTestNegate() throws Exception {
        // given
        final ThrowingPredicate<Integer, Exception> pTrue = i -> true;
        final ThrowingPredicate<Integer, Exception> pFalse = i -> false;

        // then
        assertThat(pTrue.negate().test(42)).isFalse();
        assertThat(pFalse.negate().test(42)).isTrue();
    }

    @Test
    public void shouldTestAsFunction() {
        // given
        final ThrowingPredicate<Integer, Exception> p = i -> true;

        // then
        assertThat(p.asFunction())
                .isInstanceOf(ThrowingFunction.class);
    }

    @Test
    public void shouldWrapInRuntimeExWhenUsingStandardUtilsFunctions() {
        final Exception cause = new Exception("some message");
        
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        final ThrowingPredicate<Integer, Exception> predicate = i -> {
            throw cause;
        };
        final List<Integer> integers = Collections.singletonList(42);

        // when
        integers.stream().anyMatch(i -> unchecked(predicate).test(i));

        // then RuntimeException is thrown
        fail("exception expected");
    }

    @Test
    public void shouldTestWhenUsingUncheck() {
        // given
        final ThrowingPredicate<Integer, Exception> predicate = i -> true;
        final List<Integer> integers = Collections.singletonList(42);

        // when
        integers.stream().anyMatch(predicate.uncheck());

        // then RuntimeException is thrown
    }

    @Test
    public void shouldWrapInRuntimeExWhenUsingUncheck() {
        final Exception cause = new Exception("some message");
        
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        final ThrowingPredicate<Integer, Exception> predicate = i -> {
            throw cause;
        };
        final List<Integer> integers = Collections.singletonList(42);

        // when
        integers.stream().anyMatch(i -> predicate.uncheck().test(i));

        // then RuntimeException is thrown
        fail("exception expected");
    }
}