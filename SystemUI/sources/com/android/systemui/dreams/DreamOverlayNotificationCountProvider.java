package com.android.systemui.dreams;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class DreamOverlayNotificationCountProvider implements CallbackController<Callback> {
    private final List<Callback> mCallbacks = new ArrayList();
    private final NotificationListener.NotificationHandler mNotificationHandler;
    /* access modifiers changed from: private */
    public final Set<String> mNotificationKeys = new HashSet();

    public interface Callback {
        void onNotificationCountChanged(int i);
    }

    @Inject
    public DreamOverlayNotificationCountProvider(NotificationListener notificationListener, @Background Executor executor) {
        C20771 r0 = new NotificationListener.NotificationHandler() {
            public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            }

            public void onNotificationsInitialized() {
            }

            public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
                DreamOverlayNotificationCountProvider.this.mNotificationKeys.add(statusBarNotification.getKey());
                DreamOverlayNotificationCountProvider.this.reportNotificationCountChanged();
            }

            public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
                DreamOverlayNotificationCountProvider.this.mNotificationKeys.remove(statusBarNotification.getKey());
                DreamOverlayNotificationCountProvider.this.reportNotificationCountChanged();
            }

            public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
                DreamOverlayNotificationCountProvider.this.mNotificationKeys.remove(statusBarNotification.getKey());
                DreamOverlayNotificationCountProvider.this.reportNotificationCountChanged();
            }
        };
        this.mNotificationHandler = r0;
        notificationListener.addNotificationHandler(r0);
        executor.execute(new DreamOverlayNotificationCountProvider$$ExternalSyntheticLambda1(this, notificationListener));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-dreams-DreamOverlayNotificationCountProvider */
    public /* synthetic */ void mo32511x434ff17(NotificationListener notificationListener) {
        Arrays.stream((T[]) notificationListener.getActiveNotifications()).forEach(new DreamOverlayNotificationCountProvider$$ExternalSyntheticLambda2(this));
        reportNotificationCountChanged();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-dreams-DreamOverlayNotificationCountProvider */
    public /* synthetic */ void mo32510x28738356(StatusBarNotification statusBarNotification) {
        this.mNotificationKeys.add(statusBarNotification.getKey());
    }

    public void addCallback(Callback callback) {
        if (!this.mCallbacks.contains(callback)) {
            this.mCallbacks.add(callback);
            callback.onNotificationCountChanged(this.mNotificationKeys.size());
        }
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove((Object) callback);
    }

    /* access modifiers changed from: private */
    public void reportNotificationCountChanged() {
        this.mCallbacks.forEach(new DreamOverlayNotificationCountProvider$$ExternalSyntheticLambda0(this.mNotificationKeys.size()));
    }
}
