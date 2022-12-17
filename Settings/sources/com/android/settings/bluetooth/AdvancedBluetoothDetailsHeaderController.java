package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$color;
import com.android.settings.R$dimen;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.fuelgauge.BatteryMeterView;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.utils.StringUtil;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.LayoutPreference;
import com.nothing.p005os.device.IDeviceBitmap;
import com.nothing.settings.bluetooth.NothingBluetoothUtil;
import com.nothing.settings.panel.PanelCircleDrawable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AdvancedBluetoothDetailsHeaderController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, OnDestroy, OnResume, CachedBluetoothDevice.Callback {
    private static final String BATTERY_ESTIMATE = "battery_estimate";
    private static final int CASE_DEVICE_ID = 3;
    private static final int CASE_LOW_BATTERY_LEVEL = 19;
    private static final String DATABASE_BLUETOOTH = "Bluetooth";
    private static final String DATABASE_ID = "id";
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String ESTIMATE_READY = "estimate_ready";
    private static final float HALF_ALPHA = 0.5f;
    private static final int LEFT_DEVICE_ID = 1;
    private static final int LOW_BATTERY_LEVEL = 15;
    private static final int MAIN_DEVICE_ID = 4;
    private static final String PATH = "time_remaining";
    private static final String QUERY_PARAMETER_ADDRESS = "address";
    private static final String QUERY_PARAMETER_BATTERY_ID = "battery_id";
    private static final String QUERY_PARAMETER_BATTERY_LEVEL = "battery_level";
    private static final String QUERY_PARAMETER_TIMESTAMP = "timestamp";
    private static final int RIGHT_DEVICE_ID = 2;
    private static final String TAG = "AdvancedBtHeaderCtrl";
    private static final long TIME_OF_HOUR;
    private static final long TIME_OF_MINUTE;
    private Map<String, Boolean> isNothingIconAnimRun = new HashMap();
    private int mBatteryCase;
    private int mBatteryLeft;
    private int mBatteryRight;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private CachedBluetoothDevice mCachedDevice;
    Handler mHandler = new Handler(Looper.getMainLooper());
    private IDeviceBitmap mIDeviceBitmap;
    final Map<String, Bitmap> mIconCache = new HashMap();
    private boolean mIsAirpodDevice;
    private boolean mIsConnectNothingDeviceService = false;
    private boolean mIsHasPerm = true;
    private boolean mIsInfoReceived = false;
    boolean mIsLeftDeviceEstimateReady;
    private boolean mIsNothingEarDevice;
    private boolean mIsNothingEarDeviceFromAddr;
    boolean mIsRegisterCallback = false;
    boolean mIsRightDeviceEstimateReady;
    LayoutPreference mLayoutPreference;
    final BluetoothAdapter.OnMetadataChangedListener mMetadataListener = new BluetoothAdapter.OnMetadataChangedListener() {
        public void onMetadataChanged(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
            if (AdvancedBluetoothDetailsHeaderController.DEBUG) {
                Object[] objArr = new Object[3];
                objArr[0] = bluetoothDevice;
                objArr[1] = Integer.valueOf(i);
                objArr[2] = bArr == null ? null : new String(bArr);
                Log.d(AdvancedBluetoothDetailsHeaderController.TAG, String.format("Metadata updated in Device %s: %d = %s.", objArr));
            }
            AdvancedBluetoothDetailsHeaderController.this.refresh();
        }
    };
    final Map<String, Bitmap> mNothingIconCache = new HashMap();

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    static {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        TIME_OF_HOUR = timeUnit.toMillis(3600);
        TIME_OF_MINUTE = timeUnit.toMillis(60);
    }

    public AdvancedBluetoothDetailsHeaderController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice == null) {
            return 2;
        }
        if (Utils.isAdvancedDetailsHeader(cachedBluetoothDevice.getDevice()) || this.mIsNothingEarDevice || this.mIsAirpodDevice || this.mIsNothingEarDeviceFromAddr) {
            return 0;
        }
        return 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        LayoutPreference layoutPreference = (LayoutPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mLayoutPreference = layoutPreference;
        layoutPreference.setVisible(isAvailable());
    }

    public void onStart() {
        if (isAvailable()) {
            this.mIsRegisterCallback = true;
            this.mCachedDevice.registerCallback(this);
            this.mBluetoothAdapter.addOnMetadataChangedListener(this.mCachedDevice.getDevice(), this.mContext.getMainExecutor(), this.mMetadataListener);
            refresh();
        }
    }

    public void onStop() {
        if (this.mIsRegisterCallback) {
            this.mCachedDevice.unregisterCallback(this);
            this.mBluetoothAdapter.removeOnMetadataChangedListener(this.mCachedDevice.getDevice(), this.mMetadataListener);
            this.mIsRegisterCallback = false;
        }
    }

    public void onDestroy() {
        for (Bitmap next : this.mIconCache.values()) {
            if (next != null) {
                next.recycle();
            }
        }
        this.mIconCache.clear();
        for (Bitmap next2 : this.mNothingIconCache.values()) {
            if (next2 != null) {
                next2.recycle();
            }
        }
        this.mNothingIconCache.clear();
    }

    public void init(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mCachedDevice = cachedBluetoothDevice;
        this.mIsNothingEarDevice = NothingBluetoothUtil.getinstance().isNothingEarDevice(this.mContext, this.mCachedDevice.getAddress());
        this.mIsAirpodDevice = NothingBluetoothUtil.getinstance().checkUUIDIsAirpod(this.mContext, this.mCachedDevice.getDevice());
        this.mIsHasPerm = NothingBluetoothUtil.getinstance().isNothingAppHasPermission(this.mContext, "com.nothing.smartcenter") && NothingBluetoothUtil.getinstance().isNothingAppEnabled(this.mContext, "com.nothing.smartcenter");
        this.mIsNothingEarDeviceFromAddr = NothingBluetoothUtil.getinstance().isNothingEarDeviceFromMacAddr(this.mContext, this.mCachedDevice.getAddress(), this.mCachedDevice.getDevice().getName());
    }

    /* access modifiers changed from: package-private */
    public void refresh() {
        LayoutPreference layoutPreference = this.mLayoutPreference;
        if (layoutPreference != null && this.mCachedDevice != null) {
            ((TextView) layoutPreference.findViewById(R$id.entity_header_title)).setText(this.mCachedDevice.getName());
            ((TextView) this.mLayoutPreference.findViewById(R$id.entity_header_summary)).setText(this.mCachedDevice.getConnectionSummary(true));
            if (!this.mCachedDevice.isConnected() || this.mCachedDevice.isBusy() || !this.mIsHasPerm) {
                updateDisconnectLayout();
                return;
            }
            BluetoothDevice device = this.mCachedDevice.getDevice();
            String stringMetaData = BluetoothUtils.getStringMetaData(device, 17);
            if ((TextUtils.equals(stringMetaData, "Watch") || TextUtils.equals(stringMetaData, "Default")) && !this.mIsConnectNothingDeviceService && !this.mIsInfoReceived) {
                this.mLayoutPreference.findViewById(R$id.layout_left).setVisibility(8);
                this.mLayoutPreference.findViewById(R$id.layout_right).setVisibility(8);
                updateSubLayout((LinearLayout) this.mLayoutPreference.findViewById(R$id.layout_middle), 5, 18, 20, 19, 0, 4);
            } else if (TextUtils.equals(stringMetaData, "Untethered Headset") || BluetoothUtils.getBooleanMetaData(device, 6) || ((this.mIsNothingEarDevice && this.mIsHasPerm) || this.mIsInfoReceived || (this.mIsNothingEarDeviceFromAddr && this.mIsHasPerm))) {
                updateSubLayout((LinearLayout) this.mLayoutPreference.findViewById(R$id.layout_left), 7, 10, 21, 13, R$string.bluetooth_left_name, 1);
                updateSubLayout((LinearLayout) this.mLayoutPreference.findViewById(R$id.layout_middle), 9, 12, 23, 15, R$string.bluetooth_middle_name, 3);
                updateSubLayout((LinearLayout) this.mLayoutPreference.findViewById(R$id.layout_right), 8, 11, 22, 14, R$string.bluetooth_right_name, 2);
                showBothDevicesBatteryPredictionIfNecessary();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Drawable createBtBatteryIcon(Context context, int i, boolean z) {
        BatteryMeterView.BatteryMeterDrawable batteryMeterDrawable = new BatteryMeterView.BatteryMeterDrawable(context, context.getColor(R$color.meter_background_color), context.getResources().getDimensionPixelSize(R$dimen.advanced_bluetooth_battery_meter_width), context.getResources().getDimensionPixelSize(R$dimen.advanced_bluetooth_battery_meter_height));
        batteryMeterDrawable.setBatteryLevel(i);
        batteryMeterDrawable.setColorFilter(new PorterDuffColorFilter(Utils.getColorAttrDefaultColor(context, 16843817), PorterDuff.Mode.SRC));
        batteryMeterDrawable.setCharging(z);
        return batteryMeterDrawable;
    }

    private void updateSubLayout(LinearLayout linearLayout, int i, int i2, int i3, int i4, int i5, int i6) {
        int batteryLevel;
        if (linearLayout != null) {
            BluetoothDevice device = this.mCachedDevice.getDevice();
            String stringMetaData = BluetoothUtils.getStringMetaData(device, i);
            int i7 = R$id.header_icon;
            ImageView imageView = (ImageView) linearLayout.findViewById(i7);
            Log.d(TAG, "updateSubLayout iconUri:" + stringMetaData + ", mIDeviceBitmap:" + this.mIDeviceBitmap + ",mIsNothingEarDeviceFromAddr:" + this.mIsNothingEarDeviceFromAddr);
            Log.d(TAG, "mIsNothingEarDevice:" + this.mIsNothingEarDevice + ", mIsHasPerm:" + this.mIsHasPerm + ", deviceId:" + i6);
            Log.d(TAG, "mIsConnectNothingDeviceService:" + this.mIsConnectNothingDeviceService + ", mIsInfoReceived:" + this.mIsInfoReceived + ", mIsAirpodDevice:" + this.mIsAirpodDevice);
            linearLayout.findViewById(i7).setBackground(this.mContext.getDrawable(R$drawable.circle_outline));
            if (this.mIDeviceBitmap != null) {
                Bitmap earBitmap = getEarBitmap(i6);
                updateIcon(linearLayout, imageView, earBitmap, "nothingear" + i6);
            } else if (stringMetaData != null) {
                Log.d(TAG, "mIsNothingEarDevice true, don't show fastpair");
                if ((this.mIsNothingEarDevice || this.mIsAirpodDevice) && this.mIsHasPerm) {
                    linearLayout.setVisibility(8);
                    return;
                }
                updateIcon(imageView, stringMetaData);
            } else {
                Log.d(TAG, "updateSubLayout use default");
                if ((this.mIsNothingEarDevice || this.mIsAirpodDevice) && this.mIsHasPerm) {
                    linearLayout.setVisibility(8);
                    return;
                }
                Pair<Drawable, String> btRainbowDrawableWithDescription = BluetoothUtils.getBtRainbowDrawableWithDescription(this.mContext, this.mCachedDevice);
                imageView.setImageDrawable(new PanelCircleDrawable((Drawable) btRainbowDrawableWithDescription.first));
                imageView.setContentDescription((CharSequence) btRainbowDrawableWithDescription.second);
            }
            int intMetaData = BluetoothUtils.getIntMetaData(device, i2);
            if (((this.mIsNothingEarDevice && this.mIsHasPerm && this.mIsConnectNothingDeviceService) || this.mIsInfoReceived) && ((batteryLevel = getBatteryLevel(i6)) != 0 || this.mIsAirpodDevice)) {
                intMetaData = batteryLevel;
            }
            boolean booleanMetaData = BluetoothUtils.getBooleanMetaData(device, i4);
            if (DEBUG) {
                Log.d(TAG, "updateSubLayout() icon : " + i + ", battery : " + i2 + ", charge : " + i4 + ", batteryLevel : " + intMetaData + ", charging : " + booleanMetaData + ", iconUri : " + stringMetaData);
            }
            if (i6 == 1 || i6 == 2) {
                showBatteryPredictionIfNecessary(linearLayout, i6, intMetaData);
            }
            TextView textView = (TextView) linearLayout.findViewById(R$id.bt_battery_summary);
            linearLayout.setVisibility(0);
            if (isUntetheredHeadset(device) || ((this.mIsNothingEarDevice && this.mIsConnectNothingDeviceService) || this.mIsInfoReceived)) {
                Log.d(TAG, "batteryLevel:" + intMetaData);
                if (intMetaData != -1) {
                    linearLayout.setVisibility(0);
                    textView.setText(Utils.formatPercentage(intMetaData));
                    textView.setVisibility(0);
                    int intMetaData2 = BluetoothUtils.getIntMetaData(device, i3);
                    if (intMetaData2 == -1) {
                        intMetaData2 = i2 == 12 ? 19 : 15;
                        if (this.mIsNothingEarDevice && this.mIsHasPerm) {
                            intMetaData2 = 10;
                        }
                    }
                    showBatteryIcon(linearLayout, intMetaData, intMetaData2, booleanMetaData);
                } else {
                    Log.d(TAG, "xx deviceId:" + i6);
                    if (i6 == 4) {
                        linearLayout.setVisibility(0);
                        linearLayout.findViewById(R$id.bt_battery_icon).setVisibility(8);
                        int batteryLevel2 = device.getBatteryLevel();
                        if (batteryLevel2 == -1 || batteryLevel2 == -100) {
                            textView.setVisibility(8);
                        } else {
                            textView.setText(Utils.formatPercentage(batteryLevel2));
                            textView.setVisibility(0);
                        }
                    } else {
                        linearLayout.setVisibility(8);
                    }
                }
            } else {
                textView.setVisibility(8);
            }
            TextView textView2 = (TextView) linearLayout.findViewById(R$id.header_title);
            if (i6 == 4) {
                textView2.setVisibility(8);
                return;
            }
            textView2.setText(i5);
            textView2.setVisibility(0);
        }
    }

    private boolean isUntetheredHeadset(BluetoothDevice bluetoothDevice) {
        return BluetoothUtils.getBooleanMetaData(bluetoothDevice, 6) || TextUtils.equals(BluetoothUtils.getStringMetaData(bluetoothDevice, 17), "Untethered Headset");
    }

    private void showBatteryPredictionIfNecessary(LinearLayout linearLayout, int i, int i2) {
        ThreadUtils.postOnBackgroundThread((Runnable) new C0762xd65cca4e(this, i, i2, linearLayout));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showBatteryPredictionIfNecessary$0(int i, int i2, LinearLayout linearLayout) {
        Cursor query = this.mContext.getContentResolver().query(new Uri.Builder().scheme("content").authority(this.mContext.getString(R$string.config_battery_prediction_authority)).appendPath(PATH).appendPath(DATABASE_ID).appendPath(DATABASE_BLUETOOTH).appendQueryParameter(QUERY_PARAMETER_ADDRESS, this.mCachedDevice.getAddress()).appendQueryParameter(QUERY_PARAMETER_BATTERY_ID, String.valueOf(i)).appendQueryParameter(QUERY_PARAMETER_BATTERY_LEVEL, String.valueOf(i2)).appendQueryParameter(QUERY_PARAMETER_TIMESTAMP, String.valueOf(System.currentTimeMillis())).build(), new String[]{BATTERY_ESTIMATE, ESTIMATE_READY}, (String) null, (String[]) null, (String) null);
        if (query == null) {
            Log.w(TAG, "showBatteryPredictionIfNecessary() cursor is null!");
            return;
        }
        try {
            query.moveToFirst();
            while (!query.isAfterLast()) {
                int i3 = query.getInt(query.getColumnIndex(ESTIMATE_READY));
                long j = query.getLong(query.getColumnIndex(BATTERY_ESTIMATE));
                if (DEBUG) {
                    Log.d(TAG, "showBatteryTimeIfNecessary() batteryId : " + i + ", ESTIMATE_READY : " + i3 + ", BATTERY_ESTIMATE : " + j);
                }
                showBatteryPredictionIfNecessary(i3, j, linearLayout);
                boolean z = false;
                if (i == 1) {
                    if (i3 == 1) {
                        z = true;
                    }
                    this.mIsLeftDeviceEstimateReady = z;
                } else if (i == 2) {
                    if (i3 == 1) {
                        z = true;
                    }
                    this.mIsRightDeviceEstimateReady = z;
                }
                query.moveToNext();
            }
        } finally {
            query.close();
        }
    }

    /* access modifiers changed from: package-private */
    public void showBatteryPredictionIfNecessary(int i, long j, LinearLayout linearLayout) {
        ThreadUtils.postOnMainThread(new C0765xd65cca51(this, linearLayout, i, j));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showBatteryPredictionIfNecessary$1(LinearLayout linearLayout, int i, long j) {
        TextView textView = (TextView) linearLayout.findViewById(R$id.bt_battery_prediction);
        if (i == 1) {
            textView.setVisibility(0);
            textView.setText(StringUtil.formatElapsedTime(this.mContext, (double) j, false, false));
            return;
        }
        textView.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public void showBothDevicesBatteryPredictionIfNecessary() {
        View findViewById = this.mLayoutPreference.findViewById(R$id.layout_left);
        int i = R$id.bt_battery_prediction;
        TextView textView = (TextView) findViewById.findViewById(i);
        TextView textView2 = (TextView) this.mLayoutPreference.findViewById(R$id.layout_right).findViewById(i);
        int i2 = 0;
        if (!(this.mIsLeftDeviceEstimateReady && this.mIsRightDeviceEstimateReady)) {
            i2 = 8;
        }
        ThreadUtils.postOnMainThread(new C0763xd65cca4f(textView, i2, textView2));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBothDevicesBatteryPredictionIfNecessary$2(TextView textView, int i, TextView textView2) {
        textView.setVisibility(i);
        textView2.setVisibility(i);
    }

    private void showBatteryIcon(LinearLayout linearLayout, int i, int i2, boolean z) {
        boolean z2 = i <= i2 && !z;
        ImageView imageView = (ImageView) linearLayout.findViewById(R$id.bt_battery_icon);
        if (z2) {
            imageView.setImageDrawable(this.mContext.getDrawable(R$drawable.ic_battery_alert_24dp));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.mContext.getResources().getDimensionPixelSize(R$dimen.advanced_bluetooth_battery_width), this.mContext.getResources().getDimensionPixelSize(R$dimen.advanced_bluetooth_battery_height));
            layoutParams.rightMargin = this.mContext.getResources().getDimensionPixelSize(R$dimen.advanced_bluetooth_battery_right_margin);
            imageView.setLayoutParams(layoutParams);
        } else {
            imageView.setImageDrawable(createBtBatteryIcon(this.mContext, i, z));
            imageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        }
        imageView.setVisibility(0);
    }

    private void updateDisconnectLayout() {
        this.mLayoutPreference.findViewById(R$id.layout_left).setVisibility(8);
        this.mLayoutPreference.findViewById(R$id.layout_right).setVisibility(8);
        LinearLayout linearLayout = (LinearLayout) this.mLayoutPreference.findViewById(R$id.layout_middle);
        linearLayout.setVisibility(0);
        linearLayout.findViewById(R$id.header_title).setVisibility(8);
        linearLayout.findViewById(R$id.bt_battery_summary).setVisibility(8);
        linearLayout.findViewById(R$id.bt_battery_icon).setVisibility(8);
        String stringMetaData = BluetoothUtils.getStringMetaData(this.mCachedDevice.getDevice(), 5);
        if (DEBUG) {
            Log.d(TAG, "updateDisconnectLayout() iconUri : " + stringMetaData);
        }
        Log.d(TAG, "updateDisconnectLayout() mIDeviceBitmap:" + this.mIDeviceBitmap + "iconUri:" + stringMetaData);
        int i = R$id.header_icon;
        ImageView imageView = (ImageView) linearLayout.findViewById(i);
        linearLayout.findViewById(i).setBackground(this.mContext.getDrawable(R$drawable.circle_outline));
        IDeviceBitmap iDeviceBitmap = this.mIDeviceBitmap;
        if (iDeviceBitmap != null && this.mIsHasPerm) {
            try {
                updateIcon(linearLayout, imageView, iDeviceBitmap.getDefaultBitmap(), "nothingear4");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (stringMetaData != null) {
            Log.d(TAG, "mIsNothingEarDevice true, don't show fastpair");
            if ((this.mIsNothingEarDevice || this.mIsAirpodDevice) && this.mIsHasPerm) {
                linearLayout.setVisibility(4);
            } else {
                updateIcon(imageView, stringMetaData);
            }
        } else {
            Log.d(TAG, "updateDisconnectLayout use default");
            if ((this.mIsNothingEarDevice || this.mIsAirpodDevice) && this.mIsHasPerm) {
                linearLayout.setVisibility(4);
                return;
            }
            Pair<Drawable, String> btRainbowDrawableWithDescription = BluetoothUtils.getBtRainbowDrawableWithDescription(this.mContext, this.mCachedDevice);
            linearLayout.findViewById(i).setBackground((Drawable) null);
            updateIcon(imageView, (Drawable) new PanelCircleDrawable((Drawable) btRainbowDrawableWithDescription.first));
        }
    }

    /* access modifiers changed from: package-private */
    public void updateIcon(ImageView imageView, String str) {
        if (this.mIconCache.containsKey(str)) {
            imageView.setImageBitmap(this.mIconCache.get(str));
            imageView.animate().alpha(1.0f).setDuration(300).start();
            return;
        }
        imageView.setAlpha(HALF_ALPHA);
        ThreadUtils.postOnBackgroundThread((Runnable) new C0764xd65cca50(this, str, imageView));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateIcon$4(String str, ImageView imageView) {
        Uri parse = Uri.parse(str);
        try {
            this.mContext.getContentResolver().takePersistableUriPermission(parse, 1);
            ThreadUtils.postOnMainThread(new C0766xd65cca52(this, str, MediaStore.Images.Media.getBitmap(this.mContext.getContentResolver(), parse), imageView));
        } catch (IOException e) {
            Log.e(TAG, "Failed to get bitmap for: " + str, e);
            if ((!this.mIsNothingEarDevice && !this.mIsAirpodDevice) || !this.mIsHasPerm) {
                updateIcon(imageView, (Drawable) new PanelCircleDrawable((Drawable) BluetoothUtils.getBtRainbowDrawableWithDescription(this.mContext, this.mCachedDevice).first));
            }
        } catch (SecurityException e2) {
            Log.e(TAG, "Failed to take persistable permission for: " + parse, e2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateIcon$3(String str, Bitmap bitmap, ImageView imageView) {
        this.mIconCache.put(str, bitmap);
        imageView.setImageBitmap(bitmap);
        imageView.animate().alpha(1.0f).setDuration(300).start();
    }

    public void onDeviceAttributesChanged() {
        if (this.mCachedDevice != null) {
            refresh();
        }
    }

    public void setIDeviceBitmap(IDeviceBitmap iDeviceBitmap) {
        this.mIDeviceBitmap = iDeviceBitmap;
    }

    public void setBatteryLeft(int i) {
        this.mBatteryLeft = i;
    }

    public void setBatteryRight(int i) {
        this.mBatteryRight = i;
    }

    public void setBatteryCase(int i) {
        this.mBatteryCase = i;
    }

    /* access modifiers changed from: package-private */
    public void updateIcon(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
        imageView.animate().alpha(1.0f).setDuration(300).start();
    }

    /* access modifiers changed from: package-private */
    public void updateIcon(LinearLayout linearLayout, ImageView imageView, Bitmap bitmap, String str) {
        Log.d(TAG, "---updateIcon isNothingIconAnimRun:" + this.isNothingIconAnimRun);
        if (!this.isNothingIconAnimRun.getOrDefault(str, Boolean.FALSE).booleanValue()) {
            linearLayout.setAlpha(0.0f);
            linearLayout.animate().alpha(1.0f).setDuration(300).start();
            this.isNothingIconAnimRun.put(str, Boolean.TRUE);
        }
        if (str == null || !this.mNothingIconCache.containsKey(str) || !this.mNothingIconCache.get(str).sameAs(bitmap)) {
            if (!(str == null || bitmap == null)) {
                this.mNothingIconCache.put(str, bitmap);
            }
            imageView.setImageBitmap(bitmap);
            imageView.animate().alpha(1.0f).setDuration(300).start();
            return;
        }
        Log.d(TAG, "mNothingIconCache has one already");
        imageView.setImageBitmap(this.mNothingIconCache.get(str));
        imageView.animate().alpha(1.0f).setDuration(300).start();
    }

    private Bitmap getEarBitmap(int i) {
        if (i == 1) {
            try {
                return this.mIDeviceBitmap.getLeftBitmap();
            } catch (Exception unused) {
                return null;
            }
        } else if (i == 2) {
            return this.mIDeviceBitmap.getRightBitmap();
        } else {
            return this.mIDeviceBitmap.getCaseBitmap();
        }
    }

    private int getBatteryLevel(int i) {
        if (!this.mIsConnectNothingDeviceService && !this.mIsInfoReceived) {
            return -1;
        }
        if (i == 1) {
            return this.mBatteryLeft;
        }
        if (i == 2) {
            return this.mBatteryRight;
        }
        return this.mBatteryCase;
    }

    public void setIsConnectNothingDeviceService(boolean z) {
        this.mIsConnectNothingDeviceService = z;
    }

    public void setIsInfoReceived(boolean z) {
        this.mIsInfoReceived = z;
    }

    public void onResume() {
        this.mIsHasPerm = NothingBluetoothUtil.getinstance().isNothingAppHasPermission(this.mContext, "com.nothing.smartcenter") && NothingBluetoothUtil.getinstance().isNothingAppEnabled(this.mContext, "com.nothing.smartcenter");
    }
}
