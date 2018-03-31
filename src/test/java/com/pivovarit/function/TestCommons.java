package com.pivovarit.function;

final class TestCommons {

    private TestCommons() {
    }

    static ThrowingFunction<Integer, Integer, Exception> givenThrowingFunction() {
        return i -> {
            throw new Exception();
        };
    }

    static ThrowingFunction<Integer, Integer, Exception> givenThrowingFunction(String message) {
        return i -> {
            throw new Exception(message);
        };
    }
}
