package com.android.p019wm.shell.legacysplitscreen;

import android.graphics.Region;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.os.Binder;
import android.view.View;
import android.view.WindowManager;
import com.android.p019wm.shell.common.SystemWindows;

/* renamed from: com.android.wm.shell.legacysplitscreen.DividerWindowManager */
final class DividerWindowManager {
    private static final String WINDOW_TITLE = "DockedStackDivider";
    private WindowManager.LayoutParams mLp;
    final SystemWindows mSystemWindows;
    private View mView;

    DividerWindowManager(SystemWindows systemWindows) {
        this.mSystemWindows = systemWindows;
    }

    /* access modifiers changed from: package-private */
    public void add(View view, int i, int i2, int i3) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i2, 2034, 545521704, -3);
        this.mLp = layoutParams;
        layoutParams.token = new Binder();
        this.mLp.setTitle(WINDOW_TITLE);
        this.mLp.privateFlags |= 536870976;
        this.mLp.layoutInDisplayCutoutMode = 3;
        view.setSystemUiVisibility(1792);
        this.mSystemWindows.addView(view, this.mLp, i3, 0);
        this.mView = view;
    }

    /* access modifiers changed from: package-private */
    public void remove() {
        View view = this.mView;
        if (view != null) {
            this.mSystemWindows.removeView(view);
        }
        this.mView = null;
    }

    /* access modifiers changed from: package-private */
    public void setSlippery(boolean z) {
        boolean z2 = true;
        if (z && (this.mLp.flags & NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE) == 0) {
            WindowManager.LayoutParams layoutParams = this.mLp;
            layoutParams.flags = 536870912 | layoutParams.flags;
        } else if (z || (this.mLp.flags & NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE) == 0) {
            z2 = false;
        } else {
            this.mLp.flags &= -536870913;
        }
        if (z2) {
            this.mSystemWindows.updateViewLayout(this.mView, this.mLp);
        }
    }

    /* access modifiers changed from: package-private */
    public void setTouchable(boolean z) {
        if (this.mView != null) {
            boolean z2 = true;
            if (!z && (this.mLp.flags & 16) == 0) {
                this.mLp.flags |= 16;
            } else if (!z || (this.mLp.flags & 16) == 0) {
                z2 = false;
            } else {
                this.mLp.flags &= -17;
            }
            if (z2) {
                this.mSystemWindows.updateViewLayout(this.mView, this.mLp);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setTouchRegion(Region region) {
        View view = this.mView;
        if (view != null) {
            this.mSystemWindows.setTouchableRegion(view, region);
        }
    }
}
