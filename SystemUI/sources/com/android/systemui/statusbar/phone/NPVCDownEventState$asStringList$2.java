package com.android.systemui.statusbar.phone;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\u0010\u0000\u001a\u0010\u0012\f\u0012\n \u0003*\u0004\u0018\u00010\u00020\u00020\u0001H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "", "kotlin.jvm.PlatformType", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NPVCDownEventState.kt */
final class NPVCDownEventState$asStringList$2 extends Lambda implements Function0<List<? extends String>> {
    final /* synthetic */ NPVCDownEventState this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    NPVCDownEventState$asStringList$2(NPVCDownEventState nPVCDownEventState) {
        super(0);
        this.this$0 = nPVCDownEventState;
    }

    public final List<String> invoke() {
        return CollectionsKt.listOf(NPVCDownEventStateKt.DATE_FORMAT.format(Long.valueOf(this.this$0.timeStamp)), String.valueOf(this.this$0.f385x), String.valueOf(this.this$0.f386y), String.valueOf(this.this$0.qsTouchAboveFalsingThreshold), String.valueOf(this.this$0.dozing), String.valueOf(this.this$0.collapsed), String.valueOf(this.this$0.canCollapseOnQQS), String.valueOf(this.this$0.listenForHeadsUp), String.valueOf(this.this$0.allowExpandForSmallExpansion), String.valueOf(this.this$0.touchSlopExceededBeforeDown), String.valueOf(this.this$0.lastEventSynthesized));
    }
}
