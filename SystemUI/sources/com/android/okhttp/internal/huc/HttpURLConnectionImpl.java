package com.android.okhttp.internal.huc;

import com.android.okhttp.Cache;
import com.android.okhttp.Handshake;
import com.android.okhttp.Headers;
import com.android.okhttp.HttpUrl;
import com.android.okhttp.MediaType;
import com.android.okhttp.OkHttpClient;
import com.android.okhttp.Protocol;
import com.android.okhttp.Request;
import com.android.okhttp.RequestBody;
import com.android.okhttp.Response;
import com.android.okhttp.Route;
import com.android.okhttp.internal.Internal;
import com.android.okhttp.internal.Platform;
import com.android.okhttp.internal.URLFilter;
import com.android.okhttp.internal.Util;
import com.android.okhttp.internal.Version;
import com.android.okhttp.internal.http.HttpDate;
import com.android.okhttp.internal.http.HttpEngine;
import com.android.okhttp.internal.http.HttpMethod;
import com.android.okhttp.internal.http.OkHeaders;
import com.android.okhttp.internal.http.RetryableSink;
import com.android.okhttp.internal.http.StatusLine;
import com.android.okhttp.internal.http.StreamAllocation;
import com.android.okhttp.okio.BufferedSink;
import com.android.okhttp.okio.Sink;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketPermission;
import java.net.URL;
import java.net.UnknownHostException;
import java.p026io.FileNotFoundException;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class HttpURLConnectionImpl extends HttpURLConnection {
    private static final RequestBody EMPTY_REQUEST_BODY = RequestBody.create((MediaType) null, new byte[0]);
    private static final Set<String> METHODS = new LinkedHashSet(Arrays.asList("OPTIONS", "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "PATCH"));
    final OkHttpClient client;
    private long fixedContentLength;
    private int followUpCount;
    Handshake handshake;
    protected HttpEngine httpEngine;
    protected IOException httpEngineFailure;
    private Headers.Builder requestHeaders;
    private Headers responseHeaders;
    private Route route;
    private URLFilter urlFilter;

    public HttpURLConnectionImpl(URL url, OkHttpClient okHttpClient) {
        super(url);
        this.requestHeaders = new Headers.Builder();
        this.fixedContentLength = -1;
        this.client = okHttpClient;
    }

    public HttpURLConnectionImpl(URL url, OkHttpClient okHttpClient, URLFilter uRLFilter) {
        this(url, okHttpClient);
        this.urlFilter = uRLFilter;
    }

    public final void connect() throws IOException {
        initHttpEngine();
        do {
        } while (!execute(false));
    }

    public final void disconnect() {
        HttpEngine httpEngine2 = this.httpEngine;
        if (httpEngine2 != null) {
            httpEngine2.cancel();
        }
    }

    public final InputStream getErrorStream() {
        try {
            HttpEngine response = getResponse();
            if (HttpEngine.hasBody(response.getResponse()) && response.getResponse().code() >= 400) {
                return response.getResponse().body().byteStream();
            }
        } catch (IOException unused) {
        }
        return null;
    }

    private Headers getHeaders() throws IOException {
        if (this.responseHeaders == null) {
            Response response = getResponse().getResponse();
            this.responseHeaders = response.headers().newBuilder().add(OkHeaders.SELECTED_PROTOCOL, response.protocol().toString()).add(OkHeaders.RESPONSE_SOURCE, responseSourceHeader(response)).build();
        }
        return this.responseHeaders;
    }

    private static String responseSourceHeader(Response response) {
        if (response.networkResponse() == null) {
            if (response.cacheResponse() == null) {
                return "NONE";
            }
            return "CACHE " + response.code();
        } else if (response.cacheResponse() == null) {
            return "NETWORK " + response.code();
        } else {
            return "CONDITIONAL_CACHE " + response.networkResponse().code();
        }
    }

    public final String getHeaderField(int i) {
        try {
            return getHeaders().value(i);
        } catch (IOException unused) {
            return null;
        }
    }

    public final String getHeaderField(String str) {
        if (str != null) {
            return getHeaders().get(str);
        }
        try {
            return StatusLine.get(getResponse().getResponse()).toString();
        } catch (IOException unused) {
            return null;
        }
    }

    public final String getHeaderFieldKey(int i) {
        try {
            return getHeaders().name(i);
        } catch (IOException unused) {
            return null;
        }
    }

    public final Map<String, List<String>> getHeaderFields() {
        try {
            return OkHeaders.toMultimap(getHeaders(), StatusLine.get(getResponse().getResponse()).toString());
        } catch (IOException unused) {
            return Collections.emptyMap();
        }
    }

    public final Map<String, List<String>> getRequestProperties() {
        if (!this.connected) {
            return OkHeaders.toMultimap(this.requestHeaders.build(), (String) null);
        }
        throw new IllegalStateException("Cannot access request header fields after connection is set");
    }

    public final InputStream getInputStream() throws IOException {
        if (this.doInput) {
            HttpEngine response = getResponse();
            if (getResponseCode() < 400) {
                return response.getResponse().body().byteStream();
            }
            throw new FileNotFoundException(this.url.toString());
        }
        throw new ProtocolException("This protocol does not support input");
    }

    public final OutputStream getOutputStream() throws IOException {
        connect();
        BufferedSink bufferedRequestBody = this.httpEngine.getBufferedRequestBody();
        if (bufferedRequestBody == null) {
            throw new ProtocolException("method does not support a request body: " + this.method);
        } else if (!this.httpEngine.hasResponse()) {
            return bufferedRequestBody.outputStream();
        } else {
            throw new ProtocolException("cannot write request body after response has been read");
        }
    }

    public final Permission getPermission() throws IOException {
        int i;
        URL url = getURL();
        String host = url.getHost();
        if (url.getPort() != -1) {
            i = url.getPort();
        } else {
            i = HttpUrl.defaultPort(url.getProtocol());
        }
        if (usingProxy()) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) this.client.getProxy().address();
            host = inetSocketAddress.getHostName();
            i = inetSocketAddress.getPort();
        }
        return new SocketPermission(host + ":" + i, "connect, resolve");
    }

    public final String getRequestProperty(String str) {
        if (str == null) {
            return null;
        }
        return this.requestHeaders.get(str);
    }

    public void setConnectTimeout(int i) {
        this.client.setConnectTimeout((long) i, TimeUnit.MILLISECONDS);
    }

    public void setInstanceFollowRedirects(boolean z) {
        this.client.setFollowRedirects(z);
    }

    public boolean getInstanceFollowRedirects() {
        return this.client.getFollowRedirects();
    }

    public int getConnectTimeout() {
        return this.client.getConnectTimeout();
    }

    public void setReadTimeout(int i) {
        this.client.setReadTimeout((long) i, TimeUnit.MILLISECONDS);
    }

    public int getReadTimeout() {
        return this.client.getReadTimeout();
    }

    private void initHttpEngine() throws IOException {
        IOException iOException = this.httpEngineFailure;
        if (iOException != null) {
            throw iOException;
        } else if (this.httpEngine == null) {
            this.connected = true;
            try {
                if (this.doOutput) {
                    if (this.method.equals("GET")) {
                        this.method = "POST";
                    } else if (!HttpMethod.permitsRequestBody(this.method)) {
                        throw new ProtocolException(this.method + " does not support writing");
                    }
                }
                this.httpEngine = newHttpEngine(this.method, (StreamAllocation) null, (RetryableSink) null, (Response) null);
            } catch (IOException e) {
                this.httpEngineFailure = e;
                throw e;
            }
        }
    }

    private HttpEngine newHttpEngine(String str, StreamAllocation streamAllocation, RetryableSink retryableSink, Response response) throws MalformedURLException, UnknownHostException {
        Request.Builder method = new Request.Builder().url(Internal.instance.getHttpUrlChecked(getURL().toString())).method(str, HttpMethod.requiresRequestBody(str) ? EMPTY_REQUEST_BODY : null);
        Headers build = this.requestHeaders.build();
        int size = build.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            method.addHeader(build.name(i), build.value(i));
        }
        if (HttpMethod.permitsRequestBody(str)) {
            long j = this.fixedContentLength;
            if (j != -1) {
                method.header("Content-Length", Long.toString(j));
            } else if (this.chunkLength > 0) {
                method.header("Transfer-Encoding", "chunked");
            } else {
                z = true;
            }
            if (build.get("Content-Type") == null) {
                method.header("Content-Type", "application/x-www-form-urlencoded");
            }
        }
        boolean z2 = z;
        if (build.get("User-Agent") == null) {
            method.header("User-Agent", defaultUserAgent());
        }
        Request build2 = method.build();
        OkHttpClient okHttpClient = this.client;
        return new HttpEngine((Internal.instance.internalCache(okHttpClient) == null || getUseCaches()) ? okHttpClient : this.client.clone().setCache((Cache) null), build2, z2, true, false, streamAllocation, retryableSink, response);
    }

    private String defaultUserAgent() {
        String property = System.getProperty("http.agent");
        return property != null ? Util.toHumanReadableAscii(property) : Version.userAgent();
    }

    private HttpEngine getResponse() throws IOException {
        initHttpEngine();
        if (this.httpEngine.hasResponse()) {
            return this.httpEngine;
        }
        while (true) {
            if (execute(true)) {
                Response response = this.httpEngine.getResponse();
                Request followUpRequest = this.httpEngine.followUpRequest();
                if (followUpRequest == null) {
                    this.httpEngine.releaseStreamAllocation();
                    return this.httpEngine;
                }
                int i = this.followUpCount + 1;
                this.followUpCount = i;
                if (i <= 20) {
                    this.url = followUpRequest.url();
                    this.requestHeaders = followUpRequest.headers().newBuilder();
                    Sink requestBody = this.httpEngine.getRequestBody();
                    StreamAllocation streamAllocation = null;
                    if (!followUpRequest.method().equals(this.method)) {
                        requestBody = null;
                    }
                    if (requestBody == null || (requestBody instanceof RetryableSink)) {
                        StreamAllocation close = this.httpEngine.close();
                        if (!this.httpEngine.sameConnection(followUpRequest.httpUrl())) {
                            close.release();
                        } else {
                            streamAllocation = close;
                        }
                        this.httpEngine = newHttpEngine(followUpRequest.method(), streamAllocation, (RetryableSink) requestBody, response);
                    } else {
                        throw new HttpRetryException("Cannot retry streamed HTTP body", this.responseCode);
                    }
                } else {
                    throw new ProtocolException("Too many follow-up requests: " + this.followUpCount);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x006b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean execute(boolean r5) throws java.p026io.IOException {
        /*
            r4 = this;
            com.android.okhttp.internal.URLFilter r0 = r4.urlFilter
            if (r0 == 0) goto L_0x0011
            com.android.okhttp.internal.http.HttpEngine r1 = r4.httpEngine
            com.android.okhttp.Request r1 = r1.getRequest()
            java.net.URL r1 = r1.url()
            r0.checkURLPermitted(r1)
        L_0x0011:
            r0 = 0
            r1 = 1
            com.android.okhttp.internal.http.HttpEngine r2 = r4.httpEngine     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            r2.sendRequest()     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            com.android.okhttp.internal.http.HttpEngine r2 = r4.httpEngine     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            com.android.okhttp.Connection r2 = r2.getConnection()     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            if (r2 == 0) goto L_0x002d
            com.android.okhttp.Route r3 = r2.getRoute()     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            r4.route = r3     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            com.android.okhttp.Handshake r2 = r2.getHandshake()     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            r4.handshake = r2     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            goto L_0x0032
        L_0x002d:
            r2 = 0
            r4.route = r2     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            r4.handshake = r2     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
        L_0x0032:
            if (r5 == 0) goto L_0x0039
            com.android.okhttp.internal.http.HttpEngine r5 = r4.httpEngine     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
            r5.readResponse()     // Catch:{ RequestException -> 0x0061, RouteException -> 0x004c, IOException -> 0x003d }
        L_0x0039:
            return r1
        L_0x003a:
            r5 = move-exception
            r0 = r1
            goto L_0x0069
        L_0x003d:
            r5 = move-exception
            com.android.okhttp.internal.http.HttpEngine r2 = r4.httpEngine     // Catch:{ all -> 0x003a }
            com.android.okhttp.internal.http.HttpEngine r2 = r2.recover((java.p026io.IOException) r5)     // Catch:{ all -> 0x003a }
            if (r2 == 0) goto L_0x0049
            r4.httpEngine = r2     // Catch:{ all -> 0x0058 }
            return r0
        L_0x0049:
            r4.httpEngineFailure = r5     // Catch:{ all -> 0x003a }
            throw r5     // Catch:{ all -> 0x003a }
        L_0x004c:
            r5 = move-exception
            com.android.okhttp.internal.http.HttpEngine r2 = r4.httpEngine     // Catch:{ all -> 0x003a }
            com.android.okhttp.internal.http.HttpEngine r2 = r2.recover((com.android.okhttp.internal.http.RouteException) r5)     // Catch:{ all -> 0x003a }
            if (r2 == 0) goto L_0x005a
            r4.httpEngine = r2     // Catch:{ all -> 0x0058 }
            return r0
        L_0x0058:
            r5 = move-exception
            goto L_0x0069
        L_0x005a:
            java.io.IOException r5 = r5.getLastConnectException()     // Catch:{ all -> 0x003a }
            r4.httpEngineFailure = r5     // Catch:{ all -> 0x003a }
            throw r5     // Catch:{ all -> 0x003a }
        L_0x0061:
            r5 = move-exception
            java.io.IOException r5 = r5.getCause()     // Catch:{ all -> 0x003a }
            r4.httpEngineFailure = r5     // Catch:{ all -> 0x003a }
            throw r5     // Catch:{ all -> 0x003a }
        L_0x0069:
            if (r0 == 0) goto L_0x0074
            com.android.okhttp.internal.http.HttpEngine r4 = r4.httpEngine
            com.android.okhttp.internal.http.StreamAllocation r4 = r4.close()
            r4.release()
        L_0x0074:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.huc.HttpURLConnectionImpl.execute(boolean):boolean");
    }

    public final boolean usingProxy() {
        Proxy proxy;
        Route route2 = this.route;
        if (route2 != null) {
            proxy = route2.getProxy();
        } else {
            proxy = this.client.getProxy();
        }
        return (proxy == null || proxy.type() == Proxy.Type.DIRECT) ? false : true;
    }

    public String getResponseMessage() throws IOException {
        return getResponse().getResponse().message();
    }

    public final int getResponseCode() throws IOException {
        return getResponse().getResponse().code();
    }

    public final void setRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Cannot set request property after connection is made");
        } else if (str == null) {
            throw new NullPointerException("field == null");
        } else if (str2 == null) {
            Platform platform = Platform.get();
            platform.logW("Ignoring header " + str + " because its value was null.");
        } else if ("X-Android-Transports".equals(str) || "X-Android-Protocols".equals(str)) {
            setProtocols(str2, false);
        } else {
            this.requestHeaders.set(str, str2);
        }
    }

    public void setIfModifiedSince(long j) {
        super.setIfModifiedSince(j);
        if (this.ifModifiedSince != 0) {
            this.requestHeaders.set("If-Modified-Since", HttpDate.format(new Date(this.ifModifiedSince)));
        } else {
            this.requestHeaders.removeAll("If-Modified-Since");
        }
    }

    public final void addRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Cannot add request property after connection is made");
        } else if (str == null) {
            throw new NullPointerException("field == null");
        } else if (str2 == null) {
            Platform platform = Platform.get();
            platform.logW("Ignoring header " + str + " because its value was null.");
        } else if ("X-Android-Transports".equals(str) || "X-Android-Protocols".equals(str)) {
            setProtocols(str2, true);
        } else {
            this.requestHeaders.add(str, str2);
        }
    }

    private void setProtocols(String str, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (z) {
            arrayList.addAll(this.client.getProtocols());
        }
        String[] split = str.split(NavigationBarInflaterView.BUTTON_SEPARATOR, -1);
        int length = split.length;
        int i = 0;
        while (i < length) {
            try {
                arrayList.add(Protocol.get(split[i]));
                i++;
            } catch (IOException e) {
                throw new IllegalStateException((Throwable) e);
            }
        }
        this.client.setProtocols(arrayList);
    }

    public void setRequestMethod(String str) throws ProtocolException {
        Set<String> set = METHODS;
        if (set.contains(str)) {
            this.method = str;
            return;
        }
        throw new ProtocolException("Expected one of " + set + " but was " + str);
    }

    public void setFixedLengthStreamingMode(int i) {
        setFixedLengthStreamingMode((long) i);
    }

    public void setFixedLengthStreamingMode(long j) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        } else if (this.chunkLength > 0) {
            throw new IllegalStateException("Already in chunked mode");
        } else if (j >= 0) {
            this.fixedContentLength = j;
            this.fixedContentLength = (int) Math.min(j, 2147483647L);
        } else {
            throw new IllegalArgumentException("contentLength < 0");
        }
    }
}
