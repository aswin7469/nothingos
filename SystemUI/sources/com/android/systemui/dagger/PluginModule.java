package com.android.systemui.dagger;

import com.android.systemui.ActivityStarterDelegate;
import com.android.systemui.classifier.FalsingManagerProxy;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.globalactions.GlobalActionsImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.phone.DarkIconDispatcherImpl;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class PluginModule {
    /* access modifiers changed from: package-private */
    @Binds
    public abstract DarkIconDispatcher provideDarkIconDispatcher(DarkIconDispatcherImpl darkIconDispatcherImpl);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract FalsingManager provideFalsingManager(FalsingManagerProxy falsingManagerProxy);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract GlobalActions provideGlobalActions(GlobalActionsImpl globalActionsImpl);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract GlobalActions.GlobalActionsManager provideGlobalActionsManager(GlobalActionsComponent globalActionsComponent);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract StatusBarStateController provideStatusBarStateController(StatusBarStateControllerImpl statusBarStateControllerImpl);

    /* access modifiers changed from: package-private */
    @Binds
    public abstract VolumeDialogController provideVolumeDialogController(VolumeDialogControllerImpl volumeDialogControllerImpl);

    @Provides
    static ActivityStarter provideActivityStarter(ActivityStarterDelegate activityStarterDelegate, PluginDependencyProvider pluginDependencyProvider) {
        pluginDependencyProvider.allowPluginDependency(ActivityStarter.class, activityStarterDelegate);
        return activityStarterDelegate;
    }
}
