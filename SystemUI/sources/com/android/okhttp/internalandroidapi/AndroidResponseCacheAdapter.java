package com.android.okhttp.internalandroidapi;

import android.annotation.SystemApi;
import com.android.okhttp.Cache;
import com.android.okhttp.Response;
import com.android.okhttp.internal.huc.JavaApiConverter;
import com.android.okhttp.internalandroidapi.HasCacheHolder;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.URI;
import java.net.URLConnection;
import java.p026io.IOException;
import java.util.List;
import java.util.Map;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class AndroidResponseCacheAdapter {
    private final HasCacheHolder.CacheHolder cacheHolder;
    private final Cache okHttpCache;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public AndroidResponseCacheAdapter(HasCacheHolder.CacheHolder cacheHolder2) {
        this.cacheHolder = cacheHolder2;
        this.okHttpCache = cacheHolder2.getCache();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public HasCacheHolder.CacheHolder getCacheHolder() {
        return this.cacheHolder;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public CacheResponse get(URI uri, String str, Map<String, List<String>> map) throws IOException {
        Response response = this.okHttpCache.internalCache.get(JavaApiConverter.createOkRequest(uri, str, map));
        if (response == null) {
            return null;
        }
        return JavaApiConverter.createJavaCacheResponse(response);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public CacheRequest put(URI uri, URLConnection uRLConnection) throws IOException {
        com.android.okhttp.internal.http.CacheRequest put;
        Response createOkResponseForCachePut = JavaApiConverter.createOkResponseForCachePut(uri, uRLConnection);
        if (createOkResponseForCachePut == null || (put = this.okHttpCache.internalCache.put(createOkResponseForCachePut)) == null) {
            return null;
        }
        return JavaApiConverter.createJavaCacheRequest(put);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public long getSize() throws IOException {
        return this.okHttpCache.getSize();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public long getMaxSize() {
        return this.okHttpCache.getMaxSize();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void flush() throws IOException {
        this.okHttpCache.flush();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public int getNetworkCount() {
        return this.okHttpCache.getNetworkCount();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public int getHitCount() {
        return this.okHttpCache.getHitCount();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public int getRequestCount() {
        return this.okHttpCache.getRequestCount();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void close() throws IOException {
        this.okHttpCache.close();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void delete() throws IOException {
        this.okHttpCache.delete();
    }
}
