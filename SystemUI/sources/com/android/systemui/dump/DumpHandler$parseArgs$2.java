package com.android.systemui.dump;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo65043d2 = {"<anonymous>", "", "it", "", "invoke", "(Ljava/lang/String;)Ljava/lang/Integer;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DumpHandler.kt */
final class DumpHandler$parseArgs$2 extends Lambda implements Function1<String, Integer> {
    public static final DumpHandler$parseArgs$2 INSTANCE = new DumpHandler$parseArgs$2();

    DumpHandler$parseArgs$2() {
        super(1);
    }

    public final Integer invoke(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        return Integer.valueOf(Integer.parseInt(str));
    }
}
