package com.android.okhttp.internalandroidapi;

import android.annotation.SystemApi;
import com.android.okhttp.Cache;
import java.p026io.File;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public interface HasCacheHolder {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    CacheHolder getCacheHolder();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final class CacheHolder {
        private final Cache okHttpCache;

        private CacheHolder(Cache cache) {
            this.okHttpCache = cache;
        }

        private CacheHolder() {
            throw new UnsupportedOperationException();
        }

        public Cache getCache() {
            return this.okHttpCache;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public static CacheHolder create(File file, long j) {
            return new CacheHolder(new Cache(file, j));
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public boolean isEquivalent(File file, long j) {
            return this.okHttpCache.getDirectory().equals(file) && this.okHttpCache.getMaxSize() == j && !this.okHttpCache.isClosed();
        }
    }
}
