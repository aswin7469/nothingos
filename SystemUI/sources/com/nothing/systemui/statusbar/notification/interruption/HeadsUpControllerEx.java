package com.nothing.systemui.statusbar.notification.interruption;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u001a\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X.¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u0005X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0007X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016¨\u0006\u001e"}, mo64987d2 = {"Lcom/nothing/systemui/statusbar/notification/interruption/HeadsUpControllerEx;", "", "controller", "Lcom/android/systemui/statusbar/notification/interruption/HeadsUpController;", "headsUpManager", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "headsUpViewBinder", "Lcom/android/systemui/statusbar/notification/interruption/HeadsUpViewBinder;", "(Lcom/android/systemui/statusbar/notification/interruption/HeadsUpController;Lcom/android/systemui/statusbar/policy/HeadsUpManager;Lcom/android/systemui/statusbar/notification/interruption/HeadsUpViewBinder;)V", "getController", "()Lcom/android/systemui/statusbar/notification/interruption/HeadsUpController;", "setController", "(Lcom/android/systemui/statusbar/notification/interruption/HeadsUpController;)V", "manager", "getManager", "()Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "setManager", "(Lcom/android/systemui/statusbar/policy/HeadsUpManager;)V", "viewBinder", "getViewBinder", "()Lcom/android/systemui/statusbar/notification/interruption/HeadsUpViewBinder;", "setViewBinder", "(Lcom/android/systemui/statusbar/notification/interruption/HeadsUpViewBinder;)V", "bindHeadsUpView", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "callback", "Lcom/android/systemui/statusbar/notification/row/NotifBindPipeline$BindCallback;", "showNotification", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpControllerEx.kt */
public class HeadsUpControllerEx {
    public HeadsUpController controller;
    public HeadsUpManager manager;
    public HeadsUpViewBinder viewBinder;

    @Inject
    public HeadsUpControllerEx(HeadsUpController headsUpController, HeadsUpManager headsUpManager, HeadsUpViewBinder headsUpViewBinder) {
        Intrinsics.checkNotNullParameter(headsUpController, "controller");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        Intrinsics.checkNotNullParameter(headsUpViewBinder, "headsUpViewBinder");
        setController(headsUpController);
        setManager(headsUpManager);
        setViewBinder(headsUpViewBinder);
    }

    public final HeadsUpController getController() {
        HeadsUpController headsUpController = this.controller;
        if (headsUpController != null) {
            return headsUpController;
        }
        Intrinsics.throwUninitializedPropertyAccessException("controller");
        return null;
    }

    public final void setController(HeadsUpController headsUpController) {
        Intrinsics.checkNotNullParameter(headsUpController, "<set-?>");
        this.controller = headsUpController;
    }

    public final HeadsUpManager getManager() {
        HeadsUpManager headsUpManager = this.manager;
        if (headsUpManager != null) {
            return headsUpManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("manager");
        return null;
    }

    public final void setManager(HeadsUpManager headsUpManager) {
        Intrinsics.checkNotNullParameter(headsUpManager, "<set-?>");
        this.manager = headsUpManager;
    }

    public final HeadsUpViewBinder getViewBinder() {
        HeadsUpViewBinder headsUpViewBinder = this.viewBinder;
        if (headsUpViewBinder != null) {
            return headsUpViewBinder;
        }
        Intrinsics.throwUninitializedPropertyAccessException("viewBinder");
        return null;
    }

    public final void setViewBinder(HeadsUpViewBinder headsUpViewBinder) {
        Intrinsics.checkNotNullParameter(headsUpViewBinder, "<set-?>");
        this.viewBinder = headsUpViewBinder;
    }

    public void showNotification(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Object obj = NTDependencyEx.get(NTLightweightHeadsupManager.class);
        if (obj != null) {
            NTLightweightHeadsupManager nTLightweightHeadsupManager = (NTLightweightHeadsupManager) obj;
            if (nTLightweightHeadsupManager.shouldShowLightweightHeadsup()) {
                nTLightweightHeadsupManager.showPopNotificationView(notificationEntry);
            } else {
                getManager().showNotification(notificationEntry);
            }
        } else {
            throw new NullPointerException("null cannot be cast to non-null type com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager");
        }
    }

    public void bindHeadsUpView(NotificationEntry notificationEntry, NotifBindPipeline.BindCallback bindCallback) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Object obj = NTDependencyEx.get(NTLightweightHeadsupManager.class);
        if (obj != null) {
            NTLightweightHeadsupManager nTLightweightHeadsupManager = (NTLightweightHeadsupManager) obj;
            if (nTLightweightHeadsupManager.shouldShowLightweightHeadsup()) {
                nTLightweightHeadsupManager.showPopNotificationView(notificationEntry);
            } else {
                getViewBinder().bindHeadsUpView(notificationEntry, bindCallback);
            }
        } else {
            throw new NullPointerException("null cannot be cast to non-null type com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager");
        }
    }
}
