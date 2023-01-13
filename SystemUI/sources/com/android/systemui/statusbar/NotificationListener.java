package com.android.systemui.statusbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class NotificationListener extends NotificationListenerWithPlugins {
    private static final boolean DEBUG = false;
    private static final long MAX_RANKING_DELAY_MILLIS = 500;
    private static final String TAG = "NotificationListener";
    private final Context mContext;
    private final Runnable mDispatchRankingUpdateRunnable = new NotificationListener$$ExternalSyntheticLambda0(this);
    private final Executor mMainExecutor;
    private final List<NotificationHandler> mNotificationHandlers = new ArrayList();
    private final NotificationManager mNotificationManager;
    private final Deque<NotificationListenerService.RankingMap> mRankingMapQueue = new ConcurrentLinkedDeque();
    private final ArrayList<NotificationSettingsListener> mSettingsListeners = new ArrayList<>();
    private long mSkippingRankingUpdatesSince = -1;
    private final SystemClock mSystemClock;

    public interface NotificationHandler {
        void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        }

        void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap);

        void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap);

        void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap);

        void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i);

        void onNotificationsInitialized();
    }

    public interface NotificationSettingsListener {
        void onStatusBarIconsBehaviorChanged(boolean z) {
        }
    }

    @Inject
    public NotificationListener(Context context, NotificationManager notificationManager, SystemClock systemClock, @Main Executor executor, PluginManager pluginManager) {
        super(pluginManager);
        this.mContext = context;
        this.mNotificationManager = notificationManager;
        this.mSystemClock = systemClock;
        this.mMainExecutor = executor;
    }

    public void addNotificationHandler(NotificationHandler notificationHandler) {
        if (!this.mNotificationHandlers.contains(notificationHandler)) {
            this.mNotificationHandlers.add(notificationHandler);
            return;
        }
        throw new IllegalArgumentException("Listener is already added");
    }

    public void addNotificationSettingsListener(NotificationSettingsListener notificationSettingsListener) {
        this.mSettingsListeners.add(notificationSettingsListener);
    }

    public void onListenerConnected() {
        onPluginConnected();
        StatusBarNotification[] activeNotifications = getActiveNotifications();
        if (activeNotifications == null) {
            Log.w(TAG, "onListenerConnected unable to get active notifications.");
            return;
        }
        this.mMainExecutor.execute(new NotificationListener$$ExternalSyntheticLambda3(this, activeNotifications, getCurrentRanking()));
        onSilentStatusBarIconsVisibilityChanged(this.mNotificationManager.shouldHideSilentStatusBarIcons());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onListenerConnected$0$com-android-systemui-statusbar-NotificationListener */
    public /* synthetic */ void mo38718x8f830a0c(StatusBarNotification[] statusBarNotificationArr, NotificationListenerService.RankingMap rankingMap) {
        ArrayList arrayList = new ArrayList();
        for (StatusBarNotification key : statusBarNotificationArr) {
            arrayList.add(getRankingOrTemporaryStandIn(rankingMap, key.getKey()));
        }
        NotificationListenerService.RankingMap rankingMap2 = new NotificationListenerService.RankingMap((NotificationListenerService.Ranking[]) arrayList.toArray(new NotificationListenerService.Ranking[0]));
        for (StatusBarNotification statusBarNotification : statusBarNotificationArr) {
            for (NotificationHandler onNotificationPosted : this.mNotificationHandlers) {
                onNotificationPosted.onNotificationPosted(statusBarNotification, rankingMap2);
            }
        }
        for (NotificationHandler onNotificationsInitialized : this.mNotificationHandlers) {
            onNotificationsInitialized.onNotificationsInitialized();
        }
    }

    public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        if (statusBarNotification != null && !onPluginNotificationPosted(statusBarNotification, rankingMap)) {
            this.mMainExecutor.execute(new NotificationListener$$ExternalSyntheticLambda2(this, statusBarNotification, rankingMap));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onNotificationPosted$1$com-android-systemui-statusbar-NotificationListener */
    public /* synthetic */ void mo38720x35d3369a(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        RemoteInputController.processForRemoteInput(statusBarNotification.getNotification(), this.mContext);
        for (NotificationHandler onNotificationPosted : this.mNotificationHandlers) {
            onNotificationPosted.onNotificationPosted(statusBarNotification, rankingMap);
        }
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
        if (statusBarNotification != null && !onPluginNotificationRemoved(statusBarNotification, rankingMap)) {
            this.mMainExecutor.execute(new NotificationListener$$ExternalSyntheticLambda1(this, statusBarNotification, rankingMap, i));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onNotificationRemoved$2$com-android-systemui-statusbar-NotificationListener */
    public /* synthetic */ void mo38721xd969ac4e(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
        for (NotificationHandler onNotificationRemoved : this.mNotificationHandlers) {
            onNotificationRemoved.onNotificationRemoved(statusBarNotification, rankingMap, i);
        }
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        onNotificationRemoved(statusBarNotification, rankingMap, 0);
    }

    public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        if (rankingMap != null) {
            this.mRankingMapQueue.addLast(onPluginRankingUpdate(rankingMap));
            this.mMainExecutor.execute(this.mDispatchRankingUpdateRunnable);
        }
    }

    /* access modifiers changed from: private */
    public void dispatchRankingUpdate() {
        NotificationListenerService.RankingMap pollFirst = this.mRankingMapQueue.pollFirst();
        if (pollFirst == null) {
            Log.wtf(TAG, "mRankingMapQueue was empty!");
        }
        if (!this.mRankingMapQueue.isEmpty()) {
            long elapsedRealtime = this.mSystemClock.elapsedRealtime();
            if (this.mSkippingRankingUpdatesSince == -1) {
                this.mSkippingRankingUpdatesSince = elapsedRealtime;
            }
            if (elapsedRealtime - this.mSkippingRankingUpdatesSince < 500) {
                return;
            }
        }
        this.mSkippingRankingUpdatesSince = -1;
        for (NotificationHandler onNotificationRankingUpdate : this.mNotificationHandlers) {
            onNotificationRankingUpdate.onNotificationRankingUpdate(pollFirst);
        }
    }

    public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        if (!onPluginNotificationChannelModified(str, userHandle, notificationChannel, i)) {
            this.mMainExecutor.execute(new NotificationListener$$ExternalSyntheticLambda4(this, str, userHandle, notificationChannel, i));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onNotificationChannelModified$3$com-android-systemui-statusbar-NotificationListener */
    public /* synthetic */ void mo38719x112fac5b(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        for (NotificationHandler onNotificationChannelModified : this.mNotificationHandlers) {
            onNotificationChannelModified.onNotificationChannelModified(str, userHandle, notificationChannel, i);
        }
    }

    public void onSilentStatusBarIconsVisibilityChanged(boolean z) {
        Iterator<NotificationSettingsListener> it = this.mSettingsListeners.iterator();
        while (it.hasNext()) {
            it.next().onStatusBarIconsBehaviorChanged(z);
        }
    }

    public final void unsnoozeNotification(String str) {
        if (isBound()) {
            try {
                getNotificationInterface().unsnoozeNotificationFromSystemListener(this.mWrapper, str);
            } catch (RemoteException e) {
                Log.v(TAG, "Unable to contact notification manager", e);
            }
        }
    }

    public void registerAsSystemService() {
        try {
            registerAsSystemService(this.mContext, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()), -1);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to register notification listener", e);
        }
    }

    private static NotificationListenerService.Ranking getRankingOrTemporaryStandIn(NotificationListenerService.RankingMap rankingMap, String str) {
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        if (rankingMap.getRanking(str, ranking)) {
            return ranking;
        }
        ArrayList arrayList = r0;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = r0;
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = r0;
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = r0;
        ArrayList arrayList8 = new ArrayList();
        NotificationListenerService.Ranking ranking2 = ranking;
        ranking.populate(str, 0, false, 0, 0, 0, (CharSequence) null, (String) null, (NotificationChannel) null, arrayList, arrayList3, false, 0, false, 0, false, arrayList5, arrayList7, false, false, false, (ShortcutInfo) null, 0, false);
        return ranking2;
    }
}
