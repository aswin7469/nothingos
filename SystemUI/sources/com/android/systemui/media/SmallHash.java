package com.android.systemui.media;

import java.util.Objects;

public final class SmallHash {
    public static final int MAX_HASH = 8192;

    public static int hash(String str) {
        return hash(Objects.hashCode(str));
    }

    public static int hash(int i) {
        return Math.abs(Math.floorMod(i, 8192));
    }

    private SmallHash() {
    }
}
