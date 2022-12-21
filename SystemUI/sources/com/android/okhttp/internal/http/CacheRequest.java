package com.android.okhttp.internal.http;

import com.android.okhttp.okio.Sink;
import java.p026io.IOException;

public interface CacheRequest {
    void abort();

    Sink body() throws IOException;
}
