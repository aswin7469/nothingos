package com.android.p019wm.shell.unfold;

import java.util.concurrent.Executor;

/* renamed from: com.android.wm.shell.unfold.ShellUnfoldProgressProvider */
public interface ShellUnfoldProgressProvider {
    public static final ShellUnfoldProgressProvider NO_PROVIDER = new ShellUnfoldProgressProvider() {
    };

    /* renamed from: com.android.wm.shell.unfold.ShellUnfoldProgressProvider$UnfoldListener */
    public interface UnfoldListener {
        void onStateChangeFinished() {
        }

        void onStateChangeProgress(float f) {
        }

        void onStateChangeStarted() {
        }
    }

    void addListener(Executor executor, UnfoldListener unfoldListener) {
    }
}
