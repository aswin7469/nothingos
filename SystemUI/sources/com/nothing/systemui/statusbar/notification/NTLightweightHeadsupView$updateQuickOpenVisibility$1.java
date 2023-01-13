package com.nothing.systemui.statusbar.notification;

import com.android.systemui.C1894R;
import com.nothing.systemui.NTDependencyEx;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/nothing/systemui/statusbar/notification/NTLightweightHeadsupView$updateQuickOpenVisibility$1", "Ljava/lang/Runnable;", "run", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NTLightweightHeadsupView.kt */
public final class NTLightweightHeadsupView$updateQuickOpenVisibility$1 implements Runnable {
    final /* synthetic */ NTLightweightHeadsupView this$0;

    NTLightweightHeadsupView$updateQuickOpenVisibility$1(NTLightweightHeadsupView nTLightweightHeadsupView) {
        this.this$0 = nTLightweightHeadsupView;
    }

    public void run() {
        if (!((NTLightweightHeadsupManager) NTDependencyEx.get(NTLightweightHeadsupManager.class)).isForceQuickReply()) {
            NTLightweightHeadsupView nTLightweightHeadsupView = this.this$0;
            nTLightweightHeadsupView.setMessagePaddingEnd(nTLightweightHeadsupView.getMessagePaddingEnd() - this.this$0.getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_button_width));
        }
    }
}
