package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
/* loaded from: classes.dex */
class GuidedActionItemContainer extends NonOverlappingLinearLayoutWithForeground {
    private boolean mFocusOutAllowed;

    public GuidedActionItemContainer(Context context) {
        this(context, null);
    }

    public GuidedActionItemContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuidedActionItemContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mFocusOutAllowed = true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public View focusSearch(View focused, int direction) {
        if (this.mFocusOutAllowed || !Util.isDescendant(this, focused)) {
            return super.focusSearch(focused, direction);
        }
        View focusSearch = super.focusSearch(focused, direction);
        if (!Util.isDescendant(this, focusSearch)) {
            return null;
        }
        return focusSearch;
    }
}
