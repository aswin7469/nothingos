package com.android.systemui.dump;

import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0003"}, mo65043d2 = {"<anonymous>", "", "it", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DumpHandler.kt */
final class DumpHandler$parseArgs$1 extends Lambda implements Function1<String, String> {
    public static final DumpHandler$parseArgs$1 INSTANCE = new DumpHandler$parseArgs$1();

    DumpHandler$parseArgs$1() {
        super(1);
    }

    public final String invoke(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        if (ArraysKt.contains((T[]) DumpHandlerKt.PRIORITY_OPTIONS, str)) {
            return str;
        }
        throw new IllegalArgumentException();
    }
}
