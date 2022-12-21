package com.android.okhttp;

import java.net.Proxy;
import java.p026io.IOException;

public interface Authenticator {
    Request authenticate(Proxy proxy, Response response) throws IOException;

    Request authenticateProxy(Proxy proxy, Response response) throws IOException;
}
