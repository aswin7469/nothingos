package com.android.keyguard;

import java.text.DateFormat;
import java.util.Date;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* compiled from: KeyguardListenQueue.kt */
/* loaded from: classes.dex */
final class KeyguardListenQueue$print$stringify$1 extends Lambda implements Function1<KeyguardListenModel, String> {
    final /* synthetic */ DateFormat $dateFormat;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public KeyguardListenQueue$print$stringify$1(DateFormat dateFormat) {
        super(1);
        this.$dateFormat = dateFormat;
    }

    @Override // kotlin.jvm.functions.Function1
    @NotNull
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final String mo1949invoke(@NotNull KeyguardListenModel model) {
        Intrinsics.checkNotNullParameter(model, "model");
        return "    " + ((Object) this.$dateFormat.format(new Date(model.getTimeMillis()))) + ' ' + model;
    }
}
