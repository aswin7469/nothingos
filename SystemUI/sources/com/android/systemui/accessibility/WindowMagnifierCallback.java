package com.android.systemui.accessibility;

import android.graphics.Rect;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface WindowMagnifierCallback {
    void onAccessibilityActionPerformed(int i);

    void onPerformScaleAction(int i, float f);

    void onSourceBoundsChanged(int i, Rect rect);

    void onWindowMagnifierBoundsChanged(int i, Rect rect);
}
