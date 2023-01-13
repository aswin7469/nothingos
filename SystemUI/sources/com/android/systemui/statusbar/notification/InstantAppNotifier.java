package com.android.systemui.statusbar.notification;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AppGlobals;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SynchronousUserSwitchObserver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.Pair;
import com.android.systemui.C1894R;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dependency;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.NotificationChannels;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class InstantAppNotifier extends CoreStartable implements CommandQueue.Callbacks, KeyguardStateController.Callback {
    public static final int NUM_TASKS_FOR_INSTANT_APP_INFO = 5;
    private static final String TAG = "InstantAppNotifier";
    private final CommandQueue mCommandQueue;
    private final ArraySet<Pair<String, Integer>> mCurrentNotifs = new ArraySet<>();
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    private KeyguardStateController mKeyguardStateController;
    private final Executor mUiBgExecutor;
    private final SynchronousUserSwitchObserver mUserSwitchListener = new SynchronousUserSwitchObserver() {
        public void onUserSwitching(int i) throws RemoteException {
        }

        public void onUserSwitchComplete(int i) throws RemoteException {
            InstantAppNotifier.this.mHandler.post(new InstantAppNotifier$1$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onUserSwitchComplete$0$com-android-systemui-statusbar-notification-InstantAppNotifier$1 */
        public /* synthetic */ void mo39687xe9b5432d() {
            InstantAppNotifier.this.updateForegroundInstantApps();
        }
    };

    @Inject
    public InstantAppNotifier(Context context, CommandQueue commandQueue, @UiBackground Executor executor) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mUiBgExecutor = executor;
    }

    public void start() {
        this.mKeyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);
        try {
            ActivityManager.getService().registerUserSwitchObserver(this.mUserSwitchListener, TAG);
        } catch (RemoteException unused) {
        }
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mKeyguardStateController.addCallback(this);
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
            if (statusBarNotification.getId() == 7) {
                notificationManager.cancel(statusBarNotification.getTag(), statusBarNotification.getId());
            }
        }
    }

    public void appTransitionStarting(int i, long j, long j2, boolean z) {
        if (this.mContext.getDisplayId() == i) {
            updateForegroundInstantApps();
        }
    }

    public void onKeyguardShowingChanged() {
        updateForegroundInstantApps();
    }

    public void preloadRecentApps() {
        updateForegroundInstantApps();
    }

    /* access modifiers changed from: private */
    public void updateForegroundInstantApps() {
        this.mUiBgExecutor.execute(new InstantAppNotifier$$ExternalSyntheticLambda1(this, (NotificationManager) this.mContext.getSystemService(NotificationManager.class), AppGlobals.getPackageManager()));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateForegroundInstantApps$1$com-android-systemui-statusbar-notification-InstantAppNotifier */
    public /* synthetic */ void mo39686xfbaded69(NotificationManager notificationManager, IPackageManager iPackageManager) {
        int windowingMode;
        ArraySet arraySet = new ArraySet(this.mCurrentNotifs);
        try {
            ActivityTaskManager.RootTaskInfo focusedRootTaskInfo = ActivityTaskManager.getService().getFocusedRootTaskInfo();
            if (focusedRootTaskInfo != null && ((windowingMode = focusedRootTaskInfo.configuration.windowConfiguration.getWindowingMode()) == 1 || windowingMode == 6 || windowingMode == 5)) {
                checkAndPostForStack(focusedRootTaskInfo, arraySet, notificationManager, iPackageManager);
            }
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        arraySet.forEach(new InstantAppNotifier$$ExternalSyntheticLambda0(this, notificationManager));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateForegroundInstantApps$0$com-android-systemui-statusbar-notification-InstantAppNotifier */
    public /* synthetic */ void mo39685x7d4ce98a(NotificationManager notificationManager, Pair pair) {
        this.mCurrentNotifs.remove(pair);
        notificationManager.cancelAsUser((String) pair.first, 7, new UserHandle(((Integer) pair.second).intValue()));
    }

    private void checkAndPostForStack(ActivityTaskManager.RootTaskInfo rootTaskInfo, ArraySet<Pair<String, Integer>> arraySet, NotificationManager notificationManager, IPackageManager iPackageManager) {
        ApplicationInfo applicationInfo;
        if (rootTaskInfo != null) {
            try {
                if (rootTaskInfo.topActivity != null) {
                    String packageName = rootTaskInfo.topActivity.getPackageName();
                    if (!arraySet.remove(new Pair(packageName, Integer.valueOf(rootTaskInfo.userId))) && (applicationInfo = iPackageManager.getApplicationInfo(packageName, 8192, rootTaskInfo.userId)) != null && applicationInfo.isInstantApp()) {
                        postInstantAppNotif(packageName, rootTaskInfo.userId, applicationInfo, notificationManager, rootTaskInfo.childTaskIds[rootTaskInfo.childTaskIds.length - 1]);
                    }
                }
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }

    private void postInstantAppNotif(String str, int i, ApplicationInfo applicationInfo, NotificationManager notificationManager, int i2) {
        Notification.Action action;
        PendingIntent pendingIntent;
        String str2;
        int i3;
        PendingIntent pendingIntent2;
        Notification.Builder builder;
        ComponentName componentName;
        String str3 = str;
        int i4 = i;
        ApplicationInfo applicationInfo2 = applicationInfo;
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", this.mContext.getString(C1894R.string.instant_apps));
        this.mCurrentNotifs.add(new Pair(str3, Integer.valueOf(i)));
        String string = this.mContext.getString(C1894R.string.instant_apps_help_url);
        boolean z = !string.isEmpty();
        String string2 = this.mContext.getString(z ? C1894R.string.instant_apps_message_with_help : C1894R.string.instant_apps_message);
        UserHandle of = UserHandle.of(i);
        Notification.Action build = new Notification.Action.Builder((Icon) null, this.mContext.getString(C1894R.string.app_info), PendingIntent.getActivityAsUser(this.mContext, 0, new Intent("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", str3, (String) null)), 67108864, (Bundle) null, of)).build();
        if (z) {
            str2 = "android.intent.action.VIEW";
            action = build;
            pendingIntent = PendingIntent.getActivityAsUser(this.mContext, 0, new Intent("android.intent.action.VIEW").setData(Uri.parse(string)), 67108864, (Bundle) null, of);
            i3 = i2;
        } else {
            str2 = "android.intent.action.VIEW";
            action = build;
            i3 = i2;
            pendingIntent = null;
        }
        Intent taskIntent = getTaskIntent(i3, i4);
        Notification.Builder builder2 = new Notification.Builder(this.mContext, NotificationChannels.GENERAL);
        if (taskIntent == null || !taskIntent.isWebIntent()) {
            builder = builder2;
            pendingIntent2 = pendingIntent;
        } else {
            taskIntent.setComponent((ComponentName) null).setPackage((String) null).addFlags(512).addFlags(268435456);
            Notification.Builder builder3 = builder2;
            pendingIntent2 = pendingIntent;
            PendingIntent activityAsUser = PendingIntent.getActivityAsUser(this.mContext, 0, taskIntent, 67108864, (Bundle) null, of);
            try {
                componentName = AppGlobals.getPackageManager().getInstantAppInstallerComponent();
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
                componentName = null;
            }
            builder = builder3;
            builder.addAction(new Notification.Action.Builder((Icon) null, this.mContext.getString(C1894R.string.go_to_web), PendingIntent.getActivityAsUser(this.mContext, 0, new Intent().setComponent(componentName).setAction(str2).addCategory("android.intent.category.BROWSABLE").addCategory("unique:" + System.currentTimeMillis()).putExtra("android.intent.extra.PACKAGE_NAME", applicationInfo2.packageName).putExtra("android.intent.extra.VERSION_CODE", applicationInfo2.versionCode & Integer.MAX_VALUE).putExtra("android.intent.extra.LONG_VERSION_CODE", applicationInfo2.longVersionCode).putExtra("android.intent.extra.INSTANT_APP_FAILURE", activityAsUser), 67108864, (Bundle) null, of)).build());
        }
        notificationManager.notifyAsUser(str3, 7, builder.addExtras(bundle).addAction(action).setContentIntent(pendingIntent2).setColor(this.mContext.getColor(C1894R.C1895color.instant_apps_color)).setContentTitle(this.mContext.getString(C1894R.string.instant_apps_title, new Object[]{applicationInfo2.loadLabel(this.mContext.getPackageManager())})).setLargeIcon(Icon.createWithResource(str3, applicationInfo2.icon)).setSmallIcon(Icon.createWithResource(this.mContext.getPackageName(), C1894R.C1896drawable.instant_icon)).setContentText(string2).setStyle(new Notification.BigTextStyle().bigText(string2)).setOngoing(true).build(), new UserHandle(i4));
    }

    private Intent getTaskIntent(int i, int i2) {
        List recentTasks = ActivityTaskManager.getInstance().getRecentTasks(5, 0, i2);
        for (int i3 = 0; i3 < recentTasks.size(); i3++) {
            if (((ActivityManager.RecentTaskInfo) recentTasks.get(i3)).id == i) {
                return ((ActivityManager.RecentTaskInfo) recentTasks.get(i3)).baseIntent;
            }
        }
        return null;
    }
}
