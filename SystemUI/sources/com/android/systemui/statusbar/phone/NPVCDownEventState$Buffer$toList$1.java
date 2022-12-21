package com.android.systemui.statusbar.phone;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\n¢\u0006\u0002\b\u0005"}, mo64987d2 = {"<anonymous>", "", "", "it", "Lcom/android/systemui/statusbar/phone/NPVCDownEventState;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NPVCDownEventState.kt */
final class NPVCDownEventState$Buffer$toList$1 extends Lambda implements Function1<NPVCDownEventState, List<? extends String>> {
    public static final NPVCDownEventState$Buffer$toList$1 INSTANCE = new NPVCDownEventState$Buffer$toList$1();

    NPVCDownEventState$Buffer$toList$1() {
        super(1);
    }

    public final List<String> invoke(NPVCDownEventState nPVCDownEventState) {
        Intrinsics.checkNotNullParameter(nPVCDownEventState, "it");
        return nPVCDownEventState.getAsStringList();
    }
}
