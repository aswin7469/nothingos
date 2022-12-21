package com.android.okhttp.okio;

import java.p026io.Closeable;
import java.p026io.Flushable;
import java.p026io.IOException;

public interface Sink extends Closeable, Flushable {
    void close() throws IOException;

    void flush() throws IOException;

    Timeout timeout();

    void write(Buffer buffer, long j) throws IOException;
}
