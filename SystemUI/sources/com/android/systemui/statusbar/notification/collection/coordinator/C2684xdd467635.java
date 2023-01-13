package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u00012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\n¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, mo65043d2 = {"<anonymous>", "", "R", "it", "", "invoke", "(Ljava/lang/Object;)Ljava/lang/Boolean;", "kotlin/sequences/SequencesKt___SequencesKt$filterIsInstance$1"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.collection.coordinator.GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1 */
/* compiled from: _Sequences.kt */
public final class C2684xdd467635 extends Lambda implements Function1<Object, Boolean> {
    public static final C2684xdd467635 INSTANCE = new C2684xdd467635();

    public C2684xdd467635() {
        super(1);
    }

    public final Boolean invoke(Object obj) {
        return Boolean.valueOf(obj instanceof GroupEntry);
    }
}
