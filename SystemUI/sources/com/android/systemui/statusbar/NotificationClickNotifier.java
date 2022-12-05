package com.android.systemui.statusbar;

import android.app.Notification;
import android.os.RemoteException;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotificationClickNotifier.kt */
/* loaded from: classes.dex */
public final class NotificationClickNotifier {
    @NotNull
    private final IStatusBarService barService;
    @NotNull
    private final List<NotificationInteractionListener> listeners = new ArrayList();
    @NotNull
    private final Executor mainExecutor;

    public NotificationClickNotifier(@NotNull IStatusBarService barService, @NotNull Executor mainExecutor) {
        Intrinsics.checkNotNullParameter(barService, "barService");
        Intrinsics.checkNotNullParameter(mainExecutor, "mainExecutor");
        this.barService = barService;
        this.mainExecutor = mainExecutor;
    }

    public final void addNotificationInteractionListener(@NotNull NotificationInteractionListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        Assert.isMainThread();
        this.listeners.add(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifyListenersAboutInteraction(String str) {
        for (NotificationInteractionListener notificationInteractionListener : this.listeners) {
            notificationInteractionListener.onNotificationInteraction(str);
        }
    }

    public final void onNotificationActionClick(@NotNull final String key, int i, @NotNull Notification.Action action, @NotNull NotificationVisibility visibility, boolean z) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(visibility, "visibility");
        try {
            this.barService.onNotificationActionClick(key, i, action, visibility, z);
        } catch (RemoteException unused) {
        }
        this.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.NotificationClickNotifier$onNotificationActionClick$1
            @Override // java.lang.Runnable
            public final void run() {
                NotificationClickNotifier.this.notifyListenersAboutInteraction(key);
            }
        });
    }

    public final void onNotificationClick(@NotNull final String key, @NotNull NotificationVisibility visibility) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(visibility, "visibility");
        try {
            this.barService.onNotificationClick(key, visibility);
        } catch (RemoteException unused) {
        }
        this.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.NotificationClickNotifier$onNotificationClick$1
            @Override // java.lang.Runnable
            public final void run() {
                NotificationClickNotifier.this.notifyListenersAboutInteraction(key);
            }
        });
    }
}
