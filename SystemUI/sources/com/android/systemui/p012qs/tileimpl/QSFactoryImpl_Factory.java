package com.android.systemui.p012qs.tileimpl;

import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.external.CustomTile;
import com.android.systemui.p012qs.tiles.AirplaneModeTile;
import com.android.systemui.p012qs.tiles.AlarmTile;
import com.android.systemui.p012qs.tiles.BatterySaverTile;
import com.android.systemui.p012qs.tiles.BluetoothTile;
import com.android.systemui.p012qs.tiles.CameraToggleTile;
import com.android.systemui.p012qs.tiles.CastTile;
import com.android.systemui.p012qs.tiles.CellularTile;
import com.android.systemui.p012qs.tiles.ColorCorrectionTile;
import com.android.systemui.p012qs.tiles.ColorInversionTile;
import com.android.systemui.p012qs.tiles.DataSaverTile;
import com.android.systemui.p012qs.tiles.DeviceControlsTile;
import com.android.systemui.p012qs.tiles.DndTile;
import com.android.systemui.p012qs.tiles.FlashlightTile;
import com.android.systemui.p012qs.tiles.HotspotTile;
import com.android.systemui.p012qs.tiles.InternetTile;
import com.android.systemui.p012qs.tiles.LocationTile;
import com.android.systemui.p012qs.tiles.MicrophoneToggleTile;
import com.android.systemui.p012qs.tiles.NfcTile;
import com.android.systemui.p012qs.tiles.NightDisplayTile;
import com.android.systemui.p012qs.tiles.OneHandedModeTile;
import com.android.systemui.p012qs.tiles.QRCodeScannerTile;
import com.android.systemui.p012qs.tiles.QuickAccessWalletTile;
import com.android.systemui.p012qs.tiles.ReduceBrightColorsTile;
import com.android.systemui.p012qs.tiles.RotationLockTile;
import com.android.systemui.p012qs.tiles.ScreenRecordTile;
import com.android.systemui.p012qs.tiles.UiModeNightTile;
import com.android.systemui.p012qs.tiles.WifiTile;
import com.android.systemui.p012qs.tiles.WorkModeTile;
import com.android.systemui.util.leak.GarbageMonitor;
import com.nothing.systemui.p024qs.tiles.BatteryShareTile;
import com.nothing.systemui.p024qs.tiles.GlyphsTile;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.tileimpl.QSFactoryImpl_Factory */
public final class QSFactoryImpl_Factory implements Factory<QSFactoryImpl> {
    private final Provider<AirplaneModeTile> airplaneModeTileProvider;
    private final Provider<AlarmTile> alarmTileProvider;
    private final Provider<BatterySaverTile> batterySaverTileProvider;
    private final Provider<BatteryShareTile> batteryShareTileProvider;
    private final Provider<BluetoothTile> bluetoothTileProvider;
    private final Provider<CameraToggleTile> cameraToggleTileProvider;
    private final Provider<CastTile> castTileProvider;
    private final Provider<CellularTile> cellularTileProvider;
    private final Provider<ColorCorrectionTile> colorCorrectionTileProvider;
    private final Provider<ColorInversionTile> colorInversionTileProvider;
    private final Provider<CustomTile.Builder> customTileBuilderProvider;
    private final Provider<DataSaverTile> dataSaverTileProvider;
    private final Provider<DeviceControlsTile> deviceControlsTileProvider;
    private final Provider<DndTile> dndTileProvider;
    private final Provider<FlashlightTile> flashlightTileProvider;
    private final Provider<GlyphsTile> glyphsTileProvider;
    private final Provider<HotspotTile> hotspotTileProvider;
    private final Provider<InternetTile> internetTileProvider;
    private final Provider<LocationTile> locationTileProvider;
    private final Provider<GarbageMonitor.MemoryTile> memoryTileProvider;
    private final Provider<MicrophoneToggleTile> microphoneToggleTileProvider;
    private final Provider<NfcTile> nfcTileProvider;
    private final Provider<NightDisplayTile> nightDisplayTileProvider;
    private final Provider<OneHandedModeTile> oneHandedModeTileProvider;
    private final Provider<QRCodeScannerTile> qrCodeScannerTileProvider;
    private final Provider<QSHost> qsHostLazyProvider;
    private final Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
    private final Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
    private final Provider<RotationLockTile> rotationLockTileProvider;
    private final Provider<ScreenRecordTile> screenRecordTileProvider;
    private final Provider<UiModeNightTile> uiModeNightTileProvider;
    private final Provider<WifiTile> wifiTileProvider;
    private final Provider<WorkModeTile> workModeTileProvider;

    public QSFactoryImpl_Factory(Provider<QSHost> provider, Provider<CustomTile.Builder> provider2, Provider<WifiTile> provider3, Provider<InternetTile> provider4, Provider<BluetoothTile> provider5, Provider<CellularTile> provider6, Provider<DndTile> provider7, Provider<ColorInversionTile> provider8, Provider<AirplaneModeTile> provider9, Provider<WorkModeTile> provider10, Provider<RotationLockTile> provider11, Provider<FlashlightTile> provider12, Provider<LocationTile> provider13, Provider<CastTile> provider14, Provider<HotspotTile> provider15, Provider<BatterySaverTile> provider16, Provider<DataSaverTile> provider17, Provider<NightDisplayTile> provider18, Provider<NfcTile> provider19, Provider<GarbageMonitor.MemoryTile> provider20, Provider<UiModeNightTile> provider21, Provider<ScreenRecordTile> provider22, Provider<ReduceBrightColorsTile> provider23, Provider<CameraToggleTile> provider24, Provider<MicrophoneToggleTile> provider25, Provider<DeviceControlsTile> provider26, Provider<AlarmTile> provider27, Provider<QuickAccessWalletTile> provider28, Provider<QRCodeScannerTile> provider29, Provider<OneHandedModeTile> provider30, Provider<BatteryShareTile> provider31, Provider<GlyphsTile> provider32, Provider<ColorCorrectionTile> provider33) {
        this.qsHostLazyProvider = provider;
        this.customTileBuilderProvider = provider2;
        this.wifiTileProvider = provider3;
        this.internetTileProvider = provider4;
        this.bluetoothTileProvider = provider5;
        this.cellularTileProvider = provider6;
        this.dndTileProvider = provider7;
        this.colorInversionTileProvider = provider8;
        this.airplaneModeTileProvider = provider9;
        this.workModeTileProvider = provider10;
        this.rotationLockTileProvider = provider11;
        this.flashlightTileProvider = provider12;
        this.locationTileProvider = provider13;
        this.castTileProvider = provider14;
        this.hotspotTileProvider = provider15;
        this.batterySaverTileProvider = provider16;
        this.dataSaverTileProvider = provider17;
        this.nightDisplayTileProvider = provider18;
        this.nfcTileProvider = provider19;
        this.memoryTileProvider = provider20;
        this.uiModeNightTileProvider = provider21;
        this.screenRecordTileProvider = provider22;
        this.reduceBrightColorsTileProvider = provider23;
        this.cameraToggleTileProvider = provider24;
        this.microphoneToggleTileProvider = provider25;
        this.deviceControlsTileProvider = provider26;
        this.alarmTileProvider = provider27;
        this.quickAccessWalletTileProvider = provider28;
        this.qrCodeScannerTileProvider = provider29;
        this.oneHandedModeTileProvider = provider30;
        this.batteryShareTileProvider = provider31;
        this.glyphsTileProvider = provider32;
        this.colorCorrectionTileProvider = provider33;
    }

    public QSFactoryImpl get() {
        return newInstance(DoubleCheck.lazy(this.qsHostLazyProvider), this.customTileBuilderProvider, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.batterySaverTileProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.quickAccessWalletTileProvider, this.qrCodeScannerTileProvider, this.oneHandedModeTileProvider, this.batteryShareTileProvider, this.glyphsTileProvider, this.colorCorrectionTileProvider);
    }

    public static QSFactoryImpl_Factory create(Provider<QSHost> provider, Provider<CustomTile.Builder> provider2, Provider<WifiTile> provider3, Provider<InternetTile> provider4, Provider<BluetoothTile> provider5, Provider<CellularTile> provider6, Provider<DndTile> provider7, Provider<ColorInversionTile> provider8, Provider<AirplaneModeTile> provider9, Provider<WorkModeTile> provider10, Provider<RotationLockTile> provider11, Provider<FlashlightTile> provider12, Provider<LocationTile> provider13, Provider<CastTile> provider14, Provider<HotspotTile> provider15, Provider<BatterySaverTile> provider16, Provider<DataSaverTile> provider17, Provider<NightDisplayTile> provider18, Provider<NfcTile> provider19, Provider<GarbageMonitor.MemoryTile> provider20, Provider<UiModeNightTile> provider21, Provider<ScreenRecordTile> provider22, Provider<ReduceBrightColorsTile> provider23, Provider<CameraToggleTile> provider24, Provider<MicrophoneToggleTile> provider25, Provider<DeviceControlsTile> provider26, Provider<AlarmTile> provider27, Provider<QuickAccessWalletTile> provider28, Provider<QRCodeScannerTile> provider29, Provider<OneHandedModeTile> provider30, Provider<BatteryShareTile> provider31, Provider<GlyphsTile> provider32, Provider<ColorCorrectionTile> provider33) {
        return new QSFactoryImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33);
    }

    public static QSFactoryImpl newInstance(Lazy<QSHost> lazy, Provider<CustomTile.Builder> provider, Provider<WifiTile> provider2, Provider<InternetTile> provider3, Provider<BluetoothTile> provider4, Provider<CellularTile> provider5, Provider<DndTile> provider6, Provider<ColorInversionTile> provider7, Provider<AirplaneModeTile> provider8, Provider<WorkModeTile> provider9, Provider<RotationLockTile> provider10, Provider<FlashlightTile> provider11, Provider<LocationTile> provider12, Provider<CastTile> provider13, Provider<HotspotTile> provider14, Provider<BatterySaverTile> provider15, Provider<DataSaverTile> provider16, Provider<NightDisplayTile> provider17, Provider<NfcTile> provider18, Provider<GarbageMonitor.MemoryTile> provider19, Provider<UiModeNightTile> provider20, Provider<ScreenRecordTile> provider21, Provider<ReduceBrightColorsTile> provider22, Provider<CameraToggleTile> provider23, Provider<MicrophoneToggleTile> provider24, Provider<DeviceControlsTile> provider25, Provider<AlarmTile> provider26, Provider<QuickAccessWalletTile> provider27, Provider<QRCodeScannerTile> provider28, Provider<OneHandedModeTile> provider29, Provider<BatteryShareTile> provider30, Provider<GlyphsTile> provider31, Provider<ColorCorrectionTile> provider32) {
        return new QSFactoryImpl(lazy, provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32);
    }
}
