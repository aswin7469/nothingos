package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.IWindowManager;
import android.view.MotionEvent;
import com.android.systemui.statusbar.AutoHideUiElement;
/* loaded from: classes.dex */
public class AutoHideController {
    private final Runnable mAutoHide = new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoHideController$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            AutoHideController.this.lambda$new$0();
        }
    };
    private boolean mAutoHideSuspended;
    private int mDisplayId;
    private final Handler mHandler;
    private AutoHideUiElement mNavigationBar;
    private AutoHideUiElement mStatusBar;
    private final IWindowManager mWindowManagerService;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (isAnyTransientBarShown()) {
            hideTransientBars();
        }
    }

    public AutoHideController(Context context, Handler handler, IWindowManager iWindowManager) {
        this.mHandler = handler;
        this.mWindowManagerService = iWindowManager;
        this.mDisplayId = context.getDisplayId();
    }

    public void setStatusBar(AutoHideUiElement autoHideUiElement) {
        this.mStatusBar = autoHideUiElement;
    }

    public void setNavigationBar(AutoHideUiElement autoHideUiElement) {
        this.mNavigationBar = autoHideUiElement;
    }

    private void hideTransientBars() {
        try {
            this.mWindowManagerService.hideTransientBars(this.mDisplayId);
        } catch (RemoteException unused) {
            Log.w("AutoHideController", "Cannot get WindowManager");
        }
        AutoHideUiElement autoHideUiElement = this.mStatusBar;
        if (autoHideUiElement != null) {
            autoHideUiElement.hide();
        }
        AutoHideUiElement autoHideUiElement2 = this.mNavigationBar;
        if (autoHideUiElement2 != null) {
            autoHideUiElement2.hide();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resumeSuspendedAutoHide() {
        if (this.mAutoHideSuspended) {
            scheduleAutoHide();
            Runnable checkBarModesRunnable = getCheckBarModesRunnable();
            if (checkBarModesRunnable == null) {
                return;
            }
            this.mHandler.postDelayed(checkBarModesRunnable, 500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void suspendAutoHide() {
        this.mHandler.removeCallbacks(this.mAutoHide);
        Runnable checkBarModesRunnable = getCheckBarModesRunnable();
        if (checkBarModesRunnable != null) {
            this.mHandler.removeCallbacks(checkBarModesRunnable);
        }
        this.mAutoHideSuspended = isAnyTransientBarShown();
    }

    public void touchAutoHide() {
        if (isAnyTransientBarShown()) {
            scheduleAutoHide();
        } else {
            cancelAutoHide();
        }
    }

    private Runnable getCheckBarModesRunnable() {
        if (this.mStatusBar != null) {
            return new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoHideController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AutoHideController.this.lambda$getCheckBarModesRunnable$1();
                }
            };
        }
        if (this.mNavigationBar == null) {
            return null;
        }
        return new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoHideController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AutoHideController.this.lambda$getCheckBarModesRunnable$2();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCheckBarModesRunnable$1() {
        this.mStatusBar.synchronizeState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCheckBarModesRunnable$2() {
        this.mNavigationBar.synchronizeState();
    }

    private void cancelAutoHide() {
        this.mAutoHideSuspended = false;
        this.mHandler.removeCallbacks(this.mAutoHide);
    }

    private void scheduleAutoHide() {
        cancelAutoHide();
        this.mHandler.postDelayed(this.mAutoHide, 2250L);
    }

    public void checkUserAutoHide(MotionEvent motionEvent) {
        boolean z = isAnyTransientBarShown() && motionEvent.getAction() == 4 && motionEvent.getX() == 0.0f && motionEvent.getY() == 0.0f;
        AutoHideUiElement autoHideUiElement = this.mStatusBar;
        if (autoHideUiElement != null) {
            z &= autoHideUiElement.shouldHideOnTouch();
        }
        AutoHideUiElement autoHideUiElement2 = this.mNavigationBar;
        if (autoHideUiElement2 != null) {
            z &= autoHideUiElement2.shouldHideOnTouch();
        }
        if (z) {
            userAutoHide();
        }
    }

    private void userAutoHide() {
        cancelAutoHide();
        this.mHandler.postDelayed(this.mAutoHide, 350L);
    }

    private boolean isAnyTransientBarShown() {
        AutoHideUiElement autoHideUiElement = this.mStatusBar;
        if (autoHideUiElement == null || !autoHideUiElement.isVisible()) {
            AutoHideUiElement autoHideUiElement2 = this.mNavigationBar;
            return autoHideUiElement2 != null && autoHideUiElement2.isVisible();
        }
        return true;
    }
}
