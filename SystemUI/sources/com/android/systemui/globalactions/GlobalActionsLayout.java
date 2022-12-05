package com.android.systemui.globalactions;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.HardwareBgDrawable;
import com.android.systemui.MultiListLayout;
import com.android.systemui.R$color;
import com.android.systemui.R$id;
import com.android.systemui.util.leak.RotationUtils;
import java.util.Locale;
/* loaded from: classes.dex */
public abstract class GlobalActionsLayout extends MultiListLayout {
    boolean mBackgroundsSet;

    protected abstract boolean shouldReverseListItems();

    public GlobalActionsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void setBackgrounds() {
        HardwareBgDrawable backgroundDrawable;
        ViewGroup mo610getListView = mo610getListView();
        HardwareBgDrawable backgroundDrawable2 = getBackgroundDrawable(getResources().getColor(R$color.global_actions_grid_background, null));
        if (backgroundDrawable2 != null) {
            mo610getListView.setBackground(backgroundDrawable2);
        }
        if (getSeparatedView() == null || (backgroundDrawable = getBackgroundDrawable(getResources().getColor(R$color.global_actions_separated_background, null))) == null) {
            return;
        }
        getSeparatedView().setBackground(backgroundDrawable);
    }

    protected HardwareBgDrawable getBackgroundDrawable(int i) {
        HardwareBgDrawable hardwareBgDrawable = new HardwareBgDrawable(true, true, getContext());
        hardwareBgDrawable.setTint(i);
        return hardwareBgDrawable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (mo610getListView() == null || this.mBackgroundsSet) {
            return;
        }
        setBackgrounds();
        this.mBackgroundsSet = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addToListView(View view, boolean z) {
        if (z) {
            mo610getListView().addView(view, 0);
        } else {
            mo610getListView().addView(view);
        }
    }

    protected void addToSeparatedView(View view, boolean z) {
        ViewGroup separatedView = getSeparatedView();
        if (separatedView == null) {
            addToListView(view, z);
        } else if (z) {
            separatedView.addView(view, 0);
        } else {
            separatedView.addView(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public int getCurrentLayoutDirection() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public int getCurrentRotation() {
        return RotationUtils.getRotation(((LinearLayout) this).mContext);
    }

    @Override // com.android.systemui.MultiListLayout
    public void onUpdateList() {
        View view;
        super.onUpdateList();
        ViewGroup separatedView = getSeparatedView();
        ViewGroup mo610getListView = mo610getListView();
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            boolean shouldBeSeparated = this.mAdapter.shouldBeSeparated(i);
            if (shouldBeSeparated) {
                view = this.mAdapter.getView(i, null, separatedView);
            } else {
                view = this.mAdapter.getView(i, null, mo610getListView);
            }
            if (shouldBeSeparated) {
                addToSeparatedView(view, false);
            } else {
                addToListView(view, shouldReverseListItems());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.MultiListLayout
    public ViewGroup getSeparatedView() {
        return (ViewGroup) findViewById(R$id.separated_button);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.MultiListLayout
    /* renamed from: getListView */
    public ViewGroup mo610getListView() {
        return (ViewGroup) findViewById(16908298);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public View getWrapper() {
        return getChildAt(0);
    }
}
