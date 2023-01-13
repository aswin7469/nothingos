package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/statusbar/policy/VariableDateViewController$intentReceiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: VariableDateViewController.kt */
public final class VariableDateViewController$intentReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ VariableDateViewController this$0;

    VariableDateViewController$intentReceiver$1(VariableDateViewController variableDateViewController) {
        this.this$0 = variableDateViewController;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        Handler handler = ((VariableDateView) this.this$0.mView).getHandler();
        if (handler != null) {
            String action = intent.getAction();
            if (Intrinsics.areEqual((Object) "android.intent.action.TIME_TICK", (Object) action) || Intrinsics.areEqual((Object) "android.intent.action.TIME_SET", (Object) action) || Intrinsics.areEqual((Object) "android.intent.action.TIMEZONE_CHANGED", (Object) action) || Intrinsics.areEqual((Object) "android.intent.action.LOCALE_CHANGED", (Object) action)) {
                if (Intrinsics.areEqual((Object) "android.intent.action.LOCALE_CHANGED", (Object) action) || Intrinsics.areEqual((Object) "android.intent.action.TIMEZONE_CHANGED", (Object) action)) {
                    handler.post(new C3210xa89d7060(this.this$0));
                }
                handler.post(new C3211xa89d7061(this.this$0));
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onReceive$lambda-0  reason: not valid java name */
    public static final void m3253onReceive$lambda0(VariableDateViewController variableDateViewController) {
        Intrinsics.checkNotNullParameter(variableDateViewController, "this$0");
        variableDateViewController.dateFormat = null;
    }
}
