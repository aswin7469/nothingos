package androidx.slice.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.widget.SliceActionView;
import androidx.slice.widget.SliceView;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class SliceChildView extends FrameLayout {
    protected int mInsetBottom;
    protected int mInsetEnd;
    protected int mInsetStart;
    protected int mInsetTop;
    protected long mLastUpdated;
    protected SliceActionView.SliceActionLoadingListener mLoadingListener;
    protected SliceView.OnSliceActionListener mObserver;
    protected RowStyle mRowStyle;
    protected boolean mShowLastUpdated;
    protected SliceStyle mSliceStyle;
    protected int mTintColor;
    protected SliceViewPolicy mViewPolicy;

    public abstract void resetView();

    public void setActionLoading(SliceItem item) {
    }

    public void setAllowTwoLines(boolean allowTwoLines) {
    }

    public void setLoadingActions(Set<SliceItem> loadingActions) {
    }

    public void setSliceActions(List<SliceAction> actions) {
    }

    public void setSliceContent(ListContent content) {
    }

    public void setSliceItem(SliceContent slice, boolean isHeader, int rowIndex, int rowCount, SliceView.OnSliceActionListener observer) {
    }

    public SliceChildView(Context context) {
        super(context);
        this.mTintColor = -1;
        this.mLastUpdated = -1L;
    }

    public SliceChildView(Context context, AttributeSet attributeSet) {
        this(context);
    }

    public void setInsets(int l, int t, int r, int b) {
        this.mInsetStart = l;
        this.mInsetTop = t;
        this.mInsetEnd = r;
        this.mInsetBottom = b;
    }

    public int getMode() {
        SliceViewPolicy sliceViewPolicy = this.mViewPolicy;
        if (sliceViewPolicy != null) {
            return sliceViewPolicy.getMode();
        }
        return 2;
    }

    public void setTint(int tintColor) {
        this.mTintColor = tintColor;
    }

    public void setShowLastUpdated(boolean showLastUpdated) {
        this.mShowLastUpdated = showLastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }

    public void setSliceActionListener(SliceView.OnSliceActionListener observer) {
        this.mObserver = observer;
    }

    public void setSliceActionLoadingListener(SliceActionView.SliceActionLoadingListener listener) {
        this.mLoadingListener = listener;
    }

    public void setStyle(SliceStyle styles, RowStyle rowStyle) {
        this.mSliceStyle = styles;
        this.mRowStyle = rowStyle;
    }

    public void setPolicy(SliceViewPolicy policy) {
        this.mViewPolicy = policy;
    }
}
