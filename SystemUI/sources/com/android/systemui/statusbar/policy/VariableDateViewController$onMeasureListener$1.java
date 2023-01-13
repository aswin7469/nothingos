package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.VariableDateView;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/statusbar/policy/VariableDateViewController$onMeasureListener$1", "Lcom/android/systemui/statusbar/policy/VariableDateView$OnMeasureListener;", "onMeasureAction", "", "availableWidth", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: VariableDateViewController.kt */
public final class VariableDateViewController$onMeasureListener$1 implements VariableDateView.OnMeasureListener {
    final /* synthetic */ VariableDateViewController this$0;

    VariableDateViewController$onMeasureListener$1(VariableDateViewController variableDateViewController) {
        this.this$0 = variableDateViewController;
    }

    public void onMeasureAction(int i) {
        if (i != this.this$0.lastWidth) {
            this.this$0.maybeChangeFormat(i);
            this.this$0.lastWidth = i;
        }
    }
}
