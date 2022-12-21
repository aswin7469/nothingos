package com.nothing.systemui.privacy;

import dagger.internal.Factory;

public final class PrivacyDialogEx_Factory implements Factory<PrivacyDialogEx> {
    public PrivacyDialogEx get() {
        return newInstance();
    }

    public static PrivacyDialogEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PrivacyDialogEx newInstance() {
        return new PrivacyDialogEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final PrivacyDialogEx_Factory INSTANCE = new PrivacyDialogEx_Factory();

        private InstanceHolder() {
        }
    }
}
