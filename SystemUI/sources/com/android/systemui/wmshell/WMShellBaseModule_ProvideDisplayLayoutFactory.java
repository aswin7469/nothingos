package com.android.systemui.wmshell;

import com.android.wm.shell.common.DisplayLayout;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideDisplayLayoutFactory implements Factory<DisplayLayout> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public DisplayLayout mo1933get() {
        return provideDisplayLayout();
    }

    public static WMShellBaseModule_ProvideDisplayLayoutFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DisplayLayout provideDisplayLayout() {
        return (DisplayLayout) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDisplayLayout());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final WMShellBaseModule_ProvideDisplayLayoutFactory INSTANCE = new WMShellBaseModule_ProvideDisplayLayoutFactory();
    }
}
