package com.android.systemui.statusbar.notification.collection.provider;

import dagger.internal.Factory;

public final class VisualStabilityProvider_Factory implements Factory<VisualStabilityProvider> {
    public VisualStabilityProvider get() {
        return newInstance();
    }

    public static VisualStabilityProvider_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static VisualStabilityProvider newInstance() {
        return new VisualStabilityProvider();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final VisualStabilityProvider_Factory INSTANCE = new VisualStabilityProvider_Factory();

        private InstanceHolder() {
        }
    }
}
