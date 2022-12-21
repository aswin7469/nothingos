package com.android.systemui.util.view;

import dagger.internal.Factory;

public final class ViewUtil_Factory implements Factory<ViewUtil> {
    public ViewUtil get() {
        return newInstance();
    }

    public static ViewUtil_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ViewUtil newInstance() {
        return new ViewUtil();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final ViewUtil_Factory INSTANCE = new ViewUtil_Factory();

        private InstanceHolder() {
        }
    }
}
