package com.android.p019wm.shell.back;

import com.android.p019wm.shell.common.annotations.ExternalThread;

@ExternalThread
/* renamed from: com.android.wm.shell.back.BackAnimation */
public interface BackAnimation {
    IBackAnimation createExternalInterface() {
        return null;
    }

    void onBackMotion(float f, float f2, int i, int i2);

    void setSwipeThresholds(float f, float f2);

    void setTriggerBack(boolean z);
}
