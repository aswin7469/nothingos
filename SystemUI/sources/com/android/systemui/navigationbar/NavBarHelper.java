package com.android.systemui.navigationbar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.Dumpable;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

@SysUISingleton
public final class NavBarHelper implements AccessibilityManager.AccessibilityServicesStateChangeListener, AccessibilityButtonModeObserver.ModeChangedListener, AccessibilityButtonTargetsObserver.TargetsChangedListener, OverviewProxyService.OverviewProxyListener, NavigationModeController.ModeChangedListener, Dumpable {
    private int mA11yButtonState;
    private final List<NavbarTaskbarStateUpdater> mA11yEventListeners = new ArrayList();
    private final AccessibilityButtonModeObserver mAccessibilityButtonModeObserver;
    private final AccessibilityButtonTargetsObserver mAccessibilityButtonTargetsObserver;
    private final AccessibilityManager mAccessibilityManager;
    private final ContentObserver mAssistContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean z, Uri uri) {
            NavBarHelper.this.updateAssistantAvailability();
        }
    };
    private final Lazy<AssistManager> mAssistManagerLazy;
    private boolean mAssistantAvailable;
    private boolean mAssistantTouchGestureEnabled;
    private final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    private ContentResolver mContentResolver;
    private final Context mContext;
    private boolean mLongPressHomeEnabled;
    private int mNavBarMode;
    private final SystemActions mSystemActions;
    private final UserTracker mUserTracker;

    public interface NavbarTaskbarStateUpdater {
        void updateAccessibilityServicesState();

        void updateAssistantAvailable(boolean z);
    }

    @Inject
    public NavBarHelper(Context context, AccessibilityManager accessibilityManager, AccessibilityButtonModeObserver accessibilityButtonModeObserver, AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver, SystemActions systemActions, OverviewProxyService overviewProxyService, Lazy<AssistManager> lazy, Lazy<Optional<CentralSurfaces>> lazy2, NavigationModeController navigationModeController, UserTracker userTracker, DumpManager dumpManager) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mAccessibilityManager = accessibilityManager;
        this.mAssistManagerLazy = lazy;
        this.mCentralSurfacesOptionalLazy = lazy2;
        this.mUserTracker = userTracker;
        this.mSystemActions = systemActions;
        accessibilityManager.addAccessibilityServicesStateChangeListener(this);
        this.mAccessibilityButtonModeObserver = accessibilityButtonModeObserver;
        this.mAccessibilityButtonTargetsObserver = accessibilityButtonTargetsObserver;
        accessibilityButtonModeObserver.addListener(this);
        accessibilityButtonTargetsObserver.addListener(this);
        this.mNavBarMode = navigationModeController.addListener(this);
        overviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) this);
        dumpManager.registerDumpable(this);
    }

    public void init() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assistant"), false, this.mAssistContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assist_long_press_home_enabled"), false, this.mAssistContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assist_touch_gesture_enabled"), false, this.mAssistContentObserver, -1);
        updateAssistantAvailability();
        updateA11yState();
    }

    public void destroy() {
        this.mContentResolver.unregisterContentObserver(this.mAssistContentObserver);
    }

    public void registerNavTaskStateUpdater(NavbarTaskbarStateUpdater navbarTaskbarStateUpdater) {
        this.mA11yEventListeners.add(navbarTaskbarStateUpdater);
        navbarTaskbarStateUpdater.updateAccessibilityServicesState();
        navbarTaskbarStateUpdater.updateAssistantAvailable(this.mAssistantAvailable);
    }

    public void removeNavTaskStateUpdater(NavbarTaskbarStateUpdater navbarTaskbarStateUpdater) {
        this.mA11yEventListeners.remove((Object) navbarTaskbarStateUpdater);
    }

    private void dispatchA11yEventUpdate() {
        for (NavbarTaskbarStateUpdater updateAccessibilityServicesState : this.mA11yEventListeners) {
            updateAccessibilityServicesState.updateAccessibilityServicesState();
        }
    }

    private void dispatchAssistantEventUpdate(boolean z) {
        for (NavbarTaskbarStateUpdater updateAssistantAvailable : this.mA11yEventListeners) {
            updateAssistantAvailable.updateAssistantAvailable(z);
        }
    }

    public void onAccessibilityServicesStateChanged(AccessibilityManager accessibilityManager) {
        dispatchA11yEventUpdate();
        updateA11yState();
    }

    public void onAccessibilityButtonModeChanged(int i) {
        updateA11yState();
        dispatchA11yEventUpdate();
    }

    public void onAccessibilityButtonTargetsChanged(String str) {
        updateA11yState();
        dispatchA11yEventUpdate();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateA11yState() {
        /*
            r6 = this;
            int r0 = r6.mA11yButtonState
            com.android.systemui.accessibility.AccessibilityButtonModeObserver r1 = r6.mAccessibilityButtonModeObserver
            int r1 = r1.getCurrentAccessibilityButtonMode()
            r2 = 0
            r3 = 1
            if (r1 != r3) goto L_0x0010
            r6.mA11yButtonState = r2
            r3 = r2
            goto L_0x0032
        L_0x0010:
            android.view.accessibility.AccessibilityManager r1 = r6.mAccessibilityManager
            java.util.List r1 = r1.getAccessibilityShortcutTargets(r2)
            int r1 = r1.size()
            if (r1 < r3) goto L_0x001e
            r4 = r3
            goto L_0x001f
        L_0x001e:
            r4 = r2
        L_0x001f:
            r5 = 2
            if (r1 < r5) goto L_0x0023
            goto L_0x0024
        L_0x0023:
            r3 = r2
        L_0x0024:
            if (r4 == 0) goto L_0x0029
            r1 = 16
            goto L_0x002a
        L_0x0029:
            r1 = r2
        L_0x002a:
            if (r3 == 0) goto L_0x002e
            r2 = 32
        L_0x002e:
            r1 = r1 | r2
            r6.mA11yButtonState = r1
            r2 = r4
        L_0x0032:
            int r1 = r6.mA11yButtonState
            if (r0 == r1) goto L_0x0040
            r0 = 11
            r6.updateSystemAction(r2, r0)
            r0 = 12
            r6.updateSystemAction(r3, r0)
        L_0x0040:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavBarHelper.updateA11yState():void");
    }

    private void updateSystemAction(boolean z, int i) {
        if (z) {
            this.mSystemActions.register(i);
        } else {
            this.mSystemActions.unregister(i);
        }
    }

    public int getA11yButtonState() {
        return this.mA11yButtonState;
    }

    public void onConnectionChanged(boolean z) {
        if (z) {
            updateAssistantAvailability();
        }
    }

    /* access modifiers changed from: private */
    public void updateAssistantAvailability() {
        boolean z = true;
        boolean z2 = this.mAssistManagerLazy.get().getAssistInfoForUser(-2) != null;
        this.mLongPressHomeEnabled = Settings.Secure.getIntForUser(this.mContentResolver, "assist_long_press_home_enabled", this.mContext.getResources().getBoolean(17891370) ? 1 : 0, this.mUserTracker.getUserId()) != 0;
        boolean z3 = Settings.Secure.getIntForUser(this.mContentResolver, "assist_touch_gesture_enabled", this.mContext.getResources().getBoolean(17891371) ? 1 : 0, this.mUserTracker.getUserId()) != 0;
        this.mAssistantTouchGestureEnabled = z3;
        if (!z2 || !z3 || !QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            z = false;
        }
        this.mAssistantAvailable = z;
        dispatchAssistantEventUpdate(z);
    }

    public boolean getLongPressHomeEnabled() {
        return this.mLongPressHomeEnabled;
    }

    public void startAssistant(Bundle bundle) {
        this.mAssistManagerLazy.get().startAssist(bundle);
    }

    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
        updateAssistantAvailability();
    }

    public boolean isImeShown(int i) {
        NotificationShadeWindowView notificationShadeWindowView = ((CentralSurfaces) this.mCentralSurfacesOptionalLazy.get().get()).getNotificationShadeWindowView();
        boolean isKeyguardShowing = ((CentralSurfaces) this.mCentralSurfacesOptionalLazy.get().get()).isKeyguardShowing();
        if (notificationShadeWindowView != null && notificationShadeWindowView.isAttachedToWindow() && notificationShadeWindowView.getRootWindowInsets().isVisible(WindowInsets.Type.ime())) {
            return true;
        }
        if (isKeyguardShowing || (i & 2) == 0) {
            return false;
        }
        return true;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("NavbarTaskbarFriendster");
        printWriter.println("  longPressHomeEnabled=" + this.mLongPressHomeEnabled);
        printWriter.println("  mAssistantTouchGestureEnabled=" + this.mAssistantTouchGestureEnabled);
        printWriter.println("  mAssistantAvailable=" + this.mAssistantAvailable);
        printWriter.println("  mNavBarMode=" + this.mNavBarMode);
    }
}
