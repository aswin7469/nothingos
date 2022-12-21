package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.common.DisplayLayout;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayLayoutFactory */
public final class WMShellBaseModule_ProvideDisplayLayoutFactory implements Factory<DisplayLayout> {
    public DisplayLayout get() {
        return provideDisplayLayout();
    }

    public static WMShellBaseModule_ProvideDisplayLayoutFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DisplayLayout provideDisplayLayout() {
        return (DisplayLayout) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDisplayLayout());
    }

    /* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayLayoutFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final WMShellBaseModule_ProvideDisplayLayoutFactory INSTANCE = new WMShellBaseModule_ProvideDisplayLayoutFactory();

        private InstanceHolder() {
        }
    }
}
