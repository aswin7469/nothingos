package com.android.systemui.media;

import dagger.internal.Factory;

public final class MediaHostStatesManager_Factory implements Factory<MediaHostStatesManager> {
    public MediaHostStatesManager get() {
        return newInstance();
    }

    public static MediaHostStatesManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static MediaHostStatesManager newInstance() {
        return new MediaHostStatesManager();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final MediaHostStatesManager_Factory INSTANCE = new MediaHostStatesManager_Factory();

        private InstanceHolder() {
        }
    }
}
