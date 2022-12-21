package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyDialog;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/privacy/PrivacyDialogController$onDialogDismissed$1", "Lcom/android/systemui/privacy/PrivacyDialog$OnDialogDismissed;", "onDialogDismissed", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyDialogController.kt */
public final class PrivacyDialogController$onDialogDismissed$1 implements PrivacyDialog.OnDialogDismissed {
    final /* synthetic */ PrivacyDialogController this$0;

    PrivacyDialogController$onDialogDismissed$1(PrivacyDialogController privacyDialogController) {
        this.this$0 = privacyDialogController;
    }

    public void onDialogDismissed() {
        this.this$0.privacyLogger.logPrivacyDialogDismissed();
        this.this$0.uiEventLogger.log(PrivacyDialogEvent.PRIVACY_DIALOG_DISMISSED);
        this.this$0.dialog = null;
    }
}
