package com.android.systemui.assist;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import javax.inject.Inject;

@SysUISingleton
public final class PhoneStateMonitor {
    private static final String[] DEFAULT_HOME_CHANGE_ACTIONS = {PackageManagerWrapper.ACTION_PREFERRED_ACTIVITY_CHANGED, "android.intent.action.PACKAGE_ADDED", "android.intent.action.PACKAGE_CHANGED", "android.intent.action.PACKAGE_REMOVED"};
    public static final int PHONE_STATE_ALL_APPS = 7;
    public static final int PHONE_STATE_AOD1 = 1;
    public static final int PHONE_STATE_AOD2 = 2;
    public static final int PHONE_STATE_APP_DEFAULT = 8;
    public static final int PHONE_STATE_APP_FULLSCREEN = 10;
    public static final int PHONE_STATE_APP_IMMERSIVE = 9;
    public static final int PHONE_STATE_BOUNCER = 3;
    public static final int PHONE_STATE_HOME = 5;
    public static final int PHONE_STATE_OVERVIEW = 6;
    public static final int PHONE_STATE_UNLOCKED_LOCKSCREEN = 4;
    private final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    private final Context mContext;
    /* access modifiers changed from: private */
    public ComponentName mDefaultHome = getCurrentDefaultHome();
    /* access modifiers changed from: private */
    public boolean mLauncherShowing;
    private final StatusBarStateController mStatusBarStateController;

    private boolean isLauncherInAllApps() {
        return false;
    }

    private boolean isLauncherInOverview() {
        return false;
    }

    @Inject
    PhoneStateMonitor(Context context, BroadcastDispatcher broadcastDispatcher, Lazy<Optional<CentralSurfaces>> lazy, BootCompleteCache bootCompleteCache, StatusBarStateController statusBarStateController) {
        this.mContext = context;
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mStatusBarStateController = statusBarStateController;
        bootCompleteCache.addListener(new PhoneStateMonitor$$ExternalSyntheticLambda0(this));
        IntentFilter intentFilter = new IntentFilter();
        for (String addAction : DEFAULT_HOME_CHANGE_ACTIONS) {
            intentFilter.addAction(addAction);
        }
        broadcastDispatcher.registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ComponentName unused = PhoneStateMonitor.this.mDefaultHome = PhoneStateMonitor.getCurrentDefaultHome();
            }
        }, intentFilter);
        this.mLauncherShowing = isLauncherShowing(ActivityManagerWrapper.getInstance().getRunningTask());
        TaskStackChangeListeners.getInstance().registerTaskStackListener(new TaskStackChangeListener() {
            public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
                PhoneStateMonitor phoneStateMonitor = PhoneStateMonitor.this;
                boolean unused = phoneStateMonitor.mLauncherShowing = phoneStateMonitor.isLauncherShowing(runningTaskInfo);
            }
        });
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-assist-PhoneStateMonitor  reason: not valid java name */
    public /* synthetic */ void m2551lambda$new$0$comandroidsystemuiassistPhoneStateMonitor() {
        this.mDefaultHome = getCurrentDefaultHome();
    }

    public int getPhoneState() {
        if (isShadeFullscreen()) {
            return getPhoneLockscreenState();
        }
        if (this.mLauncherShowing) {
            return getPhoneLauncherState();
        }
        return 9;
    }

    /* access modifiers changed from: private */
    public static ComponentName getCurrentDefaultHome() {
        ArrayList arrayList = new ArrayList();
        ComponentName homeActivities = PackageManagerWrapper.getInstance().getHomeActivities(arrayList);
        if (homeActivities != null) {
            return homeActivities;
        }
        Iterator it = arrayList.iterator();
        int i = Integer.MIN_VALUE;
        while (true) {
            ComponentName componentName = null;
            while (true) {
                if (!it.hasNext()) {
                    return componentName;
                }
                ResolveInfo resolveInfo = (ResolveInfo) it.next();
                if (resolveInfo.priority > i) {
                    componentName = resolveInfo.activityInfo.getComponentName();
                    i = resolveInfo.priority;
                } else if (resolveInfo.priority == i) {
                }
            }
        }
    }

    private int getPhoneLockscreenState() {
        if (isDozing()) {
            return 1;
        }
        if (isBouncerShowing()) {
            return 3;
        }
        return isKeyguardLocked() ? 2 : 4;
    }

    private int getPhoneLauncherState() {
        if (isLauncherInOverview()) {
            return 6;
        }
        return isLauncherInAllApps() ? 7 : 5;
    }

    private boolean isShadeFullscreen() {
        int state = this.mStatusBarStateController.getState();
        return state == 1 || state == 2;
    }

    private boolean isDozing() {
        return this.mStatusBarStateController.isDozing();
    }

    /* access modifiers changed from: private */
    public boolean isLauncherShowing(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (runningTaskInfo == null || runningTaskInfo.topActivity == null) {
            return false;
        }
        return runningTaskInfo.topActivity.equals(this.mDefaultHome);
    }

    private boolean isBouncerShowing() {
        return ((Boolean) this.mCentralSurfacesOptionalLazy.get().map(new PhoneStateMonitor$$ExternalSyntheticLambda1()).orElse(false)).booleanValue();
    }

    private boolean isKeyguardLocked() {
        KeyguardManager keyguardManager = (KeyguardManager) this.mContext.getSystemService(KeyguardManager.class);
        return keyguardManager != null && keyguardManager.isKeyguardLocked();
    }
}
