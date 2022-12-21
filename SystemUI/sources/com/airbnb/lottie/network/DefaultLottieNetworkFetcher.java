package com.airbnb.lottie.network;

import java.net.HttpURLConnection;
import java.net.URL;
import java.p026io.IOException;

public class DefaultLottieNetworkFetcher implements LottieNetworkFetcher {
    public LottieFetchResult fetchSync(String str) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        return new DefaultLottieFetchResult(httpURLConnection);
    }
}
