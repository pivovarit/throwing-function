package pl.touk.throwing;

import org.junit.jupiter.api.Test;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.touk.throwing.ThrowingFunction.unchecked;

class CheckerTest {

    @Test
    void shouldUnwrapOriginalExceptionWithTryCatch() {
        assertThatThrownBy(() -> Checker.checked(() -> Stream.of(". .")
          .map(unchecked(URI::new))
          .collect(toList())))
          .isInstanceOf(URISyntaxException.class)
          .hasMessage("Illegal character in path at index 1: . .");
    }

    @Test
    void shouldUnwrapSpecifiedExceptionWithTryCatch() {
        assertThatThrownBy(() -> Checker.checked(URISyntaxException.class, () -> Stream.of(". .")
          .map(unchecked(URI::new))
          .collect(toList())))
          .isInstanceOf(URISyntaxException.class)
          .hasMessage("Illegal character in path at index 1: . .");
    }

    @Test
    void shouldIgnoreUnspecifiedExceptionWithTryCatch() {
        assertThatThrownBy(() -> {
            Checker.checked(IOException.class, () -> Stream.of(". .")
              .map(unchecked(URI::new))
              .collect(toList()));
        }).isInstanceOf(WrappedException.class)
          .hasMessage("Illegal character in path at index 1: . .");
    }

    @Test
    void shouldApplyAfterUnwrapping() throws Throwable {

        // when
        final String result = Checker.checked(
          () -> Stream.of("a")
            .map(String::toUpperCase)
            .collect(joining()));

        // then
        assertThat(result).isEqualTo("A");
    }

    @Test
    void shouldApplyAfterUnwrappingSpecifiedEx() throws Throwable {

        // when
        final String result = Checker.checked(URISyntaxException.class,
          () -> Stream.of("a")
            .map(String::toUpperCase)
            .collect(joining()));

        // then
        assertThat(result).isEqualTo("A");
    }

    @Test
    void shouldUnwrapOriginalExceptionWhenUsingStandardUtilsFunctions() {
        assertThatThrownBy(() -> {
            Checker.checked(URISyntaxException.class,
              () -> Stream.of(". .")
                .map(unchecked(URI::new))
                .collect(toList())
            );
        }).isInstanceOf(URISyntaxException.class)
          .hasMessage("Illegal character in path at index 1: . .");
    }
}
