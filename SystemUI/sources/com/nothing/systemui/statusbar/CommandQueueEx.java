package com.nothing.systemui.statusbar;

import android.util.ArraySet;
import com.android.systemui.statusbar.CommandQueue;
import com.nothing.systemui.util.NTLogUtil;
import javax.inject.Inject;

public class CommandQueueEx {
    private static final String TAG = "CommandQueueEx";
    private final CommandQueue mCommandQueue;
    private final ArraySet<Integer> mDisplayDisabledIgnoreSetup = new ArraySet<>();
    private final Object mLock = new Object();

    @Inject
    public CommandQueueEx(CommandQueue commandQueue) {
        this.mCommandQueue = commandQueue;
    }

    public void addIgnoreSetupTarget(int i) {
        synchronized (this.mLock) {
            this.mDisplayDisabledIgnoreSetup.add(Integer.valueOf(i));
        }
    }

    public void removeIgnoreSetupTarget(int i) {
        synchronized (this.mLock) {
            this.mDisplayDisabledIgnoreSetup.remove(Integer.valueOf(i));
        }
    }

    public void setDisableFlagsForSetup(int i, int i2, int i3) {
        synchronized (this.mLock) {
            if (this.mDisplayDisabledIgnoreSetup.contains(Integer.valueOf(i))) {
                NTLogUtil.m1686d(TAG, "ignore setDisabledFlags for setup, state1: " + i2 + ", state2: " + i3);
            } else {
                this.mCommandQueue.disable(i, i2, i3, false);
            }
        }
    }
}
