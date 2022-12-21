package com.android.systemui.p012qs;

import java.util.List;
import java.util.function.Predicate;

/* renamed from: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda7 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda7 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ QSTileHost$$ExternalSyntheticLambda7(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return ((List) obj).remove((Object) this.f$0);
    }
}
