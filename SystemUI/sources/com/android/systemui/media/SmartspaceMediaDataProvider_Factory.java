package com.android.systemui.media;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SmartspaceMediaDataProvider_Factory implements Factory<SmartspaceMediaDataProvider> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public SmartspaceMediaDataProvider mo1933get() {
        return newInstance();
    }

    public static SmartspaceMediaDataProvider_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SmartspaceMediaDataProvider newInstance() {
        return new SmartspaceMediaDataProvider();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final SmartspaceMediaDataProvider_Factory INSTANCE = new SmartspaceMediaDataProvider_Factory();
    }
}
