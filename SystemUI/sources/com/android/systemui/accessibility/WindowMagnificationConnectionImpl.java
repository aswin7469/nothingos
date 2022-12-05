package com.android.systemui.accessibility;

import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import android.view.accessibility.IWindowMagnificationConnection;
import android.view.accessibility.IWindowMagnificationConnectionCallback;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class WindowMagnificationConnectionImpl extends IWindowMagnificationConnection.Stub {
    private IWindowMagnificationConnectionCallback mConnectionCallback;
    private final Handler mHandler;
    private final ModeSwitchesController mModeSwitchesController;
    private final WindowMagnification mWindowMagnification;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowMagnificationConnectionImpl(WindowMagnification windowMagnification, Handler handler, ModeSwitchesController modeSwitchesController) {
        this.mWindowMagnification = windowMagnification;
        this.mHandler = handler;
        this.mModeSwitchesController = modeSwitchesController;
    }

    public void enableWindowMagnification(final int i, final float f, final float f2, final float f3, final IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.this.lambda$enableWindowMagnification$0(i, f, f2, f3, iRemoteMagnificationAnimationCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enableWindowMagnification$0(int i, float f, float f2, float f3, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mWindowMagnification.enableWindowMagnification(i, f, f2, f3, iRemoteMagnificationAnimationCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setScale$1(int i, float f) {
        this.mWindowMagnification.setScale(i, f);
    }

    public void setScale(final int i, final float f) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.this.lambda$setScale$1(i, f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$disableWindowMagnification$2(int i, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mWindowMagnification.disableWindowMagnification(i, iRemoteMagnificationAnimationCallback);
    }

    public void disableWindowMagnification(final int i, final IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.this.lambda$disableWindowMagnification$2(i, iRemoteMagnificationAnimationCallback);
            }
        });
    }

    public void moveWindowMagnifier(final int i, final float f, final float f2) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.this.lambda$moveWindowMagnifier$3(i, f, f2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$moveWindowMagnifier$3(int i, float f, float f2) {
        this.mWindowMagnification.moveWindowMagnifier(i, f, f2);
    }

    public void showMagnificationButton(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.this.lambda$showMagnificationButton$4(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showMagnificationButton$4(int i, int i2) {
        this.mModeSwitchesController.showButton(i, i2);
    }

    public void removeMagnificationButton(final int i) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl.this.lambda$removeMagnificationButton$5(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeMagnificationButton$5(int i) {
        this.mModeSwitchesController.removeButton(i);
    }

    public void setConnectionCallback(IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback) {
        this.mConnectionCallback = iWindowMagnificationConnectionCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWindowMagnifierBoundsChanged(int i, Rect rect) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onWindowMagnifierBoundsChanged(i, rect);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform bounds changed", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSourceBoundsChanged(int i, Rect rect) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onSourceBoundsChanged(i, rect);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform source bounds changed", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPerformScaleAction(int i, float f) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onPerformScaleAction(i, f);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform performing scale action", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAccessibilityActionPerformed(int i) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onAccessibilityActionPerformed(i);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform an accessibility action is already performed", e);
            }
        }
    }
}
