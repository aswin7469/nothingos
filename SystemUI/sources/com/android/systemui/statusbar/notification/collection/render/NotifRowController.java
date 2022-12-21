package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.FeedbackIcon;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NotifRowController;", "", "setFeedbackIcon", "", "icon", "Lcom/android/systemui/statusbar/notification/FeedbackIcon;", "setLastAudiblyAlertedMs", "lastAudiblyAlertedMs", "", "setSystemExpanded", "systemExpanded", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifRowController.kt */
public interface NotifRowController {
    void setFeedbackIcon(FeedbackIcon feedbackIcon);

    void setLastAudiblyAlertedMs(long j);

    void setSystemExpanded(boolean z);
}
