package com.android.settings.notification.history;

import android.app.NotificationHistory;
import java.util.Comparator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationHistoryPackage$$ExternalSyntheticLambda0 implements Comparator {
    public final int compare(Object obj, Object obj2) {
        return Long.compare(((NotificationHistory.HistoricalNotification) obj2).getPostedTimeMs(), ((NotificationHistory.HistoricalNotification) obj).getPostedTimeMs());
    }
}
