package com.android.systemui;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import com.android.systemui.util.leak.RotationUtils;

public abstract class MultiListLayout extends LinearLayout {
    protected MultiListAdapter mAdapter;
    protected boolean mHasOutsideTouch;
    protected int mRotation;
    protected RotationListener mRotationListener;

    public interface RotationListener {
        void onRotate(int i, int i2);
    }

    public abstract float getAnimationOffsetX();

    public abstract float getAnimationOffsetY();

    /* access modifiers changed from: protected */
    public abstract ViewGroup getListView();

    /* access modifiers changed from: protected */
    public abstract ViewGroup getSeparatedView();

    public abstract void setDivisionView(View view);

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public MultiListLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRotation = RotationUtils.getRotation(context);
    }

    public void setListViewAccessibilityDelegate(View.AccessibilityDelegate accessibilityDelegate) {
        getListView().setAccessibilityDelegate(accessibilityDelegate);
    }

    /* access modifiers changed from: protected */
    public void setSeparatedViewVisibility(boolean z) {
        ViewGroup separatedView = getSeparatedView();
        if (separatedView != null) {
            separatedView.setVisibility(z ? 0 : 8);
        }
    }

    public void setAdapter(MultiListAdapter multiListAdapter) {
        this.mAdapter = multiListAdapter;
    }

    public void setOutsideTouchListener(View.OnClickListener onClickListener) {
        this.mHasOutsideTouch = true;
        requestLayout();
        setOnClickListener(onClickListener);
        setClickable(true);
        setFocusable(true);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int rotation = RotationUtils.getRotation(this.mContext);
        int i = this.mRotation;
        if (rotation != i) {
            rotate(i, rotation);
            this.mRotation = rotation;
        }
    }

    /* access modifiers changed from: protected */
    public void rotate(int i, int i2) {
        RotationListener rotationListener = this.mRotationListener;
        if (rotationListener != null) {
            rotationListener.onRotate(i, i2);
        }
    }

    public void updateList() {
        if (this.mAdapter != null) {
            onUpdateList();
            return;
        }
        throw new IllegalStateException("mAdapter must be set before calling updateList");
    }

    /* access modifiers changed from: protected */
    public void removeAllSeparatedViews() {
        ViewGroup separatedView = getSeparatedView();
        if (separatedView != null) {
            separatedView.removeAllViews();
        }
    }

    /* access modifiers changed from: protected */
    public void removeAllListViews() {
        ViewGroup listView = getListView();
        if (listView != null) {
            listView.removeAllViews();
        }
    }

    /* access modifiers changed from: protected */
    public void removeAllItems() {
        removeAllListViews();
        removeAllSeparatedViews();
    }

    /* access modifiers changed from: protected */
    public void onUpdateList() {
        removeAllItems();
        setSeparatedViewVisibility(this.mAdapter.hasSeparatedItems());
    }

    public void setRotationListener(RotationListener rotationListener) {
        this.mRotationListener = rotationListener;
    }

    public static MultiListLayout get(View view) {
        if (view instanceof MultiListLayout) {
            return (MultiListLayout) view;
        }
        if (view.getParent() instanceof View) {
            return get((View) view.getParent());
        }
        return null;
    }

    public static abstract class MultiListAdapter extends BaseAdapter {
        public abstract int countListItems();

        public abstract int countSeparatedItems();

        public abstract void onClickItem(int i);

        public abstract boolean onLongClickItem(int i);

        public abstract boolean shouldBeSeparated(int i);

        public boolean hasSeparatedItems() {
            return countSeparatedItems() > 0;
        }
    }
}
