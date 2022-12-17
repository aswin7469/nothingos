package com.nothing.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.widget.AdaptiveOutlineDrawable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public class NothingBluetoothUtil {
    private static NothingBluetoothUtil mNothingBluetoothUtil = new NothingBluetoothUtil();
    private static HashMap<String, String> nothingears = new HashMap<>();
    private HashMap<String, String> mAirpodsVersion = new HashMap<>();
    private String mBluetoothAddress;
    private boolean mIsBtPanelShow;
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        }
    };
    private String mNoiseSelectedMode = null;
    private ScanCallback mScanCallback = new ScanCallback() {
        public void onScanResult(int i, ScanResult scanResult) {
            super.onScanResult(i, scanResult);
        }

        public void onBatchScanResults(List<ScanResult> list) {
            super.onBatchScanResults(list);
        }

        public void onScanFailed(int i) {
            super.onScanFailed(i);
        }
    };
    private ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();

    public String getMajorDeviceClass(int i) {
        switch (i) {
            case 0:
                return "MISC";
            case 256:
                return "COMPUTER";
            case 512:
                return "PHONE";
            case 768:
                return "NETWORKING";
            case 1024:
                return "AUDIO_VIDEO";
            case 1280:
                return "PERIPHERAL";
            case 1536:
                return "IMAGING";
            case 1792:
                return "WEARABLE";
            case 2048:
                return "TOY";
            case 2304:
                return "HEALTH";
            case 7936:
                return "UNCATEGORIZED";
            default:
                return null;
        }
    }

    private NothingBluetoothUtil() {
    }

    public static NothingBluetoothUtil getinstance() {
        return mNothingBluetoothUtil;
    }

    public ScanCallback getScanCallback() {
        return this.mScanCallback;
    }

    public boolean isNothingEarDevice(Context context, String str) {
        return !TextUtils.isEmpty(getModeID(context, str)) || !TextUtils.isEmpty(getConnectedDevice(context, str));
    }

    public String getModeID(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.toUpperCase().startsWith("2C:BE")) {
            Log.d("NothingBluetoothUtil", "return not nothing ear");
            return null;
        }
        String changeToSSPAdress = changeToSSPAdress(str);
        String str2 = nothingears.get(changeToSSPAdress);
        if (!TextUtils.isEmpty(str2)) {
            Log.d("NothingBluetoothUtil", "return moduID = " + str2);
            return str2;
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = getBLEModuleForSettingGlobal(context, changeToSSPAdress);
            Log.d("NothingBluetoothUtil", "return getBLEModuleForSettingGlobal = " + str2);
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = getModeIDFromNothingApp(context, str);
        }
        nothingears.put(changeToSSPAdress, str2);
        return str2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x008d, code lost:
        if (r8 == null) goto L_0x0090;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0090, code lost:
        return r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0070, code lost:
        if (r8 != null) goto L_0x0072;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0072, code lost:
        r8.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isNothingEarDeviceFromMacAddr(android.content.Context r10, java.lang.String r11, java.lang.String r12) {
        /*
            r9 = this;
            java.lang.String r9 = "NothingBluetoothUtil"
            java.lang.String r0 = "content://com.nothing.os.device.provider/check"
            android.net.Uri r2 = android.net.Uri.parse(r0)
            r0 = 2
            r7 = 0
            r8 = 0
            java.lang.String[] r5 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0078 }
            r5[r7] = r11     // Catch:{ Exception -> 0x0078 }
            r0 = 1
            r5[r0] = r12     // Catch:{ Exception -> 0x0078 }
            android.content.ContentResolver r1 = r10.getContentResolver()     // Catch:{ Exception -> 0x0078 }
            r3 = 0
            r4 = 0
            r6 = 0
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0078 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0078 }
            r10.<init>()     // Catch:{ Exception -> 0x0078 }
            java.lang.String r0 = "cursor:"
            r10.append(r0)     // Catch:{ Exception -> 0x0078 }
            r10.append(r8)     // Catch:{ Exception -> 0x0078 }
            java.lang.String r0 = ", addr:"
            r10.append(r0)     // Catch:{ Exception -> 0x0078 }
            r10.append(r11)     // Catch:{ Exception -> 0x0078 }
            java.lang.String r11 = ", name:"
            r10.append(r11)     // Catch:{ Exception -> 0x0078 }
            r10.append(r12)     // Catch:{ Exception -> 0x0078 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x0078 }
            android.util.Log.d(r9, r10)     // Catch:{ Exception -> 0x0078 }
            if (r8 == 0) goto L_0x0070
        L_0x0043:
            boolean r10 = r8.moveToNext()     // Catch:{ Exception -> 0x0078 }
            if (r10 == 0) goto L_0x0070
            java.lang.String r10 = "KEY_VALUE_BOOLEAN"
            int r10 = r8.getColumnIndex(r10)     // Catch:{ Exception -> 0x0078 }
            java.lang.String r10 = r8.getString(r10)     // Catch:{ Exception -> 0x0078 }
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r10)     // Catch:{ Exception -> 0x0078 }
            boolean r7 = r10.booleanValue()     // Catch:{ Exception -> 0x0078 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0078 }
            r10.<init>()     // Catch:{ Exception -> 0x0078 }
            java.lang.String r11 = "isNothing:"
            r10.append(r11)     // Catch:{ Exception -> 0x0078 }
            r10.append(r7)     // Catch:{ Exception -> 0x0078 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x0078 }
            android.util.Log.d(r9, r10)     // Catch:{ Exception -> 0x0078 }
            goto L_0x0043
        L_0x0070:
            if (r8 == 0) goto L_0x0090
        L_0x0072:
            r8.close()
            goto L_0x0090
        L_0x0076:
            r9 = move-exception
            goto L_0x0091
        L_0x0078:
            r10 = move-exception
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0076 }
            r11.<init>()     // Catch:{ all -> 0x0076 }
            java.lang.String r12 = "isNothingEarDeviceFromMacAddr e:"
            r11.append(r12)     // Catch:{ all -> 0x0076 }
            r11.append(r10)     // Catch:{ all -> 0x0076 }
            java.lang.String r10 = r11.toString()     // Catch:{ all -> 0x0076 }
            android.util.Log.d(r9, r10)     // Catch:{ all -> 0x0076 }
            if (r8 == 0) goto L_0x0090
            goto L_0x0072
        L_0x0090:
            return r7
        L_0x0091:
            if (r8 == 0) goto L_0x0096
            r8.close()
        L_0x0096:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.bluetooth.NothingBluetoothUtil.isNothingEarDeviceFromMacAddr(android.content.Context, java.lang.String, java.lang.String):boolean");
    }

    public String getModeIDFromNothingApp(Context context, String str) {
        Uri parse = Uri.parse("content://com.nothing.os.device.provider/device/" + "device_address" + "/" + str);
        String str2 = null;
        try {
            Cursor query = context.getContentResolver().query(parse, (String[]) null, (String) null, (String[]) null, (String) null);
            Log.d("NothingBluetoothUtil", "cursor:" + query + ", classicAddress:" + str);
            if (query != null) {
                while (query.moveToNext()) {
                    str2 = query.getString(query.getColumnIndex("KEY_MODEL_ID"));
                    Log.d("NothingBluetoothUtil", "modeId:" + str2);
                }
                query.close();
            }
        } catch (Exception e) {
            Log.d("NothingBluetoothUtil", "getModeIDFromNothingApp e:" + e);
        }
        return str2;
    }

    public String changeToSSPAdress(String str) {
        String str2 = "";
        if (str == null) {
            return str2;
        }
        String[] split = str.toLowerCase().split(":");
        for (int length = split.length; length > 0; length--) {
            str2 = str2 + split[length - 1];
        }
        return str2;
    }

    public String getConnectedDevice(Context context, String str) {
        return context.getSharedPreferences("bluetooth_paired", 0).getString(changeToSSPAdress(str), (String) null);
    }

    public void saveConnectedDevice(Context context, String str) {
        if (str != null) {
            String changeToSSPAdress = changeToSSPAdress(str);
            String modeID = getModeID(context, str);
            if (modeID != null && modeID.length() != 0) {
                context.getSharedPreferences("bluetooth_paired", 0).edit().putString(changeToSSPAdress, modeID).apply();
            }
        }
    }

    public String getAirpodsVersion(Context context, String str) {
        HashMap<String, String> hashMap = this.mAirpodsVersion;
        String orDefault = hashMap.getOrDefault("airpods_version" + str, (Object) null);
        if (TextUtils.isEmpty(orDefault)) {
            String string = Settings.Global.getString(context.getContentResolver(), "airpods_version");
            if (!TextUtils.isEmpty(string)) {
                try {
                    orDefault = new JSONObject(string).optString(str);
                    Log.d("NothingBluetoothUtil", "getAirpodsVersion version: " + orDefault);
                    if (!TextUtils.isEmpty(orDefault)) {
                        HashMap<String, String> hashMap2 = this.mAirpodsVersion;
                        hashMap2.put("airpods_version" + str, orDefault);
                    }
                } catch (JSONException unused) {
                }
            }
        }
        return orDefault;
    }

    public void saveAirpodsVersion(Context context, String str, String str2) {
        JSONObject jSONObject;
        if (str != null) {
            HashMap<String, String> hashMap = this.mAirpodsVersion;
            hashMap.put("airpods_version" + str, str2);
            try {
                String string = Settings.Global.getString(context.getContentResolver(), "airpods_version");
                if (!TextUtils.isEmpty(string)) {
                    jSONObject = new JSONObject(string);
                    String optString = jSONObject.optString(str);
                    Log.d("NothingBluetoothUtil", "saveAirpodsVersion tmpVersion:" + optString);
                    if (!TextUtils.isEmpty(optString)) {
                        jSONObject.remove(str);
                    }
                } else {
                    jSONObject = new JSONObject();
                }
                jSONObject.put(str, str2);
                Log.d("NothingBluetoothUtil", "jsonObject.toString() : " + jSONObject);
                Settings.Global.putString(context.getContentResolver(), "airpods_version", jSONObject.toString());
            } catch (JSONException unused) {
            }
        }
    }

    public void setBluetoothAddress(String str) {
        this.mBluetoothAddress = str;
    }

    public boolean checkUUIDIsAirpod(Context context, BluetoothDevice bluetoothDevice) {
        if (!isSupportAirpods(context, bluetoothDevice.getAddress())) {
            return false;
        }
        ParcelUuid[] parcelUuidArr = {ParcelUuid.fromString("74ec2172-0bad-4d01-8f77-997b2be0722a"), ParcelUuid.fromString("2a72e02b-7b99-778f-014d-ad0b7221ec74")};
        ParcelUuid[] uuids = bluetoothDevice.getUuids();
        if (uuids == null) {
            return false;
        }
        for (ParcelUuid parcelUuid : uuids) {
            for (int i = 0; i < 2; i++) {
                if (parcelUuid.equals(parcelUuidArr[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getBLEModuleForSettingGlobal(Context context, String str) {
        try {
            String string = Settings.Global.getString(context.getContentResolver(), "nt_ear_ble_module_ids");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            String str2 = (String) new JSONObject(string).get(str);
            if (!TextUtils.isEmpty(str2)) {
                return str2;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean isNothingAppHasPermission(Context context, String str) {
        String[] strArr = {"android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_ADVERTISE"};
        PackageManager packageManager = context.getPackageManager();
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

    public boolean isNothingAppInstalled(PackageManager packageManager, String str) {
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (int i = 0; i < installedPackages.size(); i++) {
                if (installedPackages.get(i).packageName.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNothingAppEnabled(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        if (!isNothingAppInstalled(packageManager, str)) {
            return false;
        }
        Log.d("NothingBluetoothUtil", "getApplicationEnabledSetting:" + packageManager.getApplicationEnabledSetting(str));
        if (packageManager.getApplicationEnabledSetting(str) == 3) {
            return false;
        }
        return true;
    }

    public void saveModuleIDEarBitmap(Context context, final Bitmap bitmap, final String str) {
        if (str != null) {
            this.mSingleThreadExecutor.execute(new Runnable() {
                public void run() {
                    String str = Environment.getExternalStorageDirectory() + "/.nomedia/";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str, str + ".png");
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
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0079 A[SYNTHETIC, Splitter:B:29:0x0079] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0086 A[SYNTHETIC, Splitter:B:37:0x0086] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable getModuleIDBitmap(android.content.Context r7, java.lang.String r8) {
        /*
            r6 = this;
            java.lang.String r0 = "Cannot close image stream"
            java.lang.String r1 = "NothingBluetoothUtil"
            android.content.res.Resources r2 = r7.getResources()
            int r3 = com.android.settingslib.R$dimen.bt_nearby_icon_size
            int r2 = r2.getDimensionPixelSize(r3)
            r3 = 0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            r4.<init>()     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            java.io.File r5 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            r4.append(r5)     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            java.lang.String r5 = "/.nomedia/"
            r4.append(r5)     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            r5.<init>()     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            r5.append(r8)     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            java.lang.String r8 = ".png"
            r5.append(r8)     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            java.lang.String r8 = r5.toString()     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            r5.<init>(r4, r8)     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            boolean r8 = r5.exists()     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            if (r8 != 0) goto L_0x0041
            return r3
        L_0x0041:
            java.io.FileInputStream r8 = new java.io.FileInputStream     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            r8.<init>(r5)     // Catch:{ Exception -> 0x006e, all -> 0x006c }
            android.graphics.Bitmap r4 = android.graphics.BitmapFactory.decodeStream(r8)     // Catch:{ Exception -> 0x006a }
            if (r4 != 0) goto L_0x0055
            r8.close()     // Catch:{ IOException -> 0x0050 }
            goto L_0x0054
        L_0x0050:
            r6 = move-exception
            android.util.Log.e(r1, r0, r6)
        L_0x0054:
            return r3
        L_0x0055:
            r5 = 0
            android.graphics.Bitmap r2 = android.graphics.Bitmap.createScaledBitmap(r4, r2, r2, r5)     // Catch:{ Exception -> 0x006a }
            r4.recycle()     // Catch:{ Exception -> 0x006a }
            android.graphics.drawable.Drawable r6 = r6.bitmap2Drawable(r7, r2)     // Catch:{ Exception -> 0x006a }
            r8.close()     // Catch:{ IOException -> 0x0065 }
            goto L_0x0069
        L_0x0065:
            r7 = move-exception
            android.util.Log.e(r1, r0, r7)
        L_0x0069:
            return r6
        L_0x006a:
            r6 = move-exception
            goto L_0x0070
        L_0x006c:
            r6 = move-exception
            goto L_0x0084
        L_0x006e:
            r6 = move-exception
            r8 = r3
        L_0x0070:
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0082 }
            android.util.Log.e(r1, r6)     // Catch:{ all -> 0x0082 }
            if (r8 == 0) goto L_0x0081
            r8.close()     // Catch:{ IOException -> 0x007d }
            goto L_0x0081
        L_0x007d:
            r6 = move-exception
            android.util.Log.e(r1, r0, r6)
        L_0x0081:
            return r3
        L_0x0082:
            r6 = move-exception
            r3 = r8
        L_0x0084:
            if (r3 == 0) goto L_0x008e
            r3.close()     // Catch:{ IOException -> 0x008a }
            goto L_0x008e
        L_0x008a:
            r7 = move-exception
            android.util.Log.e(r1, r0, r7)
        L_0x008e:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.bluetooth.NothingBluetoothUtil.getModuleIDBitmap(android.content.Context, java.lang.String):android.graphics.drawable.Drawable");
    }

    private Drawable bitmap2Drawable(Context context, Bitmap bitmap) {
        return new AdaptiveOutlineDrawable(context.getResources(), bitmap);
    }

    public void startBleScanning(BluetoothAdapter bluetoothAdapter) {
        try {
            bluetoothAdapter.getBluetoothLeScanner().startScan(getinstance().getScanCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBleScanning(BluetoothAdapter bluetoothAdapter) {
        try {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(getinstance().getScanCallback());
        } catch (Exception unused) {
        }
    }

    public boolean getBtPanelShow() {
        return this.mIsBtPanelShow;
    }

    public void setNoiseSelectedMode(String str) {
        this.mNoiseSelectedMode = str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0066, code lost:
        if (r7 == null) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0069, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0049, code lost:
        if (r7 != null) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x004b, code lost:
        r7.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSupportAirpods(android.content.Context r9, java.lang.String r10) {
        /*
            r8 = this;
            java.lang.String r8 = "NothingBluetoothUtil"
            java.lang.String r0 = "content://com.nothing.os.device.provider/support_airpods"
            android.net.Uri r2 = android.net.Uri.parse(r0)
            r0 = 1
            r7 = 0
            java.lang.String[] r5 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0051 }
            r1 = 0
            r5[r1] = r10     // Catch:{ Exception -> 0x0051 }
            android.content.ContentResolver r1 = r9.getContentResolver()     // Catch:{ Exception -> 0x0051 }
            r3 = 0
            r4 = 0
            r6 = 0
            android.database.Cursor r7 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0051 }
            if (r7 == 0) goto L_0x0049
        L_0x001c:
            boolean r9 = r7.moveToNext()     // Catch:{ Exception -> 0x0051 }
            if (r9 == 0) goto L_0x0049
            java.lang.String r9 = "KEY_VALUE_BOOLEAN"
            int r9 = r7.getColumnIndex(r9)     // Catch:{ Exception -> 0x0051 }
            java.lang.String r9 = r7.getString(r9)     // Catch:{ Exception -> 0x0051 }
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)     // Catch:{ Exception -> 0x0051 }
            boolean r0 = r9.booleanValue()     // Catch:{ Exception -> 0x0051 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0051 }
            r9.<init>()     // Catch:{ Exception -> 0x0051 }
            java.lang.String r10 = "isSupportAirpods:"
            r9.append(r10)     // Catch:{ Exception -> 0x0051 }
            r9.append(r0)     // Catch:{ Exception -> 0x0051 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x0051 }
            android.util.Log.d(r8, r9)     // Catch:{ Exception -> 0x0051 }
            goto L_0x001c
        L_0x0049:
            if (r7 == 0) goto L_0x0069
        L_0x004b:
            r7.close()
            goto L_0x0069
        L_0x004f:
            r8 = move-exception
            goto L_0x006a
        L_0x0051:
            r9 = move-exception
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x004f }
            r10.<init>()     // Catch:{ all -> 0x004f }
            java.lang.String r1 = "isSupportAirpods e:"
            r10.append(r1)     // Catch:{ all -> 0x004f }
            r10.append(r9)     // Catch:{ all -> 0x004f }
            java.lang.String r9 = r10.toString()     // Catch:{ all -> 0x004f }
            android.util.Log.d(r8, r9)     // Catch:{ all -> 0x004f }
            if (r7 == 0) goto L_0x0069
            goto L_0x004b
        L_0x0069:
            return r0
        L_0x006a:
            if (r7 == 0) goto L_0x006f
            r7.close()
        L_0x006f:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.bluetooth.NothingBluetoothUtil.isSupportAirpods(android.content.Context, java.lang.String):boolean");
    }

    public void saveSendTrackingDevice(Context context, String str) {
        if (str != null) {
            context.getSharedPreferences("bluetooth_send_tracking_device", 0).edit().putBoolean(str, true).apply();
        }
    }

    public boolean getSendedTrackingDevice(Context context, String str) {
        if (str == null) {
            return false;
        }
        return context.getSharedPreferences("bluetooth_send_tracking_device", 0).getBoolean(str, false);
    }
}
