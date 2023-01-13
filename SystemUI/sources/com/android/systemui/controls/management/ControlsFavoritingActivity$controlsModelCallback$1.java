package com.android.systemui.controls.management;

import android.view.View;
import com.android.systemui.controls.management.ControlsModel;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsFavoritingActivity$controlsModelCallback$1", "Lcom/android/systemui/controls/management/ControlsModel$ControlsModelCallback;", "onFirstChange", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$controlsModelCallback$1 implements ControlsModel.ControlsModelCallback {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    ControlsFavoritingActivity$controlsModelCallback$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    public void onFirstChange() {
        View access$getDoneButton$p = this.this$0.doneButton;
        if (access$getDoneButton$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doneButton");
            access$getDoneButton$p = null;
        }
        access$getDoneButton$p.setEnabled(true);
    }
}
