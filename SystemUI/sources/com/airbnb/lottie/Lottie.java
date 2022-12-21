package com.airbnb.lottie;

public class Lottie {
    private Lottie() {
    }

    public static void initialize(LottieConfig lottieConfig) {
        C1488L.setFetcher(lottieConfig.networkFetcher);
        C1488L.setCacheProvider(lottieConfig.cacheProvider);
        C1488L.setTraceEnabled(lottieConfig.enableSystraceMarkers);
    }
}
