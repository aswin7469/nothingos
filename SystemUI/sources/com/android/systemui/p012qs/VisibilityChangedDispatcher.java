package com.android.systemui.p012qs;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0006J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0007À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/qs/VisibilityChangedDispatcher;", "", "setOnVisibilityChangedListener", "", "onVisibilityChangedListener", "Lcom/android/systemui/qs/VisibilityChangedDispatcher$OnVisibilityChangedListener;", "OnVisibilityChangedListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.VisibilityChangedDispatcher */
/* compiled from: VisibilityChangedDispatcher.kt */
public interface VisibilityChangedDispatcher {

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bæ\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/qs/VisibilityChangedDispatcher$OnVisibilityChangedListener;", "", "onVisibilityChanged", "", "visibility", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.VisibilityChangedDispatcher$OnVisibilityChangedListener */
    /* compiled from: VisibilityChangedDispatcher.kt */
    public interface OnVisibilityChangedListener {
        void onVisibilityChanged(int i);
    }

    void setOnVisibilityChangedListener(OnVisibilityChangedListener onVisibilityChangedListener);
}
