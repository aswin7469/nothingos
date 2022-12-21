package com.android.systemui.media.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class MediaOutputSeekbar extends SeekBar {
    private static final int SCALE_SIZE = 1000;

    static int scaleVolumeToProgress(int i) {
        return i * 1000;
    }

    public MediaOutputSeekbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setMin(0);
    }

    static int scaleProgressToVolume(int i) {
        return i / 1000;
    }

    /* access modifiers changed from: package-private */
    public int getVolume() {
        return getProgress() / 1000;
    }

    /* access modifiers changed from: package-private */
    public void setVolume(int i) {
        setProgress(i * 1000, true);
    }

    /* access modifiers changed from: package-private */
    public void setMaxVolume(int i) {
        setMax(i * 1000);
    }

    /* access modifiers changed from: package-private */
    public void resetVolume() {
        setProgress(getMin());
    }
}
