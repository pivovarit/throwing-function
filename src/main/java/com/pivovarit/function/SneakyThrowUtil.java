package com.pivovarit.function;

final class SneakyThrowUtil {

    private SneakyThrowUtil() {
    }

    static <T extends Exception> void sneakyThrow(Exception t) throws T {
        throw (T) t;
    }
}
