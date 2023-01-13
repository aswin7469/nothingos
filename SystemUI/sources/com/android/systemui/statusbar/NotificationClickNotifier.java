package com.android.systemui.statusbar;

import android.app.Notification;
import android.os.RemoteException;
import androidx.constraintlayout.motion.widget.Key;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bJ\u0010\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J.\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eJ\u0016\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010 \u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006!"}, mo65043d2 = {"Lcom/android/systemui/statusbar/NotificationClickNotifier;", "", "barService", "Lcom/android/internal/statusbar/IStatusBarService;", "mainExecutor", "Ljava/util/concurrent/Executor;", "(Lcom/android/internal/statusbar/IStatusBarService;Ljava/util/concurrent/Executor;)V", "getBarService", "()Lcom/android/internal/statusbar/IStatusBarService;", "listeners", "", "Lcom/android/systemui/statusbar/NotificationInteractionListener;", "getListeners", "()Ljava/util/List;", "getMainExecutor", "()Ljava/util/concurrent/Executor;", "addNotificationInteractionListener", "", "listener", "notifyListenersAboutInteraction", "key", "", "onNotificationActionClick", "actionIndex", "", "action", "Landroid/app/Notification$Action;", "visibility", "Lcom/android/internal/statusbar/NotificationVisibility;", "generatedByAssistant", "", "onNotificationClick", "removeNotificationInteractionListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationClickNotifier.kt */
public final class NotificationClickNotifier {
    private final IStatusBarService barService;
    private final List<NotificationInteractionListener> listeners = new ArrayList();
    private final Executor mainExecutor;

    @Inject
    public NotificationClickNotifier(IStatusBarService iStatusBarService, @Main Executor executor) {
        Intrinsics.checkNotNullParameter(iStatusBarService, "barService");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        this.barService = iStatusBarService;
        this.mainExecutor = executor;
    }

    public final IStatusBarService getBarService() {
        return this.barService;
    }

    public final Executor getMainExecutor() {
        return this.mainExecutor;
    }

    public final List<NotificationInteractionListener> getListeners() {
        return this.listeners;
    }

    public final void addNotificationInteractionListener(NotificationInteractionListener notificationInteractionListener) {
        Intrinsics.checkNotNullParameter(notificationInteractionListener, "listener");
        Assert.isMainThread();
        this.listeners.add(notificationInteractionListener);
    }

    public final void removeNotificationInteractionListener(NotificationInteractionListener notificationInteractionListener) {
        Intrinsics.checkNotNullParameter(notificationInteractionListener, "listener");
        Assert.isMainThread();
        this.listeners.remove((Object) notificationInteractionListener);
    }

    private final void notifyListenersAboutInteraction(String str) {
        for (NotificationInteractionListener onNotificationInteraction : this.listeners) {
            onNotificationInteraction.onNotificationInteraction(str);
        }
    }

    public final void onNotificationActionClick(String str, int i, Notification.Action action, NotificationVisibility notificationVisibility, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(notificationVisibility, Key.VISIBILITY);
        try {
            this.barService.onNotificationActionClick(str, i, action, notificationVisibility, z);
        } catch (RemoteException unused) {
        }
        this.mainExecutor.execute(new NotificationClickNotifier$$ExternalSyntheticLambda0(this, str));
    }

    /* access modifiers changed from: private */
    /* renamed from: onNotificationActionClick$lambda-0  reason: not valid java name */
    public static final void m3038onNotificationActionClick$lambda0(NotificationClickNotifier notificationClickNotifier, String str) {
        Intrinsics.checkNotNullParameter(notificationClickNotifier, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        notificationClickNotifier.notifyListenersAboutInteraction(str);
    }

    public final void onNotificationClick(String str, NotificationVisibility notificationVisibility) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(notificationVisibility, Key.VISIBILITY);
        try {
            this.barService.onNotificationClick(str, notificationVisibility);
        } catch (RemoteException unused) {
        }
        this.mainExecutor.execute(new NotificationClickNotifier$$ExternalSyntheticLambda1(this, str));
    }

    /* access modifiers changed from: private */
    /* renamed from: onNotificationClick$lambda-1  reason: not valid java name */
    public static final void m3039onNotificationClick$lambda1(NotificationClickNotifier notificationClickNotifier, String str) {
        Intrinsics.checkNotNullParameter(notificationClickNotifier, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        notificationClickNotifier.notifyListenersAboutInteraction(str);
    }
}
