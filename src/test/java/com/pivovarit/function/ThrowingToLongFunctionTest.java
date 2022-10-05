package com.pivovarit.function;

import com.tngtech.archunit.thirdparty.com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThrowingToLongFunctionTest {

    @Test
    void mapToLongTest() {
        List<Double> values = Lists.newArrayList(1.0, 2.0, 3.0, 4.0);
        long result = values.stream().mapToLong(ThrowingToLongFunction.<Double>unchecked(Double::longValue)).sum();
        assertThat(result).isEqualTo(10);
    }

    @Test
    void shouldWrapInRuntimeExWhenUsingUnchecked() {
        assertThatThrownBy(() -> Stream.of(". .")
                .mapToLong(ThrowingToLongFunction.unchecked(Long::parseLong))
                .sum())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("For input string: \". .\"")
                .hasCauseInstanceOf(NumberFormatException.class);
    }


}
