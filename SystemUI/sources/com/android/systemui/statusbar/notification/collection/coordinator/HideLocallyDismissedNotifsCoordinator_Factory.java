package com.android.systemui.statusbar.notification.collection.coordinator;

import dagger.internal.Factory;

public final class HideLocallyDismissedNotifsCoordinator_Factory implements Factory<HideLocallyDismissedNotifsCoordinator> {
    public HideLocallyDismissedNotifsCoordinator get() {
        return newInstance();
    }

    public static HideLocallyDismissedNotifsCoordinator_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static HideLocallyDismissedNotifsCoordinator newInstance() {
        return new HideLocallyDismissedNotifsCoordinator();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final HideLocallyDismissedNotifsCoordinator_Factory INSTANCE = new HideLocallyDismissedNotifsCoordinator_Factory();

        private InstanceHolder() {
        }
    }
}
