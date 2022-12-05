package com.android.systemui.privacy;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: PrivacyDialogController.kt */
/* loaded from: classes.dex */
/* synthetic */ class PrivacyDialogController$showDialog$1$1$d$1 extends FunctionReferenceImpl implements Function2<String, Integer, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public PrivacyDialogController$showDialog$1$1$d$1(PrivacyDialogController privacyDialogController) {
        super(2, privacyDialogController, PrivacyDialogController.class, "startActivity", "startActivity(Ljava/lang/String;I)V", 0);
    }

    @Override // kotlin.jvm.functions.Function2
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1950invoke(String str, Integer num) {
        invoke(str, num.intValue());
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull String p0, int i) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        ((PrivacyDialogController) this.receiver).startActivity(p0, i);
    }
}
