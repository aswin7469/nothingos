package com.nothing.systemui.p024qs.tiles;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.tiles.BluetoothTile;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.nothing.p023os.device.DeviceConstant;
import com.nothing.p023os.device.DeviceServiceController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSFragmentEx;
import com.nothing.systemui.statusbar.policy.TeslaInfoController;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.FileNotFoundException;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.json.JSONObject;

@Metadata(mo64986d1 = {"\u0000·\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b*\u0001#\u0018\u0000 y2\u00020\u0001:\u0003wxyB\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0012\u00105\u001a\u0004\u0018\u0001062\u0006\u00107\u001a\u000208H\u0002J\u0012\u00109\u001a\u0004\u0018\u00010\u000e2\b\u0010:\u001a\u0004\u0018\u00010\u000eJ\u0012\u0010;\u001a\u0004\u0018\u00010\u000e2\b\u0010<\u001a\u0004\u0018\u00010\u000eJ\u0012\u0010=\u001a\u0004\u0018\u00010\u000e2\b\u0010<\u001a\u0004\u0018\u00010\u000eJ\u000e\u0010>\u001a\u00020\u000e2\u0006\u0010?\u001a\u00020\u000fJ\u0016\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u00042\u0006\u0010C\u001a\u00020\u000eJ&\u0010@\u001a\u00020A2\u0006\u0010D\u001a\u00020\u00042\u0006\u0010<\u001a\u00020\u000e2\u0006\u0010E\u001a\u00020,2\u0006\u0010F\u001a\u00020\u000eJ\u0006\u0010G\u001a\u00020AJ\u0012\u0010H\u001a\u0004\u0018\u00010\u000e2\b\u0010<\u001a\u0004\u0018\u00010\u000eJ\u0010\u0010I\u001a\u0004\u0018\u00010J2\u0006\u0010K\u001a\u00020LJ\u0006\u0010M\u001a\u00020NJ\u0012\u0010O\u001a\u0004\u0018\u00010\u000e2\b\u0010<\u001a\u0004\u0018\u00010\u000eJ\u0014\u0010P\u001a\u0004\u0018\u00010\u000e2\b\u0010<\u001a\u0004\u0018\u00010\u000eH\u0002J\u0010\u0010Q\u001a\u0004\u0018\u0001062\u0006\u0010R\u001a\u00020\u000eJ\u0006\u0010S\u001a\u00020AJ.\u0010T\u001a\u00020A2\u0006\u0010 \u001a\u00020!2\u0006\u0010U\u001a\u00020\u00152\u0006\u0010V\u001a\u0002022\u0006\u0010W\u001a\u0002042\u0006\u0010X\u001a\u00020YJ\u000e\u0010Z\u001a\u00020,2\u0006\u0010[\u001a\u00020\\J\u0006\u0010]\u001a\u00020,J\u0006\u0010^\u001a\u00020,J\u000e\u0010_\u001a\u00020,2\u0006\u0010`\u001a\u00020\u000eJ\u000e\u0010a\u001a\u00020,2\u0006\u0010`\u001a\u00020\u000eJ\u0018\u0010b\u001a\u00020,2\u0006\u0010c\u001a\u00020d2\u0006\u0010`\u001a\u00020\u000eH\u0002J\u0010\u0010e\u001a\u00020,2\b\u0010<\u001a\u0004\u0018\u00010\u000eJ\u0010\u0010f\u001a\u00020,2\b\u0010<\u001a\u0004\u0018\u00010\u000eJ\u0010\u0010g\u001a\u00020A2\b\u0010<\u001a\u0004\u0018\u00010\u000eJ\u0018\u0010h\u001a\u00020A2\u0006\u00107\u001a\u0002082\b\u0010i\u001a\u0004\u0018\u00010\u000eJ\u0016\u0010j\u001a\u00020A2\u0006\u0010D\u001a\u00020\u00042\u0006\u0010k\u001a\u00020lJ\u000e\u0010m\u001a\u00020A2\u0006\u0010n\u001a\u00020oJ\u000e\u0010p\u001a\u00020A2\u0006\u0010q\u001a\u00020LJ\u0006\u0010r\u001a\u00020,J\b\u0010s\u001a\u00020AH\u0002J\b\u0010t\u001a\u00020AH\u0002J\u0016\u0010u\u001a\u00020A2\u0006\u0010v\u001a\u00020,2\u0006\u0010E\u001a\u00020,R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R*\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR&\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u000e¢\u0006\u0002\n\u0000R&\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u000e8F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR&\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00048F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0010\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u00020#X\u0004¢\u0006\u0004\n\u0002\u0010$R*\u0010&\u001a\u0004\u0018\u00010%2\b\u0010\u0005\u001a\u0004\u0018\u00010%8F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*R\u000e\u0010+\u001a\u00020,X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0004¢\u0006\u0002\n\u0000R\u0010\u00101\u001a\u0004\u0018\u000102X\u000e¢\u0006\u0002\n\u0000R\u0010\u00103\u001a\u0004\u0018\u000104X\u000e¢\u0006\u0002\n\u0000¨\u0006z"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tiles/BluetoothTileEx;", "", "()V", "airpodsSwitch", "", "value", "Lcom/nothing/systemui/qs/tiles/BluetoothTileEx$AncCallback;", "ancCallback", "getAncCallback", "()Lcom/nothing/systemui/qs/tiles/BluetoothTileEx$AncCallback;", "setAncCallback", "(Lcom/nothing/systemui/qs/tiles/BluetoothTileEx$AncCallback;)V", "bluetoothBatteryDates", "Ljava/util/HashMap;", "", "Lcom/nothing/systemui/qs/tiles/BluetoothTileEx$BluetoothBatteryDate;", "getBluetoothBatteryDates", "()Ljava/util/HashMap;", "setBluetoothBatteryDates", "(Ljava/util/HashMap;)V", "bluetoothController", "Lcom/android/systemui/statusbar/policy/BluetoothController;", "clickAddress", "getClickAddress", "()Ljava/lang/String;", "setClickAddress", "(Ljava/lang/String;)V", "clickFrom", "getClickFrom", "()I", "setClickFrom", "(I)V", "context", "Landroid/content/Context;", "deviceConnectorCallback", "com/nothing/systemui/qs/tiles/BluetoothTileEx$deviceConnectorCallback$1", "Lcom/nothing/systemui/qs/tiles/BluetoothTileEx$deviceConnectorCallback$1;", "Lcom/nothing/os/device/DeviceServiceController;", "deviceController", "getDeviceController", "()Lcom/nothing/os/device/DeviceServiceController;", "setDeviceController", "(Lcom/nothing/os/device/DeviceServiceController;)V", "isCallbackAdded", "", "qSFragmentEx", "Lcom/nothing/systemui/qs/QSFragmentEx;", "singleThreadExecutor", "Ljava/util/concurrent/ExecutorService;", "teslaInfoController", "Lcom/nothing/systemui/statusbar/policy/TeslaInfoController;", "tile", "Lcom/android/systemui/qs/tiles/BluetoothTile;", "bitmap2Drawable", "Landroid/graphics/drawable/Drawable;", "bitmap", "Landroid/graphics/Bitmap;", "changeToSSPAdress", "classicAddress", "getAirpodsVersion", "address", "getBLEModuleForSettingGlobal", "getBatteryLevel", "bbd", "getCommand", "", "i", "str", "command", "isConnected", "modelId", "getCommandBattery", "getConnectedDevice", "getDeviceSecondLabel", "", "device", "Lcom/android/settingslib/bluetooth/CachedBluetoothDevice;", "getLongClickIntentAndUpdateClickItem", "Landroid/content/Intent;", "getModeID", "getModeIDFromNothingApp", "getModuleIDBitmap", "name", "handleClick", "init", "btController", "teslaController", "bluetoothTile", "handler", "Landroid/os/Handler;", "isAdvancedDetailsHeader", "bluetoothDevice", "Landroid/bluetooth/BluetoothDevice;", "isAirpodsExperimentOn", "isBluetoothEnabled", "isNothingAppEnabled", "pkgName", "isNothingAppHasPermission", "isNothingAppInstalled", "pkgManager", "Landroid/content/pm/PackageManager;", "isNothingEarDevice", "isSupportAnc", "saveConnectedDevice", "saveModuleIDEarBitmap", "modeId", "sendCommand", "bundle", "Landroid/os/Bundle;", "setClickListener", "qSIcon", "Lcom/android/systemui/plugins/qs/QSIconView;", "setModelIdAndDevice", "cachedBluetoothDevice", "shouldShowTeslaInfo", "startDeviceService", "stopDeviceService", "updateDeviceService", "enabled", "AncCallback", "BluetoothBatteryDate", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx */
/* compiled from: BluetoothTileEx.kt */
public final class BluetoothTileEx {
    public static final String ADDRESS_FAKE = "address_fake";
    private static final String AIRPODS_EXPERIMENTAL = "nt_airpods_switch";
    private static final String AIRPODS_VERSION = "airpods_version";
    private static final String ALREADY_PAIRED_BLUETOOTHDEVICE = "bluetooth_paired";
    private static final String BT_ADVANCED_HEADER_ENABLED = "bt_advanced_header_enabled";
    private static final String BT_LE_AUDIO_CONTACT_SHARING_ENABLED = "bt_le_audio_contact_sharing_enabled";
    private static final String BT_NEAR_BY_SUGGESTION_ENABLED = "bt_near_by_suggestion_enabled";
    private static final String BT_SLICE_SETTINGS_ENABLED = "bt_slice_settings_enabled";
    public static final int CLICK_FROM_BLUETOOTH = 1;
    public static final int CLICK_FROM_TESLA = 0;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String DEVICE_DETAIL = "com.android.settings.BLUETOOTH_DEVICE_DETAIL_SETTINGS";
    private static final String GENERIC_EVENT_LOGGING_ENABLED = "event_logging_enabled";
    private static final String NOTHING_EAR_DB = "nt_ear_ble_module_ids";
    private static final String NOTHING_EAR_MAC_ADDRESS_PREFIX = "2C:BE";
    public static final String NOTHING_SMART_CENTER = "com.nothing.smartcenter";
    private static final String PERMISSION_ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    private static final String PERMISSION_ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private static final String PERMISSION_BT_ADVERTISE = "android.permission.BLUETOOTH_ADVERTISE";
    private static final String PERMISSION_BT_CONNECT = "android.permission.BLUETOOTH_CONNECT";
    private static final String PERMISSION_BT_SCAN = "android.permission.BLUETOOTH_SCAN";
    private static final String TAG = "BluetoothTileEx";
    private static HashMap<String, String> nothingEars = new HashMap<>();
    /* access modifiers changed from: private */
    public int airpodsSwitch;
    private AncCallback ancCallback;
    private HashMap<String, BluetoothBatteryDate> bluetoothBatteryDates = new HashMap<>();
    /* access modifiers changed from: private */
    public BluetoothController bluetoothController;
    private String clickAddress = "";
    private int clickFrom = 1;
    private Context context;
    private final BluetoothTileEx$deviceConnectorCallback$1 deviceConnectorCallback;
    private DeviceServiceController deviceController;
    private boolean isCallbackAdded;
    /* access modifiers changed from: private */
    public final QSFragmentEx qSFragmentEx;
    private final ExecutorService singleThreadExecutor;
    /* access modifiers changed from: private */
    public TeslaInfoController teslaInfoController;
    /* access modifiers changed from: private */
    public BluetoothTile tile;

    @Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&J\b\u0010\u0005\u001a\u00020\u0003H&J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH&J \u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH&J\u001a\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\b2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0013À\u0006\u0001"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tiles/BluetoothTileEx$AncCallback;", "", "onDestroy", "", "onDeviceServiceConnected", "onDeviceServiceDisConnected", "onFail", "i", "", "i2", "onProfileConnectionStateChanged", "device", "Lcom/android/settingslib/bluetooth/CachedBluetoothDevice;", "state", "profile", "onSuccess", "command", "bundle", "Landroid/os/Bundle;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx$AncCallback */
    /* compiled from: BluetoothTileEx.kt */
    public interface AncCallback {
        void onDestroy();

        void onDeviceServiceConnected();

        void onDeviceServiceDisConnected();

        void onFail(int i, int i2);

        void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2);

        void onSuccess(int i, Bundle bundle);
    }

    @Metadata(mo64986d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u001bX\u000e¢\u0006\u0002\n\u0000¨\u0006\u001c"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tiles/BluetoothTileEx$Companion;", "", "()V", "ADDRESS_FAKE", "", "AIRPODS_EXPERIMENTAL", "AIRPODS_VERSION", "ALREADY_PAIRED_BLUETOOTHDEVICE", "BT_ADVANCED_HEADER_ENABLED", "BT_LE_AUDIO_CONTACT_SHARING_ENABLED", "BT_NEAR_BY_SUGGESTION_ENABLED", "BT_SLICE_SETTINGS_ENABLED", "CLICK_FROM_BLUETOOTH", "", "CLICK_FROM_TESLA", "DEVICE_DETAIL", "GENERIC_EVENT_LOGGING_ENABLED", "NOTHING_EAR_DB", "NOTHING_EAR_MAC_ADDRESS_PREFIX", "NOTHING_SMART_CENTER", "PERMISSION_ACCESS_COARSE_LOCATION", "PERMISSION_ACCESS_FINE_LOCATION", "PERMISSION_BT_ADVERTISE", "PERMISSION_BT_CONNECT", "PERMISSION_BT_SCAN", "TAG", "nothingEars", "Ljava/util/HashMap;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx$Companion */
    /* compiled from: BluetoothTileEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Inject
    public BluetoothTileEx() {
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        if (newSingleThreadExecutor != null) {
            this.singleThreadExecutor = newSingleThreadExecutor;
            Object obj = NTDependencyEx.get(QSFragmentEx.class);
            Intrinsics.checkNotNullExpressionValue(obj, "get(QSFragmentEx::class.java)");
            this.qSFragmentEx = (QSFragmentEx) obj;
            this.deviceConnectorCallback = new BluetoothTileEx$deviceConnectorCallback$1(this);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.util.concurrent.ExecutorService");
    }

    public final HashMap<String, BluetoothBatteryDate> getBluetoothBatteryDates() {
        return this.bluetoothBatteryDates;
    }

    public final void setBluetoothBatteryDates(HashMap<String, BluetoothBatteryDate> hashMap) {
        Intrinsics.checkNotNullParameter(hashMap, "<set-?>");
        this.bluetoothBatteryDates = hashMap;
    }

    public final DeviceServiceController getDeviceController() {
        return this.deviceController;
    }

    public final void setDeviceController(DeviceServiceController deviceServiceController) {
        this.deviceController = deviceServiceController;
    }

    public final AncCallback getAncCallback() {
        return this.ancCallback;
    }

    public final void setAncCallback(AncCallback ancCallback2) {
        this.ancCallback = ancCallback2;
    }

    public final int getClickFrom() {
        return this.clickFrom;
    }

    public final void setClickFrom(int i) {
        this.clickFrom = i;
    }

    public final String getClickAddress() {
        return this.clickAddress;
    }

    public final void setClickAddress(String str) {
        Intrinsics.checkNotNullParameter(str, "value");
        this.clickAddress = str;
    }

    public final void init(Context context2, BluetoothController bluetoothController2, TeslaInfoController teslaInfoController2, BluetoothTile bluetoothTile, Handler handler) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(bluetoothController2, "btController");
        Intrinsics.checkNotNullParameter(teslaInfoController2, "teslaController");
        Intrinsics.checkNotNullParameter(bluetoothTile, "bluetoothTile");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.context = context2;
        this.bluetoothController = bluetoothController2;
        this.teslaInfoController = teslaInfoController2;
        this.tile = bluetoothTile;
        context2.getContentResolver().registerContentObserver(Settings.System.getUriFor(AIRPODS_EXPERIMENTAL), true, new BluetoothTileEx$init$settingsObserver$1(handler, this, context2), -1);
    }

    public final boolean shouldShowTeslaInfo() {
        TeslaInfoController teslaInfoController2 = this.teslaInfoController;
        Intrinsics.checkNotNull(teslaInfoController2);
        return teslaInfoController2.shouldShowTeslaInfo();
    }

    public final void setClickListener(QSIconView qSIconView) {
        Intrinsics.checkNotNullParameter(qSIconView, "qSIcon");
        qSIconView.setOnClickListener(new BluetoothTileEx$setClickListener$1(this));
        qSIconView.setOnLongClickListener(new BluetoothTileEx$setClickListener$2(this));
    }

    public final Intent getLongClickIntentAndUpdateClickItem() {
        Intent intent = new Intent("android.settings.BLUETOOTH_SETTINGS");
        BluetoothTile bluetoothTile = this.tile;
        Intrinsics.checkNotNull(bluetoothTile);
        QSTile.BooleanState booleanState = (QSTile.BooleanState) bluetoothTile.getState();
        int btPageIndex = this.qSFragmentEx.getBtPageIndex();
        NTLogUtil.m1680d(TAG, "LongClick address.size: " + booleanState.addressList.size() + ", page: " + btPageIndex);
        TeslaInfoController teslaInfoController2 = this.teslaInfoController;
        Intrinsics.checkNotNull(teslaInfoController2);
        setClickFrom((!teslaInfoController2.shouldShowTeslaInfo() || btPageIndex != 0) ? 1 : 0);
        BluetoothController bluetoothController2 = this.bluetoothController;
        Intrinsics.checkNotNull(bluetoothController2);
        if (bluetoothController2.isBluetoothConnected()) {
            intent = new Intent(DEVICE_DETAIL);
            if (booleanState.addressList.size() == 0) {
                BluetoothController bluetoothController3 = this.bluetoothController;
                Intrinsics.checkNotNull(bluetoothController3);
                String address = bluetoothController3.getConnectedDevices().get(0).getAddress();
                Intrinsics.checkNotNullExpressionValue(address, "bluetoothController!!.ge…ces().get(0).getAddress()");
                setClickAddress(address);
                intent.putExtra(DeviceConstant.KEY_MAC_ADDRESS, getClickAddress());
            } else {
                int i = btPageIndex - 1;
                TeslaInfoController teslaInfoController3 = this.teslaInfoController;
                Intrinsics.checkNotNull(teslaInfoController3);
                if (teslaInfoController3.shouldShowTeslaInfo()) {
                    if (btPageIndex > 0) {
                        setClickAddress(booleanState.addressList.get(i).toString());
                        intent.putExtra(DeviceConstant.KEY_MAC_ADDRESS, getClickAddress());
                    }
                } else if (btPageIndex == 0) {
                    BluetoothController bluetoothController4 = this.bluetoothController;
                    Intrinsics.checkNotNull(bluetoothController4);
                    String address2 = bluetoothController4.getActiveDevice().getAddress();
                    Intrinsics.checkNotNullExpressionValue(address2, "bluetoothController!!.ge…tiveDevice().getAddress()");
                    setClickAddress(address2);
                    intent.putExtra(DeviceConstant.KEY_MAC_ADDRESS, getClickAddress());
                } else {
                    setClickAddress(booleanState.addressList.get(i).toString());
                    intent.putExtra(DeviceConstant.KEY_MAC_ADDRESS, getClickAddress());
                }
            }
        }
        return intent;
    }

    public final void handleClick() {
        getLongClickIntentAndUpdateClickItem();
    }

    public final void updateDeviceService(boolean z, boolean z2) {
        if (!z || !z2) {
            stopDeviceService();
            return;
        }
        startDeviceService();
        getCommandBattery();
    }

    private final void startDeviceService() {
        if (!this.isCallbackAdded) {
            this.isCallbackAdded = true;
            try {
                if (getDeviceController() == null) {
                    Context context2 = this.context;
                    Intrinsics.checkNotNull(context2);
                    setDeviceController(new DeviceServiceController(context2));
                }
                NTLogUtil.m1680d(TAG, "startDeviceService() addCallback");
                DeviceServiceController deviceController2 = getDeviceController();
                Intrinsics.checkNotNull(deviceController2);
                deviceController2.addCallback(this.deviceConnectorCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final void stopDeviceService() {
        if (this.isCallbackAdded) {
            this.isCallbackAdded = false;
            this.bluetoothBatteryDates.clear();
            try {
                NTLogUtil.m1680d(TAG, "stopDeviceService() removeCallback");
                DeviceServiceController deviceController2 = getDeviceController();
                Intrinsics.checkNotNull(deviceController2);
                deviceController2.removeCallback(this.deviceConnectorCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public final void getCommandBattery() {
        if (getDeviceController() != null) {
            BluetoothController bluetoothController2 = this.bluetoothController;
            CachedBluetoothDevice cachedBluetoothDevice = null;
            List<CachedBluetoothDevice> connectedDevices = bluetoothController2 != null ? bluetoothController2.getConnectedDevices() : null;
            BluetoothController bluetoothController3 = this.bluetoothController;
            if (bluetoothController3 != null) {
                cachedBluetoothDevice = bluetoothController3.getActiveDevice();
            }
            Intrinsics.checkNotNull(connectedDevices);
            int size = connectedDevices.size();
            if (size > 1 && cachedBluetoothDevice != null) {
                int i = 0;
                while (true) {
                    if (i < size) {
                        if (Objects.equals(connectedDevices.get(i), cachedBluetoothDevice) && i != 0) {
                            Collections.swap((List<?>) connectedDevices, 0, i);
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
            }
            for (CachedBluetoothDevice next : connectedDevices) {
                try {
                    String bLEModuleForSettingGlobal = getBLEModuleForSettingGlobal(changeToSSPAdress(next.getAddress()));
                    NTLogUtil.m1680d(TAG, "moduleId: " + bLEModuleForSettingGlobal);
                    if (!TextUtils.isEmpty(bLEModuleForSettingGlobal)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(DeviceConstant.KEY_MAC_ADDRESS, next.getAddress());
                        DeviceServiceController deviceController2 = getDeviceController();
                        Intrinsics.checkNotNull(deviceController2);
                        deviceController2.getCommand(4, bundle);
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    NTLogUtil.m1681e(TAG, e.toString());
                }
            }
        }
    }

    public final CharSequence getDeviceSecondLabel(CachedBluetoothDevice cachedBluetoothDevice) {
        Integer num;
        Intrinsics.checkNotNullParameter(cachedBluetoothDevice, "device");
        int batteryLevel = cachedBluetoothDevice.getBatteryLevel();
        HashMap<String, BluetoothBatteryDate> hashMap = this.bluetoothBatteryDates;
        if (!(hashMap == null || hashMap.get(cachedBluetoothDevice.getAddress()) == null)) {
            BluetoothBatteryDate bluetoothBatteryDate = this.bluetoothBatteryDates.get(cachedBluetoothDevice.getAddress());
            if (bluetoothBatteryDate == null) {
                bluetoothBatteryDate = new BluetoothBatteryDate();
            }
            CharSequence batteryLevel2 = getBatteryLevel(bluetoothBatteryDate);
            if (!TextUtils.isEmpty(batteryLevel2)) {
                return batteryLevel2;
            }
        }
        if (batteryLevel > -1) {
            Context context2 = this.context;
            Intrinsics.checkNotNull(context2);
            return context2.getString(C1893R.string.quick_settings_bluetooth_secondary_label_battery_level, new Object[]{Utils.formatPercentage(batteryLevel)});
        }
        BluetoothClass btClass = cachedBluetoothDevice.getBtClass();
        if (btClass == null) {
            return null;
        }
        if (cachedBluetoothDevice.isHearingAidDevice()) {
            num = Integer.valueOf((int) C1893R.string.quick_settings_bluetooth_secondary_label_hearing_aids);
        } else if (btClass.doesClassMatch(1)) {
            num = Integer.valueOf((int) C1893R.string.quick_settings_bluetooth_secondary_label_audio);
        } else if (btClass.doesClassMatch(0)) {
            num = Integer.valueOf((int) C1893R.string.quick_settings_bluetooth_secondary_label_headset);
        } else if (btClass.doesClassMatch(3)) {
            num = Integer.valueOf((int) C1893R.string.quick_settings_bluetooth_secondary_label_input);
        } else {
            Integer num2 = null;
            num = null;
        }
        if (num != null) {
            int intValue = num.intValue();
            Context context3 = this.context;
            Intrinsics.checkNotNull(context3);
            String string = context3.getString(intValue);
            if (string != null) {
                return string;
            }
        }
        BluetoothTileEx bluetoothTileEx = this;
        CharSequence charSequence = null;
        return null;
    }

    @Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000e¨\u0006\u0012"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tiles/BluetoothTileEx$BluetoothBatteryDate;", "", "()V", "address", "", "getAddress", "()Ljava/lang/String;", "setAddress", "(Ljava/lang/String;)V", "batteryLeft", "", "getBatteryLeft", "()I", "setBatteryLeft", "(I)V", "batteryRight", "getBatteryRight", "setBatteryRight", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx$BluetoothBatteryDate */
    /* compiled from: BluetoothTileEx.kt */
    public static final class BluetoothBatteryDate {
        private String address;
        private int batteryLeft;
        private int batteryRight;

        public final String getAddress() {
            return this.address;
        }

        public final void setAddress(String str) {
            this.address = str;
        }

        public final int getBatteryLeft() {
            return this.batteryLeft;
        }

        public final void setBatteryLeft(int i) {
            this.batteryLeft = i;
        }

        public final int getBatteryRight() {
            return this.batteryRight;
        }

        public final void setBatteryRight(int i) {
            this.batteryRight = i;
        }
    }

    public final String getBLEModuleForSettingGlobal(String str) {
        Context context2 = this.context;
        Intrinsics.checkNotNull(context2);
        String string = Settings.Global.getString(context2.getContentResolver(), NOTHING_EAR_DB);
        NTLogUtil.m1680d(TAG, "moduleIds isEmpty: " + TextUtils.isEmpty(string));
        try {
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            Object obj = new JSONObject(string).get(str);
            if (obj != null) {
                String str2 = (String) obj;
                if (!TextUtils.isEmpty(str2)) {
                    return str2;
                }
                return null;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
        } catch (Exception unused) {
            return null;
        }
    }

    public final String getAirpodsVersion(String str) {
        Context context2 = this.context;
        Intrinsics.checkNotNull(context2);
        String string = Settings.Global.getString(context2.getContentResolver(), AIRPODS_VERSION);
        CharSequence charSequence = string;
        NTLogUtil.m1680d(TAG, "getAirpodsVersion isEmpty: " + TextUtils.isEmpty(charSequence));
        boolean isEmpty = TextUtils.isEmpty(charSequence);
        String str2 = "";
        if (isEmpty) {
            return str2;
        }
        try {
            String string2 = new JSONObject(string).getString(str);
            Intrinsics.checkNotNullExpressionValue(string2, "jsonObject.getString(address)");
            try {
                NTLogUtil.m1680d(TAG, "getAirpodsVersion version: " + string2);
                return string2;
            } catch (Exception e) {
                e = e;
                str2 = string2;
                NTLogUtil.m1681e(TAG, "getAirpodsVersion e: " + e);
                return str2;
            }
        } catch (Exception e2) {
            e = e2;
            NTLogUtil.m1681e(TAG, "getAirpodsVersion e: " + e);
            return str2;
        }
    }

    public final String changeToSSPAdress(String str) {
        String str2 = "";
        if (str == null) {
            return str2;
        }
        Locale locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
        String lowerCase = str.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "this as java.lang.String).toLowerCase(locale)");
        Object[] array = StringsKt.split$default((CharSequence) lowerCase, new String[]{":"}, false, 0, 6, (Object) null).toArray((T[]) new String[0]);
        Intrinsics.checkNotNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        String[] strArr = (String[]) array;
        for (int length = strArr.length; length > 0; length--) {
            str2 = str2 + strArr[length - 1];
        }
        return str2;
    }

    public final Drawable getModuleIDBitmap(String str) {
        Bitmap decodeStream;
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/.nomedia/", str + ".png");
            if (file.exists() && (decodeStream = BitmapFactory.decodeStream(new FileInputStream(file))) != null) {
                return bitmap2Drawable(decodeStream);
            }
            return null;
        } catch (Exception e) {
            NTLogUtil.m1681e(TAG, e.toString());
            return null;
        }
    }

    public final String getModeID(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Intrinsics.checkNotNull(str);
        String upperCase = str.toUpperCase();
        Intrinsics.checkNotNullExpressionValue(upperCase, "this as java.lang.String).toUpperCase()");
        Intrinsics.checkNotNull(upperCase);
        boolean startsWith$default = StringsKt.startsWith$default(upperCase, NOTHING_EAR_MAC_ADDRESS_PREFIX, false, 2, (Object) null);
        NTLogUtil.m1680d(TAG, "is ear: " + startsWith$default + ", " + upperCase);
        if (!startsWith$default) {
            NTLogUtil.m1680d(TAG, "return not nothing ear");
            return null;
        }
        String changeToSSPAdress = changeToSSPAdress(str);
        String str2 = (String) nothingEars.get(changeToSSPAdress);
        CharSequence charSequence = str2;
        if (!TextUtils.isEmpty(charSequence)) {
            NTLogUtil.m1680d(TAG, "return moduleID = " + str2);
            return str2;
        }
        if (TextUtils.isEmpty(charSequence)) {
            str2 = getBLEModuleForSettingGlobal(changeToSSPAdress);
            NTLogUtil.m1680d(TAG, "return getBLEModuleForSettingGlobal = " + str2);
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = getModeIDFromNothingApp(str);
        }
        HashMap<String, String> hashMap = nothingEars;
        Intrinsics.checkNotNull(changeToSSPAdress);
        Intrinsics.checkNotNull(str2);
        hashMap.put(changeToSSPAdress, str2);
        return str2;
    }

    public final void saveModuleIDEarBitmap(Bitmap bitmap, String str) {
        Intrinsics.checkNotNullParameter(bitmap, "bitmap");
        if (str != null) {
            this.singleThreadExecutor.execute(new BluetoothTileEx$$ExternalSyntheticLambda0(str, bitmap));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: saveModuleIDEarBitmap$lambda-2  reason: not valid java name */
    public static final void m3517saveModuleIDEarBitmap$lambda2(String str, Bitmap bitmap) {
        Intrinsics.checkNotNullParameter(bitmap, "$bitmap");
        String str2 = Environment.getExternalStorageDirectory().toString() + "/.nomedia/";
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(str2, str + ".png");
        if (!file2.exists()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public final void setModelIdAndDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        String str;
        Intrinsics.checkNotNullParameter(cachedBluetoothDevice, "cachedBluetoothDevice");
        if (getDeviceController() != null) {
            try {
                if (getModeID(cachedBluetoothDevice.getAddress()) == null) {
                    str = "";
                } else {
                    str = getModeID(cachedBluetoothDevice.getAddress());
                }
                DeviceServiceController deviceController2 = getDeviceController();
                Intrinsics.checkNotNull(deviceController2);
                Intrinsics.checkNotNull(str);
                deviceController2.setModelIdAndDevice(str, cachedBluetoothDevice.getDevice());
            } catch (Exception unused) {
            }
        }
    }

    public final void getCommand(int i, String str) {
        Intrinsics.checkNotNullParameter(str, "str");
        if (getDeviceController() != null) {
            Bundle bundle = new Bundle();
            bundle.putString(DeviceConstant.KEY_MAC_ADDRESS, str);
            DeviceServiceController deviceController2 = getDeviceController();
            Intrinsics.checkNotNull(deviceController2);
            deviceController2.getCommand(i, bundle);
        }
    }

    public final void getCommand(int i, String str, boolean z, String str2) {
        Intrinsics.checkNotNullParameter(str, "address");
        Intrinsics.checkNotNullParameter(str2, "modelId");
        if (getDeviceController() != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceConstant.KEY_EAR_CONNECTED, z);
            bundle.putString(DeviceConstant.KEY_MAC_ADDRESS, str);
            bundle.putString(DeviceConstant.KEY_MODEL_ID, str2);
            DeviceServiceController deviceController2 = getDeviceController();
            Intrinsics.checkNotNull(deviceController2);
            deviceController2.getCommand(i, bundle);
        }
    }

    public final void sendCommand(int i, Bundle bundle) {
        Intrinsics.checkNotNullParameter(bundle, "bundle");
        if (getDeviceController() != null) {
            DeviceServiceController deviceController2 = getDeviceController();
            Intrinsics.checkNotNull(deviceController2);
            deviceController2.sendCommand(i, bundle);
        }
    }

    public final boolean isAdvancedDetailsHeader(BluetoothDevice bluetoothDevice) {
        Intrinsics.checkNotNullParameter(bluetoothDevice, "bluetoothDevice");
        if (!DeviceConfig.getBoolean("settings_ui", "bt_advanced_header_enabled", true)) {
            NTLogUtil.m1680d(TAG, "isAdvancedDetailsHeader: advancedEnabled is false");
            return false;
        } else if (BluetoothUtils.getBooleanMetaData(bluetoothDevice, 6)) {
            NTLogUtil.m1680d(TAG, "isAdvancedDetailsHeader: untetheredHeadset is true");
            return true;
        } else {
            String stringMetaData = BluetoothUtils.getStringMetaData(bluetoothDevice, 17);
            CharSequence charSequence = stringMetaData;
            if (!TextUtils.equals(charSequence, "Untethered Headset") && !TextUtils.equals(charSequence, "Watch") && !TextUtils.equals(charSequence, "Default")) {
                return false;
            }
            NTLogUtil.m1680d(TAG, "isAdvancedDetailsHeader: deviceType is " + stringMetaData);
            return true;
        }
    }

    public final boolean isAirpodsExperimentOn() {
        NTLogUtil.m1680d(TAG, "airpodsSwitch: " + this.airpodsSwitch);
        return this.airpodsSwitch != 0;
    }

    public final boolean isNothingAppHasPermission(String str) {
        Intrinsics.checkNotNullParameter(str, "pkgName");
        String[] strArr = {PERMISSION_BT_CONNECT, PERMISSION_BT_SCAN, PERMISSION_BT_ADVERTISE};
        Context context2 = this.context;
        Intrinsics.checkNotNull(context2);
        PackageManager packageManager = context2.getPackageManager();
        Intrinsics.checkNotNullExpressionValue(packageManager, "packageManager");
        if (!isNothingAppInstalled(packageManager, str)) {
            return false;
        }
        for (String checkPermission : strArr) {
            if (packageManager.checkPermission(checkPermission, str) != 0) {
                return false;
            }
        }
        return true;
    }

    private final boolean isNothingAppInstalled(PackageManager packageManager, String str) {
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        if (installedPackages == null) {
            return false;
        }
        for (PackageInfo packageInfo : installedPackages) {
            if (packageInfo.packageName.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isNothingAppEnabled(String str) {
        Intrinsics.checkNotNullParameter(str, "pkgName");
        Context context2 = this.context;
        Intrinsics.checkNotNull(context2);
        PackageManager packageManager = context2.getPackageManager();
        Intrinsics.checkNotNullExpressionValue(packageManager, "packageManager");
        if (!isNothingAppInstalled(packageManager, str)) {
            return false;
        }
        NTLogUtil.m1680d(TAG, "getApplicationEnabledSetting:" + packageManager.getApplicationEnabledSetting(str));
        if (packageManager.getApplicationEnabledSetting(str) != 3) {
            return true;
        }
        return false;
    }

    public final boolean isNothingEarDevice(String str) {
        return !TextUtils.isEmpty(getModeID(str)) || !TextUtils.isEmpty(getConnectedDevice(str));
    }

    public final String getConnectedDevice(String str) {
        Context context2 = this.context;
        Intrinsics.checkNotNull(context2);
        return context2.getSharedPreferences(ALREADY_PAIRED_BLUETOOTHDEVICE, 0).getString(changeToSSPAdress(str), (String) null);
    }

    public final void saveConnectedDevice(String str) {
        if (str != null) {
            String changeToSSPAdress = changeToSSPAdress(str);
            String modeID = getModeID(str);
            if (modeID != null && modeID.length() != 0) {
                Context context2 = this.context;
                Intrinsics.checkNotNull(context2);
                context2.getSharedPreferences(ALREADY_PAIRED_BLUETOOTHDEVICE, 0).edit().putString(changeToSSPAdress, modeID).apply();
            }
        }
    }

    public final boolean isBluetoothEnabled() {
        BluetoothController bluetoothController2 = this.bluetoothController;
        Intrinsics.checkNotNull(bluetoothController2);
        return bluetoothController2.isBluetoothEnabled();
    }

    public final boolean isSupportAnc(String str) {
        Uri parse = Uri.parse("content://com.nothing.os.device.provider/support_anc");
        boolean z = true;
        try {
            String[] strArr = {getModeID(str)};
            Context context2 = this.context;
            Intrinsics.checkNotNull(context2);
            Cursor query = context2.getContentResolver().query(parse, (String[]) null, (String) null, strArr, (String) null);
            if (query != null) {
                while (query.moveToNext()) {
                    z = Boolean.parseBoolean(query.getString(query.getColumnIndex(DeviceConstant.KEY_VALUE_BOOLEAN)));
                    NTLogUtil.m1680d(TAG, "isSupportAnc: " + z);
                }
                query.close();
            }
        } catch (Exception e) {
            NTLogUtil.m1680d(TAG, "isSupportAnc e:" + e);
        }
        return z;
    }

    private final String getModeIDFromNothingApp(String str) {
        String str2 = "";
        Uri parse = Uri.parse("content://com.nothing.os.device.provider/device/device_address/" + str);
        try {
            Context context2 = this.context;
            Intrinsics.checkNotNull(context2);
            Cursor query = context2.getContentResolver().query(parse, (String[]) null, (String) null, (String[]) null, (String) null);
            NTLogUtil.m1680d(TAG, "cursor: " + query + ", classicAddress: " + str);
            if (query != null) {
                while (query.moveToNext()) {
                    String string = query.getString(query.getColumnIndex(DeviceConstant.KEY_MODEL_ID));
                    Intrinsics.checkNotNullExpressionValue(string, "cursor.getString(index)");
                    try {
                        NTLogUtil.m1680d(TAG, "modeId: " + string);
                        str2 = string;
                    } catch (Exception e) {
                        e = e;
                        str2 = string;
                        NTLogUtil.m1680d(TAG, "getModeIDFromNothingApp e:" + e);
                        return str2;
                    }
                }
                query.close();
            }
        } catch (Exception e2) {
            e = e2;
        }
        return str2;
    }

    private final Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public final String getBatteryLevel(BluetoothBatteryDate bluetoothBatteryDate) {
        Intrinsics.checkNotNullParameter(bluetoothBatteryDate, "bbd");
        StringBuilder sb = new StringBuilder();
        int batteryLeft = bluetoothBatteryDate.getBatteryLeft();
        int batteryRight = bluetoothBatteryDate.getBatteryRight();
        NTLogUtil.m1680d(TAG, "getBatteryLevel = " + batteryLeft + " ; right = " + batteryRight);
        if (batteryLeft != -1) {
            Context context2 = this.context;
            Intrinsics.checkNotNull(context2);
            sb.append(context2.getResources().getString(C1893R.string.nt_btpanel_battery_level_left, new Object[]{Utils.formatPercentage(batteryLeft)}));
        }
        if (batteryRight != -1) {
            if (batteryLeft != -1) {
                Context context3 = this.context;
                Intrinsics.checkNotNull(context3);
                sb.append(context3.getResources().getString(C1893R.string.nt_btpanel_battery_level_dot));
            }
            Context context4 = this.context;
            Intrinsics.checkNotNull(context4);
            sb.append(context4.getResources().getString(C1893R.string.nt_btpanel_battery_level_right, new Object[]{Utils.formatPercentage(batteryRight)}));
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }
}
