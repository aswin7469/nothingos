package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.net.wifi.WifiConfiguration;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0010\u0012\f\u0012\n \u0003*\u0004\u0018\u00010\u00020\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\n¢\u0006\u0002\b\u0006"}, mo65043d2 = {"<anonymous>", "Lkotlin/sequences/Sequence;", "Landroid/app/NotificationChannel;", "kotlin.jvm.PlatformType", "group", "Landroid/app/NotificationChannelGroup;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ChannelEditorDialogController.kt */
final class ChannelEditorDialogController$getDisplayableChannels$channels$1 extends Lambda implements Function1<NotificationChannelGroup, Sequence<? extends NotificationChannel>> {
    public static final ChannelEditorDialogController$getDisplayableChannels$channels$1 INSTANCE = new ChannelEditorDialogController$getDisplayableChannels$channels$1();

    ChannelEditorDialogController$getDisplayableChannels$channels$1() {
        super(1);
    }

    public final Sequence<NotificationChannel> invoke(NotificationChannelGroup notificationChannelGroup) {
        Intrinsics.checkNotNullParameter(notificationChannelGroup, WifiConfiguration.GroupCipher.varName);
        List<NotificationChannel> channels = notificationChannelGroup.getChannels();
        Intrinsics.checkNotNullExpressionValue(channels, "group.channels");
        return SequencesKt.filterNot(CollectionsKt.asSequence(channels), C27461.INSTANCE);
    }
}
