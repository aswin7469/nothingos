package com.nothing.systemui.p024qs;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/nothing/systemui/qs/QSPanelEx$runnable$1", "Ljava/lang/Runnable;", "run", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.QSPanelEx$runnable$1 */
/* compiled from: QSPanelEx.kt */
public final class QSPanelEx$runnable$1 implements Runnable {
    final /* synthetic */ QSPanelEx this$0;

    QSPanelEx$runnable$1(QSPanelEx qSPanelEx) {
        this.this$0 = qSPanelEx;
    }

    public void run() {
        CirclePagedTileLayout access$getBtTilePage$p = this.this$0.btTilePage;
        if (access$getBtTilePage$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btTilePage");
            access$getBtTilePage$p = null;
        }
        access$getBtTilePage$p.setCurrentItem(0, false);
    }
}
