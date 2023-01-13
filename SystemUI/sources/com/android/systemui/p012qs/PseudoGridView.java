package com.android.systemui.p012qs;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.BaseAdapter;
import com.android.systemui.C1894R;
import java.lang.ref.WeakReference;

/* renamed from: com.android.systemui.qs.PseudoGridView */
public class PseudoGridView extends ViewGroup {
    private int mFixedChildWidth = -1;
    private int mHorizontalSpacing;
    private int mNumColumns = 3;
    private int mVerticalSpacing;

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public PseudoGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1894R.styleable.PseudoGridView);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            if (index == 2) {
                this.mNumColumns = obtainStyledAttributes.getInt(index, 3);
            } else if (index == 3) {
                this.mVerticalSpacing = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == 1) {
                this.mHorizontalSpacing = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == 0) {
                this.mFixedChildWidth = obtainStyledAttributes.getDimensionPixelSize(index, -1);
            }
        }
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (View.MeasureSpec.getMode(i) != 0) {
            int size = View.MeasureSpec.getSize(i);
            int i3 = this.mFixedChildWidth;
            int i4 = this.mNumColumns;
            int i5 = this.mHorizontalSpacing;
            int i6 = (i3 * i4) + ((i4 - 1) * i5);
            if (i3 == -1 || i6 > size) {
                i3 = (size - ((i4 - 1) * i5)) / i4;
            } else {
                size = (i3 * i4) + (i5 * (i4 - 1));
            }
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
            int childCount = getChildCount();
            int i7 = this.mNumColumns;
            int i8 = ((childCount + i7) - 1) / i7;
            int i9 = 0;
            for (int i10 = 0; i10 < i8; i10++) {
                int i11 = this.mNumColumns;
                int i12 = i10 * i11;
                int min = Math.min(i11 + i12, childCount);
                int i13 = 0;
                for (int i14 = i12; i14 < min; i14++) {
                    View childAt = getChildAt(i14);
                    childAt.measure(makeMeasureSpec, 0);
                    i13 = Math.max(i13, childAt.getMeasuredHeight());
                }
                int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i13, 1073741824);
                while (i12 < min) {
                    View childAt2 = getChildAt(i12);
                    if (childAt2.getMeasuredHeight() != i13) {
                        childAt2.measure(makeMeasureSpec, makeMeasureSpec2);
                    }
                    i12++;
                }
                i9 += i13;
                if (i10 > 0) {
                    i9 += this.mVerticalSpacing;
                }
            }
            setMeasuredDimension(size, resolveSizeAndState(i9, i2, 0));
            return;
        }
        throw new UnsupportedOperationException("Needs a maximum width");
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        boolean isLayoutRtl = isLayoutRtl();
        int childCount = getChildCount();
        int i6 = this.mNumColumns;
        int i7 = ((childCount + i6) - 1) / i6;
        int i8 = 0;
        for (int i9 = 0; i9 < i7; i9++) {
            int width = isLayoutRtl ? getWidth() : 0;
            int i10 = this.mNumColumns;
            int i11 = i9 * i10;
            int min = Math.min(i10 + i11, childCount);
            int i12 = 0;
            while (i11 < min) {
                View childAt = getChildAt(i11);
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (isLayoutRtl) {
                    width -= measuredWidth;
                }
                childAt.layout(width, i8, width + measuredWidth, i8 + measuredHeight);
                i12 = Math.max(i12, measuredHeight);
                if (isLayoutRtl) {
                    i5 = width - this.mHorizontalSpacing;
                } else {
                    i5 = width + measuredWidth + this.mHorizontalSpacing;
                }
                i11++;
            }
            i8 += i12 + this.mVerticalSpacing;
        }
    }

    /* renamed from: com.android.systemui.qs.PseudoGridView$ViewGroupAdapterBridge */
    public static class ViewGroupAdapterBridge extends DataSetObserver {
        private final BaseAdapter mAdapter;
        private boolean mReleased = false;
        private final WeakReference<ViewGroup> mViewGroup;

        public static void link(ViewGroup viewGroup, BaseAdapter baseAdapter) {
            new ViewGroupAdapterBridge(viewGroup, baseAdapter);
        }

        private ViewGroupAdapterBridge(ViewGroup viewGroup, BaseAdapter baseAdapter) {
            this.mViewGroup = new WeakReference<>(viewGroup);
            this.mAdapter = baseAdapter;
            baseAdapter.registerDataSetObserver(this);
            refresh();
        }

        private void refresh() {
            if (!this.mReleased) {
                ViewGroup viewGroup = this.mViewGroup.get();
                if (viewGroup == null) {
                    release();
                    return;
                }
                int childCount = viewGroup.getChildCount();
                int count = this.mAdapter.getCount();
                int max = Math.max(childCount, count);
                int i = 0;
                while (i < max) {
                    if (i < count) {
                        View childAt = i < childCount ? viewGroup.getChildAt(i) : null;
                        View view = this.mAdapter.getView(i, childAt, viewGroup);
                        if (childAt == null) {
                            viewGroup.addView(view);
                        } else if (childAt != view) {
                            viewGroup.removeViewAt(i);
                            viewGroup.addView(view, i);
                        }
                    } else {
                        viewGroup.removeViewAt(viewGroup.getChildCount() - 1);
                    }
                    i++;
                }
            }
        }

        public void onChanged() {
            refresh();
        }

        public void onInvalidated() {
            release();
        }

        private void release() {
            if (!this.mReleased) {
                this.mReleased = true;
                this.mAdapter.unregisterDataSetObserver(this);
            }
        }
    }
}
