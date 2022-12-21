package com.android.p019wm.shell.onehanded;

import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.android.wm.shell.onehanded.OneHandedState */
public class OneHandedState {
    public static final int STATE_ACTIVE = 2;
    public static final int STATE_ENTERING = 1;
    public static final int STATE_EXITING = 3;
    public static final int STATE_NONE = 0;
    private static final String TAG = "OneHandedState";
    private static int sCurrentState;
    private List<OnStateChangedListener> mStateChangeListeners = new ArrayList();

    /* renamed from: com.android.wm.shell.onehanded.OneHandedState$OnStateChangedListener */
    public interface OnStateChangedListener {
        void onStateChanged(int i) {
        }
    }

    public OneHandedState() {
        sCurrentState = 0;
    }

    public void addSListeners(OnStateChangedListener onStateChangedListener) {
        this.mStateChangeListeners.add(onStateChangedListener);
    }

    public int getState() {
        return sCurrentState;
    }

    public boolean isTransitioning() {
        int i = sCurrentState;
        return i == 1 || i == 3;
    }

    public boolean isInOneHanded() {
        return sCurrentState == 2;
    }

    public void setState(int i) {
        sCurrentState = i;
        if (!this.mStateChangeListeners.isEmpty()) {
            this.mStateChangeListeners.forEach(new OneHandedState$$ExternalSyntheticLambda0(i));
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(TAG);
        printWriter.println("  sCurrentState=" + sCurrentState);
    }
}
