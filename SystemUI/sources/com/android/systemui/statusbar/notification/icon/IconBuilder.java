package com.android.systemui.statusbar.notification.icon;

import android.app.Notification;
import android.content.Context;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/icon/IconBuilder;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "createIconView", "Lcom/android/systemui/statusbar/StatusBarIconView;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getIconContentDescription", "", "n", "Landroid/app/Notification;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: IconBuilder.kt */
public final class IconBuilder {
    private final Context context;

    @Inject
    public IconBuilder(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
    }

    public final StatusBarIconView createIconView(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return new StatusBarIconView(this.context, notificationEntry.getSbn().getPackageName() + "/0x" + Integer.toHexString(notificationEntry.getSbn().getId()), notificationEntry.getSbn());
    }

    public final CharSequence getIconContentDescription(Notification notification) {
        Intrinsics.checkNotNullParameter(notification, "n");
        String contentDescForNotification = StatusBarIconView.contentDescForNotification(this.context, notification);
        Intrinsics.checkNotNullExpressionValue(contentDescForNotification, "contentDescForNotification(context, n)");
        return contentDescForNotification;
    }
}
