package com.android.systemui.controls.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/controls/controller/ControlsControllerImpl$userSwitchReceiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$userSwitchReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ ControlsControllerImpl this$0;

    ControlsControllerImpl$userSwitchReceiver$1(ControlsControllerImpl controlsControllerImpl) {
        this.this$0 = controlsControllerImpl;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (Intrinsics.areEqual((Object) intent.getAction(), (Object) "android.intent.action.USER_SWITCHED")) {
            this.this$0.userChanging = true;
            UserHandle of = UserHandle.of(intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId()));
            if (Intrinsics.areEqual((Object) this.this$0.currentUser, (Object) of)) {
                this.this$0.userChanging = false;
                return;
            }
            ControlsControllerImpl controlsControllerImpl = this.this$0;
            Intrinsics.checkNotNullExpressionValue(of, "newUser");
            controlsControllerImpl.setValuesForUser(of);
        }
    }
}
