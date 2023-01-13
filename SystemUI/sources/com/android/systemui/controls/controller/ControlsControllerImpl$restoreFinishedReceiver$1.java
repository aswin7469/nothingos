package com.android.systemui.controls.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/controls/controller/ControlsControllerImpl$restoreFinishedReceiver$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$restoreFinishedReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ ControlsControllerImpl this$0;

    ControlsControllerImpl$restoreFinishedReceiver$1(ControlsControllerImpl controlsControllerImpl) {
        this.this$0 = controlsControllerImpl;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (intent.getIntExtra("android.intent.extra.USER_ID", -10000) == this.this$0.getCurrentUserId()) {
            this.this$0.executor.execute(new C2031x69023679(this.this$0));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onReceive$lambda-0  reason: not valid java name */
    public static final void m2627onReceive$lambda0(ControlsControllerImpl controlsControllerImpl) {
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        Log.d("ControlsControllerImpl", "Restore finished, storing auxiliary favorites");
        controlsControllerImpl.getAuxiliaryPersistenceWrapper$SystemUI_nothingRelease().initialize();
        controlsControllerImpl.persistenceWrapper.storeFavorites(controlsControllerImpl.getAuxiliaryPersistenceWrapper$SystemUI_nothingRelease().getFavorites());
        controlsControllerImpl.resetFavorites();
    }
}
