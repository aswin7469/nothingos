package com.android.systemui.demomode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/demomode/DemoModeController$broadcastReceiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DemoModeController.kt */
public final class DemoModeController$broadcastReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ DemoModeController this$0;

    DemoModeController$broadcastReceiver$1(DemoModeController demoModeController) {
        this.this$0 = demoModeController;
    }

    public void onReceive(Context context, Intent intent) {
        Bundle extras;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (DemoMode.ACTION_DEMO.equals(intent.getAction()) && (extras = intent.getExtras()) != null) {
            String string = extras.getString(DemoMode.EXTRA_COMMAND, "");
            Intrinsics.checkNotNullExpressionValue(string, "bundle.getString(\"command\", \"\")");
            String lowerCase = StringsKt.trim((CharSequence) string).toString().toLowerCase();
            Intrinsics.checkNotNullExpressionValue(lowerCase, "this as java.lang.String).toLowerCase()");
            if (lowerCase.length() != 0) {
                try {
                    this.this$0.dispatchDemoCommand(lowerCase, extras);
                } catch (Throwable th) {
                    Log.w("DemoModeController", "Error running demo command, intent=" + intent + ' ' + th);
                }
            }
        }
    }
}
