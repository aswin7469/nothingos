package com.airbnb.lottie.network;

import java.p026io.IOException;

public interface LottieNetworkFetcher {
    LottieFetchResult fetchSync(String str) throws IOException;
}
