package com.android.systemui.accessibility;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceControl;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import android.view.accessibility.IWindowMagnificationConnection;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.CoreStartable;
import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.model.SysUiState;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import java.p026io.PrintWriter;
import java.util.Objects;
import javax.inject.Inject;

@SysUISingleton
public class WindowMagnification extends CoreStartable implements WindowMagnifierCallback, CommandQueue.Callbacks {
    private static final String TAG = "WindowMagnification";
    private final AccessibilityManager mAccessibilityManager = ((AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class));
    private final CommandQueue mCommandQueue;
    private final Handler mHandler;
    DisplayIdIndexSupplier<WindowMagnificationController> mMagnificationControllerSupplier;
    private final ModeSwitchesController mModeSwitchesController;
    private final OverviewProxyService mOverviewProxyService;
    private SysUiState mSysUiState;
    private WindowMagnificationConnectionImpl mWindowMagnificationConnectionImpl;

    private static class ControllerSupplier extends DisplayIdIndexSupplier<WindowMagnificationController> {
        private final Context mContext;
        private final Handler mHandler;
        private final SysUiState mSysUiState;
        private final WindowMagnifierCallback mWindowMagnifierCallback;

        ControllerSupplier(Context context, Handler handler, WindowMagnifierCallback windowMagnifierCallback, DisplayManager displayManager, SysUiState sysUiState) {
            super(displayManager);
            this.mContext = context;
            this.mHandler = handler;
            this.mWindowMagnifierCallback = windowMagnifierCallback;
            this.mSysUiState = sysUiState;
        }

        /* access modifiers changed from: protected */
        public WindowMagnificationController createInstance(Display display) {
            Context createWindowContext = this.mContext.createWindowContext(display, 2039, (Bundle) null);
            return new WindowMagnificationController(createWindowContext, this.mHandler, new WindowMagnificationAnimationController(createWindowContext), new SfVsyncFrameCallbackProvider(), (MirrorWindowControl) null, new SurfaceControl.Transaction(), this.mWindowMagnifierCallback, this.mSysUiState);
        }
    }

    @Inject
    public WindowMagnification(Context context, @Main Handler handler, CommandQueue commandQueue, ModeSwitchesController modeSwitchesController, SysUiState sysUiState, OverviewProxyService overviewProxyService) {
        super(context);
        this.mHandler = handler;
        this.mCommandQueue = commandQueue;
        this.mModeSwitchesController = modeSwitchesController;
        this.mSysUiState = sysUiState;
        this.mOverviewProxyService = overviewProxyService;
        this.mMagnificationControllerSupplier = new ControllerSupplier(context, handler, this, (DisplayManager) context.getSystemService(DisplayManager.class), sysUiState);
    }

    public void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mOverviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) new OverviewProxyService.OverviewProxyListener() {
            public void onConnectionChanged(boolean z) {
                if (z) {
                    WindowMagnification.this.updateSysUiStateFlag();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateSysUiStateFlag() {
        WindowMagnificationController valueAt = this.mMagnificationControllerSupplier.valueAt(0);
        if (valueAt != null) {
            valueAt.updateSysUIStateFlag();
        } else {
            this.mSysUiState.setFlag(524288, false).commitUpdate(0);
        }
    }

    /* access modifiers changed from: package-private */
    public void enableWindowMagnification(int i, float f, float f2, float f3, float f4, float f5, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        WindowMagnificationController windowMagnificationController = this.mMagnificationControllerSupplier.get(i);
        if (windowMagnificationController != null) {
            windowMagnificationController.enableWindowMagnification(f, f2, f3, f4, f5, iRemoteMagnificationAnimationCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void setScale(int i, float f) {
        WindowMagnificationController windowMagnificationController = this.mMagnificationControllerSupplier.get(i);
        if (windowMagnificationController != null) {
            windowMagnificationController.setScale(f);
        }
    }

    /* access modifiers changed from: package-private */
    public void moveWindowMagnifier(int i, float f, float f2) {
        WindowMagnificationController windowMagnificationController = this.mMagnificationControllerSupplier.get(i);
        if (windowMagnificationController != null) {
            windowMagnificationController.moveWindowMagnifier(f, f2);
        }
    }

    /* access modifiers changed from: package-private */
    public void moveWindowMagnifierToPositionInternal(int i, float f, float f2, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        WindowMagnificationController windowMagnificationController = this.mMagnificationControllerSupplier.get(i);
        if (windowMagnificationController != null) {
            windowMagnificationController.moveWindowMagnifierToPosition(f, f2, iRemoteMagnificationAnimationCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void disableWindowMagnification(int i, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        WindowMagnificationController windowMagnificationController = this.mMagnificationControllerSupplier.get(i);
        if (windowMagnificationController != null) {
            windowMagnificationController.deleteWindowMagnification(iRemoteMagnificationAnimationCallback);
        }
    }

    public void onWindowMagnifierBoundsChanged(int i, Rect rect) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null) {
            windowMagnificationConnectionImpl.onWindowMagnifierBoundsChanged(i, rect);
        }
    }

    public void onSourceBoundsChanged(int i, Rect rect) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null) {
            windowMagnificationConnectionImpl.onSourceBoundsChanged(i, rect);
        }
    }

    public void onPerformScaleAction(int i, float f) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null) {
            windowMagnificationConnectionImpl.onPerformScaleAction(i, f);
        }
    }

    public void onAccessibilityActionPerformed(int i) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null) {
            windowMagnificationConnectionImpl.onAccessibilityActionPerformed(i);
        }
    }

    public void onMove(int i) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null) {
            windowMagnificationConnectionImpl.onMove(i);
        }
    }

    public void requestWindowMagnificationConnection(boolean z) {
        if (z) {
            setWindowMagnificationConnection();
        } else {
            clearWindowMagnificationConnection();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(TAG);
        this.mMagnificationControllerSupplier.forEach(new WindowMagnification$$ExternalSyntheticLambda1(printWriter));
    }

    private void setWindowMagnificationConnection() {
        if (this.mWindowMagnificationConnectionImpl == null) {
            this.mWindowMagnificationConnectionImpl = new WindowMagnificationConnectionImpl(this, this.mHandler, this.mModeSwitchesController);
        }
        ModeSwitchesController modeSwitchesController = this.mModeSwitchesController;
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        Objects.requireNonNull(windowMagnificationConnectionImpl);
        modeSwitchesController.setSwitchListenerDelegate(new WindowMagnification$$ExternalSyntheticLambda0(windowMagnificationConnectionImpl));
        this.mAccessibilityManager.setWindowMagnificationConnection(this.mWindowMagnificationConnectionImpl);
    }

    private void clearWindowMagnificationConnection() {
        this.mAccessibilityManager.setWindowMagnificationConnection((IWindowMagnificationConnection) null);
        this.mModeSwitchesController.setSwitchListenerDelegate((MagnificationModeSwitch.SwitchListener) null);
    }
}
