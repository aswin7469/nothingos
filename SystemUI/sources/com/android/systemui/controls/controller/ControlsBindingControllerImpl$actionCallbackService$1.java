package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.IControlsActionCallback;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/controls/controller/ControlsBindingControllerImpl$actionCallbackService$1", "Landroid/service/controls/IControlsActionCallback$Stub;", "accept", "", "token", "Landroid/os/IBinder;", "controlId", "", "response", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsBindingControllerImpl.kt */
public final class ControlsBindingControllerImpl$actionCallbackService$1 extends IControlsActionCallback.Stub {
    final /* synthetic */ ControlsBindingControllerImpl this$0;

    ControlsBindingControllerImpl$actionCallbackService$1(ControlsBindingControllerImpl controlsBindingControllerImpl) {
        this.this$0 = controlsBindingControllerImpl;
    }

    public void accept(IBinder iBinder, String str, int i) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(str, "controlId");
        this.this$0.backgroundExecutor.execute(new ControlsBindingControllerImpl.OnActionResponseRunnable(this.this$0, iBinder, str, i));
    }
}
