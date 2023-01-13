package com.android.systemui.smartspace.dagger;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.smartspace.dagger.SmartspaceViewComponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.smartspace.dagger.SmartspaceViewComponent_SmartspaceViewModule_ProvidesSmartspaceViewFactory */
public final class C2535x6c99c549 implements Factory<BcSmartspaceDataPlugin.SmartspaceView> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<View.OnAttachStateChangeListener> onAttachListenerProvider;
    private final Provider<ViewGroup> parentProvider;
    private final Provider<BcSmartspaceDataPlugin> pluginProvider;

    public C2535x6c99c549(Provider<ActivityStarter> provider, Provider<FalsingManager> provider2, Provider<ViewGroup> provider3, Provider<BcSmartspaceDataPlugin> provider4, Provider<View.OnAttachStateChangeListener> provider5) {
        this.activityStarterProvider = provider;
        this.falsingManagerProvider = provider2;
        this.parentProvider = provider3;
        this.pluginProvider = provider4;
        this.onAttachListenerProvider = provider5;
    }

    public BcSmartspaceDataPlugin.SmartspaceView get() {
        return providesSmartspaceView(this.activityStarterProvider.get(), this.falsingManagerProvider.get(), this.parentProvider.get(), this.pluginProvider.get(), this.onAttachListenerProvider.get());
    }

    public static C2535x6c99c549 create(Provider<ActivityStarter> provider, Provider<FalsingManager> provider2, Provider<ViewGroup> provider3, Provider<BcSmartspaceDataPlugin> provider4, Provider<View.OnAttachStateChangeListener> provider5) {
        return new C2535x6c99c549(provider, provider2, provider3, provider4, provider5);
    }

    public static BcSmartspaceDataPlugin.SmartspaceView providesSmartspaceView(ActivityStarter activityStarter, FalsingManager falsingManager, ViewGroup viewGroup, BcSmartspaceDataPlugin bcSmartspaceDataPlugin, View.OnAttachStateChangeListener onAttachStateChangeListener) {
        return (BcSmartspaceDataPlugin.SmartspaceView) Preconditions.checkNotNullFromProvides(SmartspaceViewComponent.SmartspaceViewModule.INSTANCE.providesSmartspaceView(activityStarter, falsingManager, viewGroup, bcSmartspaceDataPlugin, onAttachStateChangeListener));
    }
}
