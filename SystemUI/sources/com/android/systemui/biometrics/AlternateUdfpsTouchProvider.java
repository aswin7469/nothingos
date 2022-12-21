package com.android.systemui.biometrics;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH&J\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\r\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000eÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/biometrics/AlternateUdfpsTouchProvider;", "", "onPointerDown", "", "pointerId", "", "x", "", "y", "minor", "", "major", "onPointerUp", "onUiReady", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AlternateUdfpsTouchProvider.kt */
public interface AlternateUdfpsTouchProvider {
    void onPointerDown(long j, int i, int i2, float f, float f2);

    void onPointerUp(long j);

    void onUiReady();
}
