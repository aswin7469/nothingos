package com.nothing.systemui.volume;

import dagger.internal.Factory;

public final class VolumeDialogImplEx_Factory implements Factory<VolumeDialogImplEx> {
    public VolumeDialogImplEx get() {
        return newInstance();
    }

    public static VolumeDialogImplEx_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static VolumeDialogImplEx newInstance() {
        return new VolumeDialogImplEx();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final VolumeDialogImplEx_Factory INSTANCE = new VolumeDialogImplEx_Factory();

        private InstanceHolder() {
        }
    }
}
