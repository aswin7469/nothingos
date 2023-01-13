package com.android.systemui.controls.controller;

import android.service.controls.Control;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/controls/controller/ControlsBindingControllerImpl$Companion$emptyCallback$1", "Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "accept", "", "controls", "", "Landroid/service/controls/Control;", "error", "message", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsBindingControllerImpl.kt */
public final class ControlsBindingControllerImpl$Companion$emptyCallback$1 implements ControlsBindingController.LoadCallback {
    public void accept(List<Control> list) {
        Intrinsics.checkNotNullParameter(list, AutoTileManager.DEVICE_CONTROLS);
    }

    public void error(String str) {
        Intrinsics.checkNotNullParameter(str, "message");
    }

    ControlsBindingControllerImpl$Companion$emptyCallback$1() {
    }
}
