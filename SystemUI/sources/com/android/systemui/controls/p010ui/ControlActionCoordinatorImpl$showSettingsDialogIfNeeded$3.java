package com.android.systemui.controls.p010ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import com.android.settingslib.Utils;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/controls/ui/ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$3", "Landroid/content/DialogInterface$OnShowListener;", "onShow", "", "arg0", "Landroid/content/DialogInterface;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$3 */
/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$3 implements DialogInterface.OnShowListener {
    final /* synthetic */ ControlActionCoordinatorImpl this$0;

    ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$3(ControlActionCoordinatorImpl controlActionCoordinatorImpl) {
        this.this$0 = controlActionCoordinatorImpl;
    }

    public void onShow(DialogInterface dialogInterface) {
        if (this.this$0.dialog instanceof AlertDialog) {
            ColorStateList colorAttr = Utils.getColorAttr(this.this$0.context, 17957103);
            Dialog access$getDialog$p = this.this$0.dialog;
            if (access$getDialog$p != null) {
                ((AlertDialog) access$getDialog$p).getButton(-1).setTextColor(colorAttr);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.app.AlertDialog");
        }
    }
}
