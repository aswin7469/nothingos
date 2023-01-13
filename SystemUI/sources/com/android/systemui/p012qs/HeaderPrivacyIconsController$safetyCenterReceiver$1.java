package com.android.systemui.p012qs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/qs/HeaderPrivacyIconsController$safetyCenterReceiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.HeaderPrivacyIconsController$safetyCenterReceiver$1 */
/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController$safetyCenterReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ HeaderPrivacyIconsController this$0;

    HeaderPrivacyIconsController$safetyCenterReceiver$1(HeaderPrivacyIconsController headerPrivacyIconsController) {
        this.this$0 = headerPrivacyIconsController;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        HeaderPrivacyIconsController headerPrivacyIconsController = this.this$0;
        headerPrivacyIconsController.safetyCenterEnabled = headerPrivacyIconsController.safetyCenterManager.isSafetyCenterEnabled();
    }
}
