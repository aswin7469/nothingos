package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.nothingos.utils.SystemUIDebugConfig;
/* loaded from: classes.dex */
public abstract class PanelBar extends FrameLayout {
    public static final String TAG = PanelBar.class.getSimpleName();
    private boolean mBouncerShowing;
    private boolean mExpanded;
    PanelViewController mPanel;
    protected float mPanelFraction;
    private int mState = 0;
    private boolean mTracking;

    public void onClosingFinished() {
    }

    public void onExpandingFinished() {
    }

    public void onPanelCollapsed() {
    }

    public void onPanelFullyOpened() {
    }

    public void onPanelPeeked() {
    }

    public boolean panelEnabled() {
        return true;
    }

    public static final void LOG(String str, Object... objArr) {
        if (!SystemUIDebugConfig.DEBUG_PANEL) {
            return;
        }
        Log.v(TAG, String.format(str, objArr));
    }

    public void go(int i) {
        boolean z = false;
        if (SystemUIDebugConfig.DEBUG_PANEL) {
            LOG("go state: %d -> %d", Integer.valueOf(this.mState), Integer.valueOf(i));
        }
        this.mState = i;
        PanelViewController panelViewController = this.mPanel;
        if (panelViewController != null) {
            if (i == 1) {
                z = true;
            }
            panelViewController.setIsShadeOpening(z);
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("panel_bar_super_parcelable", super.onSaveInstanceState());
        bundle.putInt("state", this.mState);
        return bundle;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !(parcelable instanceof Bundle)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable("panel_bar_super_parcelable"));
        if (!bundle.containsKey("state")) {
            return;
        }
        go(bundle.getInt("state", 0));
    }

    public PanelBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setPanel(PanelViewController panelViewController) {
        this.mPanel = panelViewController;
        panelViewController.setBar(this);
    }

    public void setBouncerShowing(boolean z) {
        this.mBouncerShowing = z;
        int i = z ? 4 : 0;
        setImportantForAccessibility(i);
        updateVisibility();
        PanelViewController panelViewController = this.mPanel;
        if (panelViewController != null) {
            panelViewController.getView().setImportantForAccessibility(i);
        }
    }

    public float getExpansionFraction() {
        return this.mPanelFraction;
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateVisibility() {
        this.mPanel.getView().setVisibility(shouldPanelBeVisible() ? 0 : 4);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean shouldPanelBeVisible() {
        return this.mExpanded || this.mBouncerShowing;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!panelEnabled()) {
            if (motionEvent.getAction() == 0) {
                Log.v(TAG, String.format("onTouch: all panels disabled, ignoring touch at (%d,%d)", Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
            }
            return false;
        }
        if (motionEvent.getAction() == 0) {
            PanelViewController panelViewController = this.mPanel;
            if (panelViewController == null) {
                Log.v(TAG, String.format("onTouch: no panel for touch at (%d,%d)", Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
                return true;
            }
            boolean isEnabled = panelViewController.isEnabled();
            if (SystemUIDebugConfig.DEBUG_PANEL) {
                Object[] objArr = new Object[3];
                objArr[0] = Integer.valueOf(this.mState);
                objArr[1] = panelViewController;
                objArr[2] = isEnabled ? "" : " (disabled)";
                LOG("PanelBar.onTouch: state=%d ACTION_DOWN: panel %s %s", objArr);
            }
            if (!isEnabled) {
                Log.v(TAG, String.format("onTouch: panel (%s) is disabled, ignoring touch at (%d,%d)", panelViewController, Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
                return true;
            }
        }
        PanelViewController panelViewController2 = this.mPanel;
        return panelViewController2 == null || panelViewController2.getView().dispatchTouchEvent(motionEvent);
    }

    public void onPanelMinFractionChanged(float f) {
        this.mPanel.setMinFraction(f);
    }

    public void panelExpansionChanged(float f, boolean z) {
        boolean z2;
        boolean z3;
        if (Float.isNaN(f)) {
            throw new IllegalArgumentException("frac cannot be NaN");
        }
        if (SystemUIDebugConfig.DEBUG_PANEL) {
            LOG("panelExpansionChanged: start state=%d", Integer.valueOf(this.mState));
        }
        PanelViewController panelViewController = this.mPanel;
        this.mExpanded = z;
        this.mPanelFraction = f;
        updateVisibility();
        if (z) {
            if (this.mState == 0) {
                go(1);
                onPanelPeeked();
            }
            float expandedFraction = panelViewController.getExpandedFraction();
            if (SystemUIDebugConfig.DEBUG_PANEL) {
                LOG("panelExpansionChanged:  -> %s: f=%.1f", panelViewController.getName(), Float.valueOf(expandedFraction));
            }
            z3 = expandedFraction >= 1.0f;
            z2 = false;
        } else {
            z2 = true;
            z3 = false;
        }
        if (z3 && !this.mTracking) {
            go(2);
            onPanelFullyOpened();
        } else if (z2 && !this.mTracking && this.mState != 0) {
            go(0);
            onPanelCollapsed();
        }
        if (!SystemUIDebugConfig.DEBUG_PANEL) {
            return;
        }
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(this.mState);
        String str = "";
        objArr[1] = z3 ? " fullyOpened" : str;
        if (z2) {
            str = " fullyClosed";
        }
        objArr[2] = str;
        LOG("panelExpansionChanged: end state=%d [%s%s ]", objArr);
    }

    public void collapsePanel(boolean z, boolean z2, float f) {
        boolean z3;
        PanelViewController panelViewController = this.mPanel;
        if (z && !panelViewController.isFullyCollapsed()) {
            panelViewController.collapse(z2, f);
            z3 = true;
        } else {
            panelViewController.resetViews(false);
            panelViewController.setExpandedFraction(0.0f);
            z3 = false;
        }
        if (z3 || this.mState == 0) {
            return;
        }
        go(0);
        onPanelCollapsed();
    }

    public boolean isClosed() {
        return this.mState == 0;
    }

    public void onTrackingStarted() {
        this.mTracking = true;
    }

    public void onTrackingStopped(boolean z) {
        this.mTracking = false;
    }
}
