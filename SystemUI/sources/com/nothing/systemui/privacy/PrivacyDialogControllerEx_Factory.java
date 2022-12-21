package com.nothing.systemui.privacy;

import dagger.internal.Factory;

public final class PrivacyDialogControllerEx_Factory implements Factory<PrivacyDialogControllerEx> {
    public PrivacyDialogControllerEx get() {
        return newInstance();
    }

    public static PrivacyDialogControllerEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PrivacyDialogControllerEx newInstance() {
        return new PrivacyDialogControllerEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final PrivacyDialogControllerEx_Factory INSTANCE = new PrivacyDialogControllerEx_Factory();

        private InstanceHolder() {
        }
    }
}
