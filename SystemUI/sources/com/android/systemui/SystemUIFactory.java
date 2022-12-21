package com.android.systemui;

import android.app.ActivityThread;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule;
import com.android.p019wm.shell.transition.ShellTransitions;
import com.android.systemui.dagger.DaggerGlobalRootComponent;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider;
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public class SystemUIFactory {
    private static final String TAG = "SystemUIFactory";
    static SystemUIFactory mFactory;
    private boolean mInitializeComponents;
    private GlobalRootComponent mRootComponent;
    private SysUIComponent mSysUIComponent;
    private WMComponent mWMComponent;

    /* access modifiers changed from: protected */
    public SysUIComponent.Builder prepareSysUIComponentBuilder(SysUIComponent.Builder builder, WMComponent wMComponent) {
        return builder;
    }

    public static <T extends SystemUIFactory> T getInstance() {
        return mFactory;
    }

    public static void createFromConfig(Context context) {
        createFromConfig(context, false);
    }

    public static void createFromConfig(Context context, boolean z) {
        if (mFactory == null) {
            String string = context.getString(C1893R.string.config_systemUIFactoryComponent);
            if (string == null || string.length() == 0) {
                throw new RuntimeException("No SystemUIFactory component configured");
            }
            try {
                SystemUIFactory systemUIFactory = (SystemUIFactory) context.getClassLoader().loadClass(string).newInstance();
                mFactory = systemUIFactory;
                systemUIFactory.init(context, z);
            } catch (Throwable th) {
                Log.w(TAG, "Error creating SystemUIFactory component: " + string, th);
                throw new RuntimeException(th);
            }
        }
    }

    static void cleanup() {
        mFactory = null;
    }

    public void init(Context context, boolean z) throws ExecutionException, InterruptedException {
        SysUIComponent.Builder builder;
        this.mInitializeComponents = !z && Process.myUserHandle().isSystem() && ActivityThread.currentProcessName().equals(ActivityThread.currentPackageName());
        this.mRootComponent = buildGlobalRootComponent(context);
        setupWmComponent(context);
        if (this.mInitializeComponents) {
            this.mWMComponent.init();
        }
        SysUIComponent.Builder sysUIComponent = this.mRootComponent.getSysUIComponent();
        if (this.mInitializeComponents) {
            builder = prepareSysUIComponentBuilder(sysUIComponent, this.mWMComponent).setPip(this.mWMComponent.getPip()).setLegacySplitScreen(this.mWMComponent.getLegacySplitScreen()).setSplitScreen(this.mWMComponent.getSplitScreen()).setOneHanded(this.mWMComponent.getOneHanded()).setBubbles(this.mWMComponent.getBubbles()).setHideDisplayCutout(this.mWMComponent.getHideDisplayCutout()).setShellCommandHandler(this.mWMComponent.getShellCommandHandler()).setAppPairs(this.mWMComponent.getAppPairs()).setTaskViewFactory(this.mWMComponent.getTaskViewFactory()).setTransitions(this.mWMComponent.getTransitions()).setStartingSurface(this.mWMComponent.getStartingSurface()).setDisplayAreaHelper(this.mWMComponent.getDisplayAreaHelper()).setTaskSurfaceHelper(this.mWMComponent.getTaskSurfaceHelper()).setRecentTasks(this.mWMComponent.getRecentTasks()).setCompatUI(this.mWMComponent.getCompatUI()).setDragAndDrop(this.mWMComponent.getDragAndDrop()).setBackAnimation(this.mWMComponent.getBackAnimation());
        } else {
            builder = prepareSysUIComponentBuilder(sysUIComponent, this.mWMComponent).setPip(Optional.ofNullable(null)).setLegacySplitScreen(Optional.ofNullable(null)).setSplitScreen(Optional.ofNullable(null)).setOneHanded(Optional.ofNullable(null)).setBubbles(Optional.ofNullable(null)).setHideDisplayCutout(Optional.ofNullable(null)).setShellCommandHandler(Optional.ofNullable(null)).setAppPairs(Optional.ofNullable(null)).setTaskViewFactory(Optional.ofNullable(null)).setTransitions(new ShellTransitions() {
            }).setDisplayAreaHelper(Optional.ofNullable(null)).setStartingSurface(Optional.ofNullable(null)).setTaskSurfaceHelper(Optional.ofNullable(null)).setRecentTasks(Optional.ofNullable(null)).setCompatUI(Optional.ofNullable(null)).setDragAndDrop(Optional.ofNullable(null)).setBackAnimation(Optional.ofNullable(null));
        }
        SysUIComponent build = builder.build();
        this.mSysUIComponent = build;
        if (this.mInitializeComponents) {
            build.init();
        }
        this.mSysUIComponent.createDependency().start();
        this.mSysUIComponent.createDependencyEx().start();
    }

    private void setupWmComponent(Context context) {
        WMComponent.Builder wMComponentBuilder = this.mRootComponent.getWMComponentBuilder();
        if (!this.mInitializeComponents || !WMShellConcurrencyModule.enableShellMainThread(context)) {
            this.mWMComponent = wMComponentBuilder.build();
            return;
        }
        HandlerThread createShellMainThread = WMShellConcurrencyModule.createShellMainThread();
        createShellMainThread.start();
        if (!Handler.createAsync(createShellMainThread.getLooper()).runWithScissors(new SystemUIFactory$$ExternalSyntheticLambda0(this, wMComponentBuilder, createShellMainThread), 5000)) {
            Log.w(TAG, "Failed to initialize WMComponent");
            throw new RuntimeException();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setupWmComponent$0$com-android-systemui-SystemUIFactory  reason: not valid java name */
    public /* synthetic */ void m2530lambda$setupWmComponent$0$comandroidsystemuiSystemUIFactory(WMComponent.Builder builder, HandlerThread handlerThread) {
        builder.setShellMainThread(handlerThread);
        this.mWMComponent = builder.build();
    }

    /* access modifiers changed from: protected */
    public GlobalRootComponent buildGlobalRootComponent(Context context) {
        return DaggerGlobalRootComponent.builder().context(context).build();
    }

    /* access modifiers changed from: protected */
    public boolean shouldInitializeComponents() {
        return this.mInitializeComponents;
    }

    public GlobalRootComponent getRootComponent() {
        return this.mRootComponent;
    }

    public WMComponent getWMComponent() {
        return this.mWMComponent;
    }

    public SysUIComponent getSysUIComponent() {
        return this.mSysUIComponent;
    }

    public Map<Class<?>, Provider<CoreStartable>> getStartableComponents() {
        return this.mSysUIComponent.getStartables();
    }

    public String getVendorComponent(Resources resources) {
        return resources.getString(C1893R.string.config_systemUIVendorServiceComponent);
    }

    public Map<Class<?>, Provider<CoreStartable>> getStartableComponentsPerUser() {
        return this.mSysUIComponent.getPerUserStartables();
    }

    public ScreenshotNotificationSmartActionsProvider createScreenshotNotificationSmartActionsProvider(Context context, Executor executor, Handler handler) {
        return new ScreenshotNotificationSmartActionsProvider();
    }

    public BackGestureTfClassifierProvider createBackGestureTfClassifierProvider(AssetManager assetManager, String str) {
        return new BackGestureTfClassifierProvider();
    }
}
