package com.android.systemui.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/media/MediaResumeListener$userChangeReceiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaResumeListener.kt */
public final class MediaResumeListener$userChangeReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ MediaResumeListener this$0;

    MediaResumeListener$userChangeReceiver$1(MediaResumeListener mediaResumeListener) {
        this.this$0 = mediaResumeListener;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (Intrinsics.areEqual((Object) "android.intent.action.USER_UNLOCKED", (Object) intent.getAction())) {
            this.this$0.loadMediaResumptionControls();
        } else if (Intrinsics.areEqual((Object) "android.intent.action.USER_SWITCHED", (Object) intent.getAction())) {
            this.this$0.currentUserId = intent.getIntExtra("android.intent.extra.user_handle", -1);
            this.this$0.loadSavedComponents();
        }
    }
}
