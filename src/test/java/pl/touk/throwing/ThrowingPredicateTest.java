package pl.touk.throwing;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.touk.throwing.ThrowingPredicate.unchecked;

public class ThrowingPredicateTest {

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
    public void shouldReturnOptionalFunction() throws Exception {
        // given
        final ThrowingPredicate<Integer, Exception> pTrue = i -> true;
        final ThrowingPredicate<Integer, Exception> pFalse = i -> { throw new Exception(); };

        // then
        assertThat(pTrue.lift().apply(42).isPresent()).isTrue();
        assertThat(pFalse.lift().apply(42).isPresent()).isFalse();
    }

    @Test(expected = RuntimeException.class)
    public void shouldWrapInRuntimeExWhenUsingStandardUtilsFunctions() throws Exception {
        // given
        final ThrowingPredicate<Integer, Exception> predicate = i -> {
            throw new Exception();
        };
        final List<Integer> integers = Collections.singletonList(42);

        // when
        integers.stream().anyMatch(i -> unchecked(predicate).test(i));

        // then RuntimeException is thrown
    }
}