package com.android.okhttp.internal.http;

import com.android.okhttp.Authenticator;
import com.android.okhttp.Challenge;
import com.android.okhttp.Credentials;
import com.android.okhttp.HttpUrl;
import com.android.okhttp.Request;
import com.android.okhttp.Response;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.p026io.IOException;
import java.util.List;

public final class AuthenticatorAdapter implements Authenticator {
    public static final Authenticator INSTANCE = new AuthenticatorAdapter();

    public Request authenticate(Proxy proxy, Response response) throws IOException {
        List<Challenge> challenges = response.challenges();
        Request request = response.request();
        HttpUrl httpUrl = request.httpUrl();
        int size = challenges.size();
        for (int i = 0; i < size; i++) {
            Challenge challenge = challenges.get(i);
            if (!"Basic".equalsIgnoreCase(challenge.getScheme())) {
                Proxy proxy2 = proxy;
            } else {
                PasswordAuthentication requestPasswordAuthentication = java.net.Authenticator.requestPasswordAuthentication(httpUrl.host(), getConnectToInetAddress(proxy, httpUrl), httpUrl.port(), httpUrl.scheme(), challenge.getRealm(), challenge.getScheme(), httpUrl.url(), Authenticator.RequestorType.SERVER);
                if (requestPasswordAuthentication != null) {
                    return request.newBuilder().header("Authorization", Credentials.basic(requestPasswordAuthentication.getUserName(), new String(requestPasswordAuthentication.getPassword()))).build();
                }
            }
        }
        return null;
    }

    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        List<Challenge> challenges = response.challenges();
        Request request = response.request();
        HttpUrl httpUrl = request.httpUrl();
        int size = challenges.size();
        for (int i = 0; i < size; i++) {
            Challenge challenge = challenges.get(i);
            if ("Basic".equalsIgnoreCase(challenge.getScheme())) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) proxy.address();
                PasswordAuthentication requestPasswordAuthentication = java.net.Authenticator.requestPasswordAuthentication(inetSocketAddress.getHostName(), getConnectToInetAddress(proxy, httpUrl), inetSocketAddress.getPort(), httpUrl.scheme(), challenge.getRealm(), challenge.getScheme(), httpUrl.url(), Authenticator.RequestorType.PROXY);
                if (requestPasswordAuthentication != null) {
                    return request.newBuilder().header("Proxy-Authorization", Credentials.basic(requestPasswordAuthentication.getUserName(), new String(requestPasswordAuthentication.getPassword()))).build();
                }
            }
        }
        return null;
    }

    private InetAddress getConnectToInetAddress(Proxy proxy, HttpUrl httpUrl) throws IOException {
        if (proxy == null || proxy.type() == Proxy.Type.DIRECT) {
            return InetAddress.getByName(httpUrl.host());
        }
        return ((InetSocketAddress) proxy.address()).getAddress();
    }
}