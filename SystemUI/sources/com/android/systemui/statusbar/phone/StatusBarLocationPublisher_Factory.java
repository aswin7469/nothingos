package com.android.systemui.statusbar.phone;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class StatusBarLocationPublisher_Factory implements Factory<StatusBarLocationPublisher> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public StatusBarLocationPublisher mo1933get() {
        return newInstance();
    }

    public static StatusBarLocationPublisher_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static StatusBarLocationPublisher newInstance() {
        return new StatusBarLocationPublisher();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final StatusBarLocationPublisher_Factory INSTANCE = new StatusBarLocationPublisher_Factory();
    }
}
