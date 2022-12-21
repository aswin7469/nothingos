package com.android.systemui.statusbar.phone.panelstate;

import dagger.internal.Factory;

public final class PanelExpansionStateManager_Factory implements Factory<PanelExpansionStateManager> {
    public PanelExpansionStateManager get() {
        return newInstance();
    }

    public static PanelExpansionStateManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PanelExpansionStateManager newInstance() {
        return new PanelExpansionStateManager();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final PanelExpansionStateManager_Factory INSTANCE = new PanelExpansionStateManager_Factory();

        private InstanceHolder() {
        }
    }
}
