package com.android.systemui.controls.p010ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/android/systemui/controls/ui/ChallengeDialogs$createPinDialog$1", "Landroid/app/AlertDialog;", "dismiss", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$1 */
/* compiled from: ChallengeDialogs.kt */
public final class ChallengeDialogs$createPinDialog$1 extends AlertDialog {
    ChallengeDialogs$createPinDialog$1(Context context) {
        super(context, 16974545);
    }

    public void dismiss() {
        View decorView;
        InputMethodManager inputMethodManager;
        Window window = getWindow();
        if (!(window == null || (decorView = window.getDecorView()) == null || (inputMethodManager = (InputMethodManager) decorView.getContext().getSystemService(InputMethodManager.class)) == null)) {
            inputMethodManager.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
        }
        super.dismiss();
    }
}
