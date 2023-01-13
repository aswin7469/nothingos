package com.android.systemui.navigationbar;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.Trace;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.WindowManagerGlobal;
import com.android.internal.statusbar.RegisterStatusBarResult;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.pip.Pip;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarComponent;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.navigationbar.NavigationBarControllerEx;
import java.p026io.PrintWriter;
import java.util.Optional;
import javax.inject.Inject;

@SysUISingleton
public class NavigationBarController implements CommandQueue.Callbacks, ConfigurationController.ConfigurationListener, NavigationModeController.ModeChangedListener, Dumpable {
    private static final String TAG = "NavigationBarController";
    private final InterestingConfigChanges mConfigChanges;
    private final Context mContext;
    private final DisplayManager mDisplayManager;
    private final Handler mHandler;
    boolean mIsTablet;
    private int mNavMode;
    private final NavigationBarComponent.Factory mNavigationBarComponentFactory;
    SparseArray<NavigationBar> mNavigationBars = new SparseArray<>();
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final TaskbarDelegate mTaskbarDelegate;

    @Inject
    public NavigationBarController(Context context, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, SysUiState sysUiState, CommandQueue commandQueue, @Main Handler handler, ConfigurationController configurationController, NavBarHelper navBarHelper, TaskbarDelegate taskbarDelegate, NavigationBarComponent.Factory factory, StatusBarKeyguardViewManager statusBarKeyguardViewManager, DumpManager dumpManager, AutoHideController autoHideController, LightBarController lightBarController, Optional<Pip> optional, Optional<BackAnimation> optional2) {
        Context context2 = context;
        Handler handler2 = handler;
        InterestingConfigChanges interestingConfigChanges = new InterestingConfigChanges(1073742592);
        this.mConfigChanges = interestingConfigChanges;
        this.mContext = context2;
        this.mHandler = handler2;
        this.mNavigationBarComponentFactory = factory;
        this.mDisplayManager = (DisplayManager) context2.getSystemService(DisplayManager.class);
        commandQueue.addCallback((CommandQueue.Callbacks) this);
        configurationController.addCallback(this);
        interestingConfigChanges.applyNewConfig(context.getResources());
        this.mNavMode = navigationModeController.addListener(this);
        TaskbarDelegate taskbarDelegate2 = taskbarDelegate;
        this.mTaskbarDelegate = taskbarDelegate2;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        taskbarDelegate2.setDependencies(commandQueue, overviewProxyService, navBarHelper, navigationModeController, sysUiState, dumpManager, autoHideController, lightBarController, optional, optional2.orElse(null));
        this.mIsTablet = Utilities.isTablet(context);
        dumpManager.registerDumpable(this);
        ((NavigationBarControllerEx) NTDependencyEx.get(NavigationBarControllerEx.class)).registerNavBarCombinationObserver(context2, handler2, this.mNavigationBars);
    }

    public void onConfigChanged(Configuration configuration) {
        boolean z = this.mIsTablet;
        boolean isTablet = Utilities.isTablet(this.mContext);
        this.mIsTablet = isTablet;
        int i = 0;
        if ((isTablet != z) && updateNavbarForTaskbar()) {
            return;
        }
        if (this.mConfigChanges.applyNewConfig(this.mContext.getResources())) {
            while (i < this.mNavigationBars.size()) {
                recreateNavigationBar(this.mNavigationBars.keyAt(i));
                i++;
            }
            return;
        }
        while (i < this.mNavigationBars.size()) {
            this.mNavigationBars.valueAt(i).onConfigurationChanged(configuration);
            i++;
        }
    }

    public void onNavigationModeChanged(int i) {
        int i2 = this.mNavMode;
        if (i2 != i) {
            this.mNavMode = i;
            updateAccessibilityButtonModeIfNeeded();
            this.mHandler.post(new NavigationBarController$$ExternalSyntheticLambda0(this, i2));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onNavigationModeChanged$0$com-android-systemui-navigationbar-NavigationBarController */
    public /* synthetic */ void mo34707xf469dbe4(int i) {
        if (i != this.mNavMode) {
            updateNavbarForTaskbar();
        }
        for (int i2 = 0; i2 < this.mNavigationBars.size(); i2++) {
            NavigationBar valueAt = this.mNavigationBars.valueAt(i2);
            if (valueAt != null) {
                valueAt.getView().updateStates();
            }
        }
    }

    private void updateAccessibilityButtonModeIfNeeded() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int intForUser = Settings.Secure.getIntForUser(contentResolver, "accessibility_button_mode", 0, -2);
        if (intForUser != 1) {
            if (QuickStepContract.isGesturalMode(this.mNavMode) && intForUser == 0) {
                Settings.Secure.putIntForUser(contentResolver, "accessibility_button_mode", 2, -2);
            } else if (!QuickStepContract.isGesturalMode(this.mNavMode) && intForUser == 2) {
                Settings.Secure.putIntForUser(contentResolver, "accessibility_button_mode", 0, -2);
            }
        }
    }

    private boolean updateNavbarForTaskbar() {
        boolean initializeTaskbarIfNecessary = initializeTaskbarIfNecessary();
        if (!initializeTaskbarIfNecessary && this.mNavigationBars.get(this.mContext.getDisplayId()) == null) {
            createNavigationBar(this.mContext.getDisplay(), (Bundle) null, (RegisterStatusBarResult) null);
        }
        return initializeTaskbarIfNecessary;
    }

    private boolean initializeTaskbarIfNecessary() {
        if (this.mIsTablet) {
            Trace.beginSection("NavigationBarController#initializeTaskbarIfNecessary");
            removeNavigationBar(this.mContext.getDisplayId());
            this.mTaskbarDelegate.init(this.mContext.getDisplayId());
            Trace.endSection();
        } else {
            this.mTaskbarDelegate.destroy();
        }
        return this.mIsTablet;
    }

    public void onDisplayRemoved(int i) {
        removeNavigationBar(i);
    }

    public void onDisplayReady(int i) {
        Display display = this.mDisplayManager.getDisplay(i);
        this.mIsTablet = Utilities.isTablet(this.mContext);
        createNavigationBar(display, (Bundle) null, (RegisterStatusBarResult) null);
    }

    public void setNavigationBarLumaSamplingEnabled(int i, boolean z) {
        NavigationBar navigationBar = getNavigationBar(i);
        if (navigationBar != null) {
            navigationBar.setNavigationBarLumaSamplingEnabled(z);
        }
    }

    private void recreateNavigationBar(int i) {
        Bundle bundle = new Bundle();
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.onSaveInstanceState(bundle);
        }
        removeNavigationBar(i);
        createNavigationBar(this.mDisplayManager.getDisplay(i), bundle, (RegisterStatusBarResult) null);
    }

    public void createNavigationBars(boolean z, RegisterStatusBarResult registerStatusBarResult) {
        updateAccessibilityButtonModeIfNeeded();
        boolean z2 = z && !initializeTaskbarIfNecessary();
        for (Display display : this.mDisplayManager.getDisplays()) {
            if (z2 || display.getDisplayId() != 0) {
                createNavigationBar(display, (Bundle) null, registerStatusBarResult);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void createNavigationBar(final Display display, Bundle bundle, final RegisterStatusBarResult registerStatusBarResult) {
        Context context;
        if (display != null) {
            int displayId = display.getDisplayId();
            boolean z = displayId == 0;
            if (!this.mIsTablet || !z) {
                try {
                    if (WindowManagerGlobal.getWindowManagerService().hasNavigationBar(displayId)) {
                        if (z) {
                            context = this.mContext;
                        } else {
                            context = this.mContext.createDisplayContext(display);
                        }
                        final NavigationBar navigationBar = this.mNavigationBarComponentFactory.create(context, bundle).getNavigationBar();
                        navigationBar.init();
                        this.mNavigationBars.put(displayId, navigationBar);
                        navigationBar.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                            public void onViewAttachedToWindow(View view) {
                                if (registerStatusBarResult != null) {
                                    navigationBar.setImeWindowStatus(display.getDisplayId(), registerStatusBarResult.mImeToken, registerStatusBarResult.mImeWindowVis, registerStatusBarResult.mImeBackDisposition, registerStatusBarResult.mShowImeSwitcher);
                                }
                            }

                            public void onViewDetachedFromWindow(View view) {
                                view.removeOnAttachStateChangeListener(this);
                            }
                        });
                    }
                } catch (RemoteException unused) {
                    Log.w(TAG, "Cannot get WindowManager.");
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void removeNavigationBar(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.destroyView();
            this.mNavigationBars.remove(i);
        }
    }

    public void checkNavBarModes(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.checkNavBarModes();
        }
    }

    public void finishBarAnimations(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.finishBarAnimations();
        }
    }

    public void touchAutoDim(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.touchAutoDim();
        }
    }

    public void transitionTo(int i, int i2, boolean z) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.transitionTo(i2, z);
        }
    }

    public void disableAnimationsDuringHide(int i, long j) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.disableAnimationsDuringHide(j);
        }
    }

    public NavigationBarView getDefaultNavigationBarView() {
        return getNavigationBarView(0);
    }

    public NavigationBarView getNavigationBarView(int i) {
        NavigationBar navigationBar = getNavigationBar(i);
        if (navigationBar == null) {
            return null;
        }
        return navigationBar.getView();
    }

    private NavigationBar getNavigationBar(int i) {
        return this.mNavigationBars.get(i);
    }

    public void showPinningEnterExitToast(int i, boolean z) {
        NavigationBarView navigationBarView = getNavigationBarView(i);
        if (navigationBarView != null) {
            navigationBarView.showPinningEnterExitToast(z);
        } else if (i == 0 && this.mTaskbarDelegate.isInitialized()) {
            this.mTaskbarDelegate.showPinningEnterExitToast(z);
        }
    }

    public void showPinningEscapeToast(int i) {
        NavigationBarView navigationBarView = getNavigationBarView(i);
        if (navigationBarView != null) {
            navigationBarView.showPinningEscapeToast();
        } else if (i == 0 && this.mTaskbarDelegate.isInitialized()) {
            this.mTaskbarDelegate.showPinningEscapeToast();
        }
    }

    public boolean isOverviewEnabled(int i) {
        NavigationBarView navigationBarView = getNavigationBarView(i);
        if (navigationBarView != null) {
            return navigationBarView.isOverviewEnabled();
        }
        return this.mTaskbarDelegate.isOverviewEnabled();
    }

    public NavigationBar getDefaultNavigationBar() {
        return this.mNavigationBars.get(0);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        for (int i = 0; i < this.mNavigationBars.size(); i++) {
            if (i > 0) {
                printWriter.println();
            }
            this.mNavigationBars.valueAt(i).dump(printWriter);
        }
    }
}
