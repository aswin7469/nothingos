package com.android.settings.notification.history;

import com.android.settings.notification.history.NotificationStation;
import java.util.Comparator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationStation$$ExternalSyntheticLambda0 implements Comparator {
    public final int compare(Object obj, Object obj2) {
        return Long.compare(((NotificationStation.HistoricalNotificationInfo) obj2).timestamp, ((NotificationStation.HistoricalNotificationInfo) obj).timestamp);
    }
}
