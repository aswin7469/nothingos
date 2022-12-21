package com.android.okhttp.internal.huc;

import com.android.okhttp.Request;
import com.android.okhttp.Response;
import com.android.okhttp.internal.InternalCache;
import com.android.okhttp.internal.http.CacheRequest;
import com.android.okhttp.internal.http.CacheStrategy;
import com.android.okhttp.okio.Okio;
import com.android.okhttp.okio.Sink;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.p026io.IOException;
import java.p026io.OutputStream;

public final class CacheAdapter implements InternalCache {
    private final ResponseCache delegate;

    public void remove(Request request) throws IOException {
    }

    public void trackConditionalCacheHit() {
    }

    public void trackResponse(CacheStrategy cacheStrategy) {
    }

    public void update(Response response, Response response2) throws IOException {
    }

    public CacheAdapter(ResponseCache responseCache) {
        this.delegate = responseCache;
    }

    public ResponseCache getDelegate() {
        return this.delegate;
    }

    public Response get(Request request) throws IOException {
        CacheResponse javaCachedResponse = getJavaCachedResponse(request);
        if (javaCachedResponse == null) {
            return null;
        }
        return JavaApiConverter.createOkResponseForCacheGet(request, javaCachedResponse);
    }

    public CacheRequest put(Response response) throws IOException {
        final java.net.CacheRequest put = this.delegate.put(response.request().uri(), JavaApiConverter.createJavaUrlConnectionForCachePut(response));
        if (put == null) {
            return null;
        }
        return new CacheRequest() {
            public Sink body() throws IOException {
                OutputStream body = put.getBody();
                if (body != null) {
                    return Okio.sink(body);
                }
                return null;
            }

            public void abort() {
                put.abort();
            }
        };
    }

    private CacheResponse getJavaCachedResponse(Request request) throws IOException {
        return this.delegate.get(request.uri(), request.method(), JavaApiConverter.extractJavaHeaders(request));
    }
}
