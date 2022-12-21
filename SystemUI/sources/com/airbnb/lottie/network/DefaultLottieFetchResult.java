package com.airbnb.lottie.network;

import com.airbnb.lottie.utils.Logger;
import java.net.HttpURLConnection;
import java.p026io.BufferedReader;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.InputStreamReader;

public class DefaultLottieFetchResult implements LottieFetchResult {
    private final HttpURLConnection connection;

    public DefaultLottieFetchResult(HttpURLConnection httpURLConnection) {
        this.connection = httpURLConnection;
    }

    public boolean isSuccessful() {
        try {
            return this.connection.getResponseCode() / 100 == 2;
        } catch (IOException unused) {
            return false;
        }
    }

    public InputStream bodyByteStream() throws IOException {
        return this.connection.getInputStream();
    }

    public String contentType() {
        return this.connection.getContentType();
    }

    public String error() {
        try {
            if (isSuccessful()) {
                return null;
            }
            return "Unable to fetch " + this.connection.getURL() + ". Failed with " + this.connection.getResponseCode() + "\n" + getErrorFromConnection(this.connection);
        } catch (IOException e) {
            Logger.warning("get error failed ", e);
            return e.getMessage();
        }
    }

    public void close() {
        this.connection.disconnect();
    }

    private String getErrorFromConnection(HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine).append(10);
                } else {
                    try {
                        break;
                    } catch (Exception unused) {
                    }
                }
            } catch (Exception e) {
                throw e;
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (Exception unused2) {
                }
                throw th;
            }
        }
        bufferedReader.close();
        return sb.toString();
    }
}
