package com.android.keyguard;

import java.text.DateFormat;
import java.util.Date;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "model", "Lcom/android/keyguard/KeyguardListenModel;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardListenQueue.kt */
final class KeyguardListenQueue$print$stringify$1 extends Lambda implements Function1<KeyguardListenModel, String> {
    final /* synthetic */ DateFormat $dateFormat;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    KeyguardListenQueue$print$stringify$1(DateFormat dateFormat) {
        super(1);
        this.$dateFormat = dateFormat;
    }

    public final String invoke(KeyguardListenModel keyguardListenModel) {
        Intrinsics.checkNotNullParameter(keyguardListenModel, "model");
        return "    " + this.$dateFormat.format(new Date(keyguardListenModel.getTimeMillis())) + ' ' + keyguardListenModel;
    }
}
