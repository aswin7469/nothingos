package com.android.systemui.dreams;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.policy.PhoneWindow;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class DreamOverlayService extends android.service.dreams.DreamOverlayService {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "DreamOverlayService";
    private final Context mContext;
    private boolean mDestroyed;
    private final DreamOverlayContainerViewController mDreamOverlayContainerViewController;
    private DreamOverlayTouchMonitor mDreamOverlayTouchMonitor;
    /* access modifiers changed from: private */
    public final Executor mExecutor;
    private final Complication.Host mHost;
    private final KeyguardUpdateMonitorCallback mKeyguardCallback;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public final LifecycleRegistry mLifecycleRegistry;
    private DreamOverlayStateController mStateController;
    private final UiEventLogger mUiEventLogger;
    private ViewModelStore mViewModelStore = new ViewModelStore();
    private Window mWindow;

    public enum DreamOverlayEvent implements UiEventLogger.UiEventEnum {
        DREAM_OVERLAY_ENTER_START(989),
        DREAM_OVERLAY_COMPLETE_START(990);
        
        private final int mId;

        private DreamOverlayEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    @Inject
    public DreamOverlayService(Context context, @Main Executor executor, DreamOverlayComponent.Factory factory, DreamOverlayStateController dreamOverlayStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, UiEventLogger uiEventLogger) {
        C20791 r0 = new Complication.Host() {
            public void requestExitDream() {
                DreamOverlayService.this.mExecutor.execute(new DreamOverlayService$1$$ExternalSyntheticLambda0(DreamOverlayService.this));
            }
        };
        this.mHost = r0;
        C20802 r1 = new KeyguardUpdateMonitorCallback() {
            public void onShadeExpandedChanged(boolean z) {
                if (DreamOverlayService.this.mLifecycleRegistry.getCurrentState() == Lifecycle.State.RESUMED || DreamOverlayService.this.mLifecycleRegistry.getCurrentState() == Lifecycle.State.STARTED) {
                    DreamOverlayService.this.mLifecycleRegistry.setCurrentState(z ? Lifecycle.State.STARTED : Lifecycle.State.RESUMED);
                }
            }
        };
        this.mKeyguardCallback = r1;
        this.mContext = context;
        this.mExecutor = executor;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(r1);
        this.mStateController = dreamOverlayStateController;
        this.mUiEventLogger = uiEventLogger;
        DreamOverlayComponent create = factory.create(this.mViewModelStore, r0);
        this.mDreamOverlayContainerViewController = create.getDreamOverlayContainerViewController();
        setCurrentState(Lifecycle.State.CREATED);
        this.mLifecycleRegistry = create.getLifecycleRegistry();
        DreamOverlayTouchMonitor dreamOverlayTouchMonitor = create.getDreamOverlayTouchMonitor();
        this.mDreamOverlayTouchMonitor = dreamOverlayTouchMonitor;
        dreamOverlayTouchMonitor.init();
    }

    private void setCurrentState(Lifecycle.State state) {
        this.mExecutor.execute(new DreamOverlayService$$ExternalSyntheticLambda0(this, state));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setCurrentState$0$com-android-systemui-dreams-DreamOverlayService */
    public /* synthetic */ void mo32521xe087c6d0(Lifecycle.State state) {
        this.mLifecycleRegistry.setCurrentState(state);
    }

    public void onDestroy() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardCallback);
        setCurrentState(Lifecycle.State.DESTROYED);
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        Window window = this.mWindow;
        if (window != null) {
            windowManager.removeView(window.getDecorView());
        }
        this.mStateController.setOverlayActive(false);
        this.mDestroyed = true;
        DreamOverlayService.super.onDestroy();
    }

    public void onStartDream(WindowManager.LayoutParams layoutParams) {
        this.mUiEventLogger.log(DreamOverlayEvent.DREAM_OVERLAY_ENTER_START);
        setCurrentState(Lifecycle.State.STARTED);
        this.mExecutor.execute(new DreamOverlayService$$ExternalSyntheticLambda1(this, layoutParams));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStartDream$1$com-android-systemui-dreams-DreamOverlayService */
    public /* synthetic */ void mo32520xa77d151(WindowManager.LayoutParams layoutParams) {
        if (!this.mDestroyed) {
            this.mStateController.setShouldShowComplications(shouldShowComplications());
            addOverlayWindowLocked(layoutParams);
            setCurrentState(Lifecycle.State.RESUMED);
            this.mStateController.setOverlayActive(true);
            this.mUiEventLogger.log(DreamOverlayEvent.DREAM_OVERLAY_COMPLETE_START);
        }
    }

    private void addOverlayWindowLocked(WindowManager.LayoutParams layoutParams) {
        PhoneWindow phoneWindow = new PhoneWindow(this.mContext);
        this.mWindow = phoneWindow;
        phoneWindow.setAttributes(layoutParams);
        this.mWindow.setWindowManager((WindowManager) null, layoutParams.token, "DreamOverlay", true);
        this.mWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.mWindow.clearFlags(Integer.MIN_VALUE);
        this.mWindow.addFlags(8);
        this.mWindow.requestFeature(1);
        this.mWindow.getDecorView().getWindowInsetsController().hide(WindowInsets.Type.systemBars());
        this.mWindow.setDecorFitsSystemWindows(false);
        if (DEBUG) {
            Log.d(TAG, "adding overlay window to dream");
        }
        this.mDreamOverlayContainerViewController.init();
        removeContainerViewFromParent();
        this.mWindow.setContentView(this.mDreamOverlayContainerViewController.getContainerView());
        ((WindowManager) this.mContext.getSystemService(WindowManager.class)).addView(this.mWindow.getDecorView(), this.mWindow.getAttributes());
    }

    private void removeContainerViewFromParent() {
        ViewGroup viewGroup;
        View containerView = this.mDreamOverlayContainerViewController.getContainerView();
        if (containerView != null && (viewGroup = (ViewGroup) containerView.getParent()) != null) {
            Log.w(TAG, "Removing dream overlay container view parent!");
            viewGroup.removeView(containerView);
        }
    }
}
