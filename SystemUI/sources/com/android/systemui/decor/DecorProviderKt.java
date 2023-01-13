package com.android.systemui.decor;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u001a0\u0010\u0000\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u00022\u0006\u0010\u0004\u001a\u00020\u0005¨\u0006\u0006"}, mo65043d2 = {"partitionAlignedBound", "Lkotlin/Pair;", "", "Lcom/android/systemui/decor/DecorProvider;", "alignedBound", "", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DecorProvider.kt */
public final class DecorProviderKt {
    public static final Pair<List<DecorProvider>, List<DecorProvider>> partitionAlignedBound(List<? extends DecorProvider> list, int i) {
        Intrinsics.checkNotNullParameter(list, "<this>");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Object next : list) {
            if (((DecorProvider) next).getAlignedBounds().contains(Integer.valueOf(i))) {
                arrayList.add(next);
            } else {
                arrayList2.add(next);
            }
        }
        return new Pair<>(arrayList, arrayList2);
    }
}
