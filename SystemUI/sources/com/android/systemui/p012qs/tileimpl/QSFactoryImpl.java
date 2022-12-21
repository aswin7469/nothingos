package com.android.systemui.p012qs.tileimpl;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.constraintlayout.motion.widget.Key;
import androidx.core.app.NotificationCompat;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.demomode.DemoMode;
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
import com.android.systemui.plugins.p011qs.QSFactory;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.p011qs.QSTileView;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.util.leak.GarbageMonitor;
import com.nothing.systemui.p024qs.QSTileHostEx;
import com.nothing.systemui.p024qs.QSTileViewCircle;
import com.nothing.systemui.p024qs.tiles.BatteryShareTile;
import com.nothing.systemui.p024qs.tiles.GlyphsTile;
import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Provider;

@SysUISingleton
/* renamed from: com.android.systemui.qs.tileimpl.QSFactoryImpl */
public class QSFactoryImpl implements QSFactory {
    private static final String TAG = "QSFactory";
    private final Provider<AirplaneModeTile> mAirplaneModeTileProvider;
    private final Provider<AlarmTile> mAlarmTileProvider;
    private final Provider<BatterySaverTile> mBatterySaverTileProvider;
    private final Provider<BatteryShareTile> mBatteryShareTileProvider;
    private final Provider<BluetoothTile> mBluetoothTileProvider;
    private final Provider<CameraToggleTile> mCameraToggleTileProvider;
    private final Provider<CastTile> mCastTileProvider;
    private final Provider<CellularTile> mCellularTileProvider;
    private final Provider<ColorCorrectionTile> mColorCorrectionTileProvider;
    private final Provider<ColorInversionTile> mColorInversionTileProvider;
    private final Provider<CustomTile.Builder> mCustomTileBuilderProvider;
    private final Provider<DataSaverTile> mDataSaverTileProvider;
    private final Provider<DeviceControlsTile> mDeviceControlsTileProvider;
    private final Provider<DndTile> mDndTileProvider;
    private final Provider<FlashlightTile> mFlashlightTileProvider;
    private final Provider<GlyphsTile> mGlyphsTileProvider;
    private final Provider<HotspotTile> mHotspotTileProvider;
    private final Provider<InternetTile> mInternetTileProvider;
    private final Provider<LocationTile> mLocationTileProvider;
    private final Provider<GarbageMonitor.MemoryTile> mMemoryTileProvider;
    private final Provider<MicrophoneToggleTile> mMicrophoneToggleTileProvider;
    private final Provider<NfcTile> mNfcTileProvider;
    private final Provider<NightDisplayTile> mNightDisplayTileProvider;
    private final Provider<OneHandedModeTile> mOneHandedModeTileProvider;
    private final Provider<QRCodeScannerTile> mQRCodeScannerTileProvider;
    private final Lazy<QSHost> mQsHostLazy;
    private final Provider<QuickAccessWalletTile> mQuickAccessWalletTileProvider;
    private final Provider<ReduceBrightColorsTile> mReduceBrightColorsTileProvider;
    private final Provider<RotationLockTile> mRotationLockTileProvider;
    private final Provider<ScreenRecordTile> mScreenRecordTileProvider;
    private final Provider<UiModeNightTile> mUiModeNightTileProvider;
    private final Provider<WifiTile> mWifiTileProvider;
    private final Provider<WorkModeTile> mWorkModeTileProvider;

    @Inject
    public QSFactoryImpl(Lazy<QSHost> lazy, Provider<CustomTile.Builder> provider, Provider<WifiTile> provider2, Provider<InternetTile> provider3, Provider<BluetoothTile> provider4, Provider<CellularTile> provider5, Provider<DndTile> provider6, Provider<ColorInversionTile> provider7, Provider<AirplaneModeTile> provider8, Provider<WorkModeTile> provider9, Provider<RotationLockTile> provider10, Provider<FlashlightTile> provider11, Provider<LocationTile> provider12, Provider<CastTile> provider13, Provider<HotspotTile> provider14, Provider<BatterySaverTile> provider15, Provider<DataSaverTile> provider16, Provider<NightDisplayTile> provider17, Provider<NfcTile> provider18, Provider<GarbageMonitor.MemoryTile> provider19, Provider<UiModeNightTile> provider20, Provider<ScreenRecordTile> provider21, Provider<ReduceBrightColorsTile> provider22, Provider<CameraToggleTile> provider23, Provider<MicrophoneToggleTile> provider24, Provider<DeviceControlsTile> provider25, Provider<AlarmTile> provider26, Provider<QuickAccessWalletTile> provider27, Provider<QRCodeScannerTile> provider28, Provider<OneHandedModeTile> provider29, Provider<BatteryShareTile> provider30, Provider<GlyphsTile> provider31, Provider<ColorCorrectionTile> provider32) {
        this.mQsHostLazy = lazy;
        this.mCustomTileBuilderProvider = provider;
        this.mWifiTileProvider = provider2;
        this.mInternetTileProvider = provider3;
        this.mBluetoothTileProvider = provider4;
        this.mCellularTileProvider = provider5;
        this.mDndTileProvider = provider6;
        this.mColorInversionTileProvider = provider7;
        this.mAirplaneModeTileProvider = provider8;
        this.mWorkModeTileProvider = provider9;
        this.mRotationLockTileProvider = provider10;
        this.mFlashlightTileProvider = provider11;
        this.mLocationTileProvider = provider12;
        this.mCastTileProvider = provider13;
        this.mHotspotTileProvider = provider14;
        this.mBatterySaverTileProvider = provider15;
        this.mDataSaverTileProvider = provider16;
        this.mNightDisplayTileProvider = provider17;
        this.mNfcTileProvider = provider18;
        this.mMemoryTileProvider = provider19;
        this.mUiModeNightTileProvider = provider20;
        this.mScreenRecordTileProvider = provider21;
        this.mReduceBrightColorsTileProvider = provider22;
        this.mCameraToggleTileProvider = provider23;
        this.mMicrophoneToggleTileProvider = provider24;
        this.mDeviceControlsTileProvider = provider25;
        this.mAlarmTileProvider = provider26;
        this.mQuickAccessWalletTileProvider = provider27;
        this.mQRCodeScannerTileProvider = provider28;
        this.mOneHandedModeTileProvider = provider29;
        this.mColorCorrectionTileProvider = provider32;
        this.mBatteryShareTileProvider = provider30;
        this.mGlyphsTileProvider = provider31;
    }

    public final QSTile createTile(String str) {
        QSTileImpl createTileInternal = createTileInternal(str);
        if (createTileInternal != null) {
            createTileInternal.initialize();
            createTileInternal.postStale();
        }
        return createTileInternal;
    }

    /* access modifiers changed from: protected */
    public QSTileImpl createTileInternal(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2109393100:
                if (str.equals("onehanded")) {
                    c = 0;
                    break;
                }
                break;
            case -2016941037:
                if (str.equals(AutoTileManager.INVERSION)) {
                    c = 1;
                    break;
                }
                break;
            case -1672519182:
                if (str.equals("batteryshare")) {
                    c = 2;
                    break;
                }
                break;
            case -1242708793:
                if (str.equals("glyphs")) {
                    c = 3;
                    break;
                }
                break;
            case -1183073498:
                if (str.equals("flashlight")) {
                    c = 4;
                    break;
                }
                break;
            case -805491779:
                if (str.equals("screenrecord")) {
                    c = 5;
                    break;
                }
                break;
            case -795192327:
                if (str.equals(AutoTileManager.WALLET)) {
                    c = 6;
                    break;
                }
                break;
            case -677011630:
                if (str.equals("airplane")) {
                    c = 7;
                    break;
                }
                break;
            case -657925702:
                if (str.equals("color_correction")) {
                    c = 8;
                    break;
                }
                break;
            case -566933834:
                if (str.equals(AutoTileManager.DEVICE_CONTROLS)) {
                    c = 9;
                    break;
                }
                break;
            case -343519030:
                if (str.equals(AutoTileManager.BRIGHTNESS)) {
                    c = 10;
                    break;
                }
                break;
            case -331239923:
                if (str.equals(DemoMode.COMMAND_BATTERY)) {
                    c = 11;
                    break;
                }
                break;
            case -40300674:
                if (str.equals(Key.ROTATION)) {
                    c = 12;
                    break;
                }
                break;
            case -37334949:
                if (str.equals("mictoggle")) {
                    c = 13;
                    break;
                }
                break;
            case 3154:
                if (str.equals("bt")) {
                    c = 14;
                    break;
                }
                break;
            case 99610:
                if (str.equals("dnd")) {
                    c = 15;
                    break;
                }
                break;
            case 108971:
                if (str.equals("nfc")) {
                    c = 16;
                    break;
                }
                break;
            case 3046207:
                if (str.equals(AutoTileManager.CAST)) {
                    c = 17;
                    break;
                }
                break;
            case 3049826:
                if (str.equals("cell")) {
                    c = 18;
                    break;
                }
                break;
            case 3075958:
                if (str.equals("dark")) {
                    c = 19;
                    break;
                }
                break;
            case 3649301:
                if (str.equals("wifi")) {
                    c = 20;
                    break;
                }
                break;
            case 3655441:
                if (str.equals(AutoTileManager.WORK)) {
                    c = 21;
                    break;
                }
                break;
            case 6344377:
                if (str.equals("cameratoggle")) {
                    c = 22;
                    break;
                }
                break;
            case 92895825:
                if (str.equals(NotificationCompat.CATEGORY_ALARM)) {
                    c = 23;
                    break;
                }
                break;
            case 104817688:
                if (str.equals(AutoTileManager.NIGHT)) {
                    c = 24;
                    break;
                }
                break;
            case 109211285:
                if (str.equals(AutoTileManager.SAVER)) {
                    c = 25;
                    break;
                }
                break;
            case 570410817:
                if (str.equals("internet")) {
                    c = 26;
                    break;
                }
                break;
            case 876619530:
                if (str.equals("qr_code_scanner")) {
                    c = 27;
                    break;
                }
                break;
            case 1099603663:
                if (str.equals(AutoTileManager.HOTSPOT)) {
                    c = 28;
                    break;
                }
                break;
            case 1901043637:
                if (str.equals("location")) {
                    c = 29;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return this.mOneHandedModeTileProvider.get();
            case 1:
                return this.mColorInversionTileProvider.get();
            case 2:
                return this.mBatteryShareTileProvider.get();
            case 3:
                return this.mGlyphsTileProvider.get();
            case 4:
                return this.mFlashlightTileProvider.get();
            case 5:
                return this.mScreenRecordTileProvider.get();
            case 6:
                return this.mQuickAccessWalletTileProvider.get();
            case 7:
                return this.mAirplaneModeTileProvider.get();
            case 8:
                return this.mColorCorrectionTileProvider.get();
            case 9:
                return this.mDeviceControlsTileProvider.get();
            case 10:
                return this.mReduceBrightColorsTileProvider.get();
            case 11:
                return this.mBatterySaverTileProvider.get();
            case 12:
                return this.mRotationLockTileProvider.get();
            case 13:
                return this.mMicrophoneToggleTileProvider.get();
            case 14:
                return this.mBluetoothTileProvider.get();
            case 15:
                return this.mDndTileProvider.get();
            case 16:
                return this.mNfcTileProvider.get();
            case 17:
                return this.mCastTileProvider.get();
            case 18:
                return this.mCellularTileProvider.get();
            case 19:
                return this.mUiModeNightTileProvider.get();
            case 20:
                return this.mWifiTileProvider.get();
            case 21:
                return this.mWorkModeTileProvider.get();
            case 22:
                return this.mCameraToggleTileProvider.get();
            case 23:
                return this.mAlarmTileProvider.get();
            case 24:
                return this.mNightDisplayTileProvider.get();
            case 25:
                return this.mDataSaverTileProvider.get();
            case 26:
                return this.mInternetTileProvider.get();
            case 27:
                return this.mQRCodeScannerTileProvider.get();
            case 28:
                return this.mHotspotTileProvider.get();
            case 29:
                return this.mLocationTileProvider.get();
            default:
                if (str.startsWith(CustomTile.PREFIX)) {
                    return CustomTile.create(this.mCustomTileBuilderProvider.get(), str, this.mQsHostLazy.get().getUserContext());
                }
                if (Build.IS_DEBUGGABLE && str.equals(GarbageMonitor.MemoryTile.TILE_SPEC)) {
                    return this.mMemoryTileProvider.get();
                }
                Log.w(TAG, "No stock tile spec: " + str);
                return null;
        }
    }

    public QSTileView createTileView(Context context, QSTile qSTile, boolean z) {
        QSIconView createTileView = qSTile.createTileView(context);
        createTileView.setShouldAddCircleIconBg(QSTileHostEx.isSignalOrBtTile(qSTile.getTileSpec()));
        if (!QSTileHostEx.isSignalOrBtTile(qSTile.getTileSpec()) || z) {
            return new QSTileViewImpl(context, createTileView, z);
        }
        return new QSTileViewCircle(context, createTileView, false);
    }
}
