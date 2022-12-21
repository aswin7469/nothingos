package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;

public final class NotifInflationErrorManager_Factory implements Factory<NotifInflationErrorManager> {
    public NotifInflationErrorManager get() {
        return newInstance();
    }

    public static NotifInflationErrorManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotifInflationErrorManager newInstance() {
        return new NotifInflationErrorManager();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NotifInflationErrorManager_Factory INSTANCE = new NotifInflationErrorManager_Factory();

        private InstanceHolder() {
        }
    }
}
