package com.android.systemui.dagger;

import com.android.systemui.shared.system.smartspace.SmartspaceTransitionController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class SystemUIModule_ProvideSmartspaceTransitionControllerFactory implements Factory<SmartspaceTransitionController> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public SmartspaceTransitionController mo1933get() {
        return provideSmartspaceTransitionController();
    }

    public static SystemUIModule_ProvideSmartspaceTransitionControllerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SmartspaceTransitionController provideSmartspaceTransitionController() {
        return (SmartspaceTransitionController) Preconditions.checkNotNullFromProvides(SystemUIModule.provideSmartspaceTransitionController());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final SystemUIModule_ProvideSmartspaceTransitionControllerFactory INSTANCE = new SystemUIModule_ProvideSmartspaceTransitionControllerFactory();
    }
}
