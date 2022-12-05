package com.android.systemui.qs.tileimpl;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.android.systemui.plugins.qs.QSFactory;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tiles.AirplaneModeTile;
import com.android.systemui.qs.tiles.AlarmTile;
import com.android.systemui.qs.tiles.BatterySaverTile;
import com.android.systemui.qs.tiles.BluetoothTile;
import com.android.systemui.qs.tiles.CameraToggleTile;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.qs.tiles.CellularTile;
import com.android.systemui.qs.tiles.ColorInversionTile;
import com.android.systemui.qs.tiles.DataSaverTile;
import com.android.systemui.qs.tiles.DeviceControlsTile;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.qs.tiles.FlashlightTile;
import com.android.systemui.qs.tiles.HotspotTile;
import com.android.systemui.qs.tiles.InternetTile;
import com.android.systemui.qs.tiles.LocationTile;
import com.android.systemui.qs.tiles.MicrophoneToggleTile;
import com.android.systemui.qs.tiles.NfcTile;
import com.android.systemui.qs.tiles.NightDisplayTile;
import com.android.systemui.qs.tiles.QuickAccessWalletTile;
import com.android.systemui.qs.tiles.ReduceBrightColorsTile;
import com.android.systemui.qs.tiles.RotationLockTile;
import com.android.systemui.qs.tiles.ScreenRecordTile;
import com.android.systemui.qs.tiles.UiModeNightTile;
import com.android.systemui.qs.tiles.UserTile;
import com.android.systemui.qs.tiles.WifiTile;
import com.android.systemui.qs.tiles.WorkModeTile;
import com.android.systemui.util.leak.GarbageMonitor;
import com.nothingos.systemui.qs.QSTileViewCircle;
import com.nothingos.systemui.qs.tiles.BatteryShareTile;
import com.nothingos.systemui.qs.tiles.GlyphsTile;
import dagger.Lazy;
import javax.inject.Provider;
/* loaded from: classes.dex */
public class QSFactoryImpl implements QSFactory {
    private final Provider<AirplaneModeTile> mAirplaneModeTileProvider;
    private final Provider<AlarmTile> mAlarmTileProvider;
    private final Provider<BatterySaverTile> mBatterySaverTileProvider;
    private final Provider<BatteryShareTile> mBatteryShareTileProvider;
    private final Provider<BluetoothTile> mBluetoothTileProvider;
    private final Provider<CameraToggleTile> mCameraToggleTileProvider;
    private final Provider<CastTile> mCastTileProvider;
    private final Provider<CellularTile> mCellularTileProvider;
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
    private final Lazy<QSHost> mQsHostLazy;
    private final Provider<QuickAccessWalletTile> mQuickAccessWalletTileProvider;
    private final Provider<ReduceBrightColorsTile> mReduceBrightColorsTileProvider;
    private final Provider<RotationLockTile> mRotationLockTileProvider;
    private final Provider<ScreenRecordTile> mScreenRecordTileProvider;
    private final Provider<UiModeNightTile> mUiModeNightTileProvider;
    private final Provider<UserTile> mUserTileProvider;
    private final Provider<WifiTile> mWifiTileProvider;
    private final Provider<WorkModeTile> mWorkModeTileProvider;

    public QSFactoryImpl(Lazy<QSHost> lazy, Provider<CustomTile.Builder> provider, Provider<WifiTile> provider2, Provider<InternetTile> provider3, Provider<BluetoothTile> provider4, Provider<CellularTile> provider5, Provider<DndTile> provider6, Provider<ColorInversionTile> provider7, Provider<AirplaneModeTile> provider8, Provider<WorkModeTile> provider9, Provider<RotationLockTile> provider10, Provider<FlashlightTile> provider11, Provider<LocationTile> provider12, Provider<CastTile> provider13, Provider<HotspotTile> provider14, Provider<UserTile> provider15, Provider<BatterySaverTile> provider16, Provider<DataSaverTile> provider17, Provider<NightDisplayTile> provider18, Provider<NfcTile> provider19, Provider<GarbageMonitor.MemoryTile> provider20, Provider<UiModeNightTile> provider21, Provider<ScreenRecordTile> provider22, Provider<ReduceBrightColorsTile> provider23, Provider<CameraToggleTile> provider24, Provider<MicrophoneToggleTile> provider25, Provider<DeviceControlsTile> provider26, Provider<AlarmTile> provider27, Provider<QuickAccessWalletTile> provider28, Provider<GlyphsTile> provider29, Provider<BatteryShareTile> provider30) {
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
        this.mUserTileProvider = provider15;
        this.mBatterySaverTileProvider = provider16;
        this.mDataSaverTileProvider = provider17;
        this.mNightDisplayTileProvider = provider18;
        this.mNfcTileProvider = provider19;
        this.mMemoryTileProvider = provider20;
        this.mUiModeNightTileProvider = provider21;
        this.mScreenRecordTileProvider = provider22;
        this.mReduceBrightColorsTileProvider = provider23;
        this.mCameraToggleTileProvider = provider24;
        this.mMicrophoneToggleTileProvider = provider25;
        this.mDeviceControlsTileProvider = provider26;
        this.mAlarmTileProvider = provider27;
        this.mQuickAccessWalletTileProvider = provider28;
        this.mGlyphsTileProvider = provider29;
        this.mBatteryShareTileProvider = provider30;
    }

    @Override // com.android.systemui.plugins.qs.QSFactory
    public QSTile createTile(String str) {
        QSTileImpl createTileInternal = createTileInternal(str);
        if (createTileInternal != null) {
            createTileInternal.initialize();
            createTileInternal.postStale();
        }
        return createTileInternal;
    }

    private QSTileImpl createTileInternal(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2016941037:
                if (str.equals("inversion")) {
                    c = 0;
                    break;
                }
                break;
            case -1672519182:
                if (str.equals("batteryshare")) {
                    c = 1;
                    break;
                }
                break;
            case -1242708793:
                if (str.equals("glyphs")) {
                    c = 2;
                    break;
                }
                break;
            case -1183073498:
                if (str.equals("flashlight")) {
                    c = 3;
                    break;
                }
                break;
            case -805491779:
                if (str.equals("screenrecord")) {
                    c = 4;
                    break;
                }
                break;
            case -795192327:
                if (str.equals("wallet")) {
                    c = 5;
                    break;
                }
                break;
            case -677011630:
                if (str.equals("airplane")) {
                    c = 6;
                    break;
                }
                break;
            case -566933834:
                if (str.equals("controls")) {
                    c = 7;
                    break;
                }
                break;
            case -343519030:
                if (str.equals("reduce_brightness")) {
                    c = '\b';
                    break;
                }
                break;
            case -331239923:
                if (str.equals("battery")) {
                    c = '\t';
                    break;
                }
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    c = '\n';
                    break;
                }
                break;
            case -37334949:
                if (str.equals("mictoggle")) {
                    c = 11;
                    break;
                }
                break;
            case 3154:
                if (str.equals("bt")) {
                    c = '\f';
                    break;
                }
                break;
            case 99610:
                if (str.equals("dnd")) {
                    c = '\r';
                    break;
                }
                break;
            case 108971:
                if (str.equals("nfc")) {
                    c = 14;
                    break;
                }
                break;
            case 3046207:
                if (str.equals("cast")) {
                    c = 15;
                    break;
                }
                break;
            case 3049826:
                if (str.equals("cell")) {
                    c = 16;
                    break;
                }
                break;
            case 3075958:
                if (str.equals("dark")) {
                    c = 17;
                    break;
                }
                break;
            case 3599307:
                if (str.equals("user")) {
                    c = 18;
                    break;
                }
                break;
            case 3649301:
                if (str.equals("wifi")) {
                    c = 19;
                    break;
                }
                break;
            case 3655441:
                if (str.equals("work")) {
                    c = 20;
                    break;
                }
                break;
            case 6344377:
                if (str.equals("cameratoggle")) {
                    c = 21;
                    break;
                }
                break;
            case 92895825:
                if (str.equals("alarm")) {
                    c = 22;
                    break;
                }
                break;
            case 104817688:
                if (str.equals("night")) {
                    c = 23;
                    break;
                }
                break;
            case 109211285:
                if (str.equals("saver")) {
                    c = 24;
                    break;
                }
                break;
            case 570410817:
                if (str.equals("internet")) {
                    c = 25;
                    break;
                }
                break;
            case 1099603663:
                if (str.equals("hotspot")) {
                    c = 26;
                    break;
                }
                break;
            case 1901043637:
                if (str.equals("location")) {
                    c = 27;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return this.mColorInversionTileProvider.mo1933get();
            case 1:
                return this.mBatteryShareTileProvider.mo1933get();
            case 2:
                return this.mGlyphsTileProvider.mo1933get();
            case 3:
                return this.mFlashlightTileProvider.mo1933get();
            case 4:
                return this.mScreenRecordTileProvider.mo1933get();
            case 5:
                return this.mQuickAccessWalletTileProvider.mo1933get();
            case 6:
                return this.mAirplaneModeTileProvider.mo1933get();
            case 7:
                return this.mDeviceControlsTileProvider.mo1933get();
            case '\b':
                return this.mReduceBrightColorsTileProvider.mo1933get();
            case '\t':
                return this.mBatterySaverTileProvider.mo1933get();
            case '\n':
                return this.mRotationLockTileProvider.mo1933get();
            case 11:
                return this.mMicrophoneToggleTileProvider.mo1933get();
            case '\f':
                return this.mBluetoothTileProvider.mo1933get();
            case '\r':
                return this.mDndTileProvider.mo1933get();
            case 14:
                return this.mNfcTileProvider.mo1933get();
            case 15:
                return this.mCastTileProvider.mo1933get();
            case 16:
                return this.mCellularTileProvider.mo1933get();
            case 17:
                return this.mUiModeNightTileProvider.mo1933get();
            case 18:
                return this.mUserTileProvider.mo1933get();
            case 19:
                return this.mWifiTileProvider.mo1933get();
            case 20:
                return this.mWorkModeTileProvider.mo1933get();
            case 21:
                return this.mCameraToggleTileProvider.mo1933get();
            case 22:
                return this.mAlarmTileProvider.mo1933get();
            case 23:
                return this.mNightDisplayTileProvider.mo1933get();
            case 24:
                return this.mDataSaverTileProvider.mo1933get();
            case 25:
                return this.mInternetTileProvider.mo1933get();
            case 26:
                return this.mHotspotTileProvider.mo1933get();
            case 27:
                return this.mLocationTileProvider.mo1933get();
            default:
                if (str.startsWith("custom(")) {
                    return CustomTile.create(this.mCustomTileBuilderProvider.mo1933get(), str, this.mQsHostLazy.get().getUserContext());
                }
                if (Build.IS_DEBUGGABLE && str.equals("dbg:mem")) {
                    return this.mMemoryTileProvider.mo1933get();
                }
                Log.w("QSFactory", "No stock tile spec: " + str);
                return null;
        }
    }

    @Override // com.android.systemui.plugins.qs.QSFactory
    public QSTileView createTileView(Context context, QSTile qSTile, boolean z) {
        QSIconView createTileView = qSTile.createTileView(context);
        boolean z2 = true;
        boolean z3 = QSTileHost.isSignalTile(qSTile.getTileSpec()) || QSTileHost.isBluetoothTile(qSTile.getTileSpec());
        if (z || !z3) {
            z2 = false;
        }
        createTileView.setShouldAddCircleIconBg(z3);
        if (z2) {
            return new QSTileViewCircle(context, createTileView, false);
        }
        return new QSTileViewImpl(context, createTileView, z);
    }
}
