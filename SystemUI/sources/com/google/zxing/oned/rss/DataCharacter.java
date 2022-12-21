package com.google.zxing.oned.rss;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public class DataCharacter {
    private final int checksumPortion;
    private final int value;

    public DataCharacter(int i, int i2) {
        this.value = i;
        this.checksumPortion = i2;
    }

    public final int getValue() {
        return this.value;
    }

    public final int getChecksumPortion() {
        return this.checksumPortion;
    }

    public final String toString() {
        return this.value + NavigationBarInflaterView.KEY_CODE_START + this.checksumPortion + ')';
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof DataCharacter)) {
            return false;
        }
        DataCharacter dataCharacter = (DataCharacter) obj;
        if (this.value == dataCharacter.value && this.checksumPortion == dataCharacter.checksumPortion) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return this.checksumPortion ^ this.value;
    }
}