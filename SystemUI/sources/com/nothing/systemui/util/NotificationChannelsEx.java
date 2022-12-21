package com.nothing.systemui.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import com.android.systemui.C1893R;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0016\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, mo64987d2 = {"Lcom/nothing/systemui/util/NotificationChannelsEx;", "", "()V", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationChannelsEx.kt */
public class NotificationChannelsEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String GAME_MODE_CHANNEL_NAME = "GAME";

    @JvmStatic
    public static final void createAll(Context context) {
        Companion.createAll(context);
    }

    @Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\t"}, mo64987d2 = {"Lcom/nothing/systemui/util/NotificationChannelsEx$Companion;", "", "()V", "GAME_MODE_CHANNEL_NAME", "", "createAll", "", "context", "Landroid/content/Context;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NotificationChannelsEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final void createAll(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            NotificationChannel notificationChannel = new NotificationChannel(NotificationChannelsEx.GAME_MODE_CHANNEL_NAME, context.getString(C1893R.string.notification_channel_gamemode), 2);
            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/raw/empty_sound.ogg"), (AudioAttributes) null);
            ((NotificationManager) context.getSystemService(NotificationManager.class)).createNotificationChannels(Arrays.asList(notificationChannel));
        }
    }
}
