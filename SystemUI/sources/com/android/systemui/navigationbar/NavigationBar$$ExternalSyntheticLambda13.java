package com.android.systemui.navigationbar;

import android.view.MotionEvent;
import android.view.View;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda13 implements View.OnTouchListener {
    public final /* synthetic */ NavigationBar f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda13(NavigationBar navigationBar) {
        this.f$0 = navigationBar;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return this.f$0.onNavigationTouch(view, motionEvent);
    }
}
