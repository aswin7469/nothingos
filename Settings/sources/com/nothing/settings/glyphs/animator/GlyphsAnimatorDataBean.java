package com.nothing.settings.glyphs.animator;

import android.util.Log;

public class GlyphsAnimatorDataBean {
    public int[] data = new int[5];

    public void setLedValue(int i, int i2) {
        int[] iArr = this.data;
        if (i > iArr.length - 1) {
            Log.w("GlyphsAnimatorDataBean", "position can't be bigger than data.length");
        } else {
            iArr[i] = i2;
        }
    }

    public int getValue(int i) {
        return this.data[i];
    }
}
