package com.android.okhttp;

import com.android.okhttp.internal.URLFilter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ResponseCache;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.p026io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import libcore.net.NetworkSecurityPolicy;

public class HttpHandler extends URLStreamHandler {
    private static final CleartextURLFilter CLEARTEXT_FILTER = new CleartextURLFilter();
    private static final List<ConnectionSpec> CLEARTEXT_ONLY = Collections.singletonList(ConnectionSpec.CLEARTEXT);
    private final ConfigAwareConnectionPool configAwareConnectionPool = ConfigAwareConnectionPool.getInstance();

    /* access modifiers changed from: protected */
    public int getDefaultPort() {
        return 80;
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url) throws IOException {
        return newOkUrlFactory((Proxy) null).open(url);
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        if (url != null && proxy != null) {
            return newOkUrlFactory(proxy).open(url);
        }
        throw new IllegalArgumentException("url == null || proxy == null");
    }

    /* access modifiers changed from: protected */
    public OkUrlFactory newOkUrlFactory(Proxy proxy) {
        OkUrlFactory createHttpOkUrlFactory = createHttpOkUrlFactory(proxy);
        createHttpOkUrlFactory.client().setConnectionPool(this.configAwareConnectionPool.get());
        return createHttpOkUrlFactory;
    }

    public static OkUrlFactory createHttpOkUrlFactory(Proxy proxy) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(0, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(0, TimeUnit.MILLISECONDS);
        okHttpClient.setWriteTimeout(0, TimeUnit.MILLISECONDS);
        okHttpClient.setFollowRedirects(HttpURLConnection.getFollowRedirects());
        okHttpClient.setFollowSslRedirects(false);
        okHttpClient.setConnectionSpecs(CLEARTEXT_ONLY);
        if (proxy != null) {
            okHttpClient.setProxy(proxy);
        }
        OkUrlFactory okUrlFactory = new OkUrlFactory(okHttpClient);
        OkUrlFactories.setUrlFilter(okUrlFactory, CLEARTEXT_FILTER);
        ResponseCache responseCache = ResponseCache.getDefault();
        if (responseCache != null) {
            AndroidInternal.setResponseCache(okUrlFactory, responseCache);
        }
        return okUrlFactory;
    }

    private static final class CleartextURLFilter implements URLFilter {
        private CleartextURLFilter() {
        }

        public void checkURLPermitted(URL url) throws IOException {
            String host = url.getHost();
            if (!NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(host)) {
                throw new IOException("Cleartext HTTP traffic to " + host + " not permitted");
            }
        }
    }
}
