package com.android.systemui.controls.management;

import androidx.viewpager2.widget.ViewPager2;
import com.android.systemui.controls.TooltipManager;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsFavoritingActivity$bindViews$5", "Landroidx/viewpager2/widget/ViewPager2$OnPageChangeCallback;", "onPageSelected", "", "position", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$bindViews$5 extends ViewPager2.OnPageChangeCallback {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    ControlsFavoritingActivity$bindViews$5(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    public void onPageSelected(int i) {
        super.onPageSelected(i);
        TooltipManager access$getMTooltipManager$p = this.this$0.mTooltipManager;
        if (access$getMTooltipManager$p != null) {
            access$getMTooltipManager$p.hide(true);
        }
    }
}
