package com.android.systemui.util.wrapper;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class BuildInfo_Factory implements Factory<BuildInfo> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public BuildInfo mo1933get() {
        return newInstance();
    }

    public static BuildInfo_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static BuildInfo newInstance() {
        return new BuildInfo();
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final BuildInfo_Factory INSTANCE = new BuildInfo_Factory();
    }
}
