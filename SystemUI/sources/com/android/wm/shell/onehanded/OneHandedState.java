package com.android.wm.shell.onehanded;

import com.android.wm.shell.onehanded.OneHandedState;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class OneHandedState {
    private static final String TAG = "OneHandedState";
    private static int sCurrentState;
    private List<OnStateChangedListener> mStateChangeListeners = new ArrayList();

    /* loaded from: classes2.dex */
    public interface OnStateChangedListener {
        default void onStateChanged(int i) {
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

    public void setState(final int i) {
        sCurrentState = i;
        if (!this.mStateChangeListeners.isEmpty()) {
            this.mStateChangeListeners.forEach(new Consumer() { // from class: com.android.wm.shell.onehanded.OneHandedState$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((OneHandedState.OnStateChangedListener) obj).onStateChanged(i);
                }
            });
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(TAG);
        printWriter.println("  sCurrentState=" + sCurrentState);
    }
}
