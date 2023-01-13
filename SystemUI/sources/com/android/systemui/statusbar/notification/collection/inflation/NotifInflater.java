package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifViewController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001:\u0002\f\rJ\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J \u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&J \u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000eÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/inflation/NotifInflater;", "", "abortInflation", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "inflateViews", "params", "Lcom/android/systemui/statusbar/notification/collection/inflation/NotifInflater$Params;", "callback", "Lcom/android/systemui/statusbar/notification/collection/inflation/NotifInflater$InflationCallback;", "rebindViews", "InflationCallback", "Params", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifInflater.kt */
public interface NotifInflater {

    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/inflation/NotifInflater$InflationCallback;", "", "onInflationFinished", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "controller", "Lcom/android/systemui/statusbar/notification/collection/render/NotifViewController;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotifInflater.kt */
    public interface InflationCallback {
        void onInflationFinished(NotificationEntry notificationEntry, NotifViewController notifViewController);
    }

    void abortInflation(NotificationEntry notificationEntry);

    void inflateViews(NotificationEntry notificationEntry, Params params, InflationCallback inflationCallback);

    void rebindViews(NotificationEntry notificationEntry, Params params, InflationCallback inflationCallback);

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/inflation/NotifInflater$Params;", "", "isLowPriority", "", "reason", "", "(ZLjava/lang/String;)V", "()Z", "getReason", "()Ljava/lang/String;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotifInflater.kt */
    public static final class Params {
        private final boolean isLowPriority;
        private final String reason;

        public Params(boolean z, String str) {
            Intrinsics.checkNotNullParameter(str, "reason");
            this.isLowPriority = z;
            this.reason = str;
        }

        public final String getReason() {
            return this.reason;
        }

        public final boolean isLowPriority() {
            return this.isLowPriority;
        }
    }
}
