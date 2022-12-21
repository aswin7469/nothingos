package com.nothing.systemui.p024qs.tiles;

import dagger.internal.Factory;

/* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx_Factory */
public final class BluetoothTileEx_Factory implements Factory<BluetoothTileEx> {
    public BluetoothTileEx get() {
        return newInstance();
    }

    public static BluetoothTileEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static BluetoothTileEx newInstance() {
        return new BluetoothTileEx();
    }

    /* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx_Factory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final BluetoothTileEx_Factory INSTANCE = new BluetoothTileEx_Factory();

        private InstanceHolder() {
        }
    }
}
