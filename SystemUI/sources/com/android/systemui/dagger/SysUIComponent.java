package com.android.systemui.dagger;

import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.Dependency;
import com.android.systemui.InitController;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.transition.ShellTransitions;
import java.util.Optional;
/* loaded from: classes.dex */
public interface SysUIComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        /* renamed from: build */
        SysUIComponent mo1384build();

        /* renamed from: setAppPairs */
        Builder mo1385setAppPairs(Optional<AppPairs> optional);

        /* renamed from: setBubbles */
        Builder mo1386setBubbles(Optional<Bubbles> optional);

        /* renamed from: setHideDisplayCutout */
        Builder mo1387setHideDisplayCutout(Optional<HideDisplayCutout> optional);

        /* renamed from: setLegacySplitScreen */
        Builder mo1388setLegacySplitScreen(Optional<LegacySplitScreen> optional);

        /* renamed from: setOneHanded */
        Builder mo1389setOneHanded(Optional<OneHanded> optional);

        /* renamed from: setPip */
        Builder mo1390setPip(Optional<Pip> optional);

        /* renamed from: setShellCommandHandler */
        Builder mo1391setShellCommandHandler(Optional<ShellCommandHandler> optional);

        /* renamed from: setSplitScreen */
        Builder mo1392setSplitScreen(Optional<SplitScreen> optional);

        /* renamed from: setStartingSurface */
        Builder mo1393setStartingSurface(Optional<StartingSurface> optional);

        /* renamed from: setTaskSurfaceHelper */
        Builder mo1394setTaskSurfaceHelper(Optional<TaskSurfaceHelper> optional);

        /* renamed from: setTaskViewFactory */
        Builder mo1395setTaskViewFactory(Optional<TaskViewFactory> optional);

        /* renamed from: setTransitions */
        Builder mo1396setTransitions(ShellTransitions shellTransitions);
    }

    Dependency createDependency();

    DumpManager createDumpManager();

    ConfigurationController getConfigurationController();

    ContextComponentHelper getContextComponentHelper();

    InitController getInitController();

    default void init() {
    }

    void inject(SystemUIAppComponentFactory systemUIAppComponentFactory);

    BootCompleteCacheImpl provideBootCacheImpl();
}
