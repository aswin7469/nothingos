package com.nothing.utils;

public class NTSystemUIUtils {
    private static NTSystemUIUtils sInstance;
    private static final Object sLocker = new Object();
    private boolean mSplitShadeEnabled = false;

    public static NTSystemUIUtils getInstance() {
        if (sInstance == null) {
            synchronized (sLocker) {
                if (sInstance == null) {
                    sInstance = new NTSystemUIUtils();
                }
            }
        }
        return sInstance;
    }

    private NTSystemUIUtils() {
    }

    public void setSplitShadeEnabled(boolean z) {
        this.mSplitShadeEnabled = z;
    }

    public boolean isSplitShadeEnabled() {
        return this.mSplitShadeEnabled;
    }
}
