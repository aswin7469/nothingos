package com.android.systemui.p012qs;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/* renamed from: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda9 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda9 implements Predicate {
    public final /* synthetic */ Collection f$0;

    public /* synthetic */ QSTileHost$$ExternalSyntheticLambda9(Collection collection) {
        this.f$0 = collection;
    }

    public final boolean test(Object obj) {
        return ((List) obj).removeAll(this.f$0);
    }
}
