package com.android.systemui;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import com.android.systemui.util.leak.RotationUtils;
/* loaded from: classes.dex */
public abstract class MultiListLayout extends LinearLayout {
    protected MultiListAdapter mAdapter;
    protected int mRotation;
    protected RotationListener mRotationListener;

    /* loaded from: classes.dex */
    public interface RotationListener {
        void onRotate(int i, int i2);
    }

    public abstract float getAnimationOffsetX();

    /* renamed from: getListView */
    protected abstract ViewGroup mo610getListView();

    protected abstract ViewGroup getSeparatedView();

    public MultiListLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRotation = RotationUtils.getRotation(context);
    }

    public void setListViewAccessibilityDelegate(View.AccessibilityDelegate accessibilityDelegate) {
        mo610getListView().setAccessibilityDelegate(accessibilityDelegate);
    }

    protected void setSeparatedViewVisibility(boolean z) {
        ViewGroup separatedView = getSeparatedView();
        if (separatedView != null) {
            separatedView.setVisibility(z ? 0 : 8);
        }
    }

    public void setAdapter(MultiListAdapter multiListAdapter) {
        this.mAdapter = multiListAdapter;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int rotation = RotationUtils.getRotation(((LinearLayout) this).mContext);
        int i = this.mRotation;
        if (rotation != i) {
            rotate(i, rotation);
            this.mRotation = rotation;
        }
    }

    protected void rotate(int i, int i2) {
        RotationListener rotationListener = this.mRotationListener;
        if (rotationListener != null) {
            rotationListener.onRotate(i, i2);
        }
    }

    public void updateList() {
        if (this.mAdapter == null) {
            throw new IllegalStateException("mAdapter must be set before calling updateList");
        }
        onUpdateList();
    }

    protected void removeAllSeparatedViews() {
        ViewGroup separatedView = getSeparatedView();
        if (separatedView != null) {
            separatedView.removeAllViews();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeAllListViews() {
        ViewGroup mo610getListView = mo610getListView();
        if (mo610getListView != null) {
            mo610getListView.removeAllViews();
        }
    }

    protected void removeAllItems() {
        removeAllListViews();
        removeAllSeparatedViews();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onUpdateList() {
        removeAllItems();
        setSeparatedViewVisibility(this.mAdapter.hasSeparatedItems());
    }

    public void setRotationListener(RotationListener rotationListener) {
        this.mRotationListener = rotationListener;
    }

    /* loaded from: classes.dex */
    public static abstract class MultiListAdapter extends BaseAdapter {
        public abstract int countListItems();

        public abstract int countSeparatedItems();

        public abstract boolean shouldBeSeparated(int i);

        public boolean hasSeparatedItems() {
            return countSeparatedItems() > 0;
        }
    }
}
