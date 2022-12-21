package com.nothing.systemui;

import android.util.ArrayMap;
import com.android.internal.util.Preconditions;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.power.TemperatureController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl;
import com.android.systemui.statusbar.phone.ScrimController;
import com.nothing.gamemode.NTGameModeHelper;
import com.nothing.keyguard.LockIconViewControllerEx;
import com.nothing.systemui.biometrics.NTColorController;
import com.nothing.systemui.doze.AODController;
import com.nothing.systemui.doze.LiftWakeGestureController;
import com.nothing.systemui.facerecognition.FaceRecognitionController;
import com.nothing.systemui.keyguard.KeyguardUpdateMonitorEx;
import com.nothing.systemui.keyguard.KeyguardViewMediatorEx;
import com.nothing.systemui.keyguard.calendar.CalendarManager;
import com.nothing.systemui.keyguard.weather.KeyguardWeatherController;
import com.nothing.systemui.navigationbar.NavigationBarControllerEx;
import com.nothing.systemui.navigationbar.NavigationBarEx;
import com.nothing.systemui.navigationbar.NavigationBarInflaterViewEx;
import com.nothing.systemui.navigationbar.NavigationBarViewEx;
import com.nothing.systemui.navigationbar.NavigationModeControllerEx;
import com.nothing.systemui.navigationbar.buttons.KeyButtonViewEx;
import com.nothing.systemui.navigationbar.gestural.EdgeBackGestureHandlerEx;
import com.nothing.systemui.p024qs.QSFragmentEx;
import com.nothing.systemui.p024qs.QSTileHostEx;
import com.nothing.systemui.p024qs.QuickStatusBarHeaderEx;
import com.nothing.systemui.p024qs.TileLayoutEx;
import com.nothing.systemui.p024qs.tileimpl.QSIconViewImplEx;
import com.nothing.systemui.p024qs.tileimpl.QSTileImplEx;
import com.nothing.systemui.p024qs.tileimpl.QSTileViewImplEx;
import com.nothing.systemui.p024qs.tiles.BluetoothTileEx;
import com.nothing.systemui.p024qs.tiles.InternetTileEx;
import com.nothing.systemui.privacy.OngoingPrivacyChipEx;
import com.nothing.systemui.privacy.PrivacyDialogControllerEx;
import com.nothing.systemui.privacy.PrivacyDialogEx;
import com.nothing.systemui.settings.brightness.BrightnessControllerEx;
import com.nothing.systemui.statusbar.CommandQueueEx;
import com.nothing.systemui.statusbar.KeyguardIndicationControllerEx;
import com.nothing.systemui.statusbar.connectivity.MobileSignalControllerEx;
import com.nothing.systemui.statusbar.connectivity.WifiSignalControllerEx;
import com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager;
import com.nothing.systemui.statusbar.notification.interruption.HeadsUpControllerEx;
import com.nothing.systemui.statusbar.notification.stack.AmbientStateEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import com.nothing.systemui.statusbar.phone.DozeServiceHostEx;
import com.nothing.systemui.statusbar.policy.BatteryControllerImplEx;
import com.nothing.systemui.statusbar.policy.NfcController;
import com.nothing.systemui.statusbar.policy.TeslaInfoController;
import com.nothing.systemui.volume.VolumeDialogImplEx;
import dagger.Lazy;
import java.util.Objects;
import java.util.function.Consumer;
import javax.inject.Inject;

@SysUISingleton
public class NTDependencyEx {
    private static NTDependencyEx sDependency;
    @Inject
    Lazy<AODController> mAODController;
    @Inject
    Lazy<AmbientStateEx> mAmbientStateEx;
    @Inject
    Lazy<BatteryControllerImplEx> mBatteryControllerImplEx;
    @Inject
    Lazy<BluetoothTileEx> mBluetoothTileEx;
    @Inject
    Lazy<BrightnessControllerEx> mBrighnessControllerEx;
    @Inject
    Lazy<CalendarManager> mCalendarManager;
    @Inject
    Lazy<CentralSurfacesImplEx> mCentralSurfacesImplEx;
    @Inject
    Lazy<CommandQueueEx> mCommandQueueEx;
    @Inject
    Lazy<ConfigurationControllerImpl> mConfigurationControllerImpl;
    private final ArrayMap<Object, Object> mDependencies = new ArrayMap<>();
    @Inject
    Lazy<DozeServiceHostEx> mDozeServiceHostEx;
    @Inject
    DumpManager mDumpManager;
    @Inject
    Lazy<EdgeBackGestureHandlerEx> mEdgeBackGestureHandlerEx;
    @Inject
    Lazy<FaceRecognitionController> mFaceRecognitionController;
    @Inject
    Lazy<FalsingManager> mFalsingManager;
    @Inject
    Lazy<HeadsUpControllerEx> mHeadsUpControllerEx;
    @Inject
    Lazy<InternetTileEx> mInternetTileEx;
    @Inject
    Lazy<KeyButtonViewEx> mKeyButtonViewEx;
    @Inject
    Lazy<KeyguardIndicationControllerEx> mKeyguardIndicationControllerEx;
    @Inject
    Lazy<KeyguardUpdateMonitorEx> mKeyguardUpdateMonitorEx;
    @Inject
    Lazy<KeyguardViewMediatorEx> mKeyguardViewMediatorEx;
    @Inject
    Lazy<KeyguardWeatherController> mKeyguardWeatherController;
    @Inject
    Lazy<LiftWakeGestureController> mLiftWakeGestureController;
    @Inject
    Lazy<LockIconViewControllerEx> mLockIconViewControllerEx;
    @Inject
    Lazy<LockscreenShadeTransitionController> mLockscreenShadeTransitionController;
    @Inject
    Lazy<MediaDataManager> mMediaDataManager;
    @Inject
    Lazy<MobileSignalControllerEx> mMobileSignalControllerEx;
    @Inject
    Lazy<NTColorController> mNTColorController;
    @Inject
    Lazy<NTGameModeHelper> mNTGameModeHelper;
    @Inject
    Lazy<NTLightweightHeadsupManager> mNTLightweightHeadsupManager;
    @Inject
    Lazy<NavigationBarControllerEx> mNavigationBarControllerEx;
    @Inject
    Lazy<NavigationBarEx> mNavigationBarEx;
    @Inject
    Lazy<NavigationBarInflaterViewEx> mNavigationBarInflaterViewEx;
    @Inject
    Lazy<NavigationBarViewEx> mNavigationBarViewEx;
    @Inject
    Lazy<NavigationModeControllerEx> mNavigationModeControllerEx;
    @Inject
    Lazy<NfcController> mNfcController;
    @Inject
    Lazy<OngoingPrivacyChipEx> mOngoingPrivacyChipEx;
    @Inject
    Lazy<PrivacyDialogControllerEx> mPrivacyDialogControllerEx;
    @Inject
    Lazy<PrivacyDialogEx> mPrivacyDialogEx;
    private final ArrayMap<Object, LazyDependencyCreator> mProviders = new ArrayMap<>();
    @Inject
    Lazy<QSFragmentEx> mQSFragmentEx;
    @Inject
    Lazy<QSIconViewImplEx> mQSIconViewImplEx;
    @Inject
    Lazy<QSTileHostEx> mQSTileHostEx;
    @Inject
    Lazy<QSTileImplEx> mQSTileImplEx;
    @Inject
    Lazy<QSTileViewImplEx> mQSTileViewImplEx;
    @Inject
    Lazy<QuickStatusBarHeaderEx> mQuickStatusBarHeaderEx;
    @Inject
    Lazy<ScrimController> mScrimController;
    @Inject
    Lazy<TemperatureController> mTemperatureController;
    @Inject
    Lazy<TeslaInfoController> mTeslaInfoController;
    @Inject
    Lazy<TileLayoutEx> mTileLayoutEx;
    @Inject
    Lazy<VolumeDialogImplEx> mVolumeDialogImplEx;
    @Inject
    Lazy<WifiSignalControllerEx> mWifiSignalControllerEx;

    private interface LazyDependencyCreator<T> {
        T createDependency();
    }

    public void start() {
        Lazy<BluetoothTileEx> lazy = this.mBluetoothTileEx;
        Objects.requireNonNull(lazy);
        this.mProviders.put(BluetoothTileEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy));
        Lazy<BrightnessControllerEx> lazy2 = this.mBrighnessControllerEx;
        Objects.requireNonNull(lazy2);
        this.mProviders.put(BrightnessControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy2));
        Lazy<InternetTileEx> lazy3 = this.mInternetTileEx;
        Objects.requireNonNull(lazy3);
        this.mProviders.put(InternetTileEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy3));
        Lazy<VolumeDialogImplEx> lazy4 = this.mVolumeDialogImplEx;
        Objects.requireNonNull(lazy4);
        this.mProviders.put(VolumeDialogImplEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy4));
        Lazy<NTColorController> lazy5 = this.mNTColorController;
        Objects.requireNonNull(lazy5);
        this.mProviders.put(NTColorController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy5));
        Lazy<WifiSignalControllerEx> lazy6 = this.mWifiSignalControllerEx;
        Objects.requireNonNull(lazy6);
        this.mProviders.put(WifiSignalControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy6));
        Lazy<BatteryControllerImplEx> lazy7 = this.mBatteryControllerImplEx;
        Objects.requireNonNull(lazy7);
        this.mProviders.put(BatteryControllerImplEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy7));
        Lazy<OngoingPrivacyChipEx> lazy8 = this.mOngoingPrivacyChipEx;
        Objects.requireNonNull(lazy8);
        this.mProviders.put(OngoingPrivacyChipEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy8));
        Lazy<PrivacyDialogEx> lazy9 = this.mPrivacyDialogEx;
        Objects.requireNonNull(lazy9);
        this.mProviders.put(PrivacyDialogEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy9));
        Lazy<PrivacyDialogControllerEx> lazy10 = this.mPrivacyDialogControllerEx;
        Objects.requireNonNull(lazy10);
        this.mProviders.put(PrivacyDialogControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy10));
        Lazy<QuickStatusBarHeaderEx> lazy11 = this.mQuickStatusBarHeaderEx;
        Objects.requireNonNull(lazy11);
        this.mProviders.put(QuickStatusBarHeaderEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy11));
        Lazy<NavigationBarControllerEx> lazy12 = this.mNavigationBarControllerEx;
        Objects.requireNonNull(lazy12);
        this.mProviders.put(NavigationBarControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy12));
        Lazy<NavigationBarEx> lazy13 = this.mNavigationBarEx;
        Objects.requireNonNull(lazy13);
        this.mProviders.put(NavigationBarEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy13));
        Lazy<NavigationBarViewEx> lazy14 = this.mNavigationBarViewEx;
        Objects.requireNonNull(lazy14);
        this.mProviders.put(NavigationBarViewEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy14));
        Lazy<NavigationBarInflaterViewEx> lazy15 = this.mNavigationBarInflaterViewEx;
        Objects.requireNonNull(lazy15);
        this.mProviders.put(NavigationBarInflaterViewEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy15));
        Lazy<TileLayoutEx> lazy16 = this.mTileLayoutEx;
        Objects.requireNonNull(lazy16);
        this.mProviders.put(TileLayoutEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy16));
        Lazy<QSFragmentEx> lazy17 = this.mQSFragmentEx;
        Objects.requireNonNull(lazy17);
        this.mProviders.put(QSFragmentEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy17));
        Lazy<QSIconViewImplEx> lazy18 = this.mQSIconViewImplEx;
        Objects.requireNonNull(lazy18);
        this.mProviders.put(QSIconViewImplEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy18));
        Lazy<QSTileHostEx> lazy19 = this.mQSTileHostEx;
        Objects.requireNonNull(lazy19);
        this.mProviders.put(QSTileHostEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy19));
        Lazy<QSTileImplEx> lazy20 = this.mQSTileImplEx;
        Objects.requireNonNull(lazy20);
        this.mProviders.put(QSTileImplEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy20));
        Lazy<QSTileViewImplEx> lazy21 = this.mQSTileViewImplEx;
        Objects.requireNonNull(lazy21);
        this.mProviders.put(QSTileViewImplEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy21));
        Lazy<NavigationBarControllerEx> lazy22 = this.mNavigationBarControllerEx;
        Objects.requireNonNull(lazy22);
        this.mProviders.put(NavigationBarControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy22));
        Lazy<NavigationBarViewEx> lazy23 = this.mNavigationBarViewEx;
        Objects.requireNonNull(lazy23);
        this.mProviders.put(NavigationBarViewEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy23));
        Lazy<NavigationBarInflaterViewEx> lazy24 = this.mNavigationBarInflaterViewEx;
        Objects.requireNonNull(lazy24);
        this.mProviders.put(NavigationBarInflaterViewEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy24));
        Lazy<NavigationModeControllerEx> lazy25 = this.mNavigationModeControllerEx;
        Objects.requireNonNull(lazy25);
        this.mProviders.put(NavigationModeControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy25));
        Lazy<KeyButtonViewEx> lazy26 = this.mKeyButtonViewEx;
        Objects.requireNonNull(lazy26);
        this.mProviders.put(KeyButtonViewEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy26));
        Lazy<EdgeBackGestureHandlerEx> lazy27 = this.mEdgeBackGestureHandlerEx;
        Objects.requireNonNull(lazy27);
        this.mProviders.put(EdgeBackGestureHandlerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy27));
        Lazy<KeyguardWeatherController> lazy28 = this.mKeyguardWeatherController;
        Objects.requireNonNull(lazy28);
        this.mProviders.put(KeyguardWeatherController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy28));
        Lazy<AODController> lazy29 = this.mAODController;
        Objects.requireNonNull(lazy29);
        this.mProviders.put(AODController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy29));
        Lazy<LiftWakeGestureController> lazy30 = this.mLiftWakeGestureController;
        Objects.requireNonNull(lazy30);
        this.mProviders.put(LiftWakeGestureController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy30));
        Lazy<CentralSurfacesImplEx> lazy31 = this.mCentralSurfacesImplEx;
        Objects.requireNonNull(lazy31);
        this.mProviders.put(CentralSurfacesImplEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy31));
        Lazy<DozeServiceHostEx> lazy32 = this.mDozeServiceHostEx;
        Objects.requireNonNull(lazy32);
        this.mProviders.put(DozeServiceHostEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy32));
        Lazy<NfcController> lazy33 = this.mNfcController;
        Objects.requireNonNull(lazy33);
        this.mProviders.put(NfcController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy33));
        Lazy<TeslaInfoController> lazy34 = this.mTeslaInfoController;
        Objects.requireNonNull(lazy34);
        this.mProviders.put(TeslaInfoController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy34));
        Lazy<KeyguardUpdateMonitorEx> lazy35 = this.mKeyguardUpdateMonitorEx;
        Objects.requireNonNull(lazy35);
        this.mProviders.put(KeyguardUpdateMonitorEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy35));
        Lazy<KeyguardViewMediatorEx> lazy36 = this.mKeyguardViewMediatorEx;
        Objects.requireNonNull(lazy36);
        this.mProviders.put(KeyguardViewMediatorEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy36));
        Lazy<FaceRecognitionController> lazy37 = this.mFaceRecognitionController;
        Objects.requireNonNull(lazy37);
        this.mProviders.put(FaceRecognitionController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy37));
        Lazy<NTLightweightHeadsupManager> lazy38 = this.mNTLightweightHeadsupManager;
        Objects.requireNonNull(lazy38);
        this.mProviders.put(NTLightweightHeadsupManager.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy38));
        Lazy<NTGameModeHelper> lazy39 = this.mNTGameModeHelper;
        Objects.requireNonNull(lazy39);
        this.mProviders.put(NTGameModeHelper.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy39));
        Lazy<HeadsUpControllerEx> lazy40 = this.mHeadsUpControllerEx;
        Objects.requireNonNull(lazy40);
        this.mProviders.put(HeadsUpControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy40));
        Lazy<FalsingManager> lazy41 = this.mFalsingManager;
        Objects.requireNonNull(lazy41);
        this.mProviders.put(FalsingManager.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy41));
        Lazy<TemperatureController> lazy42 = this.mTemperatureController;
        Objects.requireNonNull(lazy42);
        this.mProviders.put(TemperatureController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy42));
        Lazy<KeyguardIndicationControllerEx> lazy43 = this.mKeyguardIndicationControllerEx;
        Objects.requireNonNull(lazy43);
        this.mProviders.put(KeyguardIndicationControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy43));
        Lazy<MobileSignalControllerEx> lazy44 = this.mMobileSignalControllerEx;
        Objects.requireNonNull(lazy44);
        this.mProviders.put(MobileSignalControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy44));
        Lazy<AmbientStateEx> lazy45 = this.mAmbientStateEx;
        Objects.requireNonNull(lazy45);
        this.mProviders.put(AmbientStateEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy45));
        Lazy<CommandQueueEx> lazy46 = this.mCommandQueueEx;
        Objects.requireNonNull(lazy46);
        this.mProviders.put(CommandQueueEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy46));
        Lazy<LockscreenShadeTransitionController> lazy47 = this.mLockscreenShadeTransitionController;
        Objects.requireNonNull(lazy47);
        this.mProviders.put(LockscreenShadeTransitionController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy47));
        Lazy<ScrimController> lazy48 = this.mScrimController;
        Objects.requireNonNull(lazy48);
        this.mProviders.put(ScrimController.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy48));
        Lazy<CalendarManager> lazy49 = this.mCalendarManager;
        Objects.requireNonNull(lazy49);
        this.mProviders.put(CalendarManager.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy49));
        Lazy<LockIconViewControllerEx> lazy50 = this.mLockIconViewControllerEx;
        Objects.requireNonNull(lazy50);
        this.mProviders.put(LockIconViewControllerEx.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy50));
        Lazy<MediaDataManager> lazy51 = this.mMediaDataManager;
        Objects.requireNonNull(lazy51);
        this.mProviders.put(MediaDataManager.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy51));
        Lazy<ConfigurationControllerImpl> lazy52 = this.mConfigurationControllerImpl;
        Objects.requireNonNull(lazy52);
        this.mProviders.put(ConfigurationControllerImpl.class, new NTDependencyEx$$ExternalSyntheticLambda0(lazy52));
        setInstance(this);
    }

    public static void setInstance(NTDependencyEx nTDependencyEx) {
        sDependency = nTDependencyEx;
    }

    /* access modifiers changed from: protected */
    public final <T> T getDependency(Class<T> cls) {
        return getDependencyInner(cls);
    }

    /* access modifiers changed from: protected */
    public final <T> T getDependency(DependencyKey<T> dependencyKey) {
        return getDependencyInner(dependencyKey);
    }

    private synchronized <T> T getDependencyInner(Object obj) {
        T t;
        t = this.mDependencies.get(obj);
        if (t == null) {
            t = createDependency(obj);
            this.mDependencies.put(obj, t);
        }
        return t;
    }

    public <T> T createDependency(Object obj) {
        Preconditions.checkArgument((obj instanceof DependencyKey) || (obj instanceof Class));
        LazyDependencyCreator lazyDependencyCreator = this.mProviders.get(obj);
        if (lazyDependencyCreator != null) {
            return lazyDependencyCreator.createDependency();
        }
        throw new IllegalArgumentException("Unsupported dependency " + obj + ". " + this.mProviders.size() + " providers known.");
    }

    private <T> void destroyDependency(Class<T> cls, Consumer<T> consumer) {
        Object remove = this.mDependencies.remove(cls);
        if (remove instanceof Dumpable) {
            this.mDumpManager.unregisterDumpable(remove.getClass().getName());
        }
        if (remove != null && consumer != null) {
            consumer.accept(remove);
        }
    }

    public static void clearDependencies() {
        sDependency = null;
    }

    public static <T> void destroy(Class<T> cls, Consumer<T> consumer) {
        sDependency.destroyDependency(cls, consumer);
    }

    @Deprecated
    public static <T> T get(Class<T> cls) {
        return sDependency.getDependency(cls);
    }

    @Deprecated
    public static <T> T get(DependencyKey<T> dependencyKey) {
        return sDependency.getDependency(dependencyKey);
    }

    public static final class DependencyKey<V> {
        private final String mDisplayName;

        public DependencyKey(String str) {
            this.mDisplayName = str;
        }

        public String toString() {
            return this.mDisplayName;
        }
    }
}
