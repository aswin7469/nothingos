package com.airbnb.lottie.network;

import java.p026io.Closeable;
import java.p026io.IOException;
import java.p026io.InputStream;

public interface LottieFetchResult extends Closeable {
    InputStream bodyByteStream() throws IOException;

    String contentType();

    String error();

    boolean isSuccessful();
}
