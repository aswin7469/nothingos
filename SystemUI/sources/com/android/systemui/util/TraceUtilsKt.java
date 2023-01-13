package com.android.systemui.util;

import android.os.Trace;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a-\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0005H\bø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0002\u0007\n\u0005\b20\u0001¨\u0006\u0007"}, mo65043d2 = {"traceSection", "T", "tag", "", "block", "Lkotlin/Function0;", "(Ljava/lang/String;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: TraceUtils.kt */
public final class TraceUtilsKt {
    public static final <T> T traceSection(String str, Function0<? extends T> function0) {
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(function0, "block");
        Trace.beginSection(str);
        try {
            return function0.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            Trace.endSection();
            InlineMarker.finallyEnd(1);
        }
    }
}
