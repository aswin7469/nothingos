package com.android.p019wm.shell.common;

import android.content.Context;

/* renamed from: com.android.wm.shell.common.RemoteCallable */
public interface RemoteCallable<T> {
    Context getContext();

    ShellExecutor getRemoteCallExecutor();
}
