package com.android.systemui;

import android.app.ActivityThread;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.dagger.DaggerGlobalRootComponent;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider;
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
import com.android.wm.shell.transition.Transitions;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class SystemUIFactory {
    static SystemUIFactory mFactory;
    private boolean mInitializeComponents;
    private GlobalRootComponent mRootComponent;
    private SysUIComponent mSysUIComponent;
    private WMComponent mWMComponent;

    protected SysUIComponent.Builder prepareSysUIComponentBuilder(SysUIComponent.Builder builder, WMComponent wMComponent) {
        return builder;
    }

    public static <T extends SystemUIFactory> T getInstance() {
        return (T) mFactory;
    }

    public static void createFromConfig(Context context) {
        createFromConfig(context, false);
    }

    @VisibleForTesting
    public static void createFromConfig(Context context, boolean z) {
        if (mFactory != null) {
            return;
        }
        String string = context.getString(R$string.config_systemUIFactoryComponent);
        if (string == null || string.length() == 0) {
            throw new RuntimeException("No SystemUIFactory component configured");
        }
        try {
            SystemUIFactory systemUIFactory = (SystemUIFactory) context.getClassLoader().loadClass(string).newInstance();
            mFactory = systemUIFactory;
            systemUIFactory.init(context, z);
        } catch (Throwable th) {
            Log.w("SystemUIFactory", "Error creating SystemUIFactory component: " + string, th);
            throw new RuntimeException(th);
        }
    }

    @VisibleForTesting
    static void cleanup() {
        mFactory = null;
    }

    @VisibleForTesting
    public void init(Context context, boolean z) throws ExecutionException, InterruptedException {
        SysUIComponent.Builder mo1394setTaskSurfaceHelper;
        this.mInitializeComponents = !z && Process.myUserHandle().isSystem() && ActivityThread.currentProcessName().equals(ActivityThread.currentPackageName());
        GlobalRootComponent buildGlobalRootComponent = buildGlobalRootComponent(context);
        this.mRootComponent = buildGlobalRootComponent;
        WMComponent mo1418build = buildGlobalRootComponent.mo1379getWMComponentBuilder().mo1418build();
        this.mWMComponent = mo1418build;
        if (this.mInitializeComponents) {
            mo1418build.init();
        }
        SysUIComponent.Builder mo1378getSysUIComponent = this.mRootComponent.mo1378getSysUIComponent();
        if (this.mInitializeComponents) {
            mo1394setTaskSurfaceHelper = prepareSysUIComponentBuilder(mo1378getSysUIComponent, this.mWMComponent).mo1390setPip(this.mWMComponent.getPip()).mo1388setLegacySplitScreen(this.mWMComponent.getLegacySplitScreen()).mo1392setSplitScreen(this.mWMComponent.getSplitScreen()).mo1389setOneHanded(this.mWMComponent.getOneHanded()).mo1386setBubbles(this.mWMComponent.getBubbles()).mo1387setHideDisplayCutout(this.mWMComponent.getHideDisplayCutout()).mo1391setShellCommandHandler(this.mWMComponent.getShellCommandHandler()).mo1385setAppPairs(this.mWMComponent.getAppPairs()).mo1395setTaskViewFactory(this.mWMComponent.getTaskViewFactory()).mo1396setTransitions(this.mWMComponent.getTransitions()).mo1393setStartingSurface(this.mWMComponent.getStartingSurface()).mo1394setTaskSurfaceHelper(this.mWMComponent.getTaskSurfaceHelper());
        } else {
            mo1394setTaskSurfaceHelper = prepareSysUIComponentBuilder(mo1378getSysUIComponent, this.mWMComponent).mo1390setPip(Optional.ofNullable(null)).mo1388setLegacySplitScreen(Optional.ofNullable(null)).mo1392setSplitScreen(Optional.ofNullable(null)).mo1389setOneHanded(Optional.ofNullable(null)).mo1386setBubbles(Optional.ofNullable(null)).mo1387setHideDisplayCutout(Optional.ofNullable(null)).mo1391setShellCommandHandler(Optional.ofNullable(null)).mo1385setAppPairs(Optional.ofNullable(null)).mo1395setTaskViewFactory(Optional.ofNullable(null)).mo1396setTransitions(Transitions.createEmptyForTesting()).mo1393setStartingSurface(Optional.ofNullable(null)).mo1394setTaskSurfaceHelper(Optional.ofNullable(null));
        }
        SysUIComponent mo1384build = mo1394setTaskSurfaceHelper.mo1384build();
        this.mSysUIComponent = mo1384build;
        if (this.mInitializeComponents) {
            mo1384build.init();
        }
        this.mSysUIComponent.createDependency().start();
    }

    protected GlobalRootComponent buildGlobalRootComponent(Context context) {
        return DaggerGlobalRootComponent.builder().mo1381context(context).mo1380build();
    }

    public GlobalRootComponent getRootComponent() {
        return this.mRootComponent;
    }

    public SysUIComponent getSysUIComponent() {
        return this.mSysUIComponent;
    }

    public String[] getSystemUIServiceComponents(Resources resources) {
        return resources.getStringArray(R$array.config_systemUIServiceComponents);
    }

    public String[] getSystemUIServiceComponentsPerUser(Resources resources) {
        return resources.getStringArray(R$array.config_systemUIServiceComponentsPerUser);
    }

    public ScreenshotNotificationSmartActionsProvider createScreenshotNotificationSmartActionsProvider(Context context, Executor executor, Handler handler) {
        return new ScreenshotNotificationSmartActionsProvider();
    }

    public BackGestureTfClassifierProvider createBackGestureTfClassifierProvider(AssetManager assetManager, String str) {
        return new BackGestureTfClassifierProvider();
    }
}
