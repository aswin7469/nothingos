package com.android.systemui.statusbar.policy;

import android.view.View;
/* loaded from: classes2.dex */
public interface ScrollAdapter {
    View getHostView();

    boolean isScrolledToBottom();

    boolean isScrolledToTop();
}