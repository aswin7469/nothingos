package com.android.systemui.dagger;

import com.android.keyguard.clock.ClockOptionsProvider;
import com.android.p019wm.shell.ShellCommandHandler;
import com.android.p019wm.shell.TaskViewFactory;
import com.android.p019wm.shell.apppairs.AppPairs;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.p019wm.shell.compatui.CompatUI;
import com.android.p019wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.p019wm.shell.draganddrop.DragAndDrop;
import com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.p019wm.shell.onehanded.OneHanded;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.recents.RecentTasks;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.startingsurface.StartingSurface;
import com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.p019wm.shell.transition.ShellTransitions;
import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dependency;
import com.android.systemui.InitController;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.dagger.qualifiers.PerUser;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import com.android.systemui.people.PeopleProvider;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.unfold.FoldStateLogger;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.UnfoldLatencyTracker;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.nothing.systemui.NTDependencyEx;
import dagger.BindsInstance;
import dagger.Subcomponent;
import java.util.Map;
import java.util.Optional;
import javax.inject.Provider;

@SysUISingleton
@Subcomponent(modules = {DefaultComponentBinder.class, DependencyProvider.class, SystemUIBinder.class, SystemUIModule.class, SystemUICoreStartableModule.class, ReferenceSystemUIModule.class})
public interface SysUIComponent {

    @SysUISingleton
    @Subcomponent.Builder
    public interface Builder {
        SysUIComponent build();

        @BindsInstance
        Builder setAppPairs(Optional<AppPairs> optional);

        @BindsInstance
        Builder setBackAnimation(Optional<BackAnimation> optional);

        @BindsInstance
        Builder setBubbles(Optional<Bubbles> optional);

        @BindsInstance
        Builder setCompatUI(Optional<CompatUI> optional);

        @BindsInstance
        Builder setDisplayAreaHelper(Optional<DisplayAreaHelper> optional);

        @BindsInstance
        Builder setDragAndDrop(Optional<DragAndDrop> optional);

        @BindsInstance
        Builder setHideDisplayCutout(Optional<HideDisplayCutout> optional);

        @BindsInstance
        Builder setLegacySplitScreen(Optional<LegacySplitScreen> optional);

        @BindsInstance
        Builder setOneHanded(Optional<OneHanded> optional);

        @BindsInstance
        Builder setPip(Optional<Pip> optional);

        @BindsInstance
        Builder setRecentTasks(Optional<RecentTasks> optional);

        @BindsInstance
        Builder setShellCommandHandler(Optional<ShellCommandHandler> optional);

        @BindsInstance
        Builder setSplitScreen(Optional<SplitScreen> optional);

        @BindsInstance
        Builder setStartingSurface(Optional<StartingSurface> optional);

        @BindsInstance
        Builder setTaskSurfaceHelper(Optional<TaskSurfaceHelper> optional);

        @BindsInstance
        Builder setTaskViewFactory(Optional<TaskViewFactory> optional);

        @BindsInstance
        Builder setTransitions(ShellTransitions shellTransitions);
    }

    @SysUISingleton
    Dependency createDependency();

    @SysUISingleton
    NTDependencyEx createDependencyEx();

    @SysUISingleton
    DumpManager createDumpManager();

    @SysUISingleton
    ConfigurationController getConfigurationController();

    @SysUISingleton
    ContextComponentHelper getContextComponentHelper();

    @SysUISingleton
    Optional<FoldStateLogger> getFoldStateLogger();

    @SysUISingleton
    Optional<FoldStateLoggingProvider> getFoldStateLoggingProvider();

    @SysUISingleton
    InitController getInitController();

    Optional<MediaMuteAwaitConnectionCli> getMediaMuteAwaitConnectionCli();

    Optional<MediaTttChipControllerReceiver> getMediaTttChipControllerReceiver();

    Optional<MediaTttChipControllerSender> getMediaTttChipControllerSender();

    Optional<MediaTttCommandLineHelper> getMediaTttCommandLineHelper();

    Optional<NaturalRotationUnfoldProgressProvider> getNaturalRotationUnfoldProgressProvider();

    Optional<NearbyMediaDevicesManager> getNearbyMediaDevicesManager();

    @PerUser
    Map<Class<?>, Provider<CoreStartable>> getPerUserStartables();

    Map<Class<?>, Provider<CoreStartable>> getStartables();

    Optional<SysUIUnfoldComponent> getSysUIUnfoldComponent();

    @SysUISingleton
    UnfoldLatencyTracker getUnfoldLatencyTracker();

    void inject(ClockOptionsProvider clockOptionsProvider);

    void inject(SystemUIAppComponentFactory systemUIAppComponentFactory);

    void inject(KeyguardSliceProvider keyguardSliceProvider);

    void inject(PeopleProvider peopleProvider);

    @SysUISingleton
    BootCompleteCacheImpl provideBootCacheImpl();

    void init() {
        getSysUIUnfoldComponent().ifPresent(new SysUIComponent$$ExternalSyntheticLambda0());
        getNaturalRotationUnfoldProgressProvider().ifPresent(new SysUIComponent$$ExternalSyntheticLambda1());
        getMediaTttChipControllerSender();
        getMediaTttChipControllerReceiver();
        getMediaTttCommandLineHelper();
        getMediaMuteAwaitConnectionCli();
        getNearbyMediaDevicesManager();
        getUnfoldLatencyTracker().init();
        getFoldStateLoggingProvider().ifPresent(new SysUIComponent$$ExternalSyntheticLambda2());
        getFoldStateLogger().ifPresent(new SysUIComponent$$ExternalSyntheticLambda3());
    }

    static /* synthetic */ void lambda$init$0(SysUIUnfoldComponent sysUIUnfoldComponent) {
        sysUIUnfoldComponent.getUnfoldLightRevealOverlayAnimation().init();
        sysUIUnfoldComponent.getUnfoldTransitionWallpaperController().init();
    }
}
