package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.Trace;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.collection.render.NotifStats;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0016\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0002J\u001c\u0010\u000e\u001a\u00020\u00062\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/StackCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "notificationIconAreaController", "Lcom/android/systemui/statusbar/phone/NotificationIconAreaController;", "(Lcom/android/systemui/statusbar/phone/NotificationIconAreaController;)V", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "calculateNotifStats", "Lcom/android/systemui/statusbar/notification/collection/render/NotifStats;", "entries", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "onAfterRenderList", "controller", "Lcom/android/systemui/statusbar/notification/collection/render/NotifStackController;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@CoordinatorScope
/* compiled from: StackCoordinator.kt */
public final class StackCoordinator implements Coordinator {
    private final NotificationIconAreaController notificationIconAreaController;

    @Inject
    public StackCoordinator(NotificationIconAreaController notificationIconAreaController2) {
        Intrinsics.checkNotNullParameter(notificationIconAreaController2, "notificationIconAreaController");
        this.notificationIconAreaController = notificationIconAreaController2;
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        notifPipeline.addOnAfterRenderListListener(new StackCoordinator$$ExternalSyntheticLambda0(this));
    }

    private final NotifStats calculateNotifStats(List<? extends ListEntry> list) {
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        for (ListEntry listEntry : list) {
            NotifSection section = listEntry.getSection();
            if (section != null) {
                Intrinsics.checkNotNullExpressionValue(section, "checkNotNull(it.section)… section for ${it.key}\" }");
                NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
                if (representativeEntry != null) {
                    Intrinsics.checkNotNullExpressionValue(representativeEntry, "checkNotNull(it.represen…if entry for ${it.key}\" }");
                    boolean z5 = section.getBucket() == 6;
                    boolean isClearable = representativeEntry.isClearable();
                    if (z5 && isClearable) {
                        z4 = true;
                    } else if (z5 && !isClearable) {
                        z3 = true;
                    } else if (!z5 && isClearable) {
                        z2 = true;
                    } else if (!z5 && !isClearable) {
                        z = true;
                    }
                } else {
                    throw new IllegalStateException(("Null notif entry for " + listEntry.getKey()).toString());
                }
            } else {
                throw new IllegalStateException(("Null section for " + listEntry.getKey()).toString());
            }
        }
        return new NotifStats(list.size(), z, z2, z3, z4);
    }

    public final void onAfterRenderList(List<? extends ListEntry> list, NotifStackController notifStackController) {
        Intrinsics.checkNotNullParameter(list, "entries");
        Intrinsics.checkNotNullParameter(notifStackController, "controller");
        Trace.beginSection("StackCoordinator.onAfterRenderList");
        try {
            notifStackController.setNotifStats(calculateNotifStats(list));
            this.notificationIconAreaController.updateNotificationIcons(list);
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }
}
