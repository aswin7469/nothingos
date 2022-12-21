package com.android.p019wm.shell.transition;

import android.window.RemoteTransition;
import android.window.TransitionFilter;
import com.android.p019wm.shell.common.annotations.ExternalThread;

@ExternalThread
/* renamed from: com.android.wm.shell.transition.ShellTransitions */
public interface ShellTransitions {
    IShellTransitions createExternalInterface() {
        return null;
    }

    void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
    }

    void unregisterRemote(RemoteTransition remoteTransition) {
    }
}
