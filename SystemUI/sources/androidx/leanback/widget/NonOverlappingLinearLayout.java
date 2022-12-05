package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class NonOverlappingLinearLayout extends LinearLayout {
    boolean mDeferFocusableViewAvailableInLayout;
    boolean mFocusableViewAvailableFixEnabled;
    final ArrayList<ArrayList<View>> mSortedAvailableViews;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public NonOverlappingLinearLayout(Context context) {
        this(context, null);
    }

    public NonOverlappingLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NonOverlappingLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mFocusableViewAvailableFixEnabled = false;
        this.mSortedAvailableViews = new ArrayList<>();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    /* JADX WARN: Type inference failed for: r0v6 */
    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        boolean z;
        int size;
        ?? r0 = 0;
        int i = 0;
        try {
            boolean z2 = this.mFocusableViewAvailableFixEnabled && getOrientation() == 0 && getLayoutDirection() == 1;
            this.mDeferFocusableViewAvailableInLayout = z2;
            if (z2) {
                while (this.mSortedAvailableViews.size() > getChildCount()) {
                    ArrayList<ArrayList<View>> arrayList = this.mSortedAvailableViews;
                    arrayList.remove(arrayList.size() - 1);
                }
                while (this.mSortedAvailableViews.size() < getChildCount()) {
                    this.mSortedAvailableViews.add(new ArrayList<>());
                }
            }
            super.onLayout(changed, l, t, r, b);
            if (this.mDeferFocusableViewAvailableInLayout) {
                for (int i2 = 0; i2 < this.mSortedAvailableViews.size(); i2++) {
                    for (int i3 = 0; i3 < this.mSortedAvailableViews.get(i2).size(); i3++) {
                        super.focusableViewAvailable(this.mSortedAvailableViews.get(i2).get(i3));
                    }
                }
            }
            if (!z) {
                return;
            }
            while (true) {
                if (i >= size) {
                    return;
                }
            }
        } finally {
            if (this.mDeferFocusableViewAvailableInLayout) {
                this.mDeferFocusableViewAvailableInLayout = false;
                while (r0 < this.mSortedAvailableViews.size()) {
                    this.mSortedAvailableViews.get(r0).clear();
                    r0++;
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void focusableViewAvailable(View v) {
        int i;
        if (!this.mDeferFocusableViewAvailableInLayout) {
            super.focusableViewAvailable(v);
            return;
        }
        for (View view = v; view != this && view != null; view = (View) view.getParent()) {
            if (view.getParent() == this) {
                i = indexOfChild(view);
                break;
            }
        }
        i = -1;
        if (i == -1) {
            return;
        }
        this.mSortedAvailableViews.get(i).add(v);
    }
}
