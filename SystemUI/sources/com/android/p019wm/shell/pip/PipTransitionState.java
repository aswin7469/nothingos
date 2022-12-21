package com.android.p019wm.shell.pip;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* renamed from: com.android.wm.shell.pip.PipTransitionState */
public class PipTransitionState {
    public static final int ENTERED_PIP = 4;
    public static final int ENTERING_PIP = 3;
    public static final int ENTRY_SCHEDULED = 2;
    public static final int EXITING_PIP = 5;
    public static final int TASK_APPEARED = 1;
    public static final int UNDEFINED = 0;
    private boolean mInSwipePipToHomeTransition;
    private int mState = 0;

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: com.android.wm.shell.pip.PipTransitionState$TransitionState */
    public @interface TransitionState {
    }

    public void setTransitionState(int i) {
        this.mState = i;
    }

    public int getTransitionState() {
        return this.mState;
    }

    public boolean isInPip() {
        int i = this.mState;
        return i >= 1 && i != 5;
    }

    public void setInSwipePipToHomeTransition(boolean z) {
        this.mInSwipePipToHomeTransition = z;
    }

    public boolean getInSwipePipToHomeTransition() {
        return this.mInSwipePipToHomeTransition;
    }

    public boolean shouldBlockResizeRequest() {
        int i = this.mState;
        return i < 3 || i == 5;
    }
}
