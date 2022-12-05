package com.android.systemui.controls;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class CustomIconCache_Factory implements Factory<CustomIconCache> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public CustomIconCache mo1933get() {
        return newInstance();
    }

    public static CustomIconCache_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static CustomIconCache newInstance() {
        return new CustomIconCache();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final CustomIconCache_Factory INSTANCE = new CustomIconCache_Factory();
    }
}
