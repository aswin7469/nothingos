package com.nothing.experience.network;

import java.io.InputStream;

public interface IHttpListener {
    void onFailure(int i, String str);

    void onSuccess();

    void onSuccess(InputStream inputStream);
}
