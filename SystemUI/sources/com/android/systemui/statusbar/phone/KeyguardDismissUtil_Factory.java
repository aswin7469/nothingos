package com.android.systemui.statusbar.phone;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class KeyguardDismissUtil_Factory implements Factory<KeyguardDismissUtil> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public KeyguardDismissUtil mo1933get() {
        return newInstance();
    }

    public static KeyguardDismissUtil_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static KeyguardDismissUtil newInstance() {
        return new KeyguardDismissUtil();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final KeyguardDismissUtil_Factory INSTANCE = new KeyguardDismissUtil_Factory();
    }
}
