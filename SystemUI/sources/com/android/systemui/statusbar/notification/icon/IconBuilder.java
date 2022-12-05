package com.android.systemui.statusbar.notification.icon;

import android.app.Notification;
import android.content.Context;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: IconBuilder.kt */
/* loaded from: classes.dex */
public final class IconBuilder {
    @NotNull
    private final Context context;

    public IconBuilder(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    @NotNull
    public final StatusBarIconView createIconView(@NotNull NotificationEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        Context context = this.context;
        return new StatusBarIconView(context, ((Object) entry.getSbn().getPackageName()) + "/0x" + ((Object) Integer.toHexString(entry.getSbn().getId())), entry.getSbn());
    }

    @NotNull
    public final CharSequence getIconContentDescription(@NotNull Notification n) {
        Intrinsics.checkNotNullParameter(n, "n");
        String contentDescForNotification = StatusBarIconView.contentDescForNotification(this.context, n);
        Intrinsics.checkNotNullExpressionValue(contentDescForNotification, "contentDescForNotification(context, n)");
        return contentDescForNotification;
    }
}
