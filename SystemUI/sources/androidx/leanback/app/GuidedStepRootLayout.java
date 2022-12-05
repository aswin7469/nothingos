package androidx.leanback.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.leanback.widget.Util;
/* loaded from: classes.dex */
class GuidedStepRootLayout extends LinearLayout {
    private boolean mFocusOutStart = false;
    private boolean mFocusOutEnd = false;

    public GuidedStepRootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuidedStepRootLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public View focusSearch(View focused, int direction) {
        View focusSearch = super.focusSearch(focused, direction);
        if ((direction == 17 || direction == 66) && !Util.isDescendant(this, focusSearch)) {
            if (getLayoutDirection() != 0 ? direction == 66 : direction == 17) {
                if (!this.mFocusOutStart) {
                    return focused;
                }
            } else if (!this.mFocusOutEnd) {
                return focused;
            }
            return focusSearch;
        }
        return focusSearch;
    }
}
