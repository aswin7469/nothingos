package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.InsetsFlags;
import android.view.ViewDebug;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.policy.BatteryController;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import javax.inject.Inject;

@SysUISingleton
public class LightBarController implements BatteryController.BatteryStateChangeCallback, Dumpable {
    private static final float NAV_BAR_INVERSION_SCRIM_ALPHA_THRESHOLD = 0.1f;
    private int mAppearance;
    private AppearanceRegion[] mAppearanceRegions = new AppearanceRegion[0];
    private final BatteryController mBatteryController;
    private BiometricUnlockController mBiometricUnlockController;
    private final Color mDarkModeColor;
    private boolean mDirectReplying;
    private boolean mForceDarkForScrim;
    private boolean mHasLightNavigationBar;
    private boolean mNavbarColorManagedByIme;
    private LightBarTransitionsController mNavigationBarController;
    private int mNavigationBarMode;
    private boolean mNavigationLight;
    private int mNavigationMode;
    private boolean mQsCustomizing;
    private Resources mResources;
    private final SysuiDarkIconDispatcher mStatusBarIconController;
    private int mStatusBarMode;

    private static boolean isLight(int i, int i2, int i3) {
        return (i2 == 0 || i2 == 6) && ((i & i3) != 0);
    }

    @Inject
    public LightBarController(Context context, DarkIconDispatcher darkIconDispatcher, BatteryController batteryController, NavigationModeController navigationModeController, DumpManager dumpManager) {
        this.mDarkModeColor = Color.valueOf(context.getColor(C1893R.C1894color.dark_mode_icon_color_single_tone));
        this.mStatusBarIconController = (SysuiDarkIconDispatcher) darkIconDispatcher;
        this.mBatteryController = batteryController;
        batteryController.addCallback(this);
        this.mNavigationMode = navigationModeController.addListener(new LightBarController$$ExternalSyntheticLambda0(this));
        if (context.getDisplayId() == 0) {
            dumpManager.registerDumpable(getClass().getSimpleName(), this);
        }
        this.mResources = context.getResources();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-LightBarController */
    public /* synthetic */ void mo44370x3a2764af(int i) {
        this.mNavigationMode = i;
    }

    public void setNavigationBar(LightBarTransitionsController lightBarTransitionsController) {
        this.mNavigationBarController = lightBarTransitionsController;
        updateNavigation();
    }

    public void setBiometricUnlockController(BiometricUnlockController biometricUnlockController) {
        this.mBiometricUnlockController = biometricUnlockController;
    }

    /* access modifiers changed from: package-private */
    public void onStatusBarAppearanceChanged(AppearanceRegion[] appearanceRegionArr, boolean z, int i, boolean z2) {
        int length = appearanceRegionArr.length;
        boolean z3 = this.mAppearanceRegions.length != length;
        for (int i2 = 0; i2 < length && !z3; i2++) {
            z3 |= !appearanceRegionArr[i2].equals(this.mAppearanceRegions[i2]);
        }
        if (z3 || z) {
            this.mAppearanceRegions = appearanceRegionArr;
            onStatusBarModeChanged(i);
        }
        this.mNavbarColorManagedByIme = z2;
    }

    /* access modifiers changed from: package-private */
    public void onStatusBarModeChanged(int i) {
        this.mStatusBarMode = i;
        updateStatus();
    }

    public void onNavigationBarAppearanceChanged(int i, boolean z, int i2, boolean z2) {
        if (i2 == 4) {
            updateOpaqueNavigation();
        }
        this.mAppearance = i;
        this.mNavigationBarMode = i2;
        this.mNavbarColorManagedByIme = z2;
    }

    private void updateOpaqueNavigation() {
        if (!QuickStepContract.isGesturalMode(this.mNavigationMode)) {
            this.mNavigationLight = !((this.mResources.getConfiguration().uiMode & 48) == 32);
            updateNavigation();
        }
    }

    public void onNavigationBarModeChanged(int i) {
        this.mHasLightNavigationBar = isLight(this.mAppearance, i, 16);
    }

    private void reevaluate() {
        onStatusBarAppearanceChanged(this.mAppearanceRegions, true, this.mStatusBarMode, this.mNavbarColorManagedByIme);
        onNavigationBarAppearanceChanged(this.mAppearance, true, this.mNavigationBarMode, this.mNavbarColorManagedByIme);
    }

    public void setQsCustomizing(boolean z) {
        if (this.mQsCustomizing != z) {
            this.mQsCustomizing = z;
            reevaluate();
        }
    }

    public void setDirectReplying(boolean z) {
        if (this.mDirectReplying != z) {
            this.mDirectReplying = z;
            reevaluate();
        }
    }

    public void setScrimState(ScrimState scrimState, float f, ColorExtractor.GradientColors gradientColors) {
        boolean z = this.mForceDarkForScrim;
        boolean z2 = scrimState != ScrimState.BOUNCER && scrimState != ScrimState.BOUNCER_SCRIMMED && f >= 0.1f && !gradientColors.supportsDarkText();
        this.mForceDarkForScrim = z2;
        if (this.mHasLightNavigationBar && z2 != z) {
            reevaluate();
        }
    }

    private boolean animateChange() {
        int mode;
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        if (biometricUnlockController == null || (mode = biometricUnlockController.getMode()) == 2 || mode == 1) {
            return false;
        }
        return true;
    }

    private void updateStatus() {
        ArrayList arrayList = new ArrayList();
        for (AppearanceRegion appearanceRegion : this.mAppearanceRegions) {
            if (isLight(appearanceRegion.getAppearance(), this.mStatusBarMode, 8)) {
                arrayList.add(appearanceRegion.getBounds());
            }
        }
        if (arrayList.isEmpty()) {
            this.mStatusBarIconController.getTransitionsController().setIconsDark(false, animateChange());
        } else if (arrayList.size() == r0) {
            this.mStatusBarIconController.setIconsDarkArea((ArrayList<Rect>) null);
            this.mStatusBarIconController.getTransitionsController().setIconsDark(true, animateChange());
        } else {
            this.mStatusBarIconController.setIconsDarkArea(arrayList);
            this.mStatusBarIconController.getTransitionsController().setIconsDark(true, animateChange());
        }
    }

    private void updateNavigation() {
        LightBarTransitionsController lightBarTransitionsController = this.mNavigationBarController;
        if (lightBarTransitionsController != null && lightBarTransitionsController.supportsIconTintForNavMode(this.mNavigationMode)) {
            this.mNavigationBarController.setIconsDark(this.mNavigationLight, animateChange());
        }
    }

    public void onPowerSaveChanged(boolean z) {
        reevaluate();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("LightBarController: ");
        printWriter.print(" mAppearance=");
        printWriter.println(ViewDebug.flagsToString(InsetsFlags.class, "appearance", this.mAppearance));
        int length = this.mAppearanceRegions.length;
        for (int i = 0; i < length; i++) {
            boolean isLight = isLight(this.mAppearanceRegions[i].getAppearance(), this.mStatusBarMode, 8);
            printWriter.print(" stack #");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.print(this.mAppearanceRegions[i].toString());
            printWriter.print(" isLight=");
            printWriter.println(isLight);
        }
        printWriter.print(" mNavigationLight=");
        printWriter.print(this.mNavigationLight);
        printWriter.print(" mHasLightNavigationBar=");
        printWriter.println(this.mHasLightNavigationBar);
        printWriter.print(" mStatusBarMode=");
        printWriter.print(this.mStatusBarMode);
        printWriter.print(" mNavigationBarMode=");
        printWriter.println(this.mNavigationBarMode);
        printWriter.print(" mForceDarkForScrim=");
        printWriter.print(this.mForceDarkForScrim);
        printWriter.print(" mQsCustomizing=");
        printWriter.print(this.mQsCustomizing);
        printWriter.print(" mDirectReplying=");
        printWriter.println(this.mDirectReplying);
        printWriter.print(" mNavbarColorManagedByIme=");
        printWriter.println(this.mNavbarColorManagedByIme);
        printWriter.println();
        LightBarTransitionsController transitionsController = this.mStatusBarIconController.getTransitionsController();
        if (transitionsController != null) {
            printWriter.println(" StatusBarTransitionsController:");
            transitionsController.dump(printWriter, strArr);
            printWriter.println();
        }
        if (this.mNavigationBarController != null) {
            printWriter.println(" NavigationBarTransitionsController:");
            this.mNavigationBarController.dump(printWriter, strArr);
            printWriter.println();
        }
    }

    public static class Factory {
        private final BatteryController mBatteryController;
        private final DarkIconDispatcher mDarkIconDispatcher;
        private final DumpManager mDumpManager;
        private final NavigationModeController mNavModeController;

        @Inject
        public Factory(DarkIconDispatcher darkIconDispatcher, BatteryController batteryController, NavigationModeController navigationModeController, DumpManager dumpManager) {
            this.mDarkIconDispatcher = darkIconDispatcher;
            this.mBatteryController = batteryController;
            this.mNavModeController = navigationModeController;
            this.mDumpManager = dumpManager;
        }

        public LightBarController create(Context context) {
            return new LightBarController(context, this.mDarkIconDispatcher, this.mBatteryController, this.mNavModeController, this.mDumpManager);
        }
    }
}
