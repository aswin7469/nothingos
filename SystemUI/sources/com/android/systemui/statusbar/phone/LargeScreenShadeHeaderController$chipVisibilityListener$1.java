package com.android.systemui.statusbar.phone;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.ChipVisibilityListener;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/statusbar/phone/LargeScreenShadeHeaderController$chipVisibilityListener$1", "Lcom/android/systemui/qs/ChipVisibilityListener;", "onChipVisibilityRefreshed", "", "visible", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LargeScreenShadeHeaderController.kt */
public final class LargeScreenShadeHeaderController$chipVisibilityListener$1 implements ChipVisibilityListener {
    final /* synthetic */ LargeScreenShadeHeaderController this$0;

    LargeScreenShadeHeaderController$chipVisibilityListener$1(LargeScreenShadeHeaderController largeScreenShadeHeaderController) {
        this.this$0 = largeScreenShadeHeaderController;
    }

    public void onChipVisibilityRefreshed(boolean z) {
        if (this.this$0.header instanceof MotionLayout) {
            ConstraintSet constraintSet = ((MotionLayout) this.this$0.header).getConstraintSet(LargeScreenShadeHeaderController.QQS_HEADER_CONSTRAINT);
            float f = 0.0f;
            constraintSet.setAlpha(C1894R.C1898id.statusIcons, z ? 0.0f : 1.0f);
            if (!z) {
                f = 1.0f;
            }
            constraintSet.setAlpha(C1894R.C1898id.batteryRemainingIcon, f);
            ((MotionLayout) this.this$0.header).updateState(LargeScreenShadeHeaderController.QQS_HEADER_CONSTRAINT, constraintSet);
        }
    }
}
