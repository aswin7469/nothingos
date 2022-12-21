package com.nothing.experience.network;

import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.nothing.experience.utils.EncryptUtils;
import com.nothing.experience.utils.SecurityUtils;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPOutputStream;

public class JsonHttpRequest implements IHttpRequest {
    private IHttpListener httpListener;
    private String key;
    private byte[] requestData;
    private String requestDataString;
    private URL targetUrl;
    private String url;
    private HttpURLConnection urlConnection = null;

    public void setUrl(String str) {
        this.url = str;
        if (this.targetUrl == null) {
            try {
                this.targetUrl = new URL(str);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRequestData(byte[] bArr) {
        this.requestData = bArr;
    }

    public void setRequestData(String str) {
        this.requestDataString = str;
    }

    public void execute() {
        httpPost();
    }

    private void codeData() throws Exception {
        if (this.requestDataString != null) {
            String key2 = SecurityUtils.getKey();
            this.key = key2;
            this.requestData = SecurityUtils.encrypt(this.requestDataString, key2);
        }
    }

    public synchronized void httpPost() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            codeData();
            String encryptionByPublicKey = SecurityUtils.encryptionByPublicKey(this.key, SecurityUtils.rsa_public_key);
            HttpURLConnection httpURLConnection = (HttpURLConnection) this.targetUrl.openConnection();
            this.urlConnection = httpURLConnection;
            httpURLConnection.setConnectTimeout(PathInterpolatorCompat.MAX_NUM_POINTS);
            this.urlConnection.setUseCaches(false);
            this.urlConnection.setInstanceFollowRedirects(true);
            this.urlConnection.setReadTimeout(1500);
            this.urlConnection.setDoInput(true);
            this.urlConnection.setDoOutput(true);
            this.urlConnection.setRequestMethod("POST");
            HttpURLConnection httpURLConnection2 = this.urlConnection;
            httpURLConnection2.setRequestProperty("auth", EncryptUtils.getEncryptForString(Http.apiKey + currentTimeMillis).toLowerCase());
            this.urlConnection.setRequestProperty("Content-Encoding", "gzip");
            this.urlConnection.setRequestProperty("timestamp", String.valueOf(currentTimeMillis));
            this.urlConnection.setRequestProperty("Connection", "close");
            this.urlConnection.setRequestProperty("dc_augment", encryptionByPublicKey);
            this.urlConnection.connect();
            OutputStream outputStream = this.urlConnection.getOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(outputStream);
            byte[] bArr = this.requestData;
            if (bArr != null) {
                gZIPOutputStream.write(bArr);
            }
            gZIPOutputStream.flush();
            gZIPOutputStream.close();
            outputStream.close();
            int responseCode = this.urlConnection.getResponseCode();
            String responseMessage = this.urlConnection.getResponseMessage();
            if (this.urlConnection.getResponseCode() == 200) {
                this.httpListener.onSuccess();
            } else {
                this.httpListener.onFailure(responseCode, responseMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.urlConnection.disconnect();
    }

    public void setHttpCallBack(IHttpListener iHttpListener) {
        this.httpListener = iHttpListener;
    }
}
