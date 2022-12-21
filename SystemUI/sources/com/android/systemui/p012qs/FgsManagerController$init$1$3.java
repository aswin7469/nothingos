package com.android.systemui.p012qs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/qs/FgsManagerController$init$1$3", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.FgsManagerController$init$1$3 */
/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$init$1$3 extends BroadcastReceiver {
    final /* synthetic */ FgsManagerController this$0;

    FgsManagerController$init$1$3(FgsManagerController fgsManagerController) {
        this.this$0 = fgsManagerController;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (Intrinsics.areEqual((Object) intent.getAction(), (Object) "android.intent.action.SHOW_FOREGROUND_SERVICE_MANAGER")) {
            this.this$0.showDialog((View) null);
        }
    }
}
