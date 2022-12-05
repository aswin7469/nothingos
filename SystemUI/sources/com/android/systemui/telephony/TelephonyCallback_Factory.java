package com.android.systemui.telephony;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class TelephonyCallback_Factory implements Factory<TelephonyCallback> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public TelephonyCallback mo1933get() {
        return newInstance();
    }

    public static TelephonyCallback_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static TelephonyCallback newInstance() {
        return new TelephonyCallback();
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final TelephonyCallback_Factory INSTANCE = new TelephonyCallback_Factory();
    }
}
