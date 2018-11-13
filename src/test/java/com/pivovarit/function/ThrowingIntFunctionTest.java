package com.pivovarit.function;

import com.pivovarit.function.exception.WrappedException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.pivovarit.function.ThrowingIntFunction.lifted;
import static com.pivovarit.function.ThrowingIntFunction.unchecked;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingIntFunctionTest {

    private static final String EXPECTED_MESSAGE = "Oh no! " + UUID.randomUUID();
    private static final ThrowingIntFunction<String, Exception> THROWS_CHECKED = i -> {
        throw new Exception(EXPECTED_MESSAGE);
    };

    @Test
    void shouldReturnOptional() {
        ThrowingIntFunction<String, Exception> f = Integer::toString;

        Optional<String> result = f.lift().apply(42);

        assertThat(result).isEqualTo(Optional.of("42"));
    }

    @Test
    void shouldReturnEmptyOptional() {
        Optional<String> result = THROWS_CHECKED.lift().apply(42);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void shouldThrowEx() {
        assertThatThrownBy(() -> THROWS_CHECKED.apply(42))
                .isInstanceOf(Exception.class)
                .hasMessage(EXPECTED_MESSAGE);
    }

    @Test
    void shouldWrapInRuntimeEx() {
        assertThatThrownBy(() -> THROWS_CHECKED.uncheck().apply(42))
                .isInstanceOf(WrappedException.class)
                .hasMessage(EXPECTED_MESSAGE);
    }


    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        assertThatThrownBy(() -> IntStream.of(1).mapToObj(unchecked(THROWS_CHECKED)).count())
                .isInstanceOf(WrappedException.class)
                .hasMessage(EXPECTED_MESSAGE)
                .hasCauseInstanceOf(Exception.class);
    }

    @Test
    void shouldApplyWhenNoExceptionThrown() {
        ThrowingIntFunction<String, Exception> f = Integer::toString;

        List<String> result = IntStream.of(42, 256).mapToObj(f.uncheck()).collect(toList());

        assertThat(result).containsExactly("42", "256");
    }

    @Test
    void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() {
        Long result = IntStream.of(42).mapToObj(lifted(THROWS_CHECKED)).filter(Optional::isPresent).count();

        assertThat(result).isZero();
    }

}