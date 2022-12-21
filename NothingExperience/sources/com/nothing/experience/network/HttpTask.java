package com.nothing.experience.network;

import com.google.gson.Gson;

public class HttpTask<T> implements Runnable {
    private IHttpListener httpListener;
    private IHttpRequest httpRequest;

    public <T> HttpTask(T t, String str, IHttpRequest iHttpRequest, IHttpListener iHttpListener) {
        this.httpRequest = iHttpRequest;
        this.httpListener = iHttpListener;
        iHttpRequest.setUrl(str);
        this.httpRequest.setHttpCallBack(iHttpListener);
        if (t != null) {
            this.httpRequest.setRequestData(new Gson().toJson((Object) t));
        }
    }

    public void run() {
        this.httpRequest.execute();
    }
}
