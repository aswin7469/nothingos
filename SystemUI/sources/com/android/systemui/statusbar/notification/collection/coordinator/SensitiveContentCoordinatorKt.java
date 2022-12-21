package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\u001a\u0016\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0002\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006H\u0002¨\u0006\u0007"}, mo64987d2 = {"extractAllRepresentativeEntries", "Lkotlin/sequences/Sequence;", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "listEntry", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "entries", "", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SensitiveContentCoordinator.kt */
public final class SensitiveContentCoordinatorKt {
    /* access modifiers changed from: private */
    public static final Sequence<NotificationEntry> extractAllRepresentativeEntries(List<? extends ListEntry> list) {
        return SequencesKt.flatMap(CollectionsKt.asSequence(list), SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static final Sequence<NotificationEntry> extractAllRepresentativeEntries(ListEntry listEntry) {
        return SequencesKt.sequence(new SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2(listEntry, (Continuation<? super SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2>) null));
    }
}
