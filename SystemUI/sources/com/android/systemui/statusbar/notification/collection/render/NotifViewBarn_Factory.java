package com.android.systemui.statusbar.notification.collection.render;

import dagger.internal.Factory;

public final class NotifViewBarn_Factory implements Factory<NotifViewBarn> {
    public NotifViewBarn get() {
        return newInstance();
    }

    public static NotifViewBarn_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotifViewBarn newInstance() {
        return new NotifViewBarn();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NotifViewBarn_Factory INSTANCE = new NotifViewBarn_Factory();

        private InstanceHolder() {
        }
    }
}
