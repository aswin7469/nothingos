package com.android.systemui.privacy;

import android.content.Context;
import android.content.Intent;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyDialogController;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000;\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\u0010\r\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001JH\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072(\u0010\t\u001a$\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0006\u0012\u0004\u0018\u00010\u000e\u0012\u0004\u0012\u00020\u000f0\nH\u0016¨\u0006\u0010"}, mo65043d2 = {"com/android/systemui/privacy/PrivacyDialogControllerKt$defaultDialogProvider$1", "Lcom/android/systemui/privacy/PrivacyDialogController$DialogProvider;", "makeDialog", "Lcom/android/systemui/privacy/PrivacyDialog;", "context", "Landroid/content/Context;", "list", "", "Lcom/android/systemui/privacy/PrivacyDialog$PrivacyElement;", "starter", "Lkotlin/Function4;", "", "", "", "Landroid/content/Intent;", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyDialogController.kt */
public final class PrivacyDialogControllerKt$defaultDialogProvider$1 implements PrivacyDialogController.DialogProvider {
    PrivacyDialogControllerKt$defaultDialogProvider$1() {
    }

    public PrivacyDialog makeDialog(Context context, List<PrivacyDialog.PrivacyElement> list, Function4<? super String, ? super Integer, ? super CharSequence, ? super Intent, Unit> function4) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(list, "list");
        Intrinsics.checkNotNullParameter(function4, "starter");
        return new PrivacyDialog(context, list, function4);
    }
}
