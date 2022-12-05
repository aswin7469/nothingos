package com.android.systemui.screenshot;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ScreenshotSmartActions_Factory implements Factory<ScreenshotSmartActions> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public ScreenshotSmartActions mo1933get() {
        return newInstance();
    }

    public static ScreenshotSmartActions_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ScreenshotSmartActions newInstance() {
        return new ScreenshotSmartActions();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final ScreenshotSmartActions_Factory INSTANCE = new ScreenshotSmartActions_Factory();
    }
}
