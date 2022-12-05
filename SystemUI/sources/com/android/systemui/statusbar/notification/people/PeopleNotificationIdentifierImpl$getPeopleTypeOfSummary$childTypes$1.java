package com.android.systemui.statusbar.notification.people;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PeopleNotificationIdentifier.kt */
/* loaded from: classes.dex */
public final class PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1 extends Lambda implements Function1<NotificationEntry, Integer> {
    final /* synthetic */ PeopleNotificationIdentifierImpl this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1(PeopleNotificationIdentifierImpl peopleNotificationIdentifierImpl) {
        super(1);
        this.this$0 = peopleNotificationIdentifierImpl;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final int invoke2(NotificationEntry it) {
        PeopleNotificationIdentifierImpl peopleNotificationIdentifierImpl = this.this$0;
        Intrinsics.checkNotNullExpressionValue(it, "it");
        return peopleNotificationIdentifierImpl.getPeopleNotificationType(it);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Integer mo1949invoke(NotificationEntry notificationEntry) {
        return Integer.valueOf(invoke2(notificationEntry));
    }
}
