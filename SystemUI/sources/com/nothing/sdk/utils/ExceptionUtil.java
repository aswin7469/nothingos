package com.nothing.sdk.utils;

public class ExceptionUtil {
    public static void handleReflectionException(Exception exc) {
        if (exc instanceof ReflectiveOperationException) {
            throw new RuntimeException(exc.getMessage());
        }
    }
}
