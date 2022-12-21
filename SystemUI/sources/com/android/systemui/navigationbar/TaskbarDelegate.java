package com.android.systemui.navigationbar;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.InsetsState;
import android.view.InsetsVisibilities;
import com.android.internal.view.AppearanceRegion;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.pip.Pip;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavBarHelper;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import java.p026io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javax.inject.Inject;

@SysUISingleton
public class TaskbarDelegate implements CommandQueue.Callbacks, OverviewProxyService.OverviewProxyListener, NavigationModeController.ModeChangedListener, ComponentCallbacks, Dumpable {
    private static final String TAG = "TaskbarDelegate";
    private AutoHideController mAutoHideController;
    private final AutoHideUiElement mAutoHideUiElement = new AutoHideUiElement() {
        public void synchronizeState() {
        }

        public boolean isVisible() {
            return TaskbarDelegate.this.mTaskbarTransientShowing;
        }

        public void hide() {
            TaskbarDelegate.this.clearTransient();
        }
    };
    private BackAnimation mBackAnimation;
    private int mBehavior;
    private CommandQueue mCommandQueue;
    private final Context mContext;
    private int mDisabledFlags;
    private int mDisplayId;
    private final DisplayManager mDisplayManager;
    private final EdgeBackGestureHandler mEdgeBackGestureHandler;
    private boolean mInitialized;
    private LightBarController mLightBarController;
    private LightBarTransitionsController mLightBarTransitionsController;
    private final LightBarTransitionsController.Factory mLightBarTransitionsControllerFactory;
    private NavBarHelper mNavBarHelper;
    private final Consumer<Boolean> mNavbarOverlayVisibilityChangeCallback = new TaskbarDelegate$$ExternalSyntheticLambda2(this);
    private final NavBarHelper.NavbarTaskbarStateUpdater mNavbarTaskbarStateUpdater = new NavBarHelper.NavbarTaskbarStateUpdater() {
        public void updateAccessibilityServicesState() {
            TaskbarDelegate.this.updateSysuiFlags();
        }

        public void updateAssistantAvailable(boolean z) {
            TaskbarDelegate.this.updateAssistantAvailability(z);
        }
    };
    private int mNavigationIconHints;
    private int mNavigationMode;
    private NavigationModeController mNavigationModeController;
    /* access modifiers changed from: private */
    public OverviewProxyService mOverviewProxyService;
    private final Consumer<Rect> mPipListener;
    private Optional<Pip> mPipOptional;
    private ScreenPinningNotify mScreenPinningNotify;
    private SysUiState mSysUiState;
    private int mTaskBarWindowState = 0;
    /* access modifiers changed from: private */
    public boolean mTaskbarTransientShowing;
    private Context mWindowContext;

    public void onLowMemory() {
    }

    public void onRecentsAnimationStateChanged(boolean z) {
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-navigationbar-TaskbarDelegate  reason: not valid java name */
    public /* synthetic */ void m2855lambda$new$0$comandroidsystemuinavigationbarTaskbarDelegate(Boolean bool) {
        if (bool.booleanValue()) {
            this.mAutoHideController.touchAutoHide();
        }
    }

    @Inject
    public TaskbarDelegate(Context context, EdgeBackGestureHandler.Factory factory, LightBarTransitionsController.Factory factory2) {
        this.mLightBarTransitionsControllerFactory = factory2;
        EdgeBackGestureHandler create = factory.create(context);
        this.mEdgeBackGestureHandler = create;
        this.mContext = context;
        this.mDisplayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        Objects.requireNonNull(create);
        this.mPipListener = new TaskbarDelegate$$ExternalSyntheticLambda3(create);
    }

    public void setDependencies(CommandQueue commandQueue, OverviewProxyService overviewProxyService, NavBarHelper navBarHelper, NavigationModeController navigationModeController, SysUiState sysUiState, DumpManager dumpManager, AutoHideController autoHideController, LightBarController lightBarController, Optional<Pip> optional, BackAnimation backAnimation) {
        this.mCommandQueue = commandQueue;
        this.mOverviewProxyService = overviewProxyService;
        this.mNavBarHelper = navBarHelper;
        this.mNavigationModeController = navigationModeController;
        this.mSysUiState = sysUiState;
        dumpManager.registerDumpable(this);
        this.mAutoHideController = autoHideController;
        this.mLightBarController = lightBarController;
        this.mPipOptional = optional;
        this.mBackAnimation = backAnimation;
        this.mLightBarTransitionsController = createLightBarTransitionsController();
    }

    private LightBarTransitionsController createLightBarTransitionsController() {
        LightBarTransitionsController create = this.mLightBarTransitionsControllerFactory.create(new LightBarTransitionsController.DarkIntensityApplier() {
            public int getTintAnimationDuration() {
                return 120;
            }

            public void applyDarkIntensity(float f) {
                TaskbarDelegate.this.mOverviewProxyService.onNavButtonsDarkIntensityChanged(f);
            }
        });
        create.overrideIconTintForNavMode(true);
        return create;
    }

    public void init(int i) {
        if (!this.mInitialized) {
            this.mDisplayId = i;
            this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
            this.mOverviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) this);
            this.mEdgeBackGestureHandler.onNavigationModeChanged(this.mNavigationModeController.addListener(this));
            this.mNavBarHelper.registerNavTaskStateUpdater(this.mNavbarTaskbarStateUpdater);
            this.mNavBarHelper.init();
            this.mEdgeBackGestureHandler.onNavBarAttached();
            Context createWindowContext = this.mContext.createWindowContext(this.mDisplayManager.getDisplay(i), 2, (Bundle) null);
            this.mWindowContext = createWindowContext;
            createWindowContext.registerComponentCallbacks(this);
            this.mScreenPinningNotify = new ScreenPinningNotify(this.mWindowContext);
            updateSysuiFlags();
            this.mAutoHideController.setNavigationBar(this.mAutoHideUiElement);
            this.mLightBarController.setNavigationBar(this.mLightBarTransitionsController);
            this.mPipOptional.ifPresent(new TaskbarDelegate$$ExternalSyntheticLambda0(this));
            this.mEdgeBackGestureHandler.setBackAnimation(this.mBackAnimation);
            this.mInitialized = true;
        }
    }

    public void destroy() {
        if (this.mInitialized) {
            this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
            this.mOverviewProxyService.removeCallback((OverviewProxyService.OverviewProxyListener) this);
            this.mNavigationModeController.removeListener(this);
            this.mNavBarHelper.removeNavTaskStateUpdater(this.mNavbarTaskbarStateUpdater);
            this.mNavBarHelper.destroy();
            this.mEdgeBackGestureHandler.onNavBarDetached();
            this.mScreenPinningNotify = null;
            Context context = this.mWindowContext;
            if (context != null) {
                context.unregisterComponentCallbacks(this);
                this.mWindowContext = null;
            }
            this.mAutoHideController.setNavigationBar((AutoHideUiElement) null);
            this.mLightBarTransitionsController.destroy();
            this.mLightBarController.setNavigationBar((LightBarTransitionsController) null);
            this.mPipOptional.ifPresent(new TaskbarDelegate$$ExternalSyntheticLambda1(this));
            this.mInitialized = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void addPipExclusionBoundsChangeListener(Pip pip) {
        pip.addPipExclusionBoundsChangeListener(this.mPipListener);
    }

    /* access modifiers changed from: package-private */
    public void removePipExclusionBoundsChangeListener(Pip pip) {
        pip.removePipExclusionBoundsChangeListener(this.mPipListener);
    }

    public boolean isInitialized() {
        return this.mInitialized;
    }

    /* access modifiers changed from: private */
    public void updateSysuiFlags() {
        int a11yButtonState = this.mNavBarHelper.getA11yButtonState();
        boolean z = false;
        SysUiState flag = this.mSysUiState.setFlag(16, (a11yButtonState & 16) != 0).setFlag(32, (a11yButtonState & 32) != 0).setFlag(262144, (this.mNavigationIconHints & 1) != 0).setFlag(1048576, (this.mNavigationIconHints & 4) != 0).setFlag(128, (this.mDisabledFlags & 16777216) != 0).setFlag(256, (this.mDisabledFlags & 2097152) != 0);
        if ((this.mDisabledFlags & 4194304) != 0) {
            z = true;
        }
        flag.setFlag(4194304, z).setFlag(2, !isWindowVisible()).setFlag(131072, allowSystemGestureIgnoringBarVisibility()).setFlag(1, ActivityManagerWrapper.getInstance().isScreenPinningActive()).setFlag(16777216, isImmersiveMode()).commitUpdate(this.mDisplayId);
    }

    /* access modifiers changed from: package-private */
    public boolean isOverviewEnabled() {
        return (this.mSysUiState.getFlags() & 16777216) == 0;
    }

    /* access modifiers changed from: private */
    public void updateAssistantAvailability(boolean z) {
        if (this.mOverviewProxyService.getProxy() != null) {
            try {
                this.mOverviewProxyService.getProxy().onAssistantAvailable(z);
            } catch (RemoteException e) {
                Log.e(TAG, "onAssistantAvailable() failed, available: " + z, e);
            }
        }
    }

    public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        boolean isImeShown = this.mNavBarHelper.isImeShown(i2);
        boolean z2 = true;
        if (!isImeShown) {
            isImeShown = (i2 & 8) != 0;
        }
        if (!isImeShown || !z) {
            z2 = false;
        }
        int calculateBackDispositionHints = Utilities.calculateBackDispositionHints(this.mNavigationIconHints, i3, isImeShown, z2);
        if (calculateBackDispositionHints != this.mNavigationIconHints) {
            this.mNavigationIconHints = calculateBackDispositionHints;
            updateSysuiFlags();
        }
    }

    public void setWindowState(int i, int i2, int i3) {
        if (i == this.mDisplayId && i2 == 2 && this.mTaskBarWindowState != i3) {
            this.mTaskBarWindowState = i3;
            updateSysuiFlags();
        }
    }

    public void onRotationProposal(int i, boolean z) {
        this.mOverviewProxyService.onRotationProposal(i, z);
    }

    public void disable(int i, int i2, int i3, boolean z) {
        this.mDisabledFlags = i2;
        updateSysuiFlags();
        this.mOverviewProxyService.disable(i, i2, i3, z);
    }

    public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
        this.mOverviewProxyService.onSystemBarAttributesChanged(i, i3);
        LightBarController lightBarController = this.mLightBarController;
        if (lightBarController != null && i == this.mDisplayId) {
            lightBarController.onNavigationBarAppearanceChanged(i2, false, 0, z);
        }
        if (this.mBehavior != i3) {
            this.mBehavior = i3;
            updateSysuiFlags();
        }
    }

    public void showTransient(int i, int[] iArr, boolean z) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 21) && !this.mTaskbarTransientShowing) {
            this.mTaskbarTransientShowing = true;
            onTransientStateChanged();
        }
    }

    public void abortTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 21)) {
            clearTransient();
        }
    }

    public void onTaskbarAutohideSuspend(boolean z) {
        if (z) {
            this.mAutoHideController.suspendAutoHide();
        } else {
            this.mAutoHideController.resumeSuspendedAutoHide();
        }
    }

    /* access modifiers changed from: private */
    public void clearTransient() {
        if (this.mTaskbarTransientShowing) {
            this.mTaskbarTransientShowing = false;
            onTransientStateChanged();
        }
    }

    private void onTransientStateChanged() {
        this.mEdgeBackGestureHandler.onNavBarTransientStateChanged(this.mTaskbarTransientShowing);
    }

    public void onNavigationModeChanged(int i) {
        this.mNavigationMode = i;
        this.mEdgeBackGestureHandler.onNavigationModeChanged(i);
    }

    private boolean isWindowVisible() {
        return this.mTaskBarWindowState == 0;
    }

    private boolean allowSystemGestureIgnoringBarVisibility() {
        return this.mBehavior != 2;
    }

    private boolean isImmersiveMode() {
        return this.mBehavior == 2;
    }

    public void onConfigurationChanged(Configuration configuration) {
        this.mEdgeBackGestureHandler.onConfigurationChanged(configuration);
    }

    public void showPinningEnterExitToast(boolean z) {
        updateSysuiFlags();
        ScreenPinningNotify screenPinningNotify = this.mScreenPinningNotify;
        if (screenPinningNotify != null) {
            if (z) {
                screenPinningNotify.showPinningStartToast();
            } else {
                screenPinningNotify.showPinningExitToast();
            }
        }
    }

    public void showPinningEscapeToast() {
        updateSysuiFlags();
        ScreenPinningNotify screenPinningNotify = this.mScreenPinningNotify;
        if (screenPinningNotify != null) {
            screenPinningNotify.showEscapeToast(QuickStepContract.isGesturalMode(this.mNavigationMode), !QuickStepContract.isGesturalMode(this.mNavigationMode));
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("TaskbarDelegate (displayId=" + this.mDisplayId + "):");
        printWriter.println("  mNavigationIconHints=" + this.mNavigationIconHints);
        printWriter.println("  mNavigationMode=" + this.mNavigationMode);
        printWriter.println("  mDisabledFlags=" + this.mDisabledFlags);
        printWriter.println("  mTaskBarWindowState=" + this.mTaskBarWindowState);
        printWriter.println("  mBehavior=" + this.mBehavior);
        printWriter.println("  mTaskbarTransientShowing=" + this.mTaskbarTransientShowing);
        this.mEdgeBackGestureHandler.dump(printWriter);
    }
}
