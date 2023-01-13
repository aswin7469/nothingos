package com.android.systemui.statusbar.notification.collection.render;

import android.net.wifi.WifiConfiguration;
import android.os.Trace;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00009\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0016\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0016¨\u0006\u0011"}, mo65043d2 = {"com/android/systemui/statusbar/notification/collection/render/ShadeViewManager$viewRenderer$1", "Lcom/android/systemui/statusbar/notification/collection/render/NotifViewRenderer;", "getGroupController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifGroupController;", "group", "Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "getRowController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifRowController;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getStackController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifStackController;", "onRenderList", "", "notifList", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ShadeViewManager.kt */
public final class ShadeViewManager$viewRenderer$1 implements NotifViewRenderer {
    final /* synthetic */ ShadeViewManager this$0;

    ShadeViewManager$viewRenderer$1(ShadeViewManager shadeViewManager) {
        this.this$0 = shadeViewManager;
    }

    public void onRenderList(List<? extends ListEntry> list) {
        Intrinsics.checkNotNullParameter(list, "notifList");
        ShadeViewManager shadeViewManager = this.this$0;
        Trace.beginSection("ShadeViewManager.onRenderList");
        try {
            shadeViewManager.viewDiffer.applySpec(shadeViewManager.specBuilder.buildNodeSpec(shadeViewManager.rootController, list));
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public NotifStackController getStackController() {
        return this.this$0.stackController;
    }

    public NotifGroupController getGroupController(GroupEntry groupEntry) {
        Intrinsics.checkNotNullParameter(groupEntry, WifiConfiguration.GroupCipher.varName);
        NotifViewBarn access$getViewBarn$p = this.this$0.viewBarn;
        NotificationEntry summary = groupEntry.getSummary();
        if (summary != null) {
            Intrinsics.checkNotNullExpressionValue(summary, "<get-requireSummary>");
            return access$getViewBarn$p.requireGroupController(summary);
        }
        throw new IllegalStateException(("No Summary: " + groupEntry).toString());
    }

    public NotifRowController getRowController(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return this.this$0.viewBarn.requireRowController(notificationEntry);
    }
}
