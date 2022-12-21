package com.android.systemui.statusbar;

import android.view.InsetsVisibilities;
import android.view.View;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface SysuiStatusBarStateController extends StatusBarStateController {
    public static final int RANK_SHELF = 3;
    public static final int RANK_STACK_SCROLLER = 2;
    public static final int RANK_STATUS_BAR = 0;
    public static final int RANK_STATUS_BAR_WINDOW_CONTROLLER = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SbStateListenerRank {
    }

    @Deprecated
    void addCallback(StatusBarStateController.StateListener stateListener, int i);

    boolean fromShadeLocked();

    int getCurrentOrUpcomingState();

    float getInterpolatedDozeAmount();

    boolean goingToFullShade();

    boolean isKeyguardRequested();

    boolean leaveOpenOnKeyguardHide();

    void setAndInstrumentDozeAmount(View view, float f, boolean z);

    void setDozeAmount(float f, boolean z);

    boolean setIsDozing(boolean z);

    void setKeyguardRequested(boolean z);

    void setLeaveOpenOnKeyguardHide(boolean z);

    boolean setPanelExpanded(boolean z);

    void setPulsing(boolean z);

    boolean setState(int i, boolean z);

    void setSystemBarAttributes(int i, int i2, InsetsVisibilities insetsVisibilities, String str);

    void setUpcomingState(int i);

    boolean setState(int i) {
        return setState(i, false);
    }

    public static class RankedListener {
        final StatusBarStateController.StateListener mListener;
        /* access modifiers changed from: package-private */
        public final int mRank;

        RankedListener(StatusBarStateController.StateListener stateListener, int i) {
            this.mListener = stateListener;
            this.mRank = i;
        }
    }
}
