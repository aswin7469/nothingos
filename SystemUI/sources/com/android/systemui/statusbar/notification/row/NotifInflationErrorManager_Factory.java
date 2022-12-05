package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class NotifInflationErrorManager_Factory implements Factory<NotifInflationErrorManager> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public NotifInflationErrorManager mo1933get() {
        return newInstance();
    }

    public static NotifInflationErrorManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotifInflationErrorManager newInstance() {
        return new NotifInflationErrorManager();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final NotifInflationErrorManager_Factory INSTANCE = new NotifInflationErrorManager_Factory();
    }
}
