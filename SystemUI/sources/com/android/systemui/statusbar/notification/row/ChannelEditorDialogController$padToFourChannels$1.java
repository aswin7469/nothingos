package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ChannelEditorDialogController.kt */
/* loaded from: classes.dex */
public final class ChannelEditorDialogController$padToFourChannels$1 extends Lambda implements Function1<NotificationChannel, Boolean> {
    final /* synthetic */ ChannelEditorDialogController this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelEditorDialogController$padToFourChannels$1(ChannelEditorDialogController channelEditorDialogController) {
        super(1);
        this.this$0 = channelEditorDialogController;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1949invoke(NotificationChannel notificationChannel) {
        return Boolean.valueOf(invoke2(notificationChannel));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final boolean invoke2(@NotNull NotificationChannel it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return this.this$0.getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core().contains(it);
    }
}
