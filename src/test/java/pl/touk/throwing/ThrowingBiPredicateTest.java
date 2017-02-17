package pl.touk.throwing;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ThrowingBiPredicateTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldTest() throws Exception {
        // given
        final ThrowingBiPredicate<Integer, Integer, Exception> p = (i, j) -> true;

        // when
        final boolean result = p.test(42, 0);

        // then
        assertThat(result).isTrue();

    }

    @Test
    public void shouldTestOR() throws Exception {
        // given
        final ThrowingBiPredicate<Integer, Integer, Exception> pTrue = (i, j) -> true;
        final ThrowingBiPredicate<Integer, Integer, Exception> pFalse = (i, j) -> false;

        // then
        assertThat(pTrue.or(pFalse).test(42, 0)).isTrue();
        assertThat(pTrue.or(pTrue).test(42, 0)).isTrue();
        assertThat(pFalse.or(pTrue).test(42, 0)).isTrue();
        assertThat(pFalse.or(pFalse).test(42, 0)).isFalse();
    }

    @Test
    public void shouldTestXOR() throws Exception {
        // given
        final ThrowingBiPredicate<Integer, Integer, Exception> pTrue = (i, j) -> true;
        final ThrowingBiPredicate<Integer, Integer, Exception> pFalse = (i, j) -> false;

        // then
        assertThat(pTrue.xor(pFalse).test(42, 0)).isTrue();
        assertThat(pTrue.xor(pTrue).test(42, 0)).isFalse();
        assertThat(pFalse.xor(pTrue).test(42, 0)).isTrue();
        assertThat(pFalse.xor(pFalse).test(42, 0)).isFalse();
    }

    @Test
    public void shouldTestAND() throws Exception {
        // given
        final ThrowingBiPredicate<Integer, Integer, Exception> pTrue = (i, j) -> true;
        final ThrowingBiPredicate<Integer, Integer, Exception> pFalse = (i, j) -> false;

        // then
        assertThat(pTrue.and(pFalse).test(42, 0)).isFalse();
        assertThat(pTrue.and(pTrue).test(42, 0)).isTrue();
        assertThat(pFalse.and(pTrue).test(42, 0)).isFalse();
        assertThat(pFalse.and(pFalse).test(42, 0)).isFalse();
    }

    @Test
    public void shouldTestNegate() throws Exception {
        // given
        final ThrowingBiPredicate<Integer, Integer, Exception> pTrue = (i, j) -> true;
        final ThrowingBiPredicate<Integer, Integer, Exception> pFalse = (i, j) -> false;

        // then
        assertThat(pTrue.negate().test(42, 0)).isFalse();
        assertThat(pFalse.negate().test(42, 0)).isTrue();
    }

    @Test
    public void shouldTestAsFunction() throws Exception {
        // given
        final ThrowingBiPredicate<Integer, Integer, Exception> p = (i, j) -> true;

        // then
        assertThat(p.asFunction().apply(1, 2)).isTrue();
    }

    @Test
    public void shouldWrapInRuntimeExWhenUsingStandardUtilsFunctions() throws Exception {
        final Exception cause = new Exception("some message");
        
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("some message");
        thrown.expectCause(is(cause));

        // given
        final ThrowingBiPredicate<Integer, Integer, Exception> predicate = (i, j) -> {
            throw cause;
        };

        // when
        ThrowingBiPredicate.unchecked(predicate).test(42, 0);

        // then RuntimeException is thrown
        fail("exception expected");
    }

    @Test
    public void shouldTestWhenUsingUncheck() throws Exception {
        // given
        final ThrowingBiPredicate<Integer, Integer, Exception> predicate = (i, j) -> true;

        // when
        predicate.uncheck().test(1, 2);

        // then no exception is thrown
    }
}