package com.android.systemui.dump;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/dump/LogBufferFreezer$attach$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LogBufferFreezer.kt */
public final class LogBufferFreezer$attach$1 extends BroadcastReceiver {
    final /* synthetic */ LogBufferFreezer this$0;

    LogBufferFreezer$attach$1(LogBufferFreezer logBufferFreezer) {
        this.this$0 = logBufferFreezer;
    }

    public void onReceive(Context context, Intent intent) {
        this.this$0.onBugreportStarted();
    }
}
