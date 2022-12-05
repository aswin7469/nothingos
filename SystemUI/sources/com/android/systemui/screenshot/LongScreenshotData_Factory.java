package com.android.systemui.screenshot;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class LongScreenshotData_Factory implements Factory<LongScreenshotData> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public LongScreenshotData mo1933get() {
        return newInstance();
    }

    public static LongScreenshotData_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static LongScreenshotData newInstance() {
        return new LongScreenshotData();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final LongScreenshotData_Factory INSTANCE = new LongScreenshotData_Factory();
    }
}
