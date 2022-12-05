package com.android.systemui.navigationbar;

import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.SparseArray;
import android.view.IWallpaperVisibilityListener;
import android.view.IWindowManager;
import android.view.View;
import androidx.appcompat.R$styleable;
import com.android.systemui.Dependency;
import com.android.systemui.R$bool;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.util.Utils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class NavigationBarTransitions extends BarTransitions implements LightBarTransitionsController.DarkIntensityApplier {
    private final boolean mAllowAutoDimWallpaperNotVisible;
    private boolean mAutoDim;
    private final LightBarTransitionsController mLightTransitionsController;
    private boolean mLightsOut;
    private View mNavButtons;
    private final NavigationBarView mView;
    private final IWallpaperVisibilityListener mWallpaperVisibilityListener;
    private boolean mWallpaperVisible;
    private int mNavBarMode = 0;
    private final Handler mHandler = Handler.getMain();
    private List<DarkIntensityListener> mDarkIntensityListeners = new ArrayList();

    /* loaded from: classes.dex */
    public interface DarkIntensityListener {
        void onDarkIntensity(float f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.navigationbar.NavigationBarTransitions$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IWallpaperVisibilityListener.Stub {
        AnonymousClass1() {
        }

        public void onWallpaperVisibilityChanged(boolean z, int i) throws RemoteException {
            NavigationBarTransitions.this.mWallpaperVisible = z;
            NavigationBarTransitions.this.mHandler.post(new Runnable() { // from class: com.android.systemui.navigationbar.NavigationBarTransitions$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NavigationBarTransitions.AnonymousClass1.this.lambda$onWallpaperVisibilityChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onWallpaperVisibilityChanged$0() {
            NavigationBarTransitions.this.applyLightsOut(true, false);
        }
    }

    public NavigationBarTransitions(NavigationBarView navigationBarView, CommandQueue commandQueue) {
        super(navigationBarView, R$drawable.nav_background);
        AnonymousClass1 anonymousClass1 = new AnonymousClass1();
        this.mWallpaperVisibilityListener = anonymousClass1;
        this.mView = navigationBarView;
        this.mLightTransitionsController = new LightBarTransitionsController(navigationBarView.getContext(), this, commandQueue);
        this.mAllowAutoDimWallpaperNotVisible = navigationBarView.getContext().getResources().getBoolean(R$bool.config_navigation_bar_enable_auto_dim_no_visible_wallpaper);
        try {
            this.mWallpaperVisible = ((IWindowManager) Dependency.get(IWindowManager.class)).registerWallpaperVisibilityListener(anonymousClass1, 0);
        } catch (RemoteException unused) {
        }
        this.mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.navigationbar.NavigationBarTransitions$$ExternalSyntheticLambda0
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                NavigationBarTransitions.this.lambda$new$0(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
        View currentView = this.mView.getCurrentView();
        if (currentView != null) {
            this.mNavButtons = currentView.findViewById(R$id.nav_buttons);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        View currentView = this.mView.getCurrentView();
        if (currentView != null) {
            this.mNavButtons = currentView.findViewById(R$id.nav_buttons);
            applyLightsOut(false, true);
        }
    }

    public void init() {
        applyModeBackground(-1, getMode(), false);
        applyLightsOut(false, true);
    }

    public void destroy() {
        try {
            ((IWindowManager) Dependency.get(IWindowManager.class)).unregisterWallpaperVisibilityListener(this.mWallpaperVisibilityListener, 0);
        } catch (RemoteException unused) {
        }
    }

    public void setAutoDim(boolean z) {
        if ((!z || !Utils.isGesturalModeOnDefaultDisplay(this.mView.getContext(), this.mNavBarMode)) && this.mAutoDim != z) {
            this.mAutoDim = z;
            applyLightsOut(true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBackgroundFrame(Rect rect) {
        this.mBarBackground.setFrame(rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBackgroundOverrideAlpha(float f) {
        this.mBarBackground.setOverrideAlpha(f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.BarTransitions
    public boolean isLightsOut(int i) {
        return super.isLightsOut(i) || (this.mAllowAutoDimWallpaperNotVisible && this.mAutoDim && !this.mWallpaperVisible && i != 5);
    }

    public LightBarTransitionsController getLightTransitionsController() {
        return this.mLightTransitionsController;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.BarTransitions
    public void onTransition(int i, int i2, boolean z) {
        super.onTransition(i, i2, z);
        applyLightsOut(z, false);
        this.mView.onBarTransition(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyLightsOut(boolean z, boolean z2) {
        applyLightsOut(isLightsOut(getMode()), z, z2);
    }

    private void applyLightsOut(boolean z, boolean z2, boolean z3) {
        if (z3 || z != this.mLightsOut) {
            this.mLightsOut = z;
            View view = this.mNavButtons;
            if (view == null) {
                return;
            }
            view.animate().cancel();
            float currentDarkIntensity = z ? (this.mLightTransitionsController.getCurrentDarkIntensity() / 10.0f) + 0.6f : 1.0f;
            if (!z2) {
                this.mNavButtons.setAlpha(currentDarkIntensity);
            } else {
                this.mNavButtons.animate().alpha(currentDarkIntensity).setDuration(z ? 1500 : 250).start();
            }
        }
    }

    public void reapplyDarkIntensity() {
        applyDarkIntensity(this.mLightTransitionsController.getCurrentDarkIntensity());
    }

    @Override // com.android.systemui.statusbar.phone.LightBarTransitionsController.DarkIntensityApplier
    public void applyDarkIntensity(float f) {
        SparseArray<ButtonDispatcher> buttonDispatchers = this.mView.getButtonDispatchers();
        for (int size = buttonDispatchers.size() - 1; size >= 0; size--) {
            buttonDispatchers.valueAt(size).setDarkIntensity(f);
        }
        this.mView.getRotationButtonController().setDarkIntensity(f);
        for (DarkIntensityListener darkIntensityListener : this.mDarkIntensityListeners) {
            darkIntensityListener.onDarkIntensity(f);
        }
        if (this.mAutoDim) {
            applyLightsOut(false, true);
        }
    }

    @Override // com.android.systemui.statusbar.phone.LightBarTransitionsController.DarkIntensityApplier
    public int getTintAnimationDuration() {
        return Utils.isGesturalModeOnDefaultDisplay(this.mView.getContext(), this.mNavBarMode) ? Math.max(1700, 400) : R$styleable.AppCompatTheme_windowFixedHeightMajor;
    }

    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    public float addDarkIntensityListener(DarkIntensityListener darkIntensityListener) {
        this.mDarkIntensityListeners.add(darkIntensityListener);
        return this.mLightTransitionsController.getCurrentDarkIntensity();
    }

    public void removeDarkIntensityListener(DarkIntensityListener darkIntensityListener) {
        this.mDarkIntensityListeners.remove(darkIntensityListener);
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
}
