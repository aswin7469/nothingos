package com.android.okhttp;

import java.p026io.IOException;

public interface Interceptor {

    public interface Chain {
        Connection connection();

        Response proceed(Request request) throws IOException;

        Request request();
    }

    Response intercept(Chain chain) throws IOException;
}
