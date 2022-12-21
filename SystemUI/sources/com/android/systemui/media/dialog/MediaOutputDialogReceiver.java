package com.android.systemui.media.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.android.settingslib.media.MediaOutputConstants;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/media/dialog/MediaOutputDialogReceiver;", "Landroid/content/BroadcastReceiver;", "mediaOutputDialogFactory", "Lcom/android/systemui/media/dialog/MediaOutputDialogFactory;", "mediaOutputBroadcastDialogFactory", "Lcom/android/systemui/media/dialog/MediaOutputBroadcastDialogFactory;", "(Lcom/android/systemui/media/dialog/MediaOutputDialogFactory;Lcom/android/systemui/media/dialog/MediaOutputBroadcastDialogFactory;)V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaOutputDialogReceiver.kt */
public final class MediaOutputDialogReceiver extends BroadcastReceiver {
    private final MediaOutputBroadcastDialogFactory mediaOutputBroadcastDialogFactory;
    private final MediaOutputDialogFactory mediaOutputDialogFactory;

    @Inject
    public MediaOutputDialogReceiver(MediaOutputDialogFactory mediaOutputDialogFactory2, MediaOutputBroadcastDialogFactory mediaOutputBroadcastDialogFactory2) {
        Intrinsics.checkNotNullParameter(mediaOutputDialogFactory2, "mediaOutputDialogFactory");
        Intrinsics.checkNotNullParameter(mediaOutputBroadcastDialogFactory2, "mediaOutputBroadcastDialogFactory");
        this.mediaOutputDialogFactory = mediaOutputDialogFactory2;
        this.mediaOutputBroadcastDialogFactory = mediaOutputBroadcastDialogFactory2;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (TextUtils.equals(MediaOutputConstants.ACTION_LAUNCH_MEDIA_OUTPUT_DIALOG, intent.getAction())) {
            String stringExtra = intent.getStringExtra("package_name");
            if (!TextUtils.isEmpty(stringExtra)) {
                MediaOutputDialogFactory mediaOutputDialogFactory2 = this.mediaOutputDialogFactory;
                Intrinsics.checkNotNull(stringExtra);
                MediaOutputDialogFactory.create$default(mediaOutputDialogFactory2, stringExtra, false, (View) null, 4, (Object) null);
            } else if (MediaOutputDialogReceiverKt.DEBUG) {
                Log.e("MediaOutputDlgReceiver", "Unable to launch media output dialog. Package name is empty.");
            }
        } else if (TextUtils.equals(MediaOutputConstants.ACTION_LAUNCH_MEDIA_OUTPUT_BROADCAST_DIALOG, intent.getAction())) {
            String stringExtra2 = intent.getStringExtra("package_name");
            if (!TextUtils.isEmpty(stringExtra2)) {
                MediaOutputBroadcastDialogFactory mediaOutputBroadcastDialogFactory2 = this.mediaOutputBroadcastDialogFactory;
                Intrinsics.checkNotNull(stringExtra2);
                MediaOutputBroadcastDialogFactory.create$default(mediaOutputBroadcastDialogFactory2, stringExtra2, false, (View) null, 4, (Object) null);
            } else if (MediaOutputDialogReceiverKt.DEBUG) {
                Log.e("MediaOutputDlgReceiver", "Unable to launch media output broadcast dialog. Package name is empty.");
            }
        }
    }
}
