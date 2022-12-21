package com.android.icu.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public final class CharsetICU extends Charset {
    protected CharsetICU(String str, String str2, String[] strArr) {
        super((String) null, (String[]) null);
        throw new RuntimeException("Stub!");
    }

    public CharsetDecoder newDecoder() {
        throw new RuntimeException("Stub!");
    }

    public CharsetEncoder newEncoder() {
        throw new RuntimeException("Stub!");
    }

    public boolean contains(Charset charset) {
        throw new RuntimeException("Stub!");
    }
}
