package com.android.systemui.statusbar.notification.interruption;

import android.util.ArrayMap;
import androidx.core.p004os.CancellationSignal;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import java.util.Map;
import javax.inject.Inject;

@SysUISingleton
public class HeadsUpViewBinder {
    private final HeadsUpViewBinderLogger mLogger;
    private final NotificationMessagingUtil mNotificationMessagingUtil;
    private NotificationPresenter mNotificationPresenter;
    private final Map<NotificationEntry, CancellationSignal> mOngoingBindCallbacks = new ArrayMap();
    private final RowContentBindStage mStage;

    @Inject
    HeadsUpViewBinder(NotificationMessagingUtil notificationMessagingUtil, RowContentBindStage rowContentBindStage, HeadsUpViewBinderLogger headsUpViewBinderLogger) {
        this.mNotificationMessagingUtil = notificationMessagingUtil;
        this.mStage = rowContentBindStage;
        this.mLogger = headsUpViewBinderLogger;
    }

    public void setPresenter(NotificationPresenter notificationPresenter) {
        this.mNotificationPresenter = notificationPresenter;
    }

    public void bindHeadsUpView(NotificationEntry notificationEntry, NotifBindPipeline.BindCallback bindCallback) {
        RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mStage.getStageParams(notificationEntry);
        rowContentBindParams.setUseIncreasedHeadsUpHeight(this.mNotificationMessagingUtil.isImportantMessaging(notificationEntry.getSbn(), notificationEntry.getImportance()) && !this.mNotificationPresenter.isPresenterFullyCollapsed());
        rowContentBindParams.requireContentViews(4);
        CancellationSignal requestRebind = this.mStage.requestRebind(notificationEntry, new HeadsUpViewBinder$$ExternalSyntheticLambda1(this, notificationEntry, rowContentBindParams, bindCallback));
        abortBindCallback(notificationEntry);
        this.mLogger.startBindingHun(notificationEntry.getKey());
        this.mOngoingBindCallbacks.put(notificationEntry, requestRebind);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindHeadsUpView$0$com-android-systemui-statusbar-notification-interruption-HeadsUpViewBinder */
    public /* synthetic */ void mo40710xea628d4a(NotificationEntry notificationEntry, RowContentBindParams rowContentBindParams, NotifBindPipeline.BindCallback bindCallback, NotificationEntry notificationEntry2) {
        this.mLogger.entryBoundSuccessfully(notificationEntry.getKey());
        notificationEntry2.getRow().setUsesIncreasedHeadsUpHeight(rowContentBindParams.useIncreasedHeadsUpHeight());
        this.mOngoingBindCallbacks.remove(notificationEntry);
        if (bindCallback != null) {
            bindCallback.onBindFinished(notificationEntry2);
        }
    }

    public void abortBindCallback(NotificationEntry notificationEntry) {
        CancellationSignal remove = this.mOngoingBindCallbacks.remove(notificationEntry);
        if (remove != null) {
            this.mLogger.currentOngoingBindingAborted(notificationEntry.getKey());
            remove.cancel();
        }
    }

    public void unbindHeadsUpView(NotificationEntry notificationEntry) {
        abortBindCallback(notificationEntry);
        ((RowContentBindParams) this.mStage.getStageParams(notificationEntry)).markContentViewsFreeable(4);
        this.mLogger.entryContentViewMarkedFreeable(notificationEntry.getKey());
        this.mStage.requestRebind(notificationEntry, new HeadsUpViewBinder$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$unbindHeadsUpView$1$com-android-systemui-statusbar-notification-interruption-HeadsUpViewBinder */
    public /* synthetic */ void mo40711x8f861682(NotificationEntry notificationEntry) {
        this.mLogger.entryUnbound(notificationEntry.getKey());
    }
}
