package com.android.systemui.statusbar.notification.collection.coordinator;

import dagger.internal.Factory;

public final class GroupCountCoordinator_Factory implements Factory<GroupCountCoordinator> {
    public GroupCountCoordinator get() {
        return newInstance();
    }

    public static GroupCountCoordinator_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static GroupCountCoordinator newInstance() {
        return new GroupCountCoordinator();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final GroupCountCoordinator_Factory INSTANCE = new GroupCountCoordinator_Factory();

        private InstanceHolder() {
        }
    }
}
