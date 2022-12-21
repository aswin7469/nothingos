package com.nothing.systemui.privacy;

import dagger.internal.Factory;

public final class OngoingPrivacyChipEx_Factory implements Factory<OngoingPrivacyChipEx> {
    public OngoingPrivacyChipEx get() {
        return newInstance();
    }

    public static OngoingPrivacyChipEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static OngoingPrivacyChipEx newInstance() {
        return new OngoingPrivacyChipEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final OngoingPrivacyChipEx_Factory INSTANCE = new OngoingPrivacyChipEx_Factory();

        private InstanceHolder() {
        }
    }
}
