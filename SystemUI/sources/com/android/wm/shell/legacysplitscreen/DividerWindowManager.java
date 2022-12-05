package com.android.wm.shell.legacysplitscreen;

import android.graphics.Region;
import android.os.Binder;
import android.view.View;
import android.view.WindowManager;
import com.android.wm.shell.common.SystemWindows;
/* loaded from: classes2.dex */
final class DividerWindowManager {
    private WindowManager.LayoutParams mLp;
    final SystemWindows mSystemWindows;
    private View mView;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DividerWindowManager(SystemWindows systemWindows) {
        this.mSystemWindows = systemWindows;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(View view, int i, int i2, int i3) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i2, 2034, 545521704, -3);
        this.mLp = layoutParams;
        layoutParams.token = new Binder();
        this.mLp.setTitle("DockedStackDivider");
        WindowManager.LayoutParams layoutParams2 = this.mLp;
        layoutParams2.privateFlags |= 536870976;
        layoutParams2.layoutInDisplayCutoutMode = 3;
        view.setSystemUiVisibility(1792);
        this.mSystemWindows.addView(view, this.mLp, i3, 0);
        this.mView = view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove() {
        View view = this.mView;
        if (view != null) {
            this.mSystemWindows.removeView(view);
        }
        this.mView = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setSlippery(boolean z) {
        boolean z2 = true;
        if (z) {
            WindowManager.LayoutParams layoutParams = this.mLp;
            int i = layoutParams.flags;
            if ((i & 536870912) == 0) {
                layoutParams.flags = i | 536870912;
                if (!z2) {
                    return;
                }
                this.mSystemWindows.updateViewLayout(this.mView, this.mLp);
                return;
            }
        }
        if (!z) {
            WindowManager.LayoutParams layoutParams2 = this.mLp;
            int i2 = layoutParams2.flags;
            if ((536870912 & i2) != 0) {
                layoutParams2.flags = (-536870913) & i2;
                if (!z2) {
                }
            }
        }
        z2 = false;
        if (!z2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0028  */
    /* JADX WARN: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setTouchable(boolean z) {
        View view = this.mView;
        if (view == null) {
            return;
        }
        boolean z2 = false;
        if (!z) {
            WindowManager.LayoutParams layoutParams = this.mLp;
            int i = layoutParams.flags;
            if ((i & 16) == 0) {
                layoutParams.flags = i | 16;
                z2 = true;
                if (z2) {
                    return;
                }
                this.mSystemWindows.updateViewLayout(view, this.mLp);
                return;
            }
        }
        if (z) {
            WindowManager.LayoutParams layoutParams2 = this.mLp;
            int i2 = layoutParams2.flags;
            if ((i2 & 16) != 0) {
                layoutParams2.flags = i2 & (-17);
                z2 = true;
            }
        }
        if (z2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTouchRegion(Region region) {
        View view = this.mView;
        if (view == null) {
            return;
        }
        this.mSystemWindows.setTouchableRegion(view, region);
    }
}
