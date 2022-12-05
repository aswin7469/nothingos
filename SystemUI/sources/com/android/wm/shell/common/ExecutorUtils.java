package com.android.wm.shell.common;

import android.util.Slog;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class ExecutorUtils {
    public static <T> void executeRemoteCallWithTaskPermission(RemoteCallable<T> remoteCallable, String str, Consumer<T> consumer) {
        executeRemoteCallWithTaskPermission(remoteCallable, str, consumer, false);
    }

    public static <T> void executeRemoteCallWithTaskPermission(final RemoteCallable<T> remoteCallable, String str, final Consumer<T> consumer, boolean z) {
        if (remoteCallable == null) {
            return;
        }
        remoteCallable.getContext().enforceCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS", str);
        if (z) {
            try {
                remoteCallable.getRemoteCallExecutor().executeBlocking(new Runnable() { // from class: com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        consumer.accept(remoteCallable);
                    }
                });
                return;
            } catch (InterruptedException e) {
                Slog.e("ExecutorUtils", "Remote call failed", e);
                return;
            }
        }
        remoteCallable.getRemoteCallExecutor().execute(new Runnable() { // from class: com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(remoteCallable);
            }
        });
    }
}
