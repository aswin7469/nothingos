package com.android.systemui.appops;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public class AppOpItem {
    private int mCode;
    private boolean mIsDisabled;
    private String mPackageName;
    private StringBuilder mState;
    private long mTimeStartedElapsed;
    private int mUid;

    public AppOpItem(int i, int i2, String str, long j) {
        this.mCode = i;
        this.mUid = i2;
        this.mPackageName = str;
        this.mTimeStartedElapsed = j;
        this.mState = new StringBuilder().append("AppOpItem(Op code=").append(i).append(", UID=").append(i2).append(", Package name=").append(str).append(", Paused=");
    }

    public int getCode() {
        return this.mCode;
    }

    public int getUid() {
        return this.mUid;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public long getTimeStartedElapsed() {
        return this.mTimeStartedElapsed;
    }

    public void setDisabled(boolean z) {
        this.mIsDisabled = z;
    }

    public boolean isDisabled() {
        return this.mIsDisabled;
    }

    public String toString() {
        return this.mState.append(this.mIsDisabled).append(NavigationBarInflaterView.KEY_CODE_END).toString();
    }
}
