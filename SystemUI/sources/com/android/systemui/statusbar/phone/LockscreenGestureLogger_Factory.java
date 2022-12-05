package com.android.systemui.statusbar.phone;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class LockscreenGestureLogger_Factory implements Factory<LockscreenGestureLogger> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public LockscreenGestureLogger mo1933get() {
        return newInstance();
    }

    public static LockscreenGestureLogger_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static LockscreenGestureLogger newInstance() {
        return new LockscreenGestureLogger();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final LockscreenGestureLogger_Factory INSTANCE = new LockscreenGestureLogger_Factory();
    }
}
