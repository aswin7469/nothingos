package com.android.p019wm.shell;

import android.os.RemoteException;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.pip.PinnedStackListenerForwarder;

/* renamed from: com.android.wm.shell.WindowManagerShellWrapper */
public class WindowManagerShellWrapper {
    private static final String TAG = "WindowManagerShellWrapper";
    private final PinnedStackListenerForwarder mPinnedStackListenerForwarder;

    public WindowManagerShellWrapper(ShellExecutor shellExecutor) {
        this.mPinnedStackListenerForwarder = new PinnedStackListenerForwarder(shellExecutor);
    }

    public void addPinnedStackListener(PinnedStackListenerForwarder.PinnedTaskListener pinnedTaskListener) throws RemoteException {
        this.mPinnedStackListenerForwarder.addListener(pinnedTaskListener);
        this.mPinnedStackListenerForwarder.register(0);
    }

    public void removePinnedStackListener(PinnedStackListenerForwarder.PinnedTaskListener pinnedTaskListener) {
        this.mPinnedStackListenerForwarder.removeListener(pinnedTaskListener);
    }
}
