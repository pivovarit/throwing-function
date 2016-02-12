package pl.touk.throwing;

public final class TestCommons {

    private TestCommons() {
    }

    static ThrowingFunction<Integer, Integer, Exception> givenThrowingFunction() throws Exception {
        return i -> {
            throw new Exception();
        };
    }
}
