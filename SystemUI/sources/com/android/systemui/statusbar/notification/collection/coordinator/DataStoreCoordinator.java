package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u001c\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0002J\u0014\u0010\u000f\u001a\u00020\u00062\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/DataStoreCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "notifLiveDataStoreImpl", "Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataStoreImpl;", "(Lcom/android/systemui/statusbar/notification/collection/NotifLiveDataStoreImpl;)V", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "flattenedEntryList", "", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "entries", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "onAfterRenderList", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@CoordinatorScope
/* compiled from: DataStoreCoordinator.kt */
public final class DataStoreCoordinator implements Coordinator {
    private final NotifLiveDataStoreImpl notifLiveDataStoreImpl;

    @Inject
    public DataStoreCoordinator(NotifLiveDataStoreImpl notifLiveDataStoreImpl2) {
        Intrinsics.checkNotNullParameter(notifLiveDataStoreImpl2, "notifLiveDataStoreImpl");
        this.notifLiveDataStoreImpl = notifLiveDataStoreImpl2;
    }

    /* access modifiers changed from: private */
    /* renamed from: attach$lambda-0  reason: not valid java name */
    public static final void m3102attach$lambda0(DataStoreCoordinator dataStoreCoordinator, List list, NotifStackController notifStackController) {
        Intrinsics.checkNotNullParameter(dataStoreCoordinator, "this$0");
        Intrinsics.checkNotNullParameter(list, "entries");
        Intrinsics.checkNotNullParameter(notifStackController, "<anonymous parameter 1>");
        dataStoreCoordinator.onAfterRenderList(list);
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        notifPipeline.addOnAfterRenderListListener(new DataStoreCoordinator$$ExternalSyntheticLambda0(this));
    }

    public final void onAfterRenderList(List<? extends ListEntry> list) {
        Intrinsics.checkNotNullParameter(list, "entries");
        this.notifLiveDataStoreImpl.setActiveNotifList(flattenedEntryList(list));
    }

    private final List<NotificationEntry> flattenedEntryList(List<? extends ListEntry> list) {
        List<NotificationEntry> arrayList = new ArrayList<>();
        for (ListEntry listEntry : list) {
            if (listEntry instanceof NotificationEntry) {
                arrayList.add(listEntry);
            } else if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry summary = groupEntry.getSummary();
                if (summary != null) {
                    Intrinsics.checkNotNullExpressionValue(summary, "<get-requireSummary>");
                    arrayList.add(summary);
                    List<NotificationEntry> children = groupEntry.getChildren();
                    Intrinsics.checkNotNullExpressionValue(children, "entry.children");
                    arrayList.addAll(children);
                } else {
                    throw new IllegalStateException(("No Summary: " + groupEntry).toString());
                }
            } else {
                throw new IllegalStateException(("Unexpected entry " + listEntry).toString());
            }
        }
        return arrayList;
    }
}
