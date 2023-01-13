package com.android.systemui.statusbar.notification.collection.listbuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PipelineState {
    public static final int STATE_BUILD_STARTED = 1;
    public static final int STATE_FINALIZE_FILTERING = 8;
    public static final int STATE_FINALIZING = 9;
    public static final int STATE_GROUPING = 4;
    public static final int STATE_GROUP_STABILIZING = 6;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PRE_GROUP_FILTERING = 3;
    public static final int STATE_RESETTING = 2;
    public static final int STATE_SORTING = 7;
    public static final int STATE_TRANSFORMING = 5;
    private int mState = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface StateName {
    }

    /* renamed from: is */
    public boolean mo40420is(int i) {
        return i == this.mState;
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public void incrementTo(int i) {
        if (this.mState == i - 1) {
            this.mState = i;
            return;
        }
        throw new IllegalStateException("Cannot increment from state " + this.mState + " to state " + i);
    }

    public void requireState(int i) {
        if (i != this.mState) {
            throw new IllegalStateException("Required state is <" + i + " but actual state is " + this.mState);
        }
    }

    public void requireIsBefore(int i) {
        if (this.mState >= i) {
            throw new IllegalStateException("Required state is <" + i + " but actual state is " + this.mState);
        }
    }

    public String getStateName() {
        return getStateName(this.mState);
    }

    public static String getStateName(int i) {
        switch (i) {
            case 0:
                return "STATE_IDLE";
            case 1:
                return "STATE_BUILD_STARTED";
            case 2:
                return "STATE_RESETTING";
            case 3:
                return "STATE_PRE_GROUP_FILTERING";
            case 4:
                return "STATE_GROUPING";
            case 5:
                return "STATE_TRANSFORMING";
            case 6:
                return "STATE_GROUP_STABILIZING";
            case 7:
                return "STATE_SORTING";
            case 8:
                return "STATE_FINALIZE_FILTERING";
            case 9:
                return "STATE_FINALIZING";
            default:
                return "STATE:" + i;
        }
    }
}
