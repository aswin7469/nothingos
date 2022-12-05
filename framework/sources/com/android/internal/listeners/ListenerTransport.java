package com.android.internal.listeners;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes4.dex */
public interface ListenerTransport<TListener> {
    /* renamed from: getListener */
    TListener mo1484getListener();

    void unregister();

    default void execute(Executor executor, final Consumer<TListener> operation) {
        Objects.requireNonNull(operation);
        if (mo1484getListener() == null) {
            return;
        }
        executor.execute(new Runnable() { // from class: com.android.internal.listeners.ListenerTransport$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ListenerTransport.lambda$execute$0(ListenerTransport.this, operation);
            }
        });
    }

    static /* synthetic */ void lambda$execute$0(ListenerTransport _this, Consumer operation) {
        Object mo1484getListener = _this.mo1484getListener();
        if (mo1484getListener == null) {
            return;
        }
        operation.accept(mo1484getListener);
    }
}
