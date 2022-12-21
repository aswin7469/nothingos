package com.android.systemui.statusbar.phone.shade.transition;

import dagger.internal.Factory;

public final class NoOpOverScroller_Factory implements Factory<NoOpOverScroller> {
    public NoOpOverScroller get() {
        return newInstance();
    }

    public static NoOpOverScroller_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NoOpOverScroller newInstance() {
        return new NoOpOverScroller();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NoOpOverScroller_Factory INSTANCE = new NoOpOverScroller_Factory();

        private InstanceHolder() {
        }
    }
}
