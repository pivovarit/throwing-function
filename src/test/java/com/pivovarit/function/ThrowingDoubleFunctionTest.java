package com.pivovarit.function;

import com.pivovarit.function.exception.WrappedException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.DoubleStream;

import static com.pivovarit.function.ThrowingDoubleFunction.lifted;
import static com.pivovarit.function.ThrowingDoubleFunction.unchecked;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowingDoubleFunctionTest {

    private static final String EXPECTED_MESSAGE = "Oh no! " + UUID.randomUUID();
    private static final ThrowingDoubleFunction<String, Exception> THROWS_CHECKED = i -> {
        throw new Exception(EXPECTED_MESSAGE);
    };

    @Test
    void shouldReturnOptional() {
        ThrowingDoubleFunction<String, Exception> f = Double::toString;

        Optional<String> result = f.lift().apply(42.0);

        assertThat(result).isEqualTo(Optional.of("42.0"));
    }

    @Test
    void shouldReturnEmptyOptional() {
        Optional<String> result = THROWS_CHECKED.lift().apply(42.0);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void shouldThrowEx() {
        assertThatThrownBy(() -> THROWS_CHECKED.apply(42.0))
                .isInstanceOf(Exception.class)
                .hasMessage(EXPECTED_MESSAGE);
    }

    @Test
    void shouldWrapInRuntimeEx() {
        assertThatThrownBy(() -> THROWS_CHECKED.uncheck().apply(42.0))
                .isInstanceOf(WrappedException.class)
                .hasMessage(EXPECTED_MESSAGE);
    }


    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        assertThatThrownBy(() -> DoubleStream.of(1).mapToObj(unchecked(THROWS_CHECKED)).count())
                .isInstanceOf(WrappedException.class)
                .hasMessage(EXPECTED_MESSAGE)
                .hasCauseInstanceOf(Exception.class);
    }

    @Test
    void shouldApplyWhenNoExceptionThrown() {
        ThrowingDoubleFunction<String, Exception> f = Double::toString;

        List<String> result = DoubleStream.of(42.0, 256).mapToObj(f.uncheck()).collect(toList());

        assertThat(result).containsExactly("42.0", "256.0");
    }

    @Test
    void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() {
        Long result = DoubleStream.of(42.0).mapToObj(lifted(THROWS_CHECKED)).filter(Optional::isPresent).count();

        assertThat(result).isZero();
    }

}