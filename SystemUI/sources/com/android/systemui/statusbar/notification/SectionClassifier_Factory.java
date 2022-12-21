package com.android.systemui.statusbar.notification;

import dagger.internal.Factory;

public final class SectionClassifier_Factory implements Factory<SectionClassifier> {
    public SectionClassifier get() {
        return newInstance();
    }

    public static SectionClassifier_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SectionClassifier newInstance() {
        return new SectionClassifier();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final SectionClassifier_Factory INSTANCE = new SectionClassifier_Factory();

        private InstanceHolder() {
        }
    }
}
