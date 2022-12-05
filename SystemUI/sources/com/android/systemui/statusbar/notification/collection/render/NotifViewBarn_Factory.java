package com.android.systemui.statusbar.notification.collection.render;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class NotifViewBarn_Factory implements Factory<NotifViewBarn> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public NotifViewBarn mo1933get() {
        return newInstance();
    }

    public static NotifViewBarn_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotifViewBarn newInstance() {
        return new NotifViewBarn();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final NotifViewBarn_Factory INSTANCE = new NotifViewBarn_Factory();
    }
}
