package com.nothing.experience.network;

public interface IHttpRequest {
    void execute();

    void setHttpCallBack(IHttpListener iHttpListener);

    void setRequestData(String str);

    void setRequestData(byte[] bArr);

    void setUrl(String str);
}
