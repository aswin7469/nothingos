package com.android.systemui;

import org.jetbrains.annotations.NotNull;
/* compiled from: BootCompleteCache.kt */
/* loaded from: classes.dex */
public interface BootCompleteCache {

    /* compiled from: BootCompleteCache.kt */
    /* loaded from: classes.dex */
    public interface BootCompleteListener {
        void onBootComplete();
    }

    boolean addListener(@NotNull BootCompleteListener bootCompleteListener);

    boolean isBootComplete();
}
