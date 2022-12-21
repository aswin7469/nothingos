package com.nothing.systemui.p024qs.tiles;

import dagger.internal.Factory;

/* renamed from: com.nothing.systemui.qs.tiles.InternetTileEx_Factory */
public final class InternetTileEx_Factory implements Factory<InternetTileEx> {
    public InternetTileEx get() {
        return newInstance();
    }

    public static InternetTileEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static InternetTileEx newInstance() {
        return new InternetTileEx();
    }

    /* renamed from: com.nothing.systemui.qs.tiles.InternetTileEx_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final InternetTileEx_Factory INSTANCE = new InternetTileEx_Factory();

        private InstanceHolder() {
        }
    }
}
