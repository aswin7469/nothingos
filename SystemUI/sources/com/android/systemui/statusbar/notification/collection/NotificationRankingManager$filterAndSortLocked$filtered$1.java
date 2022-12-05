package com.android.systemui.statusbar.notification.collection;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationRankingManager.kt */
/* loaded from: classes.dex */
public /* synthetic */ class NotificationRankingManager$filterAndSortLocked$filtered$1 extends FunctionReferenceImpl implements Function1<NotificationEntry, Boolean> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public NotificationRankingManager$filterAndSortLocked$filtered$1(NotificationRankingManager notificationRankingManager) {
        super(1, notificationRankingManager, NotificationRankingManager.class, "filter", "filter(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Z", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1949invoke(NotificationEntry notificationEntry) {
        return Boolean.valueOf(invoke2(notificationEntry));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final boolean invoke2(@NotNull NotificationEntry p0) {
        boolean filter;
        Intrinsics.checkNotNullParameter(p0, "p0");
        filter = ((NotificationRankingManager) this.receiver).filter(p0);
        return filter;
    }
}
