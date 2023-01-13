package com.android.systemui.statusbar.phone;

import dalvik.bytecode.Opcodes;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/phone/NPVCDownEventState;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NPVCDownEventState.kt */
final class NPVCDownEventState$Buffer$buffer$1 extends Lambda implements Function0<NPVCDownEventState> {
    public static final NPVCDownEventState$Buffer$buffer$1 INSTANCE = new NPVCDownEventState$Buffer$buffer$1();

    NPVCDownEventState$Buffer$buffer$1() {
        super(0);
    }

    public final NPVCDownEventState invoke() {
        return new NPVCDownEventState(0, 0.0f, 0.0f, false, false, false, false, false, false, false, false, Opcodes.OP_IGET_WIDE_JUMBO, (DefaultConstructorMarker) null);
    }
}
