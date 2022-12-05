package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: NotifViewBarn.kt */
/* loaded from: classes.dex */
public final class NotifViewBarn {
    @NotNull
    private final Map<String, ExpandableNotificationRowController> rowMap = new LinkedHashMap();

    @NotNull
    public final ExpandableNotificationRowController requireView(@NotNull ListEntry forEntry) {
        Intrinsics.checkNotNullParameter(forEntry, "forEntry");
        ExpandableNotificationRowController expandableNotificationRowController = this.rowMap.get(forEntry.getKey());
        if (expandableNotificationRowController != null) {
            return expandableNotificationRowController;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("No view has been registered for entry: ", forEntry));
    }

    public final void registerViewForEntry(@NotNull ListEntry entry, @NotNull ExpandableNotificationRowController controller) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        Intrinsics.checkNotNullParameter(controller, "controller");
        Map<String, ExpandableNotificationRowController> map = this.rowMap;
        String key = entry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        map.put(key, controller);
    }

    public final void removeViewForEntry(@NotNull ListEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        this.rowMap.remove(entry.getKey());
    }
}
