package com.android.systemui.navigationbar;

import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.SparseArray;
import android.view.IWallpaperVisibilityListener;
import android.view.IWindowManager;
import android.view.View;
import com.android.systemui.C1893R;
import com.android.systemui.navigationbar.NavigationBarComponent;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.util.Utils;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@NavigationBarComponent.NavigationBarScope
public final class NavigationBarTransitions extends BarTransitions implements LightBarTransitionsController.DarkIntensityApplier {
    public static final int DEFAULT_COLOR_ADAPT_TRANSITION_TIME = 1700;
    public static final int MIN_COLOR_ADAPT_TRANSITION_TIME = 400;
    private final boolean mAllowAutoDimWallpaperNotVisible;
    private boolean mAutoDim;
    private List<DarkIntensityListener> mDarkIntensityListeners;
    /* access modifiers changed from: private */
    public final Handler mHandler = Handler.getMain();
    private final LightBarTransitionsController mLightTransitionsController;
    private boolean mLightsOut;
    private List<Listener> mListeners = new ArrayList();
    private int mNavBarMode = 0;
    private View mNavButtons;
    private final NavigationBarView mView;
    private final IWallpaperVisibilityListener mWallpaperVisibilityListener;
    /* access modifiers changed from: private */
    public boolean mWallpaperVisible;
    private final IWindowManager mWindowManagerService;

    public interface DarkIntensityListener {
        void onDarkIntensity(float f);
    }

    interface Listener {
        void onTransition(int i);
    }

    @Inject
    public NavigationBarTransitions(NavigationBarView navigationBarView, IWindowManager iWindowManager, LightBarTransitionsController.Factory factory) {
        super(navigationBarView, C1893R.C1895drawable.nav_background);
        C22581 r1 = new IWallpaperVisibilityListener.Stub() {
            public void onWallpaperVisibilityChanged(boolean z, int i) throws RemoteException {
                boolean unused = NavigationBarTransitions.this.mWallpaperVisible = z;
                NavigationBarTransitions.this.mHandler.post(new NavigationBarTransitions$1$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onWallpaperVisibilityChanged$0$com-android-systemui-navigationbar-NavigationBarTransitions$1 */
            public /* synthetic */ void mo34753x88dc2d7e() {
                NavigationBarTransitions.this.applyLightsOut(true, false);
            }
        };
        this.mWallpaperVisibilityListener = r1;
        this.mView = navigationBarView;
        this.mWindowManagerService = iWindowManager;
        this.mLightTransitionsController = factory.create(this);
        this.mAllowAutoDimWallpaperNotVisible = navigationBarView.getContext().getResources().getBoolean(C1893R.bool.config_navigation_bar_enable_auto_dim_no_visible_wallpaper);
        this.mDarkIntensityListeners = new ArrayList();
        try {
            this.mWallpaperVisible = iWindowManager.registerWallpaperVisibilityListener(r1, 0);
        } catch (RemoteException unused) {
        }
        this.mView.addOnLayoutChangeListener(new NavigationBarTransitions$$ExternalSyntheticLambda0(this));
        View currentView = this.mView.getCurrentView();
        if (currentView != null) {
            this.mNavButtons = currentView.findViewById(C1893R.C1897id.nav_buttons);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-navigationbar-NavigationBarTransitions */
    public /* synthetic */ void mo34744x342af7d8(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        View currentView = this.mView.getCurrentView();
        if (currentView != null) {
            this.mNavButtons = currentView.findViewById(C1893R.C1897id.nav_buttons);
            applyLightsOut(false, true);
        }
    }

    public void init() {
        applyModeBackground(-1, getMode(), false);
        applyLightsOut(false, true);
    }

    public void destroy() {
        try {
            this.mWindowManagerService.unregisterWallpaperVisibilityListener(this.mWallpaperVisibilityListener, 0);
        } catch (RemoteException unused) {
        }
        this.mLightTransitionsController.destroy();
    }

    public void setAutoDim(boolean z) {
        if ((!z || !Utils.isGesturalModeOnDefaultDisplay(this.mView.getContext(), this.mNavBarMode)) && this.mAutoDim != z) {
            this.mAutoDim = z;
            applyLightsOut(true, false);
        }
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundFrame(Rect rect) {
        this.mBarBackground.setFrame(rect);
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundOverrideAlpha(float f) {
        this.mBarBackground.setOverrideAlpha(f);
    }

    /* access modifiers changed from: protected */
    public boolean isLightsOut(int i) {
        return super.isLightsOut(i) || (this.mAllowAutoDimWallpaperNotVisible && this.mAutoDim && !this.mWallpaperVisible && i != 5);
    }

    public LightBarTransitionsController getLightTransitionsController() {
        return this.mLightTransitionsController;
    }

    /* access modifiers changed from: protected */
    public void onTransition(int i, int i2, boolean z) {
        super.onTransition(i, i2, z);
        applyLightsOut(z, false);
        for (Listener onTransition : this.mListeners) {
            onTransition.onTransition(i2);
        }
    }

    /* access modifiers changed from: private */
    public void applyLightsOut(boolean z, boolean z2) {
        applyLightsOut(isLightsOut(getMode()), z, z2);
    }

    private void applyLightsOut(boolean z, boolean z2, boolean z3) {
        if (z3 || z != this.mLightsOut) {
            this.mLightsOut = z;
            View view = this.mNavButtons;
            if (view != null) {
                view.animate().cancel();
                float currentDarkIntensity = z ? (this.mLightTransitionsController.getCurrentDarkIntensity() / 10.0f) + 0.6f : 1.0f;
                if (!z2) {
                    this.mNavButtons.setAlpha(currentDarkIntensity);
                } else {
                    this.mNavButtons.animate().alpha(currentDarkIntensity).setDuration((long) (z ? 1500 : 250)).start();
                }
            }
        }
    }

    public void reapplyDarkIntensity() {
        applyDarkIntensity(this.mLightTransitionsController.getCurrentDarkIntensity());
    }

    public void applyDarkIntensity(float f) {
        SparseArray<ButtonDispatcher> buttonDispatchers = this.mView.getButtonDispatchers();
        for (int size = buttonDispatchers.size() - 1; size >= 0; size--) {
            buttonDispatchers.valueAt(size).setDarkIntensity(f);
        }
        this.mView.getRotationButtonController().setDarkIntensity(f);
        for (DarkIntensityListener onDarkIntensity : this.mDarkIntensityListeners) {
            onDarkIntensity.onDarkIntensity(f);
        }
        if (this.mAutoDim) {
            applyLightsOut(false, true);
        }
    }

    public int getTintAnimationDuration() {
        if (Utils.isGesturalModeOnDefaultDisplay(this.mView.getContext(), this.mNavBarMode)) {
            return Math.max((int) DEFAULT_COLOR_ADAPT_TRANSITION_TIME, 400);
        }
        return 120;
    }

    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    public float addDarkIntensityListener(DarkIntensityListener darkIntensityListener) {
        this.mDarkIntensityListeners.add(darkIntensityListener);
        return this.mLightTransitionsController.getCurrentDarkIntensity();
    }

    public void removeDarkIntensityListener(DarkIntensityListener darkIntensityListener) {
        this.mDarkIntensityListeners.remove((Object) darkIntensityListener);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("NavigationBarTransitions:");
        printWriter.println("  mMode: " + getMode());
        printWriter.println("  mAlwaysOpaque: " + isAlwaysOpaque());
        printWriter.println("  mAllowAutoDimWallpaperNotVisible: " + this.mAllowAutoDimWallpaperNotVisible);
        printWriter.println("  mWallpaperVisible: " + this.mWallpaperVisible);
        printWriter.println("  mLightsOut: " + this.mLightsOut);
        printWriter.println("  mAutoDim: " + this.mAutoDim);
        printWriter.println("  bg overrideAlpha: " + this.mBarBackground.getOverrideAlpha());
        printWriter.println("  bg color: " + this.mBarBackground.getColor());
        printWriter.println("  bg frame: " + this.mBarBackground.getFrame());
    }

    /* access modifiers changed from: package-private */
    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeListener(Listener listener) {
        this.mListeners.remove((Object) listener);
    }
}
