package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SensitiveContentCoordinator.kt */
/* synthetic */ class SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1 extends FunctionReferenceImpl implements Function1<ListEntry, Sequence<? extends NotificationEntry>> {
    public static final SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1 INSTANCE = new SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1();

    SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1() {
        super(1, SensitiveContentCoordinatorKt.class, "extractAllRepresentativeEntries", "extractAllRepresentativeEntries(Lcom/android/systemui/statusbar/notification/collection/ListEntry;)Lkotlin/sequences/Sequence;", 1);
    }

    public final Sequence<NotificationEntry> invoke(ListEntry listEntry) {
        Intrinsics.checkNotNullParameter(listEntry, "p0");
        return SensitiveContentCoordinatorKt.extractAllRepresentativeEntries(listEntry);
    }
}
