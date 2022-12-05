package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ShadeViewManager.kt */
/* loaded from: classes.dex */
public final class ShadeViewManagerFactory {
    @NotNull
    private final Context context;
    @NotNull
    private final ShadeViewDifferLogger logger;
    @NotNull
    private final NotificationIconAreaController notificationIconAreaController;
    @NotNull
    private final NotifViewBarn viewBarn;

    public ShadeViewManagerFactory(@NotNull Context context, @NotNull ShadeViewDifferLogger logger, @NotNull NotifViewBarn viewBarn, @NotNull NotificationIconAreaController notificationIconAreaController) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(logger, "logger");
        Intrinsics.checkNotNullParameter(viewBarn, "viewBarn");
        Intrinsics.checkNotNullParameter(notificationIconAreaController, "notificationIconAreaController");
        this.context = context;
        this.logger = logger;
        this.viewBarn = viewBarn;
        this.notificationIconAreaController = notificationIconAreaController;
    }

    @NotNull
    public final ShadeViewManager create(@NotNull NotificationListContainer listContainer) {
        Intrinsics.checkNotNullParameter(listContainer, "listContainer");
        return new ShadeViewManager(this.context, listContainer, this.logger, this.viewBarn, this.notificationIconAreaController);
    }
}
