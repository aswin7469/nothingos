package com.nothing.systemui;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.power.TemperatureController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl;
import com.android.systemui.statusbar.phone.ScrimController;
import com.nothing.gamemode.NTGameModeHelper;
import com.nothing.keyguard.LockIconViewControllerEx;
import com.nothing.systemui.assist.AssistManagerEx;
import com.nothing.systemui.biometrics.AuthRippleControllerEx;
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
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import javax.inject.Provider;

public final class NTDependencyEx_MembersInjector implements MembersInjector<NTDependencyEx> {
    private final Provider<AODController> mAODControllerProvider;
    private final Provider<AmbientStateEx> mAmbientStateExProvider;
    private final Provider<AssistManagerEx> mAssistManagerExProvider;
    private final Provider<AuthRippleControllerEx> mAuthRippleControllerExProvider;
    private final Provider<BatteryControllerImplEx> mBatteryControllerImplExProvider;
    private final Provider<BluetoothTileEx> mBluetoothTileExProvider;
    private final Provider<BrightnessControllerEx> mBrighnessControllerExProvider;
    private final Provider<CalendarManager> mCalendarManagerProvider;
    private final Provider<CentralSurfacesImplEx> mCentralSurfacesImplExProvider;
    private final Provider<CommandQueueEx> mCommandQueueExProvider;
    private final Provider<ConfigurationControllerImpl> mConfigurationControllerImplProvider;
    private final Provider<DozeServiceHostEx> mDozeServiceHostExProvider;
    private final Provider<DumpManager> mDumpManagerProvider;
    private final Provider<EdgeBackGestureHandlerEx> mEdgeBackGestureHandlerExProvider;
    private final Provider<FaceRecognitionController> mFaceRecognitionControllerProvider;
    private final Provider<FalsingManager> mFalsingManagerProvider;
    private final Provider<HeadsUpControllerEx> mHeadsUpControllerExProvider;
    private final Provider<InternetTileEx> mInternetTileExProvider;
    private final Provider<KeyButtonViewEx> mKeyButtonViewExProvider;
    private final Provider<KeyguardIndicationControllerEx> mKeyguardIndicationControllerExProvider;
    private final Provider<KeyguardUpdateMonitorEx> mKeyguardUpdateMonitorExProvider;
    private final Provider<KeyguardViewMediatorEx> mKeyguardViewMediatorExProvider;
    private final Provider<KeyguardWeatherController> mKeyguardWeatherControllerProvider;
    private final Provider<LiftWakeGestureController> mLiftWakeGestureControllerProvider;
    private final Provider<LockIconViewControllerEx> mLockIconViewControllerExProvider;
    private final Provider<LockscreenShadeTransitionController> mLockscreenShadeTransitionControllerProvider;
    private final Provider<MediaDataManager> mMediaDataManagerProvider;
    private final Provider<MobileSignalControllerEx> mMobileSignalControllerExProvider;
    private final Provider<NTColorController> mNTColorControllerProvider;
    private final Provider<NTGameModeHelper> mNTGameModeHelperProvider;
    private final Provider<NTLightweightHeadsupManager> mNTLightweightHeadsupManagerProvider;
    private final Provider<NavigationBarControllerEx> mNavigationBarControllerExProvider;
    private final Provider<NavigationBarEx> mNavigationBarExProvider;
    private final Provider<NavigationBarInflaterViewEx> mNavigationBarInflaterViewExProvider;
    private final Provider<NavigationBarViewEx> mNavigationBarViewExProvider;
    private final Provider<NavigationModeControllerEx> mNavigationModeControllerExProvider;
    private final Provider<NfcController> mNfcControllerProvider;
    private final Provider<NotificationRoundnessManager> mNotificationRoundnessManagerProvider;
    private final Provider<OngoingPrivacyChipEx> mOngoingPrivacyChipExProvider;
    private final Provider<PrivacyDialogControllerEx> mPrivacyDialogControllerExProvider;
    private final Provider<PrivacyDialogEx> mPrivacyDialogExProvider;
    private final Provider<QSFragmentEx> mQSFragmentExProvider;
    private final Provider<QSIconViewImplEx> mQSIconViewImplExProvider;
    private final Provider<QSTileHostEx> mQSTileHostExProvider;
    private final Provider<QSTileImplEx> mQSTileImplExProvider;
    private final Provider<QSTileViewImplEx> mQSTileViewImplExProvider;
    private final Provider<QuickStatusBarHeaderEx> mQuickStatusBarHeaderExProvider;
    private final Provider<ScrimController> mScrimControllerProvider;
    private final Provider<TemperatureController> mTemperatureControllerProvider;
    private final Provider<TeslaInfoController> mTeslaInfoControllerProvider;
    private final Provider<TileLayoutEx> mTileLayoutExProvider;
    private final Provider<VolumeDialogImplEx> mVolumeDialogImplExProvider;
    private final Provider<WifiSignalControllerEx> mWifiSignalControllerExProvider;

    public NTDependencyEx_MembersInjector(Provider<DumpManager> provider, Provider<BatteryControllerImplEx> provider2, Provider<BluetoothTileEx> provider3, Provider<BrightnessControllerEx> provider4, Provider<InternetTileEx> provider5, Provider<NTColorController> provider6, Provider<VolumeDialogImplEx> provider7, Provider<WifiSignalControllerEx> provider8, Provider<OngoingPrivacyChipEx> provider9, Provider<PrivacyDialogEx> provider10, Provider<PrivacyDialogControllerEx> provider11, Provider<QSFragmentEx> provider12, Provider<QSIconViewImplEx> provider13, Provider<QSTileHostEx> provider14, Provider<QSTileImplEx> provider15, Provider<QSTileViewImplEx> provider16, Provider<QuickStatusBarHeaderEx> provider17, Provider<TileLayoutEx> provider18, Provider<NavigationBarControllerEx> provider19, Provider<NavigationBarEx> provider20, Provider<NavigationBarViewEx> provider21, Provider<NavigationBarInflaterViewEx> provider22, Provider<NavigationModeControllerEx> provider23, Provider<KeyButtonViewEx> provider24, Provider<EdgeBackGestureHandlerEx> provider25, Provider<KeyguardWeatherController> provider26, Provider<AODController> provider27, Provider<LiftWakeGestureController> provider28, Provider<CentralSurfacesImplEx> provider29, Provider<DozeServiceHostEx> provider30, Provider<NfcController> provider31, Provider<TeslaInfoController> provider32, Provider<KeyguardUpdateMonitorEx> provider33, Provider<KeyguardViewMediatorEx> provider34, Provider<FaceRecognitionController> provider35, Provider<NTLightweightHeadsupManager> provider36, Provider<NTGameModeHelper> provider37, Provider<HeadsUpControllerEx> provider38, Provider<FalsingManager> provider39, Provider<TemperatureController> provider40, Provider<KeyguardIndicationControllerEx> provider41, Provider<MobileSignalControllerEx> provider42, Provider<AmbientStateEx> provider43, Provider<CommandQueueEx> provider44, Provider<LockscreenShadeTransitionController> provider45, Provider<ScrimController> provider46, Provider<CalendarManager> provider47, Provider<LockIconViewControllerEx> provider48, Provider<MediaDataManager> provider49, Provider<ConfigurationControllerImpl> provider50, Provider<AssistManagerEx> provider51, Provider<NotificationRoundnessManager> provider52, Provider<AuthRippleControllerEx> provider53) {
        this.mDumpManagerProvider = provider;
        this.mBatteryControllerImplExProvider = provider2;
        this.mBluetoothTileExProvider = provider3;
        this.mBrighnessControllerExProvider = provider4;
        this.mInternetTileExProvider = provider5;
        this.mNTColorControllerProvider = provider6;
        this.mVolumeDialogImplExProvider = provider7;
        this.mWifiSignalControllerExProvider = provider8;
        this.mOngoingPrivacyChipExProvider = provider9;
        this.mPrivacyDialogExProvider = provider10;
        this.mPrivacyDialogControllerExProvider = provider11;
        this.mQSFragmentExProvider = provider12;
        this.mQSIconViewImplExProvider = provider13;
        this.mQSTileHostExProvider = provider14;
        this.mQSTileImplExProvider = provider15;
        this.mQSTileViewImplExProvider = provider16;
        this.mQuickStatusBarHeaderExProvider = provider17;
        this.mTileLayoutExProvider = provider18;
        this.mNavigationBarControllerExProvider = provider19;
        this.mNavigationBarExProvider = provider20;
        this.mNavigationBarViewExProvider = provider21;
        this.mNavigationBarInflaterViewExProvider = provider22;
        this.mNavigationModeControllerExProvider = provider23;
        this.mKeyButtonViewExProvider = provider24;
        this.mEdgeBackGestureHandlerExProvider = provider25;
        this.mKeyguardWeatherControllerProvider = provider26;
        this.mAODControllerProvider = provider27;
        this.mLiftWakeGestureControllerProvider = provider28;
        this.mCentralSurfacesImplExProvider = provider29;
        this.mDozeServiceHostExProvider = provider30;
        this.mNfcControllerProvider = provider31;
        this.mTeslaInfoControllerProvider = provider32;
        this.mKeyguardUpdateMonitorExProvider = provider33;
        this.mKeyguardViewMediatorExProvider = provider34;
        this.mFaceRecognitionControllerProvider = provider35;
        this.mNTLightweightHeadsupManagerProvider = provider36;
        this.mNTGameModeHelperProvider = provider37;
        this.mHeadsUpControllerExProvider = provider38;
        this.mFalsingManagerProvider = provider39;
        this.mTemperatureControllerProvider = provider40;
        this.mKeyguardIndicationControllerExProvider = provider41;
        this.mMobileSignalControllerExProvider = provider42;
        this.mAmbientStateExProvider = provider43;
        this.mCommandQueueExProvider = provider44;
        this.mLockscreenShadeTransitionControllerProvider = provider45;
        this.mScrimControllerProvider = provider46;
        this.mCalendarManagerProvider = provider47;
        this.mLockIconViewControllerExProvider = provider48;
        this.mMediaDataManagerProvider = provider49;
        this.mConfigurationControllerImplProvider = provider50;
        this.mAssistManagerExProvider = provider51;
        this.mNotificationRoundnessManagerProvider = provider52;
        this.mAuthRippleControllerExProvider = provider53;
    }

    public static MembersInjector<NTDependencyEx> create(Provider<DumpManager> provider, Provider<BatteryControllerImplEx> provider2, Provider<BluetoothTileEx> provider3, Provider<BrightnessControllerEx> provider4, Provider<InternetTileEx> provider5, Provider<NTColorController> provider6, Provider<VolumeDialogImplEx> provider7, Provider<WifiSignalControllerEx> provider8, Provider<OngoingPrivacyChipEx> provider9, Provider<PrivacyDialogEx> provider10, Provider<PrivacyDialogControllerEx> provider11, Provider<QSFragmentEx> provider12, Provider<QSIconViewImplEx> provider13, Provider<QSTileHostEx> provider14, Provider<QSTileImplEx> provider15, Provider<QSTileViewImplEx> provider16, Provider<QuickStatusBarHeaderEx> provider17, Provider<TileLayoutEx> provider18, Provider<NavigationBarControllerEx> provider19, Provider<NavigationBarEx> provider20, Provider<NavigationBarViewEx> provider21, Provider<NavigationBarInflaterViewEx> provider22, Provider<NavigationModeControllerEx> provider23, Provider<KeyButtonViewEx> provider24, Provider<EdgeBackGestureHandlerEx> provider25, Provider<KeyguardWeatherController> provider26, Provider<AODController> provider27, Provider<LiftWakeGestureController> provider28, Provider<CentralSurfacesImplEx> provider29, Provider<DozeServiceHostEx> provider30, Provider<NfcController> provider31, Provider<TeslaInfoController> provider32, Provider<KeyguardUpdateMonitorEx> provider33, Provider<KeyguardViewMediatorEx> provider34, Provider<FaceRecognitionController> provider35, Provider<NTLightweightHeadsupManager> provider36, Provider<NTGameModeHelper> provider37, Provider<HeadsUpControllerEx> provider38, Provider<FalsingManager> provider39, Provider<TemperatureController> provider40, Provider<KeyguardIndicationControllerEx> provider41, Provider<MobileSignalControllerEx> provider42, Provider<AmbientStateEx> provider43, Provider<CommandQueueEx> provider44, Provider<LockscreenShadeTransitionController> provider45, Provider<ScrimController> provider46, Provider<CalendarManager> provider47, Provider<LockIconViewControllerEx> provider48, Provider<MediaDataManager> provider49, Provider<ConfigurationControllerImpl> provider50, Provider<AssistManagerEx> provider51, Provider<NotificationRoundnessManager> provider52, Provider<AuthRippleControllerEx> provider53) {
        return new NTDependencyEx_MembersInjector(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34, provider35, provider36, provider37, provider38, provider39, provider40, provider41, provider42, provider43, provider44, provider45, provider46, provider47, provider48, provider49, provider50, provider51, provider52, provider53);
    }

    public void injectMembers(NTDependencyEx nTDependencyEx) {
        injectMDumpManager(nTDependencyEx, this.mDumpManagerProvider.get());
        injectMBatteryControllerImplEx(nTDependencyEx, DoubleCheck.lazy(this.mBatteryControllerImplExProvider));
        injectMBluetoothTileEx(nTDependencyEx, DoubleCheck.lazy(this.mBluetoothTileExProvider));
        injectMBrighnessControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mBrighnessControllerExProvider));
        injectMInternetTileEx(nTDependencyEx, DoubleCheck.lazy(this.mInternetTileExProvider));
        injectMNTColorController(nTDependencyEx, DoubleCheck.lazy(this.mNTColorControllerProvider));
        injectMVolumeDialogImplEx(nTDependencyEx, DoubleCheck.lazy(this.mVolumeDialogImplExProvider));
        injectMWifiSignalControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mWifiSignalControllerExProvider));
        injectMOngoingPrivacyChipEx(nTDependencyEx, DoubleCheck.lazy(this.mOngoingPrivacyChipExProvider));
        injectMPrivacyDialogEx(nTDependencyEx, DoubleCheck.lazy(this.mPrivacyDialogExProvider));
        injectMPrivacyDialogControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mPrivacyDialogControllerExProvider));
        injectMQSFragmentEx(nTDependencyEx, DoubleCheck.lazy(this.mQSFragmentExProvider));
        injectMQSIconViewImplEx(nTDependencyEx, DoubleCheck.lazy(this.mQSIconViewImplExProvider));
        injectMQSTileHostEx(nTDependencyEx, DoubleCheck.lazy(this.mQSTileHostExProvider));
        injectMQSTileImplEx(nTDependencyEx, DoubleCheck.lazy(this.mQSTileImplExProvider));
        injectMQSTileViewImplEx(nTDependencyEx, DoubleCheck.lazy(this.mQSTileViewImplExProvider));
        injectMQuickStatusBarHeaderEx(nTDependencyEx, DoubleCheck.lazy(this.mQuickStatusBarHeaderExProvider));
        injectMTileLayoutEx(nTDependencyEx, DoubleCheck.lazy(this.mTileLayoutExProvider));
        injectMNavigationBarControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mNavigationBarControllerExProvider));
        injectMNavigationBarEx(nTDependencyEx, DoubleCheck.lazy(this.mNavigationBarExProvider));
        injectMNavigationBarViewEx(nTDependencyEx, DoubleCheck.lazy(this.mNavigationBarViewExProvider));
        injectMNavigationBarInflaterViewEx(nTDependencyEx, DoubleCheck.lazy(this.mNavigationBarInflaterViewExProvider));
        injectMNavigationModeControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mNavigationModeControllerExProvider));
        injectMKeyButtonViewEx(nTDependencyEx, DoubleCheck.lazy(this.mKeyButtonViewExProvider));
        injectMEdgeBackGestureHandlerEx(nTDependencyEx, DoubleCheck.lazy(this.mEdgeBackGestureHandlerExProvider));
        injectMKeyguardWeatherController(nTDependencyEx, DoubleCheck.lazy(this.mKeyguardWeatherControllerProvider));
        injectMAODController(nTDependencyEx, DoubleCheck.lazy(this.mAODControllerProvider));
        injectMLiftWakeGestureController(nTDependencyEx, DoubleCheck.lazy(this.mLiftWakeGestureControllerProvider));
        injectMCentralSurfacesImplEx(nTDependencyEx, DoubleCheck.lazy(this.mCentralSurfacesImplExProvider));
        injectMDozeServiceHostEx(nTDependencyEx, DoubleCheck.lazy(this.mDozeServiceHostExProvider));
        injectMNfcController(nTDependencyEx, DoubleCheck.lazy(this.mNfcControllerProvider));
        injectMTeslaInfoController(nTDependencyEx, DoubleCheck.lazy(this.mTeslaInfoControllerProvider));
        injectMKeyguardUpdateMonitorEx(nTDependencyEx, DoubleCheck.lazy(this.mKeyguardUpdateMonitorExProvider));
        injectMKeyguardViewMediatorEx(nTDependencyEx, DoubleCheck.lazy(this.mKeyguardViewMediatorExProvider));
        injectMFaceRecognitionController(nTDependencyEx, DoubleCheck.lazy(this.mFaceRecognitionControllerProvider));
        injectMNTLightweightHeadsupManager(nTDependencyEx, DoubleCheck.lazy(this.mNTLightweightHeadsupManagerProvider));
        injectMNTGameModeHelper(nTDependencyEx, DoubleCheck.lazy(this.mNTGameModeHelperProvider));
        injectMHeadsUpControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mHeadsUpControllerExProvider));
        injectMFalsingManager(nTDependencyEx, DoubleCheck.lazy(this.mFalsingManagerProvider));
        injectMTemperatureController(nTDependencyEx, DoubleCheck.lazy(this.mTemperatureControllerProvider));
        injectMKeyguardIndicationControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mKeyguardIndicationControllerExProvider));
        injectMMobileSignalControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mMobileSignalControllerExProvider));
        injectMAmbientStateEx(nTDependencyEx, DoubleCheck.lazy(this.mAmbientStateExProvider));
        injectMCommandQueueEx(nTDependencyEx, DoubleCheck.lazy(this.mCommandQueueExProvider));
        injectMLockscreenShadeTransitionController(nTDependencyEx, DoubleCheck.lazy(this.mLockscreenShadeTransitionControllerProvider));
        injectMScrimController(nTDependencyEx, DoubleCheck.lazy(this.mScrimControllerProvider));
        injectMCalendarManager(nTDependencyEx, DoubleCheck.lazy(this.mCalendarManagerProvider));
        injectMLockIconViewControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mLockIconViewControllerExProvider));
        injectMMediaDataManager(nTDependencyEx, DoubleCheck.lazy(this.mMediaDataManagerProvider));
        injectMConfigurationControllerImpl(nTDependencyEx, DoubleCheck.lazy(this.mConfigurationControllerImplProvider));
        injectMAssistManagerEx(nTDependencyEx, DoubleCheck.lazy(this.mAssistManagerExProvider));
        injectMNotificationRoundnessManager(nTDependencyEx, DoubleCheck.lazy(this.mNotificationRoundnessManagerProvider));
        injectMAuthRippleControllerEx(nTDependencyEx, DoubleCheck.lazy(this.mAuthRippleControllerExProvider));
    }

    public static void injectMDumpManager(NTDependencyEx nTDependencyEx, DumpManager dumpManager) {
        nTDependencyEx.mDumpManager = dumpManager;
    }

    public static void injectMBatteryControllerImplEx(NTDependencyEx nTDependencyEx, Lazy<BatteryControllerImplEx> lazy) {
        nTDependencyEx.mBatteryControllerImplEx = lazy;
    }

    public static void injectMBluetoothTileEx(NTDependencyEx nTDependencyEx, Lazy<BluetoothTileEx> lazy) {
        nTDependencyEx.mBluetoothTileEx = lazy;
    }

    public static void injectMBrighnessControllerEx(NTDependencyEx nTDependencyEx, Lazy<BrightnessControllerEx> lazy) {
        nTDependencyEx.mBrighnessControllerEx = lazy;
    }

    public static void injectMInternetTileEx(NTDependencyEx nTDependencyEx, Lazy<InternetTileEx> lazy) {
        nTDependencyEx.mInternetTileEx = lazy;
    }

    public static void injectMNTColorController(NTDependencyEx nTDependencyEx, Lazy<NTColorController> lazy) {
        nTDependencyEx.mNTColorController = lazy;
    }

    public static void injectMVolumeDialogImplEx(NTDependencyEx nTDependencyEx, Lazy<VolumeDialogImplEx> lazy) {
        nTDependencyEx.mVolumeDialogImplEx = lazy;
    }

    public static void injectMWifiSignalControllerEx(NTDependencyEx nTDependencyEx, Lazy<WifiSignalControllerEx> lazy) {
        nTDependencyEx.mWifiSignalControllerEx = lazy;
    }

    public static void injectMOngoingPrivacyChipEx(NTDependencyEx nTDependencyEx, Lazy<OngoingPrivacyChipEx> lazy) {
        nTDependencyEx.mOngoingPrivacyChipEx = lazy;
    }

    public static void injectMPrivacyDialogEx(NTDependencyEx nTDependencyEx, Lazy<PrivacyDialogEx> lazy) {
        nTDependencyEx.mPrivacyDialogEx = lazy;
    }

    public static void injectMPrivacyDialogControllerEx(NTDependencyEx nTDependencyEx, Lazy<PrivacyDialogControllerEx> lazy) {
        nTDependencyEx.mPrivacyDialogControllerEx = lazy;
    }

    public static void injectMQSFragmentEx(NTDependencyEx nTDependencyEx, Lazy<QSFragmentEx> lazy) {
        nTDependencyEx.mQSFragmentEx = lazy;
    }

    public static void injectMQSIconViewImplEx(NTDependencyEx nTDependencyEx, Lazy<QSIconViewImplEx> lazy) {
        nTDependencyEx.mQSIconViewImplEx = lazy;
    }

    public static void injectMQSTileHostEx(NTDependencyEx nTDependencyEx, Lazy<QSTileHostEx> lazy) {
        nTDependencyEx.mQSTileHostEx = lazy;
    }

    public static void injectMQSTileImplEx(NTDependencyEx nTDependencyEx, Lazy<QSTileImplEx> lazy) {
        nTDependencyEx.mQSTileImplEx = lazy;
    }

    public static void injectMQSTileViewImplEx(NTDependencyEx nTDependencyEx, Lazy<QSTileViewImplEx> lazy) {
        nTDependencyEx.mQSTileViewImplEx = lazy;
    }

    public static void injectMQuickStatusBarHeaderEx(NTDependencyEx nTDependencyEx, Lazy<QuickStatusBarHeaderEx> lazy) {
        nTDependencyEx.mQuickStatusBarHeaderEx = lazy;
    }

    public static void injectMTileLayoutEx(NTDependencyEx nTDependencyEx, Lazy<TileLayoutEx> lazy) {
        nTDependencyEx.mTileLayoutEx = lazy;
    }

    public static void injectMNavigationBarControllerEx(NTDependencyEx nTDependencyEx, Lazy<NavigationBarControllerEx> lazy) {
        nTDependencyEx.mNavigationBarControllerEx = lazy;
    }

    public static void injectMNavigationBarEx(NTDependencyEx nTDependencyEx, Lazy<NavigationBarEx> lazy) {
        nTDependencyEx.mNavigationBarEx = lazy;
    }

    public static void injectMNavigationBarViewEx(NTDependencyEx nTDependencyEx, Lazy<NavigationBarViewEx> lazy) {
        nTDependencyEx.mNavigationBarViewEx = lazy;
    }

    public static void injectMNavigationBarInflaterViewEx(NTDependencyEx nTDependencyEx, Lazy<NavigationBarInflaterViewEx> lazy) {
        nTDependencyEx.mNavigationBarInflaterViewEx = lazy;
    }

    public static void injectMNavigationModeControllerEx(NTDependencyEx nTDependencyEx, Lazy<NavigationModeControllerEx> lazy) {
        nTDependencyEx.mNavigationModeControllerEx = lazy;
    }

    public static void injectMKeyButtonViewEx(NTDependencyEx nTDependencyEx, Lazy<KeyButtonViewEx> lazy) {
        nTDependencyEx.mKeyButtonViewEx = lazy;
    }

    public static void injectMEdgeBackGestureHandlerEx(NTDependencyEx nTDependencyEx, Lazy<EdgeBackGestureHandlerEx> lazy) {
        nTDependencyEx.mEdgeBackGestureHandlerEx = lazy;
    }

    public static void injectMKeyguardWeatherController(NTDependencyEx nTDependencyEx, Lazy<KeyguardWeatherController> lazy) {
        nTDependencyEx.mKeyguardWeatherController = lazy;
    }

    public static void injectMAODController(NTDependencyEx nTDependencyEx, Lazy<AODController> lazy) {
        nTDependencyEx.mAODController = lazy;
    }

    public static void injectMLiftWakeGestureController(NTDependencyEx nTDependencyEx, Lazy<LiftWakeGestureController> lazy) {
        nTDependencyEx.mLiftWakeGestureController = lazy;
    }

    public static void injectMCentralSurfacesImplEx(NTDependencyEx nTDependencyEx, Lazy<CentralSurfacesImplEx> lazy) {
        nTDependencyEx.mCentralSurfacesImplEx = lazy;
    }

    public static void injectMDozeServiceHostEx(NTDependencyEx nTDependencyEx, Lazy<DozeServiceHostEx> lazy) {
        nTDependencyEx.mDozeServiceHostEx = lazy;
    }

    public static void injectMNfcController(NTDependencyEx nTDependencyEx, Lazy<NfcController> lazy) {
        nTDependencyEx.mNfcController = lazy;
    }

    public static void injectMTeslaInfoController(NTDependencyEx nTDependencyEx, Lazy<TeslaInfoController> lazy) {
        nTDependencyEx.mTeslaInfoController = lazy;
    }

    public static void injectMKeyguardUpdateMonitorEx(NTDependencyEx nTDependencyEx, Lazy<KeyguardUpdateMonitorEx> lazy) {
        nTDependencyEx.mKeyguardUpdateMonitorEx = lazy;
    }

    public static void injectMKeyguardViewMediatorEx(NTDependencyEx nTDependencyEx, Lazy<KeyguardViewMediatorEx> lazy) {
        nTDependencyEx.mKeyguardViewMediatorEx = lazy;
    }

    public static void injectMFaceRecognitionController(NTDependencyEx nTDependencyEx, Lazy<FaceRecognitionController> lazy) {
        nTDependencyEx.mFaceRecognitionController = lazy;
    }

    public static void injectMNTLightweightHeadsupManager(NTDependencyEx nTDependencyEx, Lazy<NTLightweightHeadsupManager> lazy) {
        nTDependencyEx.mNTLightweightHeadsupManager = lazy;
    }

    public static void injectMNTGameModeHelper(NTDependencyEx nTDependencyEx, Lazy<NTGameModeHelper> lazy) {
        nTDependencyEx.mNTGameModeHelper = lazy;
    }

    public static void injectMHeadsUpControllerEx(NTDependencyEx nTDependencyEx, Lazy<HeadsUpControllerEx> lazy) {
        nTDependencyEx.mHeadsUpControllerEx = lazy;
    }

    public static void injectMFalsingManager(NTDependencyEx nTDependencyEx, Lazy<FalsingManager> lazy) {
        nTDependencyEx.mFalsingManager = lazy;
    }

    public static void injectMTemperatureController(NTDependencyEx nTDependencyEx, Lazy<TemperatureController> lazy) {
        nTDependencyEx.mTemperatureController = lazy;
    }

    public static void injectMKeyguardIndicationControllerEx(NTDependencyEx nTDependencyEx, Lazy<KeyguardIndicationControllerEx> lazy) {
        nTDependencyEx.mKeyguardIndicationControllerEx = lazy;
    }

    public static void injectMMobileSignalControllerEx(NTDependencyEx nTDependencyEx, Lazy<MobileSignalControllerEx> lazy) {
        nTDependencyEx.mMobileSignalControllerEx = lazy;
    }

    public static void injectMAmbientStateEx(NTDependencyEx nTDependencyEx, Lazy<AmbientStateEx> lazy) {
        nTDependencyEx.mAmbientStateEx = lazy;
    }

    public static void injectMCommandQueueEx(NTDependencyEx nTDependencyEx, Lazy<CommandQueueEx> lazy) {
        nTDependencyEx.mCommandQueueEx = lazy;
    }

    public static void injectMLockscreenShadeTransitionController(NTDependencyEx nTDependencyEx, Lazy<LockscreenShadeTransitionController> lazy) {
        nTDependencyEx.mLockscreenShadeTransitionController = lazy;
    }

    public static void injectMScrimController(NTDependencyEx nTDependencyEx, Lazy<ScrimController> lazy) {
        nTDependencyEx.mScrimController = lazy;
    }

    public static void injectMCalendarManager(NTDependencyEx nTDependencyEx, Lazy<CalendarManager> lazy) {
        nTDependencyEx.mCalendarManager = lazy;
    }

    public static void injectMLockIconViewControllerEx(NTDependencyEx nTDependencyEx, Lazy<LockIconViewControllerEx> lazy) {
        nTDependencyEx.mLockIconViewControllerEx = lazy;
    }

    public static void injectMMediaDataManager(NTDependencyEx nTDependencyEx, Lazy<MediaDataManager> lazy) {
        nTDependencyEx.mMediaDataManager = lazy;
    }

    public static void injectMConfigurationControllerImpl(NTDependencyEx nTDependencyEx, Lazy<ConfigurationControllerImpl> lazy) {
        nTDependencyEx.mConfigurationControllerImpl = lazy;
    }

    public static void injectMAssistManagerEx(NTDependencyEx nTDependencyEx, Lazy<AssistManagerEx> lazy) {
        nTDependencyEx.mAssistManagerEx = lazy;
    }

    public static void injectMNotificationRoundnessManager(NTDependencyEx nTDependencyEx, Lazy<NotificationRoundnessManager> lazy) {
        nTDependencyEx.mNotificationRoundnessManager = lazy;
    }

    public static void injectMAuthRippleControllerEx(NTDependencyEx nTDependencyEx, Lazy<AuthRippleControllerEx> lazy) {
        nTDependencyEx.mAuthRippleControllerEx = lazy;
    }
}
