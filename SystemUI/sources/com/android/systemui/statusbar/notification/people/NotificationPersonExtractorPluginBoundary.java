package com.android.systemui.statusbar.notification.people;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.NotificationPersonExtractorPlugin;
import com.android.systemui.statusbar.policy.ExtensionController;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PeopleHubNotificationListener.kt */
/* loaded from: classes.dex */
public final class NotificationPersonExtractorPluginBoundary implements NotificationPersonExtractor {
    @Nullable
    private NotificationPersonExtractorPlugin plugin;

    public NotificationPersonExtractorPluginBoundary(@NotNull ExtensionController extensionController) {
        Intrinsics.checkNotNullParameter(extensionController, "extensionController");
        this.plugin = (NotificationPersonExtractorPlugin) extensionController.mo1298newExtension(NotificationPersonExtractorPlugin.class).withPlugin(NotificationPersonExtractorPlugin.class).withCallback(new Consumer<NotificationPersonExtractorPlugin>() { // from class: com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary.1
            @Override // java.util.function.Consumer
            public final void accept(NotificationPersonExtractorPlugin notificationPersonExtractorPlugin) {
                NotificationPersonExtractorPluginBoundary.this.plugin = notificationPersonExtractorPlugin;
            }
        }).build().get();
    }

    @Override // com.android.systemui.statusbar.notification.people.NotificationPersonExtractor
    public boolean isPersonNotification(@NotNull StatusBarNotification sbn) {
        Intrinsics.checkNotNullParameter(sbn, "sbn");
        NotificationPersonExtractorPlugin notificationPersonExtractorPlugin = this.plugin;
        if (notificationPersonExtractorPlugin == null) {
            return false;
        }
        return notificationPersonExtractorPlugin.isPersonNotification(sbn);
    }
}
