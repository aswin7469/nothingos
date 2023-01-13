package com.android.systemui.shortcut;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import com.android.internal.policy.IShortcutService;

public class ShortcutKeyServiceProxy extends IShortcutService.Stub {
    private static final int MSG_SHORTCUT_RECEIVED = 1;
    /* access modifiers changed from: private */
    public Callbacks mCallbacks;
    private final Handler mHandler = new C2533H();
    private final Object mLock = new Object();

    public interface Callbacks {
        void onShortcutKeyPressed(long j);
    }

    public ShortcutKeyServiceProxy(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public void notifyShortcutKeyPressed(long j) throws RemoteException {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1, Long.valueOf(j)).sendToTarget();
        }
    }

    /* renamed from: com.android.systemui.shortcut.ShortcutKeyServiceProxy$H */
    private final class C2533H extends Handler {
        private C2533H() {
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                ShortcutKeyServiceProxy.this.mCallbacks.onShortcutKeyPressed(((Long) message.obj).longValue());
            }
        }
    }
}
