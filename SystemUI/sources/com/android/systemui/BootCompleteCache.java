package com.android.systemui;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\tJ\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0003H&J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/BootCompleteCache;", "", "addListener", "", "listener", "Lcom/android/systemui/BootCompleteCache$BootCompleteListener;", "isBootComplete", "removeListener", "", "BootCompleteListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: BootCompleteCache.kt */
public interface BootCompleteCache {

    @Metadata(mo65042d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/BootCompleteCache$BootCompleteListener;", "", "onBootComplete", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: BootCompleteCache.kt */
    public interface BootCompleteListener {
        void onBootComplete();
    }

    boolean addListener(BootCompleteListener bootCompleteListener);

    boolean isBootComplete();

    void removeListener(BootCompleteListener bootCompleteListener);
}
