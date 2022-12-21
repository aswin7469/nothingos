package com.nothing.systemui.p024qs;

import dagger.internal.Factory;

/* renamed from: com.nothing.systemui.qs.TileLayoutEx_Factory */
public final class TileLayoutEx_Factory implements Factory<TileLayoutEx> {
    public TileLayoutEx get() {
        return newInstance();
    }

    public static TileLayoutEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static TileLayoutEx newInstance() {
        return new TileLayoutEx();
    }

    /* renamed from: com.nothing.systemui.qs.TileLayoutEx_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final TileLayoutEx_Factory INSTANCE = new TileLayoutEx_Factory();

        private InstanceHolder() {
        }
    }
}
