package com.android.p019wm.shell.bubbles;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.view.View;

/* renamed from: com.android.wm.shell.bubbles.BubbleViewProvider */
public interface BubbleViewProvider {
    Bitmap getAppBadge();

    Bitmap getBubbleIcon();

    int getDotColor();

    Path getDotPath();

    BubbleExpandedView getExpandedView();

    View getIconView();

    String getKey();

    Bitmap getRawAppBadge();

    int getTaskId();

    void setTaskViewVisibility(boolean z);

    boolean showDot();
}
