package pl.touk.throwing;

import org.junit.jupiter.api.Test;
import pl.touk.throwing.exception.WrappedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingBinaryOperatorTest {

    @Test
    void shouldAndThen() throws Exception {
        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = (i, j) -> i * j;
        ThrowingFunction<Integer, Integer, Exception> f2 = i -> i + 10;

        // when
        Integer result = f1.andThen(f2).apply(2, 2);

        // then
        assertThat(result).isEqualTo(14);
    }

    @Test
    void shouldReturnOptional() {
        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = (i, j) -> i * j;

        // when
        Optional<Integer> result = f1.lift().apply(2, 2);

        // then
        assertThat(result).isPresent();
    }

    @Test
    void shouldReturnEmptyOptional() {
        // given
        ThrowingBinaryOperator<Integer, Exception> f1 =
          (i, j) -> { throw new Exception(); };

        // when
        Optional<Integer> result = f1.lift().apply(42, 42);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowEx() {
        // given
        ThrowingBinaryOperator<Integer, Exception> f1 =
          (i, j) -> { throw new Exception("some message"); };

        // when
        assertThatThrownBy(() -> f1.apply(42, 42))
          .isInstanceOf(Exception.class)
          .hasMessage("some message");
    }

    @Test
    void shouldWrapInWrappedEx() {
        Exception cause = new Exception("some message");

        // given
        ThrowingBinaryOperator<Integer, Exception> f1 =
          (i, j) -> { throw cause; };

        // when
        assertThatThrownBy(() -> f1.unchecked().apply(42, 42))
          .isInstanceOf(WrappedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        Exception cause = new Exception("some message");

        // given
        ThrowingBinaryOperator<Integer, Exception> f1 =
          (i, j) -> { throw cause; };

        // when
        assertThatThrownBy(() -> ThrowingBinaryOperator.unchecked(f1).apply(42, 42))
          .isInstanceOf(WrappedException.class)
          .hasMessage(cause.getMessage())
          .hasCause(cause);
    }

    @Test
    void shouldApplyWhenNoExceptionThrown() {
        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = (i, j) -> i + j;

        // when
        ThrowingBinaryOperator.unchecked(f1).apply(42, 0);

        // then no exception thrown
    }

    @Test
    void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() {

        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = (i, j) -> i + j;

        // when
        Optional<Integer> result = f1.lift().apply(2, 2);

        //then
        assertThat(result).isPresent();
    }

    @Test
    void shouldWrapInOptionalWhenUsingLifted() {

        // given
        ThrowingBinaryOperator<Integer, Exception> f1 = (i, j) -> i + j;

        // when
        Optional<Integer> result =
          ThrowingBiFunction.lifted(f1).apply(2, 2);

        //then
        assertThat(result).isPresent();
    }
}
