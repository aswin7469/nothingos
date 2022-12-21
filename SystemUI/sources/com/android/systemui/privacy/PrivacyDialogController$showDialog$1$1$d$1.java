package com.android.systemui.privacy;

import android.content.Intent;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyDialogController.kt */
/* synthetic */ class PrivacyDialogController$showDialog$1$1$d$1 extends FunctionReferenceImpl implements Function4<String, Integer, CharSequence, Intent, Unit> {
    PrivacyDialogController$showDialog$1$1$d$1(Object obj) {
        super(4, obj, PrivacyDialogController.class, "startActivity", "startActivity(Ljava/lang/String;ILjava/lang/CharSequence;Landroid/content/Intent;)V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
        invoke((String) obj, ((Number) obj2).intValue(), (CharSequence) obj3, (Intent) obj4);
        return Unit.INSTANCE;
    }

    public final void invoke(String str, int i, CharSequence charSequence, Intent intent) {
        Intrinsics.checkNotNullParameter(str, "p0");
        ((PrivacyDialogController) this.receiver).startActivity(str, i, charSequence, intent);
    }
}
