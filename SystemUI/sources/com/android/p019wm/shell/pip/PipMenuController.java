package com.android.p019wm.shell.pip;

import android.app.ActivityManager;
import android.app.RemoteAction;
import android.graphics.Rect;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.view.SurfaceControl;
import android.view.WindowManager;
import java.util.List;

/* renamed from: com.android.wm.shell.pip.PipMenuController */
public interface PipMenuController {
    public static final String MENU_WINDOW_TITLE = "PipMenuView";

    void attach(SurfaceControl surfaceControl);

    void detach();

    boolean isMenuVisible();

    void movePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
    }

    void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    void resizePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
    }

    void setAppActions(List<RemoteAction> list, RemoteAction remoteAction);

    void showMenu();

    void updateMenuBounds(Rect rect) {
    }

    WindowManager.LayoutParams getPipMenuLayoutParams(String str, int i, int i2) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i2, 2038, 545521680, -3);
        layoutParams.privateFlags |= NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE;
        layoutParams.setTitle(str);
        return layoutParams;
    }
}
