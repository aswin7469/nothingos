package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.IWindowManager;
import android.view.MotionEvent;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.AutoHideUiElement;
import javax.inject.Inject;

@SysUISingleton
public class AutoHideController {
    private static final long AUTO_HIDE_TIMEOUT_MS = 2250;
    private static final String TAG = "AutoHideController";
    private final Runnable mAutoHide = new AutoHideController$$ExternalSyntheticLambda0(this);
    private boolean mAutoHideSuspended;
    private int mDisplayId;
    private final Handler mHandler;
    private AutoHideUiElement mNavigationBar;
    private AutoHideUiElement mStatusBar;
    private final IWindowManager mWindowManagerService;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-AutoHideController */
    public /* synthetic */ void mo43654x84907423() {
        if (isAnyTransientBarShown()) {
            hideTransientBars();
        }
    }

    @Inject
    public AutoHideController(Context context, @Main Handler handler, IWindowManager iWindowManager) {
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
            Log.w(TAG, "Cannot get WindowManager");
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

    public void resumeSuspendedAutoHide() {
        if (this.mAutoHideSuspended) {
            scheduleAutoHide();
            Runnable checkBarModesRunnable = getCheckBarModesRunnable();
            if (checkBarModesRunnable != null) {
                this.mHandler.postDelayed(checkBarModesRunnable, 500);
            }
        }
    }

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
            return new AutoHideController$$ExternalSyntheticLambda1(this);
        }
        if (this.mNavigationBar != null) {
            return new AutoHideController$$ExternalSyntheticLambda2(this);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getCheckBarModesRunnable$1$com-android-systemui-statusbar-phone-AutoHideController */
    public /* synthetic */ void mo43652x3377612a() {
        this.mStatusBar.synchronizeState();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getCheckBarModesRunnable$2$com-android-systemui-statusbar-phone-AutoHideController */
    public /* synthetic */ void mo43653x590b6a2b() {
        this.mNavigationBar.synchronizeState();
    }

    private void cancelAutoHide() {
        this.mAutoHideSuspended = false;
        this.mHandler.removeCallbacks(this.mAutoHide);
    }

    private void scheduleAutoHide() {
        cancelAutoHide();
        this.mHandler.postDelayed(this.mAutoHide, AUTO_HIDE_TIMEOUT_MS);
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
        this.mHandler.postDelayed(this.mAutoHide, 350);
    }

    private boolean isAnyTransientBarShown() {
        AutoHideUiElement autoHideUiElement = this.mStatusBar;
        if (autoHideUiElement != null && autoHideUiElement.isVisible()) {
            return true;
        }
        AutoHideUiElement autoHideUiElement2 = this.mNavigationBar;
        if (autoHideUiElement2 == null || !autoHideUiElement2.isVisible()) {
            return false;
        }
        return true;
    }

    public static class Factory {
        private final Handler mHandler;
        private final IWindowManager mIWindowManager;

        @Inject
        public Factory(@Main Handler handler, IWindowManager iWindowManager) {
            this.mHandler = handler;
            this.mIWindowManager = iWindowManager;
        }

        public AutoHideController create(Context context) {
            return new AutoHideController(context, this.mHandler, this.mIWindowManager);
        }
    }
}
