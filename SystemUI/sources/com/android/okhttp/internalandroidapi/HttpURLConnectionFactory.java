package com.android.okhttp.internalandroidapi;

import com.android.okhttp.ConnectionPool;
import com.android.okhttp.Dns;
import com.android.okhttp.HttpHandler;
import com.android.okhttp.HttpsHandler;
import com.android.okhttp.OkHttpClient;
import com.android.okhttp.OkUrlFactories;
import com.android.okhttp.OkUrlFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.p026io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;

public final class HttpURLConnectionFactory {
    private ConnectionPool connectionPool;
    private Dns dns;

    public void setNewConnectionPool(int i, long j, TimeUnit timeUnit) {
        this.connectionPool = new ConnectionPool(i, j, timeUnit);
    }

    public void setDns(Dns dns2) {
        Objects.requireNonNull(dns2);
        this.dns = new DnsAdapter(dns2);
    }

    public URLConnection openConnection(URL url) throws IOException {
        return internalOpenConnection(url, (SocketFactory) null, (Proxy) null);
    }

    public URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        Objects.requireNonNull(proxy);
        return internalOpenConnection(url, (SocketFactory) null, proxy);
    }

    public URLConnection openConnection(URL url, SocketFactory socketFactory) throws IOException {
        Objects.requireNonNull(socketFactory);
        return internalOpenConnection(url, socketFactory, (Proxy) null);
    }

    public URLConnection openConnection(URL url, SocketFactory socketFactory, Proxy proxy) throws IOException {
        Objects.requireNonNull(socketFactory);
        Objects.requireNonNull(proxy);
        return internalOpenConnection(url, socketFactory, proxy);
    }

    private URLConnection internalOpenConnection(URL url, SocketFactory socketFactory, Proxy proxy) throws IOException {
        OkUrlFactory okUrlFactory;
        String protocol = url.getProtocol();
        if (protocol.equals("http")) {
            okUrlFactory = HttpHandler.createHttpOkUrlFactory(proxy);
        } else if (protocol.equals("https")) {
            okUrlFactory = HttpsHandler.createHttpsOkUrlFactory(proxy);
        } else {
            throw new MalformedURLException("Invalid URL or unrecognized protocol " + protocol);
        }
        OkHttpClient client = okUrlFactory.client();
        ConnectionPool connectionPool2 = this.connectionPool;
        if (connectionPool2 != null) {
            client.setConnectionPool(connectionPool2);
        }
        Dns dns2 = this.dns;
        if (dns2 != null) {
            client.setDns(dns2);
        }
        if (socketFactory != null) {
            client.setSocketFactory(socketFactory);
        }
        if (proxy == null) {
            return okUrlFactory.open(url);
        }
        return OkUrlFactories.open(okUrlFactory, url, proxy);
    }

    static final class DnsAdapter implements Dns {
        private final Dns adaptee;

        DnsAdapter(Dns dns) {
            this.adaptee = (Dns) Objects.requireNonNull(dns);
        }

        public List<InetAddress> lookup(String str) throws UnknownHostException {
            return this.adaptee.lookup(str);
        }

        public int hashCode() {
            return (DnsAdapter.class.hashCode() * 31) + this.adaptee.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof DnsAdapter)) {
                return false;
            }
            return this.adaptee.equals(((DnsAdapter) obj).adaptee);
        }

        public String toString() {
            return this.adaptee.toString();
        }
    }
}
