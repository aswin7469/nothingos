package com.android.icu.util;

public class Icu4cMetadata {
    public static native String getCldrVersion();

    public static native String getIcuVersion();

    public static native String getTzdbVersion();

    public static native String getUnicodeVersion();

    public Icu4cMetadata() {
        throw new RuntimeException("Stub!");
    }
}
