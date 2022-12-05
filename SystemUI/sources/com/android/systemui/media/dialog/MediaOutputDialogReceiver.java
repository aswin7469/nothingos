package com.android.systemui.media.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: MediaOutputDialogReceiver.kt */
/* loaded from: classes.dex */
public final class MediaOutputDialogReceiver extends BroadcastReceiver {
    @NotNull
    private final MediaOutputDialogFactory mediaOutputDialogFactory;

    public MediaOutputDialogReceiver(@NotNull MediaOutputDialogFactory mediaOutputDialogFactory) {
        Intrinsics.checkNotNullParameter(mediaOutputDialogFactory, "mediaOutputDialogFactory");
        this.mediaOutputDialogFactory = mediaOutputDialogFactory;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        boolean z;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (TextUtils.equals("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_DIALOG", intent.getAction())) {
            String stringExtra = intent.getStringExtra("package_name");
            if (TextUtils.isEmpty(stringExtra)) {
                z = MediaOutputDialogReceiverKt.DEBUG;
                if (!z) {
                    return;
                }
                Log.e("MediaOutputDlgReceiver", "Unable to launch media output dialog. Package name is empty.");
                return;
            }
            MediaOutputDialogFactory mediaOutputDialogFactory = this.mediaOutputDialogFactory;
            Intrinsics.checkNotNull(stringExtra);
            mediaOutputDialogFactory.create(stringExtra, false);
        }
    }
}
