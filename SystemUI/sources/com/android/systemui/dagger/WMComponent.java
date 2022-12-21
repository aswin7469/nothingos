package com.android.systemui.dagger;

import android.os.HandlerThread;
import com.android.p019wm.shell.ShellCommandHandler;
import com.android.p019wm.shell.ShellInit;
import com.android.p019wm.shell.TaskViewFactory;
import com.android.p019wm.shell.apppairs.AppPairs;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import com.android.p019wm.shell.compatui.CompatUI;
import com.android.p019wm.shell.dagger.WMShellModule;
import com.android.p019wm.shell.dagger.WMSingleton;
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
import dagger.BindsInstance;
import dagger.Subcomponent;
import java.util.Optional;

@Subcomponent(modules = {WMShellModule.class})
@WMSingleton
public interface WMComponent {

    @Subcomponent.Builder
    public interface Builder {
        WMComponent build();

        @BindsInstance
        Builder setShellMainThread(@ShellMainThread HandlerThread handlerThread);
    }

    @WMSingleton
    Optional<AppPairs> getAppPairs();

    @WMSingleton
    Optional<BackAnimation> getBackAnimation();

    @WMSingleton
    Optional<Bubbles> getBubbles();

    @WMSingleton
    Optional<CompatUI> getCompatUI();

    @WMSingleton
    Optional<DisplayAreaHelper> getDisplayAreaHelper();

    @WMSingleton
    Optional<DragAndDrop> getDragAndDrop();

    @WMSingleton
    Optional<HideDisplayCutout> getHideDisplayCutout();

    @WMSingleton
    Optional<LegacySplitScreen> getLegacySplitScreen();

    @WMSingleton
    Optional<OneHanded> getOneHanded();

    @WMSingleton
    Optional<Pip> getPip();

    @WMSingleton
    Optional<RecentTasks> getRecentTasks();

    @WMSingleton
    Optional<ShellCommandHandler> getShellCommandHandler();

    @WMSingleton
    ShellInit getShellInit();

    @WMSingleton
    Optional<SplitScreen> getSplitScreen();

    @WMSingleton
    Optional<StartingSurface> getStartingSurface();

    @WMSingleton
    Optional<TaskSurfaceHelper> getTaskSurfaceHelper();

    @WMSingleton
    Optional<TaskViewFactory> getTaskViewFactory();

    @WMSingleton
    ShellTransitions getTransitions();

    void init() {
        getShellInit().init();
    }
}
