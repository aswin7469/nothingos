package com.android.p019wm.shell.common;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Slog;
import com.android.p019wm.shell.common.RemoteCallable;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.common.SingleInstanceRemoteListener */
public class SingleInstanceRemoteListener<C extends RemoteCallable, L extends IInterface> {
    private static final String TAG = "SingleInstanceRemoteListener";
    /* access modifiers changed from: private */
    public final C mCallableController;
    L mListener;
    private final IBinder.DeathRecipient mListenerDeathRecipient = new IBinder.DeathRecipient() {
        public void binderDied() {
            SingleInstanceRemoteListener.this.mCallableController.getRemoteCallExecutor().execute(new SingleInstanceRemoteListener$1$$ExternalSyntheticLambda0(this, SingleInstanceRemoteListener.this.mCallableController));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$binderDied$0$com-android-wm-shell-common-SingleInstanceRemoteListener$1 */
        public /* synthetic */ void mo49128xc5449869(RemoteCallable remoteCallable) {
            SingleInstanceRemoteListener.this.mListener = null;
            SingleInstanceRemoteListener.this.mOnUnregisterCallback.accept(remoteCallable);
        }
    };
    private final Consumer<C> mOnRegisterCallback;
    /* access modifiers changed from: private */
    public final Consumer<C> mOnUnregisterCallback;

    /* renamed from: com.android.wm.shell.common.SingleInstanceRemoteListener$RemoteCall */
    public interface RemoteCall<L> {
        void accept(L l) throws RemoteException;
    }

    public SingleInstanceRemoteListener(C c, Consumer<C> consumer, Consumer<C> consumer2) {
        this.mCallableController = c;
        this.mOnRegisterCallback = consumer;
        this.mOnUnregisterCallback = consumer2;
    }

    public void register(L l) {
        L l2 = this.mListener;
        if (l2 != null) {
            l2.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
        }
        if (l != null) {
            try {
                l.asBinder().linkToDeath(this.mListenerDeathRecipient, 0);
            } catch (RemoteException unused) {
                Slog.e(TAG, "Failed to link to death");
                return;
            }
        }
        this.mListener = l;
        this.mOnRegisterCallback.accept(this.mCallableController);
    }

    public void unregister() {
        L l = this.mListener;
        if (l != null) {
            l.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
        }
        this.mListener = null;
        this.mOnUnregisterCallback.accept(this.mCallableController);
    }

    public void call(RemoteCall<L> remoteCall) {
        L l = this.mListener;
        if (l == null) {
            Slog.e(TAG, "Failed remote call on null listener");
            return;
        }
        try {
            remoteCall.accept(l);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed remote call", e);
        }
    }
}
