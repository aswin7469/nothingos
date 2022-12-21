package com.android.okhttp.internal;

import java.net.URL;
import java.p026io.IOException;

public interface URLFilter {
    void checkURLPermitted(URL url) throws IOException;
}
