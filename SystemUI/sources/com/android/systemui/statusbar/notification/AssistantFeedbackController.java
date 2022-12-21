package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.service.notification.NotificationListenerService;
import android.util.SparseArray;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.DeviceConfigProxy;
import javax.inject.Inject;

@SysUISingleton
public class AssistantFeedbackController {
    public static final int STATUS_ALERTED = 1;
    public static final int STATUS_DEMOTED = 4;
    public static final int STATUS_PROMOTED = 3;
    public static final int STATUS_SILENCED = 2;
    public static final int STATUS_UNCHANGED = 0;
    private final Context mContext;
    private final DeviceConfigProxy mDeviceConfigProxy;
    /* access modifiers changed from: private */
    public volatile boolean mFeedbackEnabled;
    private final Handler mHandler;
    private final SparseArray<FeedbackIcon> mIcons;
    private final DeviceConfig.OnPropertiesChangedListener mPropertiesChangedListener;

    @Inject
    public AssistantFeedbackController(@Main Handler handler, Context context, DeviceConfigProxy deviceConfigProxy) {
        C26451 r0 = new DeviceConfig.OnPropertiesChangedListener() {
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("enable_nas_feedback")) {
                    boolean unused = AssistantFeedbackController.this.mFeedbackEnabled = properties.getBoolean("enable_nas_feedback", false);
                }
            }
        };
        this.mPropertiesChangedListener = r0;
        this.mHandler = handler;
        this.mContext = context;
        this.mDeviceConfigProxy = deviceConfigProxy;
        this.mFeedbackEnabled = deviceConfigProxy.getBoolean("systemui", "enable_nas_feedback", false);
        deviceConfigProxy.addOnPropertiesChangedListener("systemui", new AssistantFeedbackController$$ExternalSyntheticLambda0(this), r0);
        SparseArray<FeedbackIcon> sparseArray = new SparseArray<>(4);
        this.mIcons = sparseArray;
        sparseArray.set(1, new FeedbackIcon(17302449, 17040909));
        sparseArray.set(2, new FeedbackIcon(17302452, 17040912));
        sparseArray.set(3, new FeedbackIcon(17302453, 17040911));
        sparseArray.set(4, new FeedbackIcon(17302450, 17040910));
    }

    /* access modifiers changed from: private */
    public void postToHandler(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public boolean isFeedbackEnabled() {
        return this.mFeedbackEnabled;
    }

    public int getFeedbackStatus(NotificationEntry notificationEntry) {
        if (!isFeedbackEnabled()) {
            return 0;
        }
        NotificationListenerService.Ranking ranking = notificationEntry.getRanking();
        int importance = ranking.getChannel().getImportance();
        int importance2 = ranking.getImportance();
        if (importance < 3 && importance2 >= 3) {
            return 1;
        }
        if (importance >= 3 && importance2 < 3) {
            return 2;
        }
        if (importance < importance2 || ranking.getRankingAdjustment() == 1) {
            return 3;
        }
        if (importance > importance2 || ranking.getRankingAdjustment() == -1) {
            return 4;
        }
        return 0;
    }

    public FeedbackIcon getFeedbackIcon(NotificationEntry notificationEntry) {
        return this.mIcons.get(getFeedbackStatus(notificationEntry));
    }

    public int getInlineDescriptionResource(NotificationEntry notificationEntry) {
        int feedbackStatus = getFeedbackStatus(notificationEntry);
        if (feedbackStatus == 1) {
            return C1893R.string.notification_channel_summary_automatic_alerted;
        }
        if (feedbackStatus == 2) {
            return C1893R.string.notification_channel_summary_automatic_silenced;
        }
        if (feedbackStatus != 3) {
            return feedbackStatus != 4 ? C1893R.string.notification_channel_summary_automatic : C1893R.string.notification_channel_summary_automatic_demoted;
        }
        return C1893R.string.notification_channel_summary_automatic_promoted;
    }
}
