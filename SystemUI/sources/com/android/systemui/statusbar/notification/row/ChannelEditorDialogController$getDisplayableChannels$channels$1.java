package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ChannelEditorDialogController.kt */
/* loaded from: classes.dex */
public final class ChannelEditorDialogController$getDisplayableChannels$channels$1 extends Lambda implements Function1<NotificationChannelGroup, Sequence<? extends NotificationChannel>> {
    public static final ChannelEditorDialogController$getDisplayableChannels$channels$1 INSTANCE = new ChannelEditorDialogController$getDisplayableChannels$channels$1();

    ChannelEditorDialogController$getDisplayableChannels$channels$1() {
        super(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ChannelEditorDialogController.kt */
    /* renamed from: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$getDisplayableChannels$channels$1$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends Lambda implements Function1<NotificationChannel, Boolean> {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        AnonymousClass1() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Boolean mo1949invoke(NotificationChannel notificationChannel) {
            return Boolean.valueOf(invoke2(notificationChannel));
        }

        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final boolean invoke2(NotificationChannel notificationChannel) {
            return notificationChannel.isImportanceLockedByOEM() || notificationChannel.getImportance() == 0 || notificationChannel.isImportanceLockedByCriticalDeviceFunction();
        }
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Sequence<NotificationChannel> mo1949invoke(@NotNull NotificationChannelGroup group) {
        Sequence asSequence;
        Sequence<NotificationChannel> filterNot;
        Intrinsics.checkNotNullParameter(group, "group");
        List<NotificationChannel> channels = group.getChannels();
        Intrinsics.checkNotNullExpressionValue(channels, "group.channels");
        asSequence = CollectionsKt___CollectionsKt.asSequence(channels);
        filterNot = SequencesKt___SequencesKt.filterNot(asSequence, AnonymousClass1.INSTANCE);
        return filterNot;
    }
}
