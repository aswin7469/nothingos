package com.nothing.experience.network;

import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonHttpListener<M> implements IHttpListener {
    private IDataListener<M> dataListener;
    Handler handler = new Handler(Looper.getMainLooper());
    Class<M> responseClass;

    public JsonHttpListener(Class<M> cls, IDataListener<M> iDataListener) {
        this.responseClass = cls;
        this.dataListener = iDataListener;
    }

    public synchronized void onSuccess(InputStream inputStream) {
        this.handler.post(new JsonHttpListener$$ExternalSyntheticLambda2(this, new Gson().fromJson(getContent(inputStream), this.responseClass)));
    }

    /* renamed from: lambda$onSuccess$0$com-nothing-experience-network-JsonHttpListener */
    public /* synthetic */ void mo13928x20b373a9(Object obj) {
        IDataListener<M> iDataListener = this.dataListener;
        if (iDataListener != null) {
            iDataListener.onSuccess(obj);
        }
    }

    public synchronized void onSuccess() {
        this.handler.post(new JsonHttpListener$$ExternalSyntheticLambda0(this));
    }

    /* renamed from: lambda$onSuccess$1$com-nothing-experience-network-JsonHttpListener */
    public /* synthetic */ void mo13929x203d0daa() {
        IDataListener<M> iDataListener = this.dataListener;
        if (iDataListener != null) {
            iDataListener.onSuccess();
        }
    }

    public synchronized void onFailure(int i, String str) {
        this.handler.post(new JsonHttpListener$$ExternalSyntheticLambda1(this, i, str));
    }

    /* renamed from: lambda$onFailure$2$com-nothing-experience-network-JsonHttpListener */
    public /* synthetic */ void mo13927xf62c0972(int i, String str) {
        IDataListener<M> iDataListener = this.dataListener;
        if (iDataListener != null) {
            iDataListener.onFailure(i, str);
        }
    }

    private String getContent(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine + "\n");
                } else {
                    try {
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                inputStream.close();
                bufferedReader.close();
            } catch (Throwable th) {
                try {
                    inputStream.close();
                    bufferedReader.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                throw th;
            }
        }
        inputStream.close();
        bufferedReader.close();
        return sb.toString();
    }
}
