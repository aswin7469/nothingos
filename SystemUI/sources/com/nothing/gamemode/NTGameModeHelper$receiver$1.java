package com.nothing.gamemode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/nothing/gamemode/NTGameModeHelper$receiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTGameModeHelper.kt */
public final class NTGameModeHelper$receiver$1 extends BroadcastReceiver {
    final /* synthetic */ NTGameModeHelper this$0;

    NTGameModeHelper$receiver$1(NTGameModeHelper nTGameModeHelper) {
        this.this$0 = nTGameModeHelper;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        String action = intent.getAction();
        Intrinsics.checkNotNullExpressionValue(action, "intent.getAction()");
        if (Intrinsics.areEqual((Object) "android.intent.action.USER_SWITCHED", (Object) action)) {
            this.this$0.loadNotificationDisplayMode();
            this.this$0.loadGameModeEnabled();
            this.this$0.loadMistouchPreventEnabled();
        }
    }
}
