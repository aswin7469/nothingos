package com.android.p019wm.shell.common;

import android.os.RemoteException;
import android.util.Slog;
import android.view.IDisplayWindowRotationCallback;
import android.view.IDisplayWindowRotationController;
import android.view.IWindowManager;
import android.window.WindowContainerTransaction;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: com.android.wm.shell.common.DisplayChangeController */
public class DisplayChangeController {
    private static final String TAG = "DisplayChangeController";
    private final IDisplayWindowRotationController mControllerImpl;
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private final CopyOnWriteArrayList<OnDisplayChangingListener> mRotationListener = new CopyOnWriteArrayList<>();
    private final IWindowManager mWmService;

    @ShellMainThread
    /* renamed from: com.android.wm.shell.common.DisplayChangeController$OnDisplayChangingListener */
    public interface OnDisplayChangingListener {
        void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction);
    }

    public DisplayChangeController(IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
        this.mWmService = iWindowManager;
        DisplayWindowRotationControllerImpl displayWindowRotationControllerImpl = new DisplayWindowRotationControllerImpl();
        this.mControllerImpl = displayWindowRotationControllerImpl;
        try {
            iWindowManager.setDisplayWindowRotationController(displayWindowRotationControllerImpl);
        } catch (RemoteException unused) {
            throw new RuntimeException("Unable to register rotation controller");
        }
    }

    public void addRotationListener(OnDisplayChangingListener onDisplayChangingListener) {
        this.mRotationListener.add(onDisplayChangingListener);
    }

    public void removeRotationListener(OnDisplayChangingListener onDisplayChangingListener) {
        this.mRotationListener.remove((Object) onDisplayChangingListener);
    }

    public void dispatchOnRotateDisplay(WindowContainerTransaction windowContainerTransaction, int i, int i2, int i3) {
        Iterator<OnDisplayChangingListener> it = this.mRotationListener.iterator();
        while (it.hasNext()) {
            it.next().onRotateDisplay(i, i2, i3, windowContainerTransaction);
        }
    }

    /* access modifiers changed from: private */
    public void onRotateDisplay(int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        dispatchOnRotateDisplay(windowContainerTransaction, i, i2, i3);
        try {
            iDisplayWindowRotationCallback.continueRotateDisplay(i3, windowContainerTransaction);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to continue rotation", e);
        }
    }

    /* renamed from: com.android.wm.shell.common.DisplayChangeController$DisplayWindowRotationControllerImpl */
    private class DisplayWindowRotationControllerImpl extends IDisplayWindowRotationController.Stub {
        private DisplayWindowRotationControllerImpl() {
        }

        public void onRotateDisplay(int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
            DisplayChangeController.this.mMainExecutor.execute(new C3417x93d164a5(this, i, i2, i3, iDisplayWindowRotationCallback));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onRotateDisplay$0$com-android-wm-shell-common-DisplayChangeController$DisplayWindowRotationControllerImpl */
        public /* synthetic */ void mo49011x15447031(int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
            DisplayChangeController.this.onRotateDisplay(i, i2, i3, iDisplayWindowRotationCallback);
        }
    }
}
