package com.android.okhttp.internal.huc;

import com.android.okhttp.Handshake;
import com.android.okhttp.Headers;
import com.android.okhttp.MediaType;
import com.android.okhttp.Request;
import com.android.okhttp.RequestBody;
import com.android.okhttp.Response;
import com.android.okhttp.ResponseBody;
import com.android.okhttp.internal.Internal;
import com.android.okhttp.internal.Util;
import com.android.okhttp.internal.http.HttpMethod;
import com.android.okhttp.internal.http.OkHeaders;
import com.android.okhttp.internal.http.StatusLine;
import com.android.okhttp.okio.BufferedSource;
import com.android.okhttp.okio.Okio;
import com.android.okhttp.okio.Sink;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SecureCacheResponse;
import java.net.URI;
import java.net.URLConnection;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;

public final class JavaApiConverter {
    private static final RequestBody EMPTY_REQUEST_BODY = RequestBody.create((MediaType) null, new byte[0]);

    private JavaApiConverter() {
    }

    public static Response createOkResponseForCachePut(URI uri, URLConnection uRLConnection) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) uRLConnection;
        Response.Builder builder = new Response.Builder();
        Headers varyHeaders = varyHeaders(uRLConnection, createHeaders(uRLConnection.getHeaderFields()));
        Certificate[] certificateArr = null;
        if (varyHeaders == null) {
            return null;
        }
        String requestMethod = httpURLConnection.getRequestMethod();
        builder.request(new Request.Builder().url(uri.toString()).method(requestMethod, HttpMethod.requiresRequestBody(requestMethod) ? EMPTY_REQUEST_BODY : null).headers(varyHeaders).build());
        StatusLine parse = StatusLine.parse(extractStatusLine(httpURLConnection));
        builder.protocol(parse.protocol);
        builder.code(parse.code);
        builder.message(parse.message);
        builder.networkResponse(builder.build());
        builder.headers(extractOkResponseHeaders(httpURLConnection));
        builder.body(createOkBody(uRLConnection));
        if (httpURLConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            try {
                certificateArr = httpsURLConnection.getServerCertificates();
            } catch (SSLPeerUnverifiedException unused) {
            }
            builder.handshake(Handshake.get(httpsURLConnection.getCipherSuite(), nullSafeImmutableList(certificateArr), nullSafeImmutableList(httpsURLConnection.getLocalCertificates())));
        }
        return builder.build();
    }

    private static Headers createHeaders(Map<String, List<String>> map) {
        Headers.Builder builder = new Headers.Builder();
        for (Map.Entry next : map.entrySet()) {
            if (!(next.getKey() == null || next.getValue() == null)) {
                String trim = ((String) next.getKey()).trim();
                for (String trim2 : (List) next.getValue()) {
                    Internal.instance.addLenient(builder, trim, trim2.trim());
                }
            }
        }
        return builder.build();
    }

    private static Headers varyHeaders(URLConnection uRLConnection, Headers headers) {
        if (OkHeaders.hasVaryAll(headers)) {
            return null;
        }
        Set<String> varyFields = OkHeaders.varyFields(headers);
        if (varyFields.isEmpty()) {
            return new Headers.Builder().build();
        }
        if (!(uRLConnection instanceof CacheHttpURLConnection) && !(uRLConnection instanceof CacheHttpsURLConnection)) {
            return null;
        }
        Map<String, List<String>> requestProperties = uRLConnection.getRequestProperties();
        Headers.Builder builder = new Headers.Builder();
        for (String next : varyFields) {
            List<String> list = requestProperties.get(next);
            if (list != null) {
                for (String addLenient : list) {
                    Internal.instance.addLenient(builder, next, addLenient);
                }
            } else if (next.equals("Accept-Encoding")) {
                builder.add("Accept-Encoding", "gzip");
            }
        }
        return builder.build();
    }

    static Response createOkResponseForCacheGet(Request request, CacheResponse cacheResponse) throws IOException {
        Headers headers;
        List<Certificate> list;
        Headers createHeaders = createHeaders(cacheResponse.getHeaders());
        if (OkHeaders.hasVaryAll(createHeaders)) {
            headers = new Headers.Builder().build();
        } else {
            headers = OkHeaders.varyHeaders(request.headers(), createHeaders);
        }
        Request build = new Request.Builder().url(request.httpUrl()).method(request.method(), (RequestBody) null).headers(headers).build();
        Response.Builder builder = new Response.Builder();
        builder.request(build);
        StatusLine parse = StatusLine.parse(extractStatusLine(cacheResponse));
        builder.protocol(parse.protocol);
        builder.code(parse.code);
        builder.message(parse.message);
        Headers extractOkHeaders = extractOkHeaders(cacheResponse);
        builder.headers(extractOkHeaders);
        builder.body(createOkBody(extractOkHeaders, cacheResponse));
        if (cacheResponse instanceof SecureCacheResponse) {
            SecureCacheResponse secureCacheResponse = (SecureCacheResponse) cacheResponse;
            try {
                list = secureCacheResponse.getServerCertificateChain();
            } catch (SSLPeerUnverifiedException unused) {
                list = Collections.emptyList();
            }
            List<Certificate> localCertificateChain = secureCacheResponse.getLocalCertificateChain();
            if (localCertificateChain == null) {
                localCertificateChain = Collections.emptyList();
            }
            builder.handshake(Handshake.get(secureCacheResponse.getCipherSuite(), list, localCertificateChain));
        }
        return builder.build();
    }

    public static Request createOkRequest(URI uri, String str, Map<String, List<String>> map) {
        Request.Builder method = new Request.Builder().url(uri.toString()).method(str, HttpMethod.requiresRequestBody(str) ? EMPTY_REQUEST_BODY : null);
        if (map != null) {
            method.headers(extractOkHeaders(map));
        }
        return method.build();
    }

    public static CacheResponse createJavaCacheResponse(final Response response) {
        final Headers headers = response.headers();
        final ResponseBody body = response.body();
        if (!response.request().isHttps()) {
            return new CacheResponse() {
                public Map<String, List<String>> getHeaders() throws IOException {
                    return OkHeaders.toMultimap(Headers.this, StatusLine.get(response).toString());
                }

                public InputStream getBody() throws IOException {
                    ResponseBody responseBody = body;
                    if (responseBody == null) {
                        return null;
                    }
                    return responseBody.byteStream();
                }
            };
        }
        final Handshake handshake = response.handshake();
        return new SecureCacheResponse() {
            public String getCipherSuite() {
                Handshake handshake = Handshake.this;
                if (handshake != null) {
                    return handshake.cipherSuite();
                }
                return null;
            }

            public List<Certificate> getLocalCertificateChain() {
                Handshake handshake = Handshake.this;
                if (handshake == null) {
                    return null;
                }
                List<Certificate> localCertificates = handshake.localCertificates();
                if (localCertificates.size() > 0) {
                    return localCertificates;
                }
                return null;
            }

            public List<Certificate> getServerCertificateChain() throws SSLPeerUnverifiedException {
                Handshake handshake = Handshake.this;
                if (handshake == null) {
                    return null;
                }
                List<Certificate> peerCertificates = handshake.peerCertificates();
                if (peerCertificates.size() > 0) {
                    return peerCertificates;
                }
                return null;
            }

            public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
                Handshake handshake = Handshake.this;
                if (handshake == null) {
                    return null;
                }
                return handshake.peerPrincipal();
            }

            public Principal getLocalPrincipal() {
                Handshake handshake = Handshake.this;
                if (handshake == null) {
                    return null;
                }
                return handshake.localPrincipal();
            }

            public Map<String, List<String>> getHeaders() throws IOException {
                return OkHeaders.toMultimap(headers, StatusLine.get(response).toString());
            }

            public InputStream getBody() throws IOException {
                ResponseBody responseBody = body;
                if (responseBody == null) {
                    return null;
                }
                return responseBody.byteStream();
            }
        };
    }

    public static CacheRequest createJavaCacheRequest(final com.android.okhttp.internal.http.CacheRequest cacheRequest) {
        return new CacheRequest() {
            public void abort() {
                com.android.okhttp.internal.http.CacheRequest.this.abort();
            }

            public OutputStream getBody() throws IOException {
                Sink body = com.android.okhttp.internal.http.CacheRequest.this.body();
                if (body == null) {
                    return null;
                }
                return Okio.buffer(body).outputStream();
            }
        };
    }

    static HttpURLConnection createJavaUrlConnectionForCachePut(Response response) {
        if (response.request().isHttps()) {
            return new CacheHttpsURLConnection(new CacheHttpURLConnection(response));
        }
        return new CacheHttpURLConnection(response);
    }

    static Map<String, List<String>> extractJavaHeaders(Request request) {
        return OkHeaders.toMultimap(request.headers(), (String) null);
    }

    private static Headers extractOkHeaders(CacheResponse cacheResponse) throws IOException {
        return extractOkHeaders(cacheResponse.getHeaders());
    }

    private static Headers extractOkResponseHeaders(HttpURLConnection httpURLConnection) {
        return extractOkHeaders(httpURLConnection.getHeaderFields());
    }

    static Headers extractOkHeaders(Map<String, List<String>> map) {
        Headers.Builder builder = new Headers.Builder();
        for (Map.Entry next : map.entrySet()) {
            String str = (String) next.getKey();
            if (str != null) {
                for (String addLenient : (List) next.getValue()) {
                    Internal.instance.addLenient(builder, str, addLenient);
                }
            }
        }
        return builder.build();
    }

    private static String extractStatusLine(HttpURLConnection httpURLConnection) {
        return httpURLConnection.getHeaderField((String) null);
    }

    private static String extractStatusLine(CacheResponse cacheResponse) throws IOException {
        return extractStatusLine(cacheResponse.getHeaders());
    }

    static String extractStatusLine(Map<String, List<String>> map) throws ProtocolException {
        List list = map.get((Object) null);
        if (list != null && list.size() != 0) {
            return (String) list.get(0);
        }
        throw new ProtocolException("CacheResponse is missing a 'null' header containing the status line. Headers=" + map);
    }

    private static ResponseBody createOkBody(final Headers headers, final CacheResponse cacheResponse) {
        return new ResponseBody() {
            private BufferedSource body;

            public MediaType contentType() {
                String str = Headers.this.get("Content-Type");
                if (str == null) {
                    return null;
                }
                return MediaType.parse(str);
            }

            public long contentLength() {
                return OkHeaders.contentLength(Headers.this);
            }

            public BufferedSource source() throws IOException {
                if (this.body == null) {
                    this.body = Okio.buffer(Okio.source(cacheResponse.getBody()));
                }
                return this.body;
            }
        };
    }

    private static ResponseBody createOkBody(final URLConnection uRLConnection) {
        if (!uRLConnection.getDoInput()) {
            return null;
        }
        return new ResponseBody() {
            private BufferedSource body;

            public MediaType contentType() {
                String contentType = URLConnection.this.getContentType();
                if (contentType == null) {
                    return null;
                }
                return MediaType.parse(contentType);
            }

            public long contentLength() {
                return JavaApiConverter.stringToLong(URLConnection.this.getHeaderField("Content-Length"));
            }

            public BufferedSource source() throws IOException {
                if (this.body == null) {
                    this.body = Okio.buffer(Okio.source(URLConnection.this.getInputStream()));
                }
                return this.body;
            }
        };
    }

    private static final class CacheHttpURLConnection extends HttpURLConnection {
        private final Request request;
        /* access modifiers changed from: private */
        public final Response response;

        public boolean getAllowUserInteraction() {
            return false;
        }

        public int getConnectTimeout() {
            return 0;
        }

        public InputStream getErrorStream() {
            return null;
        }

        public int getReadTimeout() {
            return 0;
        }

        public boolean usingProxy() {
            return false;
        }

        public CacheHttpURLConnection(Response response2) {
            super(response2.request().url());
            Request request2 = response2.request();
            this.request = request2;
            this.response = response2;
            this.connected = true;
            this.doOutput = request2.body() != null;
            this.doInput = true;
            this.useCaches = true;
            this.method = request2.method();
        }

        public void connect() throws IOException {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public void disconnect() {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public void setRequestProperty(String str, String str2) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public void addRequestProperty(String str, String str2) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public String getRequestProperty(String str) {
            return this.request.header(str);
        }

        public Map<String, List<String>> getRequestProperties() {
            return OkHeaders.toMultimap(this.request.headers(), (String) null);
        }

        public void setFixedLengthStreamingMode(int i) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public void setFixedLengthStreamingMode(long j) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public void setChunkedStreamingMode(int i) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public void setInstanceFollowRedirects(boolean z) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public boolean getInstanceFollowRedirects() {
            return super.getInstanceFollowRedirects();
        }

        public void setRequestMethod(String str) throws ProtocolException {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public String getRequestMethod() {
            return this.request.method();
        }

        public String getHeaderFieldKey(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("Invalid header index: " + i);
            } else if (i == 0) {
                return null;
            } else {
                return this.response.headers().name(i - 1);
            }
        }

        public String getHeaderField(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("Invalid header index: " + i);
            } else if (i == 0) {
                return StatusLine.get(this.response).toString();
            } else {
                return this.response.headers().value(i - 1);
            }
        }

        public String getHeaderField(String str) {
            if (str == null) {
                return StatusLine.get(this.response).toString();
            }
            return this.response.headers().get(str);
        }

        public Map<String, List<String>> getHeaderFields() {
            return OkHeaders.toMultimap(this.response.headers(), StatusLine.get(this.response).toString());
        }

        public int getResponseCode() throws IOException {
            return this.response.code();
        }

        public String getResponseMessage() throws IOException {
            return this.response.message();
        }

        public void setConnectTimeout(int i) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public void setReadTimeout(int i) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public Object getContent() throws IOException {
            throw JavaApiConverter.throwResponseBodyAccessException();
        }

        public Object getContent(Class[] clsArr) throws IOException {
            throw JavaApiConverter.throwResponseBodyAccessException();
        }

        public InputStream getInputStream() throws IOException {
            throw JavaApiConverter.throwResponseBodyAccessException();
        }

        public OutputStream getOutputStream() throws IOException {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public void setDoInput(boolean z) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public boolean getDoInput() {
            return this.doInput;
        }

        public void setDoOutput(boolean z) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public boolean getDoOutput() {
            return this.doOutput;
        }

        public void setAllowUserInteraction(boolean z) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public void setUseCaches(boolean z) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public boolean getUseCaches() {
            return super.getUseCaches();
        }

        public void setIfModifiedSince(long j) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public long getIfModifiedSince() {
            return JavaApiConverter.stringToLong(this.request.headers().get("If-Modified-Since"));
        }

        public boolean getDefaultUseCaches() {
            return super.getDefaultUseCaches();
        }

        public void setDefaultUseCaches(boolean z) {
            super.setDefaultUseCaches(z);
        }
    }

    private static final class CacheHttpsURLConnection extends DelegatingHttpsURLConnection {
        private final CacheHttpURLConnection delegate;

        public CacheHttpsURLConnection(CacheHttpURLConnection cacheHttpURLConnection) {
            super(cacheHttpURLConnection);
            this.delegate = cacheHttpURLConnection;
        }

        /* access modifiers changed from: protected */
        public Handshake handshake() {
            return this.delegate.response.handshake();
        }

        public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public HostnameVerifier getHostnameVerifier() {
            throw JavaApiConverter.throwRequestSslAccessException();
        }

        public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
            throw JavaApiConverter.throwRequestModificationException();
        }

        public SSLSocketFactory getSSLSocketFactory() {
            throw JavaApiConverter.throwRequestSslAccessException();
        }

        public long getContentLengthLong() {
            return this.delegate.getContentLengthLong();
        }

        public void setFixedLengthStreamingMode(long j) {
            this.delegate.setFixedLengthStreamingMode(j);
        }

        public long getHeaderFieldLong(String str, long j) {
            return this.delegate.getHeaderFieldLong(str, j);
        }
    }

    /* access modifiers changed from: private */
    public static RuntimeException throwRequestModificationException() {
        throw new UnsupportedOperationException("ResponseCache cannot modify the request.");
    }

    private static RuntimeException throwRequestHeaderAccessException() {
        throw new UnsupportedOperationException("ResponseCache cannot access request headers");
    }

    /* access modifiers changed from: private */
    public static RuntimeException throwRequestSslAccessException() {
        throw new UnsupportedOperationException("ResponseCache cannot access SSL internals");
    }

    /* access modifiers changed from: private */
    public static RuntimeException throwResponseBodyAccessException() {
        throw new UnsupportedOperationException("ResponseCache cannot access the response body.");
    }

    private static <T> List<T> nullSafeImmutableList(T[] tArr) {
        return tArr == null ? Collections.emptyList() : Util.immutableList(tArr);
    }

    /* access modifiers changed from: private */
    public static long stringToLong(String str) {
        if (str == null) {
            return -1;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException unused) {
            return -1;
        }
    }
}
