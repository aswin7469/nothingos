package com.nothing.experience.network;

import java.net.HttpURLConnection;
import java.util.HashMap;

public class ConnectionPool {
    private static HashMap<String, HttpURLConnection> sPool = new HashMap<>();

    public static void set(HttpURLConnection httpURLConnection) {
        if (httpURLConnection != null || httpURLConnection.getURL() != null) {
            httpURLConnection.disconnect();
            String url = httpURLConnection.getURL().toString();
            if (!sPool.containsKey(url)) {
                sPool.put(url, httpURLConnection);
            }
        }
    }

    public static HttpURLConnection get(String str) {
        if (sPool.containsKey(str)) {
            return sPool.get(str);
        }
        return null;
    }
}
