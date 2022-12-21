package com.android.systemui;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.Application;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Dumpable;
import android.util.DumpableContainer;
import android.util.Log;
import android.util.TimingsTraceLog;
import android.view.SurfaceControl;
import android.view.ThreadedRenderer;
import com.android.internal.protolog.common.ProtoLog;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.NotificationChannels;
import com.android.systemui.utils.FwkResIdLoader;
import com.nothing.utils.NTSystemUIUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.inject.Provider;

public class SystemUIApplication extends Application implements SystemUIAppComponentFactory.ContextInitializer, DumpableContainer {
    private static final boolean DEBUG = false;
    public static final String TAG = "SystemUIService";
    /* access modifiers changed from: private */
    public BootCompleteCacheImpl mBootCompleteCache;
    private ContextComponentHelper mComponentHelper;
    private SystemUIAppComponentFactory.ContextAvailableCallback mContextAvailableCallback;
    private DumpManager mDumpManager;
    private final ArrayMap<String, Dumpable> mDumpables = new ArrayMap<>();
    private GlobalRootComponent mRootComponent;
    /* access modifiers changed from: private */
    public CoreStartable[] mServices;
    /* access modifiers changed from: private */
    public boolean mServicesStarted;
    private SysUIComponent mSysUIComponent;

    public SystemUIApplication() {
        Log.v(TAG, "SystemUIApplication constructed.");
        ProtoLog.REQUIRE_PROTOLOGTOOL = false;
    }

    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "SystemUIApplication created.");
        TimingsTraceLog timingsTraceLog = new TimingsTraceLog("SystemUIBootTiming", 4096);
        timingsTraceLog.traceBegin("DependencyInjection");
        this.mContextAvailableCallback.onContextAvailable(this);
        FwkResIdLoader.init(getApplicationContext());
        this.mRootComponent = SystemUIFactory.getInstance().getRootComponent();
        SysUIComponent sysUIComponent = SystemUIFactory.getInstance().getSysUIComponent();
        this.mSysUIComponent = sysUIComponent;
        this.mComponentHelper = sysUIComponent.getContextComponentHelper();
        this.mBootCompleteCache = this.mSysUIComponent.provideBootCacheImpl();
        timingsTraceLog.traceEnd();
        Looper.getMainLooper().setTraceTag(4096);
        setTheme(C1893R.style.Theme_SystemUI);
        NTSystemUIUtils.getInstance().setSplitShadeEnabled(getBaseContext().getResources().getBoolean(C1893R.bool.config_use_split_notification_shade));
        if (Process.myUserHandle().equals(UserHandle.SYSTEM)) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
            intentFilter.setPriority(1000);
            int gPUContextPriority = SurfaceControl.getGPUContextPriority();
            Log.i(TAG, "Found SurfaceFlinger's GPU Priority: " + gPUContextPriority);
            if (gPUContextPriority == ThreadedRenderer.EGL_CONTEXT_PRIORITY_REALTIME_NV) {
                Log.i(TAG, "Setting SysUI's GPU Context priority to: " + ThreadedRenderer.EGL_CONTEXT_PRIORITY_HIGH_IMG);
                ThreadedRenderer.setContextPriority(ThreadedRenderer.EGL_CONTEXT_PRIORITY_HIGH_IMG);
            }
            try {
                ActivityManager.getService().enableBinderTracing();
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to enable binder tracing", e);
            }
            registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (!SystemUIApplication.this.mBootCompleteCache.isBootComplete()) {
                        SystemUIApplication.this.unregisterReceiver(this);
                        SystemUIApplication.this.mBootCompleteCache.setBootComplete();
                        if (SystemUIApplication.this.mServicesStarted) {
                            for (CoreStartable onBootCompleted : SystemUIApplication.this.mServices) {
                                onBootCompleted.onBootCompleted();
                            }
                        }
                    }
                }
            }, intentFilter);
            registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction()) && SystemUIApplication.this.mBootCompleteCache.isBootComplete()) {
                        NotificationChannels.createAll(context);
                    }
                }
            }, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
            return;
        }
        String currentProcessName = ActivityThread.currentProcessName();
        ApplicationInfo applicationInfo = getApplicationInfo();
        if (currentProcessName == null || !currentProcessName.startsWith(applicationInfo.processName + ":")) {
            startSecondaryUserServicesIfNeeded();
        }
    }

    public void startServicesIfNeeded() {
        String vendorComponent = SystemUIFactory.getInstance().getVendorComponent(getResources());
        TreeMap treeMap = new TreeMap(Comparator.comparing(new SystemUIApplication$$ExternalSyntheticLambda0()));
        treeMap.putAll(SystemUIFactory.getInstance().getStartableComponents());
        treeMap.putAll(SystemUIFactory.getInstance().getStartableComponentsPerUser());
        startServicesIfNeeded(treeMap, "StartServices", vendorComponent);
    }

    /* access modifiers changed from: package-private */
    public void startSecondaryUserServicesIfNeeded() {
        TreeMap treeMap = new TreeMap(Comparator.comparing(new SystemUIApplication$$ExternalSyntheticLambda0()));
        treeMap.putAll(SystemUIFactory.getInstance().getStartableComponentsPerUser());
        startServicesIfNeeded(treeMap, "StartSecondaryServices", (String) null);
    }

    private void startServicesIfNeeded(Map<Class<?>, Provider<CoreStartable>> map, String str, String str2) {
        if (!this.mServicesStarted) {
            this.mServices = new CoreStartable[(map.size() + (str2 == null ? 0 : 1))];
            if (!this.mBootCompleteCache.isBootComplete() && "1".equals(SystemProperties.get("sys.boot_completed"))) {
                this.mBootCompleteCache.setBootComplete();
            }
            this.mDumpManager = this.mSysUIComponent.createDumpManager();
            Log.v(TAG, "Starting SystemUI services for user " + Process.myUserHandle().getIdentifier() + BaseIconCache.EMPTY_CLASS_NAME);
            TimingsTraceLog timingsTraceLog = new TimingsTraceLog("SystemUIBootTiming", 4096);
            timingsTraceLog.traceBegin(str);
            int i = 0;
            for (Map.Entry next : map.entrySet()) {
                String name = ((Class) next.getKey()).getName();
                timeInitialization(name, new SystemUIApplication$$ExternalSyntheticLambda1(this, i, name, next), timingsTraceLog, str);
                i++;
            }
            if (str2 != null) {
                timeInitialization(str2, new SystemUIApplication$$ExternalSyntheticLambda2(this, str2), timingsTraceLog, str);
            }
            for (int i2 = 0; i2 < this.mServices.length; i2++) {
                if (this.mBootCompleteCache.isBootComplete()) {
                    this.mServices[i2].onBootCompleted();
                }
                this.mDumpManager.registerDumpable(this.mServices[i2].getClass().getName(), this.mServices[i2]);
            }
            this.mSysUIComponent.getInitController().executePostInitTasks();
            timingsTraceLog.traceEnd();
            this.mServicesStarted = true;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startServicesIfNeeded$0$com-android-systemui-SystemUIApplication */
    public /* synthetic */ void mo29856x898064b0(int i, String str, Map.Entry entry) {
        this.mServices[i] = startStartable(str, (Provider) entry.getValue());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startServicesIfNeeded$1$com-android-systemui-SystemUIApplication */
    public /* synthetic */ void mo29857x90e599cf(String str) {
        CoreStartable[] coreStartableArr = this.mServices;
        coreStartableArr[coreStartableArr.length - 1] = startAdditionalStartable(str);
    }

    private void timeInitialization(String str, Runnable runnable, TimingsTraceLog timingsTraceLog, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        timingsTraceLog.traceBegin(str2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + str);
        runnable.run();
        timingsTraceLog.traceEnd();
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (currentTimeMillis2 > 1000) {
            Log.w(TAG, "Initialization of " + str + " took " + currentTimeMillis2 + " ms");
        }
    }

    private CoreStartable startAdditionalStartable(String str) {
        try {
            return startStartable((CoreStartable) Class.forName(str).getConstructor(Context.class).newInstance(this));
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private CoreStartable startStartable(String str, Provider<CoreStartable> provider) {
        return startStartable(provider.get());
    }

    private CoreStartable startStartable(CoreStartable coreStartable) {
        coreStartable.start();
        return coreStartable;
    }

    public boolean addDumpable(Dumpable dumpable) {
        String dumpableName = dumpable.getDumpableName();
        if (this.mDumpables.containsKey(dumpableName)) {
            return false;
        }
        this.mDumpables.put(dumpableName, dumpable);
        DumpManager dumpManager = this.mDumpManager;
        String dumpableName2 = dumpable.getDumpableName();
        Objects.requireNonNull(dumpable);
        dumpManager.registerDumpable(dumpableName2, new SystemUIApplication$$ExternalSyntheticLambda3(dumpable));
        return true;
    }

    public boolean removeDumpable(Dumpable dumpable) {
        Log.w(TAG, "removeDumpable(" + dumpable + "): not implemented");
        return false;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mServicesStarted) {
            this.mSysUIComponent.getConfigurationController().onConfigurationChanged(configuration);
            NTSystemUIUtils.getInstance().setSplitShadeEnabled(getBaseContext().getResources().getBoolean(C1893R.bool.config_use_split_notification_shade));
            for (CoreStartable coreStartable : this.mServices) {
                if (coreStartable != null) {
                    coreStartable.onConfigurationChanged(configuration);
                }
            }
        }
    }

    public CoreStartable[] getServices() {
        return this.mServices;
    }

    public void setContextAvailableCallback(SystemUIAppComponentFactory.ContextAvailableCallback contextAvailableCallback) {
        this.mContextAvailableCallback = contextAvailableCallback;
    }

    public static void overrideNotificationAppName(Context context, Notification.Builder builder, boolean z) {
        String str;
        Bundle bundle = new Bundle();
        if (z) {
            str = context.getString(17040870);
        } else {
            str = context.getString(17040869);
        }
        bundle.putString("android.substName", str);
        builder.addExtras(bundle);
    }
}
