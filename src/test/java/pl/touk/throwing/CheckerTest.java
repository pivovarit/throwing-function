package pl.touk.throwing;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.touk.throwing.exception.WrappedException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.fail;
import static pl.touk.throwing.ThrowingFunction.unchecked;

public class CheckerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldUnwrapOriginalExceptionWithTryCatch() throws Throwable {
        thrown.expect(URISyntaxException.class);
        thrown.expectMessage("Illegal character in path at index 1: . .");

        // when
        Checker.checked(() -> Stream.of(". .").map(unchecked(URI::new)).collect(toList()));

        // then a checked exception is thrown
        fail("exception expected");
    }

    @Test
    public void shouldUnwrapSpecifiedExceptionWithTryCatch() throws Throwable {
        thrown.expect(URISyntaxException.class);
        thrown.expectMessage("Illegal character in path at index 1: . .");

        // when
        Checker.checked(URISyntaxException.class,
          () -> Stream.of(". .").map(unchecked(URI::new)).collect(toList()));

        // then a checked exception is thrown
        fail("exception expected");
    }

    @Test
    public void shouldIgnoreUnspecifiedExceptionWithTryCatch() throws Throwable {
        thrown.expect(WrappedException.class);
        thrown.expectMessage("Illegal character in path at index 1: . .");
        thrown.expectCause(not(isA(IOException.class)));

        // when
        Checker.checked(IOException.class, () -> Stream.of(". .").map(unchecked(URI::new)).collect(toList()));

        // then a checked exception is thrown
        fail("exception expected");
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

    @Test
    public void shouldUnwrapOriginalExceptionWhenUsingStandardUtilsFunctions() throws URISyntaxException {
        thrown.expect(URISyntaxException.class);
        thrown.expectMessage("Illegal character in path at index 1: . .");

        // when
        Checker.checked(URISyntaxException.class,
          () -> Stream.of(". .")
            .map(unchecked(URI::new))
            .collect(toList())
        );

        // then a checked exception is thrown
        fail("exception expected");
    }
}
