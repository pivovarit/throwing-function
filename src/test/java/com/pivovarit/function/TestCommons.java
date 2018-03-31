package com.pivovarit.function;

final class TestCommons {

    private TestCommons() {
    }

    static ThrowingFunction<Integer, Integer, Exception> givenThrowingFunction() throws Exception {
        return i -> {
            throw new Exception();
        };
    }

    static ThrowingFunction<Integer, Integer, Exception> givenThrowingFunction(String message) throws Exception {
        return i -> {
            throw new Exception(message);
        };
    }
}
