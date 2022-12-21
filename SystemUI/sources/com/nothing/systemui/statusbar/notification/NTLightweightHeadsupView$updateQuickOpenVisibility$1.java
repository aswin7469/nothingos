package com.nothing.systemui.statusbar.notification;

import com.android.systemui.C1893R;
import com.nothing.systemui.NTDependencyEx;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/nothing/systemui/statusbar/notification/NTLightweightHeadsupView$updateQuickOpenVisibility$1", "Ljava/lang/Runnable;", "run", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTLightweightHeadsupView.kt */
public final class NTLightweightHeadsupView$updateQuickOpenVisibility$1 implements Runnable {
    final /* synthetic */ NTLightweightHeadsupView this$0;

    NTLightweightHeadsupView$updateQuickOpenVisibility$1(NTLightweightHeadsupView nTLightweightHeadsupView) {
        this.this$0 = nTLightweightHeadsupView;
    }

    public void run() {
        if (!((NTLightweightHeadsupManager) NTDependencyEx.get(NTLightweightHeadsupManager.class)).isForceQuickReply()) {
            NTLightweightHeadsupView nTLightweightHeadsupView = this.this$0;
            nTLightweightHeadsupView.setMessagePaddingEnd(nTLightweightHeadsupView.getMessagePaddingEnd() - this.this$0.getResources().getDimensionPixelSize(C1893R.dimen.nt_pop_view_button_width));
        }
    }
}
