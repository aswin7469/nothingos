package com.android.systemui.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/media/MediaDataManager$appChangeReceiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$appChangeReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ MediaDataManager this$0;

    MediaDataManager$appChangeReceiver$1(MediaDataManager mediaDataManager) {
        this.this$0 = mediaDataManager;
    }

    public void onReceive(Context context, Intent intent) {
        String[] stringArrayExtra;
        String encodedSchemeSpecificPart;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        String action = intent.getAction();
        if (action != null) {
            int hashCode = action.hashCode();
            if (hashCode != -1001645458) {
                if (hashCode != -757780528) {
                    if (hashCode != 525384130 || !action.equals("android.intent.action.PACKAGE_REMOVED")) {
                        return;
                    }
                } else if (!action.equals("android.intent.action.PACKAGE_RESTARTED")) {
                    return;
                }
                Uri data = intent.getData();
                if (data != null && (encodedSchemeSpecificPart = data.getEncodedSchemeSpecificPart()) != null) {
                    this.this$0.removeAllForPackage(encodedSchemeSpecificPart);
                }
            } else if (action.equals("android.intent.action.PACKAGES_SUSPENDED") && (stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list")) != null) {
                MediaDataManager mediaDataManager = this.this$0;
                for (String str : stringArrayExtra) {
                    Intrinsics.checkNotNullExpressionValue(str, "it");
                    mediaDataManager.removeAllForPackage(str);
                }
            }
        }
    }
}
