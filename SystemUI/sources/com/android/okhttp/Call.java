package com.android.okhttp;

import androidx.core.app.NotificationCompat;
import com.android.okhttp.Interceptor;
import com.android.okhttp.Request;
import com.android.okhttp.internal.NamedRunnable;
import com.android.okhttp.internal.http.HttpEngine;
import com.android.okhttp.internal.http.RequestException;
import com.android.okhttp.internal.http.RetryableSink;
import com.android.okhttp.internal.http.RouteException;
import com.android.okhttp.internal.http.StreamAllocation;
import com.android.okhttp.okio.Sink;
import java.net.ProtocolException;
import java.p026io.IOException;

public class Call {
    volatile boolean canceled;
    /* access modifiers changed from: private */
    public final OkHttpClient client;
    HttpEngine engine;
    private boolean executed;
    Request originalRequest;

    protected Call(OkHttpClient okHttpClient, Request request) {
        this.client = okHttpClient.copyWithDefaults();
        this.originalRequest = request;
    }

    public Response execute() throws IOException {
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
            } else {
                throw new IllegalStateException("Already Executed");
            }
        }
        try {
            this.client.getDispatcher().executed(this);
            Response responseWithInterceptorChain = getResponseWithInterceptorChain(false);
            if (responseWithInterceptorChain != null) {
                return responseWithInterceptorChain;
            }
            throw new IOException("Canceled");
        } finally {
            this.client.getDispatcher().finished(this);
        }
    }

    /* access modifiers changed from: package-private */
    public Object tag() {
        return this.originalRequest.tag();
    }

    public void enqueue(Callback callback) {
        enqueue(callback, false);
    }

    /* access modifiers changed from: package-private */
    public void enqueue(Callback callback, boolean z) {
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
            } else {
                throw new IllegalStateException("Already Executed");
            }
        }
        this.client.getDispatcher().enqueue(new AsyncCall(callback, z));
    }

    public void cancel() {
        this.canceled = true;
        HttpEngine httpEngine = this.engine;
        if (httpEngine != null) {
            httpEngine.cancel();
        }
    }

    public synchronized boolean isExecuted() {
        return this.executed;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    final class AsyncCall extends NamedRunnable {
        private final boolean forWebSocket;
        private final Callback responseCallback;

        private AsyncCall(Callback callback, boolean z) {
            super("OkHttp %s", Call.this.originalRequest.urlString());
            this.responseCallback = callback;
            this.forWebSocket = z;
        }

        /* access modifiers changed from: package-private */
        public String host() {
            return Call.this.originalRequest.httpUrl().host();
        }

        /* access modifiers changed from: package-private */
        public Request request() {
            return Call.this.originalRequest;
        }

        /* access modifiers changed from: package-private */
        public Object tag() {
            return Call.this.originalRequest.tag();
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            Call.this.cancel();
        }

        /* access modifiers changed from: package-private */
        public Call get() {
            return Call.this;
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x003f A[SYNTHETIC, Splitter:B:14:0x003f] */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x0059 A[Catch:{ all -> 0x0038 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void execute() {
            /*
                r6 = this;
                java.lang.String r0 = "Callback failure for "
                r1 = 0
                com.android.okhttp.Call r2 = com.android.okhttp.Call.this     // Catch:{ IOException -> 0x003a }
                boolean r3 = r6.forWebSocket     // Catch:{ IOException -> 0x003a }
                com.android.okhttp.Response r2 = r2.getResponseWithInterceptorChain(r3)     // Catch:{ IOException -> 0x003a }
                com.android.okhttp.Call r3 = com.android.okhttp.Call.this     // Catch:{ IOException -> 0x003a }
                boolean r1 = r3.canceled     // Catch:{ IOException -> 0x003a }
                r3 = 1
                if (r1 == 0) goto L_0x0023
                com.android.okhttp.Callback r1 = r6.responseCallback     // Catch:{ IOException -> 0x0036 }
                com.android.okhttp.Call r2 = com.android.okhttp.Call.this     // Catch:{ IOException -> 0x0036 }
                com.android.okhttp.Request r2 = r2.originalRequest     // Catch:{ IOException -> 0x0036 }
                java.io.IOException r4 = new java.io.IOException     // Catch:{ IOException -> 0x0036 }
                java.lang.String r5 = "Canceled"
                r4.<init>((java.lang.String) r5)     // Catch:{ IOException -> 0x0036 }
                r1.onFailure(r2, r4)     // Catch:{ IOException -> 0x0036 }
                goto L_0x0028
            L_0x0023:
                com.android.okhttp.Callback r1 = r6.responseCallback     // Catch:{ IOException -> 0x0036 }
                r1.onResponse(r2)     // Catch:{ IOException -> 0x0036 }
            L_0x0028:
                com.android.okhttp.Call r0 = com.android.okhttp.Call.this
                com.android.okhttp.OkHttpClient r0 = r0.client
                com.android.okhttp.Dispatcher r0 = r0.getDispatcher()
                r0.finished((com.android.okhttp.Call.AsyncCall) r6)
                goto L_0x0072
            L_0x0036:
                r1 = move-exception
                goto L_0x003d
            L_0x0038:
                r0 = move-exception
                goto L_0x0073
            L_0x003a:
                r2 = move-exception
                r3 = r1
                r1 = r2
            L_0x003d:
                if (r3 == 0) goto L_0x0059
                java.util.logging.Logger r2 = com.android.okhttp.internal.Internal.logger     // Catch:{ all -> 0x0038 }
                java.util.logging.Level r3 = java.util.logging.Level.INFO     // Catch:{ all -> 0x0038 }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0038 }
                r4.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0038 }
                com.android.okhttp.Call r0 = com.android.okhttp.Call.this     // Catch:{ all -> 0x0038 }
                java.lang.String r0 = r0.toLoggableString()     // Catch:{ all -> 0x0038 }
                r4.append((java.lang.String) r0)     // Catch:{ all -> 0x0038 }
                java.lang.String r0 = r4.toString()     // Catch:{ all -> 0x0038 }
                r2.log((java.util.logging.Level) r3, (java.lang.String) r0, (java.lang.Throwable) r1)     // Catch:{ all -> 0x0038 }
                goto L_0x0028
            L_0x0059:
                com.android.okhttp.Call r0 = com.android.okhttp.Call.this     // Catch:{ all -> 0x0038 }
                com.android.okhttp.internal.http.HttpEngine r0 = r0.engine     // Catch:{ all -> 0x0038 }
                if (r0 != 0) goto L_0x0064
                com.android.okhttp.Call r0 = com.android.okhttp.Call.this     // Catch:{ all -> 0x0038 }
                com.android.okhttp.Request r0 = r0.originalRequest     // Catch:{ all -> 0x0038 }
                goto L_0x006c
            L_0x0064:
                com.android.okhttp.Call r0 = com.android.okhttp.Call.this     // Catch:{ all -> 0x0038 }
                com.android.okhttp.internal.http.HttpEngine r0 = r0.engine     // Catch:{ all -> 0x0038 }
                com.android.okhttp.Request r0 = r0.getRequest()     // Catch:{ all -> 0x0038 }
            L_0x006c:
                com.android.okhttp.Callback r2 = r6.responseCallback     // Catch:{ all -> 0x0038 }
                r2.onFailure(r0, r1)     // Catch:{ all -> 0x0038 }
                goto L_0x0028
            L_0x0072:
                return
            L_0x0073:
                com.android.okhttp.Call r1 = com.android.okhttp.Call.this
                com.android.okhttp.OkHttpClient r1 = r1.client
                com.android.okhttp.Dispatcher r1 = r1.getDispatcher()
                r1.finished((com.android.okhttp.Call.AsyncCall) r6)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.Call.AsyncCall.execute():void");
        }
    }

    /* access modifiers changed from: private */
    public String toLoggableString() {
        String str = this.canceled ? "canceled call" : NotificationCompat.CATEGORY_CALL;
        HttpUrl resolve = this.originalRequest.httpUrl().resolve("/...");
        return str + " to " + resolve;
    }

    /* access modifiers changed from: private */
    public Response getResponseWithInterceptorChain(boolean z) throws IOException {
        return new ApplicationInterceptorChain(0, this.originalRequest, z).proceed(this.originalRequest);
    }

    class ApplicationInterceptorChain implements Interceptor.Chain {
        private final boolean forWebSocket;
        private final int index;
        private final Request request;

        public Connection connection() {
            return null;
        }

        ApplicationInterceptorChain(int i, Request request2, boolean z) {
            this.index = i;
            this.request = request2;
            this.forWebSocket = z;
        }

        public Request request() {
            return this.request;
        }

        public Response proceed(Request request2) throws IOException {
            if (this.index >= Call.this.client.interceptors().size()) {
                return Call.this.getResponse(request2, this.forWebSocket);
            }
            ApplicationInterceptorChain applicationInterceptorChain = new ApplicationInterceptorChain(this.index + 1, request2, this.forWebSocket);
            Interceptor interceptor = Call.this.client.interceptors().get(this.index);
            Response intercept = interceptor.intercept(applicationInterceptorChain);
            if (intercept != null) {
                return intercept;
            }
            throw new NullPointerException("application interceptor " + interceptor + " returned null");
        }
    }

    /* access modifiers changed from: package-private */
    public Response getResponse(Request request, boolean z) throws IOException {
        boolean z2;
        Throwable th;
        StreamAllocation streamAllocation;
        RequestBody body = request.body();
        if (body != null) {
            Request.Builder newBuilder = request.newBuilder();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                newBuilder.header("Content-Type", contentType.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                newBuilder.header("Content-Length", Long.toString(contentLength));
                newBuilder.removeHeader("Transfer-Encoding");
            } else {
                newBuilder.header("Transfer-Encoding", "chunked");
                newBuilder.removeHeader("Content-Length");
            }
            request = newBuilder.build();
        }
        this.engine = new HttpEngine(this.client, request, false, false, z, (StreamAllocation) null, (RetryableSink) null, (Response) null);
        int i = 0;
        while (!this.canceled) {
            z2 = true;
            try {
                this.engine.sendRequest();
                this.engine.readResponse();
                Response response = this.engine.getResponse();
                Request followUpRequest = this.engine.followUpRequest();
                if (followUpRequest == null) {
                    if (!z) {
                        this.engine.releaseStreamAllocation();
                    }
                    return response;
                }
                StreamAllocation close = this.engine.close();
                i++;
                if (i <= 20) {
                    if (!this.engine.sameConnection(followUpRequest.httpUrl())) {
                        close.release();
                        streamAllocation = null;
                    } else {
                        streamAllocation = close;
                    }
                    this.engine = new HttpEngine(this.client, followUpRequest, false, false, z, streamAllocation, (RetryableSink) null, response);
                } else {
                    close.release();
                    throw new ProtocolException("Too many follow-up requests: " + i);
                }
            } catch (RequestException e) {
                throw e.getCause();
            } catch (RouteException e2) {
                HttpEngine recover = this.engine.recover(e2);
                if (recover != null) {
                    this.engine = recover;
                } else {
                    throw e2.getLastConnectException();
                }
            } catch (IOException e3) {
                HttpEngine recover2 = this.engine.recover(e3, (Sink) null);
                if (recover2 != null) {
                    this.engine = recover2;
                } else {
                    throw e3;
                }
            } catch (Throwable th2) {
                z2 = false;
                th = th2;
            }
        }
        this.engine.releaseStreamAllocation();
        throw new IOException("Canceled");
        if (z2) {
            this.engine.close().release();
        }
        throw th;
    }
}
