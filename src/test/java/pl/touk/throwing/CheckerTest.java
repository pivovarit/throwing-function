package pl.touk.throwing;

import org.junit.Test;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.touk.throwing.ThrowingFunction.unchecked;

public class CheckerTest {

    @Test(expected = URISyntaxException.class)
    public void shouldUnwrapOriginalExceptionWithTryCatch() throws Throwable {

        // when
        Checker.checked(() -> Stream.of(". .").map(unchecked(URI::new)).collect(toList()));

        // then a checked exception is thrown
    }

    @Test(expected = URISyntaxException.class)
    public void shouldUnwrapSpecifiedExceptionWithTryCatch() throws Throwable {

        // when
        Checker.checked(URISyntaxException.class,
                () -> Stream.of(". .").map(unchecked(URI::new)).collect(toList()));

        // then a checked exception is thrown
    }

    @Test(expected = WrappedException.class)
    public void shouldIgnoreUnspecifiedExceptionWithTryCatch() throws Throwable {

        // when
        Checker.checked(IOException.class, () -> Stream.of(". .").map(unchecked(URI::new)).collect(toList()));

        // then a checked exception is thrown
    }

    @Test
    public void shouldApplyAfterUnwrapping() throws Throwable {

        // when
        final String result = Checker.checked(
                () -> Stream.of("a")
                        .map(String::toUpperCase)
                        .collect(joining()));

        // then
        assertThat(result).isEqualTo("A");
    }

    @Test
    public void shouldApplyAfterUnwrappingSpecifiecEx() throws Throwable {

        // when
        final String result = Checker.checked(URISyntaxException.class,
                () -> Stream.of("a")
                        .map(String::toUpperCase)
                        .collect(joining()));

        // then
        assertThat(result).isEqualTo("A");
    }

    @Test(expected = URISyntaxException.class)
    public void shouldUnwrapOriginalExceptionWhenUsingStandardUtilsFunctions() throws URISyntaxException {

        // when
        Checker.checked(URISyntaxException.class,
                () -> Stream.of(". .")
                        .map(unchecked(URI::new))
                        .collect(toList())
        );

        // then a checked exception is thrown
    }
}
