package com.nothing.systemui.navigationbar.gestural;

import dagger.internal.Factory;

public final class EdgeBackGestureHandlerEx_Factory implements Factory<EdgeBackGestureHandlerEx> {
    public EdgeBackGestureHandlerEx get() {
        return newInstance();
    }

    public static EdgeBackGestureHandlerEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static EdgeBackGestureHandlerEx newInstance() {
        return new EdgeBackGestureHandlerEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final EdgeBackGestureHandlerEx_Factory INSTANCE = new EdgeBackGestureHandlerEx_Factory();

        private InstanceHolder() {
        }
    }
}
