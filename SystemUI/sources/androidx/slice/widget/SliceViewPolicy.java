package androidx.slice.widget;
/* loaded from: classes.dex */
public class SliceViewPolicy {
    private PolicyChangeListener mListener;
    private int mMaxHeight = 0;
    private int mMaxSmallHeight = 0;
    private boolean mScrollable = true;
    private int mMode = 2;

    /* loaded from: classes.dex */
    public interface PolicyChangeListener {
        void onMaxHeightChanged(int newNewHeight);

        void onMaxSmallChanged(int newMaxSmallHeight);
    }

    public void setListener(PolicyChangeListener listener) {
        this.mListener = listener;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public int getMaxSmallHeight() {
        return this.mMaxSmallHeight;
    }

    public boolean isScrollable() {
        return this.mScrollable;
    }

    public int getMode() {
        return this.mMode;
    }

    public void setMaxHeight(int max) {
        if (max != this.mMaxHeight) {
            this.mMaxHeight = max;
            PolicyChangeListener policyChangeListener = this.mListener;
            if (policyChangeListener == null) {
                return;
            }
            policyChangeListener.onMaxHeightChanged(max);
        }
    }

    public void setMaxSmallHeight(int maxSmallHeight) {
        if (this.mMaxSmallHeight != maxSmallHeight) {
            this.mMaxSmallHeight = maxSmallHeight;
            PolicyChangeListener policyChangeListener = this.mListener;
            if (policyChangeListener == null) {
                return;
            }
            policyChangeListener.onMaxSmallChanged(maxSmallHeight);
        }
    }
}
