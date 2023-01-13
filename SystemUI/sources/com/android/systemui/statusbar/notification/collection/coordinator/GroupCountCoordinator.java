package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.ArrayMap;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.render.NotifGroupController;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0016\u0010\u000f\u001a\u00020\b2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u0002R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/GroupCountCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "()V", "untruncatedChildCounts", "Landroid/util/ArrayMap;", "Lcom/android/systemui/statusbar/notification/collection/GroupEntry;", "", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "onAfterRenderGroup", "group", "controller", "Lcom/android/systemui/statusbar/notification/collection/render/NotifGroupController;", "onBeforeFinalizeFilter", "entries", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@CoordinatorScope
/* compiled from: GroupCountCoordinator.kt */
public final class GroupCountCoordinator implements Coordinator {
    private final ArrayMap<GroupEntry, Integer> untruncatedChildCounts = new ArrayMap<>();

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        notifPipeline.addOnBeforeFinalizeFilterListener(new GroupCountCoordinator$$ExternalSyntheticLambda0(this));
        notifPipeline.addOnAfterRenderGroupListener(new GroupCountCoordinator$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public final void onBeforeFinalizeFilter(List<? extends ListEntry> list) {
        this.untruncatedChildCounts.clear();
        Sequence<GroupEntry> filter = SequencesKt.filter(CollectionsKt.asSequence(list), C2684xdd467635.INSTANCE);
        Intrinsics.checkNotNull(filter, "null cannot be cast to non-null type kotlin.sequences.Sequence<R of kotlin.sequences.SequencesKt___SequencesKt.filterIsInstance>");
        for (GroupEntry groupEntry : filter) {
            this.untruncatedChildCounts.put(groupEntry, Integer.valueOf(groupEntry.getChildren().size()));
        }
    }

    /* access modifiers changed from: private */
    public final void onAfterRenderGroup(GroupEntry groupEntry, NotifGroupController notifGroupController) {
        Integer num = this.untruncatedChildCounts.get(groupEntry);
        if (num != null) {
            notifGroupController.setUntruncatedChildCount(num.intValue());
            return;
        }
        throw new IllegalStateException(("No untruncated child count for group: " + groupEntry.getKey()).toString());
    }
}
