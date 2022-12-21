package com.google.zxing;

public abstract class ReaderException extends Exception {
    protected static final StackTraceElement[] NO_TRACE = new StackTraceElement[0];
    protected static boolean isStackTrace = (System.getProperty("surefire.test.class.path") != null);

    ReaderException() {
    }

    ReaderException(Throwable th) {
        super(th);
    }

    public final synchronized Throwable fillInStackTrace() {
        return null;
    }

    public static void setStackTrace(boolean z) {
        isStackTrace = z;
    }
}
