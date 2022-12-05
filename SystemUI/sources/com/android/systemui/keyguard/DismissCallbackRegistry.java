package com.android.systemui.keyguard;

import com.android.internal.policy.IKeyguardDismissCallback;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class DismissCallbackRegistry {
    private final ArrayList<DismissCallbackWrapper> mDismissCallbacks = new ArrayList<>();
    private final Executor mUiBgExecutor;

    public DismissCallbackRegistry(Executor executor) {
        this.mUiBgExecutor = executor;
    }

    public void addCallback(IKeyguardDismissCallback iKeyguardDismissCallback) {
        this.mDismissCallbacks.add(new DismissCallbackWrapper(iKeyguardDismissCallback));
    }

    public void notifyDismissCancelled() {
        for (int size = this.mDismissCallbacks.size() - 1; size >= 0; size--) {
            final DismissCallbackWrapper dismissCallbackWrapper = this.mDismissCallbacks.get(size);
            Executor executor = this.mUiBgExecutor;
            Objects.requireNonNull(dismissCallbackWrapper);
            executor.execute(new Runnable() { // from class: com.android.systemui.keyguard.DismissCallbackRegistry$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DismissCallbackWrapper.this.notifyDismissCancelled();
                }
            });
        }
        this.mDismissCallbacks.clear();
    }

    public void notifyDismissSucceeded() {
        for (int size = this.mDismissCallbacks.size() - 1; size >= 0; size--) {
            final DismissCallbackWrapper dismissCallbackWrapper = this.mDismissCallbacks.get(size);
            Executor executor = this.mUiBgExecutor;
            Objects.requireNonNull(dismissCallbackWrapper);
            executor.execute(new Runnable() { // from class: com.android.systemui.keyguard.DismissCallbackRegistry$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DismissCallbackWrapper.this.notifyDismissSucceeded();
                }
            });
        }
        this.mDismissCallbacks.clear();
    }
}