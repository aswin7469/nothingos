package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo65043d2 = {"<anonymous>", "", "it", "Landroid/app/NotificationChannel;", "invoke", "(Landroid/app/NotificationChannel;)Ljava/lang/Boolean;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ChannelEditorDialogController.kt */
final class ChannelEditorDialogController$padToFourChannels$1 extends Lambda implements Function1<NotificationChannel, Boolean> {
    final /* synthetic */ ChannelEditorDialogController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ChannelEditorDialogController$padToFourChannels$1(ChannelEditorDialogController channelEditorDialogController) {
        super(1);
        this.this$0 = channelEditorDialogController;
    }

    public final Boolean invoke(NotificationChannel notificationChannel) {
        Intrinsics.checkNotNullParameter(notificationChannel, "it");
        return Boolean.valueOf(this.this$0.getPaddedChannels$SystemUI_nothingRelease().contains(notificationChannel));
    }
}
