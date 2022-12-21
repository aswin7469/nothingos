package com.android.okhttp.internal.http;

import java.p026io.IOException;

public final class RequestException extends Exception {
    public RequestException(IOException iOException) {
        super((Throwable) iOException);
    }

    public IOException getCause() {
        return (IOException) super.getCause();
    }
}
