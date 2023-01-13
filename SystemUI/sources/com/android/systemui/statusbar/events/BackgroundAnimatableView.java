package com.android.systemui.statusbar.events;

import android.view.View;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J(\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0003H&R\u0014\u0010\u0002\u001a\u00020\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0010À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/events/BackgroundAnimatableView;", "", "chipWidth", "", "getChipWidth", "()I", "view", "Landroid/view/View;", "getView", "()Landroid/view/View;", "setBoundsForAnimation", "", "l", "t", "r", "b", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SystemEventChipAnimationController.kt */
public interface BackgroundAnimatableView {
    void setBoundsForAnimation(int i, int i2, int i3, int i4);

    View getView() {
        return (View) this;
    }

    int getChipWidth() {
        return getView().getMeasuredWidth();
    }
}
