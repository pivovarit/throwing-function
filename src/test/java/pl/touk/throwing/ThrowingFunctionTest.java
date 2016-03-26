package pl.touk.throwing;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.touk.throwing.TestCommons.givenThrowingFunction;
import static pl.touk.throwing.ThrowingFunction.checked;
import static pl.touk.throwing.ThrowingFunction.lifted;
import static pl.touk.throwing.ThrowingFunction.unchecked;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ThrowingFunctionTest {

    @Test
    public void shouldCompose() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = i -> 2 * i;
        final ThrowingFunction<Integer, Integer, Exception> f2 = i -> i + 10;

        // when
        final Integer result = f1.compose(f2).apply(3);

        // then
        assertThat(result).isEqualTo(26);
    }


    @Test
    public void shouldAndThen() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = i -> 2 * i;
        final ThrowingFunction<Integer, Integer, Exception> f2 = i -> i + 10;

        // when
        final Integer result = f1.andThen(f2).apply(3);

        // then
        assertThat(result).isEqualTo(16);
    }

    @Test
    public void shouldReturnOptional() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = ThrowingFunction.identity();

        // when
        final Optional<Integer> result = f1.lift().apply(42);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void shouldReturnEmptyOptional() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        final Optional<Integer> result = f1.lift().apply(42);

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test(expected = Exception.class)
    public void shouldThrowEx() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        f1.apply(42);

        // then RuntimeException is thrown
    }

    @Test(expected = RuntimeException.class)
    public void shouldWrapInRuntimeEx() throws Exception {
        // given
        final ThrowingFunction<Integer, Integer, Exception> f1 = givenThrowingFunction();

        // when
        f1.unchecked().apply(42);

        // then RuntimeException is thrown
    }

    @Test(expected = RuntimeException.class)
    public void shouldWrapInRuntimeExWhenUsingStandardUtilsFunctions() throws Exception {

        // when
        Stream.of(". .").map(unchecked(URI::new)).collect(toList());

        // then RuntimeException is thrown
    }

    @Test(expected = URISyntaxException.class)
    public void shouldUnwrapOriginalExceptionWhenUsingStandardUtilsFunctions() throws URISyntaxException {

        // when
        checked(URISyntaxException.class,
                () -> Stream.of(". .")
                .map(unchecked(URI::new))
                        .collect(toList())
        );

        // then a checked exception is thrown
    }

    @Test(expected = URISyntaxException.class)
    public void shouldUnwrapOriginalExceptionWithTryCatch() throws Throwable {

        // when
        ThrowingFunction.checked(() ->Stream.of(". .").map(unchecked(URI::new)).collect(toList()));

        // then a checked exception is thrown
    }

    @Test
    public void shouldWrapInOptionalWhenUsingStandardUtilsFunctions() throws Exception {

        // when
        final Optional<URI> result = Stream.of(". .").map(lifted(URI::new)).findAny().get();

        // then RuntimeException is thrown
        assertThat(result.isPresent()).isFalse();
    }


    @Test
    public void testName() throws Exception {

        Assertions.assertThat(Optional.of(new Bean("aaa"))).hasValue(new Bean("bbb"));

    }

    public static class Bean {
        public Bean(String a) {
            this.a = a;
        }

        private String a = "dupa";
    }
}