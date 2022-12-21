package com.android.systemui.statusbar.notification.collection.render;

import dagger.internal.Factory;

public final class RenderStageManager_Factory implements Factory<RenderStageManager> {
    public RenderStageManager get() {
        return newInstance();
    }

    public static RenderStageManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static RenderStageManager newInstance() {
        return new RenderStageManager();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final RenderStageManager_Factory INSTANCE = new RenderStageManager_Factory();

        private InstanceHolder() {
        }
    }
}
