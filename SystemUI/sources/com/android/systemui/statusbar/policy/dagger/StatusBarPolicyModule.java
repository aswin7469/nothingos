package com.android.systemui.statusbar.policy.dagger;

import android.content.Context;
import android.content.res.Resources;
import android.os.UserManager;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.BluetoothControllerImpl;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.CastControllerImpl;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.statusbar.policy.DevicePostureControllerImpl;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
import com.android.systemui.statusbar.policy.FlashlightController;
import com.android.systemui.statusbar.policy.FlashlightControllerImpl;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.HotspotControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.NextAlarmControllerImpl;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.statusbar.policy.SecurityControllerImpl;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.statusbar.policy.WalletControllerImpl;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import com.nothing.systemui.keyguard.weather.KeyguardWeatherController;
import com.nothing.systemui.keyguard.weather.KeyguardWeatherControllerImpl;
import com.nothing.systemui.statusbar.policy.BatteryShareController;
import com.nothing.systemui.statusbar.policy.BatteryShareControllerImpl;
import com.nothing.systemui.statusbar.policy.GlyphsController;
import com.nothing.systemui.statusbar.policy.GlyphsControllerImpl;
import com.nothing.systemui.statusbar.policy.NfcController;
import com.nothing.systemui.statusbar.policy.NfcControllerImpl;
import com.nothing.systemui.statusbar.policy.TeslaInfoController;
import com.nothing.systemui.statusbar.policy.TeslaInfoControllerImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import javax.inject.Named;

@Module
public interface StatusBarPolicyModule {
    public static final String DEVICE_STATE_ROTATION_LOCK_DEFAULTS = "DEVICE_STATE_ROTATION_LOCK_DEFAULTS";

    @Binds
    ConfigurationController bindConfigurationController(ConfigurationControllerImpl configurationControllerImpl);

    @Binds
    AccessPointController provideAccessPointController(AccessPointControllerImpl accessPointControllerImpl);

    @Binds
    BatteryShareController provideBatteryShareController(BatteryShareControllerImpl batteryShareControllerImpl);

    @Binds
    BluetoothController provideBluetoothController(BluetoothControllerImpl bluetoothControllerImpl);

    @Binds
    CastController provideCastController(CastControllerImpl castControllerImpl);

    @Binds
    DeviceControlsController provideDeviceControlsController(DeviceControlsControllerImpl deviceControlsControllerImpl);

    @Binds
    DevicePostureController provideDevicePostureController(DevicePostureControllerImpl devicePostureControllerImpl);

    @Binds
    ExtensionController provideExtensionController(ExtensionControllerImpl extensionControllerImpl);

    @Binds
    FlashlightController provideFlashlightController(FlashlightControllerImpl flashlightControllerImpl);

    @Binds
    GlyphsController provideGlyphsController(GlyphsControllerImpl glyphsControllerImpl);

    @Binds
    HotspotController provideHotspotController(HotspotControllerImpl hotspotControllerImpl);

    @Binds
    KeyguardStateController provideKeyguardMonitor(KeyguardStateControllerImpl keyguardStateControllerImpl);

    @Binds
    KeyguardWeatherController provideKeyguardWeatherController(KeyguardWeatherControllerImpl keyguardWeatherControllerImpl);

    @Binds
    LocationController provideLocationController(LocationControllerImpl locationControllerImpl);

    @Binds
    NetworkController provideNetworkController(NetworkControllerImpl networkControllerImpl);

    @Binds
    NextAlarmController provideNextAlarmController(NextAlarmControllerImpl nextAlarmControllerImpl);

    @Binds
    NfcController provideNfcController(NfcControllerImpl nfcControllerImpl);

    @Binds
    RotationLockController provideRotationLockController(RotationLockControllerImpl rotationLockControllerImpl);

    @Binds
    SecurityController provideSecurityController(SecurityControllerImpl securityControllerImpl);

    @Binds
    TeslaInfoController provideTeslaInfoController(TeslaInfoControllerImpl teslaInfoControllerImpl);

    @Binds
    UserInfoController provideUserInfoContrller(UserInfoControllerImpl userInfoControllerImpl);

    @Binds
    WalletController provideWalletController(WalletControllerImpl walletControllerImpl);

    @Binds
    ZenModeController provideZenModeController(ZenModeControllerImpl zenModeControllerImpl);

    @SysUISingleton
    @Provides
    static AccessPointControllerImpl provideAccessPointControllerImpl(UserManager userManager, UserTracker userTracker, @Main Executor executor, AccessPointControllerImpl.WifiPickerTrackerFactory wifiPickerTrackerFactory) {
        AccessPointControllerImpl accessPointControllerImpl = new AccessPointControllerImpl(userManager, userTracker, executor, wifiPickerTrackerFactory);
        accessPointControllerImpl.init();
        return accessPointControllerImpl;
    }

    @SysUISingleton
    @Provides
    static DeviceStateRotationLockSettingsManager provideAutoRotateSettingsManager(Context context) {
        return DeviceStateRotationLockSettingsManager.getInstance(context);
    }

    @Provides
    @Named("DEVICE_STATE_ROTATION_LOCK_DEFAULTS")
    static String[] providesDeviceStateRotationLockDefaults(@Main Resources resources) {
        return resources.getStringArray(17236105);
    }

    @SysUISingleton
    @Provides
    static DataSaverController provideDataSaverController(NetworkController networkController) {
        return networkController.getDataSaverController();
    }
}
