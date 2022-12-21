package com.nothing.experience.datas;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.nothing.experience.App;
import com.nothing.experience.network.ThreadPoolManager;
import com.nothing.experience.utils.DeviceIdFactory;
import com.nothing.experience.utils.SoftwareInfoUtil;
import java.util.ArrayList;
import java.util.List;

public class EventProvider extends ContentProvider {
    private static final String AUTHORITY = "com.nothing.experience";
    public static final String CONTENT_ACTIVATION_ITEM_TYPE = "vnd.android.cursor.item/activation";
    public static final String CONTENT_ACTIVATION_TYPE = "vnd.android.cursor.dir/activation";
    public static final Uri CONTENT_ACTIVATION_URI = Uri.parse("content://com.nothing.experience/activation");
    public static final String CONTENT_ITEM_PRODUCT_TYPE = "vnd.android.cursor.item/events";
    public static final String CONTENT_PRODUCT_TYPE = "vnd.android.cursor.dir/events";
    public static final Uri CONTENT_PRODUCT_URI = Uri.parse("content://com.nothing.experience/events");
    public static final String CONTENT_QUALITY_ITEM_TYPE = "vnd.android.cursor.item/quality";
    public static final String CONTENT_QUALITY_TYPE = "vnd.android.cursor.dir/quality";
    public static final Uri CONTENT_QUALITY_URI = Uri.parse("content://com.nothing.experience/quality");
    static final String EVENT_APP_NAME = "app_name";
    static final String EVENT_EVENT_TIME = "event_time";
    static final String EVENT_NAME = "event_name";
    static final String EVENT_PACKAGE_NAME = "package_name";
    static final String EVENT_PACKAGE_VERSION = "app_version";
    static final String EVENT_PARAMS = "event_params_key";
    static final String EVENT_PARAMS_STRING_VALUE = "event_params_value_string_value";
    private static final int EVENT_TYPE_ACTIVATION = 1;
    private static final String EVENT_TYPE_KEY = "event_type";
    private static final int EVENT_TYPE_PRODUCT = 0;
    private static final int EVENT_TYPE_QUALITY = 2;
    public static final int ITEM_ACTIVATION = 5;
    public static final int ITEM_ACTIVATION_ID = 6;
    public static final int ITEM_PRODUCT = 1;
    public static final int ITEM_PRODUCT_ID = 2;
    public static final int ITEM_QUALITY = 3;
    public static final int ITEM_QUALITY_ID = 4;
    private static final String PERMISSION = "com.nothing.experience.PERMISSION";
    private static final String TAG = "Events";
    private static UriMatcher mUriMatcher;
    private DataHelper mDataHelper;
    private SQLiteDatabase mDb;
    private ThreadPoolManager mPool = ThreadPoolManager.getInsertInstance();

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        mUriMatcher = uriMatcher;
        uriMatcher.addURI("com.nothing.experience", DataHelper.TABLE_PRODUCT, 1);
        mUriMatcher.addURI("com.nothing.experience", "events/#", 2);
        mUriMatcher.addURI("com.nothing.experience", DataHelper.TABLE_ACTIVATION, 5);
        mUriMatcher.addURI("com.nothing.experience", "activation/#", 6);
        mUriMatcher.addURI("com.nothing.experience", DataHelper.TABLE_QUALITY, 3);
        mUriMatcher.addURI("com.nothing.experience", "quality/#", 4);
    }

    public boolean onCreate() {
        DataHelper instance = DataHelper.getInstance(getContext());
        this.mDataHelper = instance;
        this.mDb = instance.getWritableDatabase();
        return true;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Cursor cursor;
        int match = mUriMatcher.match(uri);
        if (match == 1) {
            cursor = this.mDb.query(DataHelper.TABLE_PRODUCT, strArr, str, strArr2, (String) null, (String) null, str2);
        } else if (match == 2) {
            SQLiteDatabase sQLiteDatabase = this.mDb;
            cursor = sQLiteDatabase.query(DataHelper.TABLE_PRODUCT, strArr, "event_id=" + uri.getLastPathSegment(), strArr2, (String) null, (String) null, str2);
        } else {
            throw new IllegalArgumentException("Unknown URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    public String getType(Uri uri) {
        mUriMatcher.match(uri);
        switch (mUriMatcher.match(uri)) {
            case 1:
                return CONTENT_PRODUCT_TYPE;
            case 2:
                return CONTENT_ITEM_PRODUCT_TYPE;
            case 3:
                return CONTENT_QUALITY_TYPE;
            case 4:
                return CONTENT_QUALITY_ITEM_TYPE;
            case 5:
                return CONTENT_ACTIVATION_TYPE;
            case 6:
                return CONTENT_ACTIVATION_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI->" + uri);
        }
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        if (!App.startCompleted) {
            return null;
        }
        int intValue = contentValues.getAsInteger(EVENT_TYPE_KEY).intValue();
        boolean z = false;
        boolean z2 = intValue == 1;
        if (intValue == 2) {
            z = true;
        }
        mUriMatcher.match(uri);
        if (mUriMatcher.match(uri) == 1 || mUriMatcher.match(uri) == 3 || mUriMatcher.match(uri) == 5) {
            contentValues.remove(EVENT_TYPE_KEY);
            insetMainProperties(contentValues);
            String deviceId = getDeviceId();
            if (z) {
                if (!userAgreeExperience() || TextUtils.isEmpty(deviceId)) {
                    return null;
                }
                contentValues.put(DataHelper.EVENT_DEVICE_ID, deviceId);
                return insertQualityInner(uri, contentValues);
            } else if (z2) {
                return insertActivationInner(uri, contentValues);
            } else {
                if ((!userAgreeExperience() && !isCarrier(contentValues.getAsString("event_params_key"))) || TextUtils.isEmpty(deviceId)) {
                    return null;
                }
                contentValues.put(DataHelper.EVENT_DEVICE_ID, deviceId);
                return insertProductInner(uri, contentValues);
            }
            return null;
        }
        throw new IllegalArgumentException("Unknown URI->" + uri);
    }

    private String getDeviceId() {
        return DeviceIdFactory.uuid;
    }

    private Uri insertProductInner(Uri uri, ContentValues contentValues) {
        long insert = this.mDb.insert(DataHelper.TABLE_PRODUCT, (String) null, contentValues);
        if (insert > 0) {
            Uri withAppendedId = ContentUris.withAppendedId(CONTENT_PRODUCT_URI, insert);
            getContext().getContentResolver().notifyChange(withAppendedId, (ContentObserver) null);
            return withAppendedId;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    private Uri insertQualityInner(Uri uri, ContentValues contentValues) {
        long insert = this.mDb.insert(DataHelper.TABLE_QUALITY, (String) null, contentValues);
        if (insert > 0) {
            Uri withAppendedId = ContentUris.withAppendedId(CONTENT_QUALITY_URI, insert);
            getContext().getContentResolver().notifyChange(withAppendedId, (ContentObserver) null);
            return withAppendedId;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    private Uri insertActivationInner(Uri uri, ContentValues contentValues) {
        long insert = this.mDb.insert(DataHelper.TABLE_ACTIVATION, (String) null, contentValues);
        if (insert > 0) {
            Uri withAppendedId = ContentUris.withAppendedId(CONTENT_ACTIVATION_URI, insert);
            getContext().getContentResolver().notifyChange(withAppendedId, (ContentObserver) null);
            return withAppendedId;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    private void insetMainProperties(ContentValues contentValues) {
        contentValues.put(DataHelper.EVENT_SYSTEM_VERSION, SoftwareInfoUtil.getAndroidVersion());
        contentValues.put(DataHelper.EVENT_LANGUAGE, SoftwareInfoUtil.getSystemLanguage());
        contentValues.put(DataHelper.EVENT_DEVICE_MODEL, SoftwareInfoUtil.getSystemModel());
        contentValues.put(DataHelper.EVENT_BRAND_NAME, SoftwareInfoUtil.getDeviceBrand());
        contentValues.put(DataHelper.EVENT_SIM_COUNTRY_CODE, SoftwareInfoUtil.getNonPreciseCountryCode(getContext()));
        contentValues.put(DataHelper.EVENT_BRAND_OS_VERSION, SoftwareInfoUtil.getDisplayBuildNumber());
        contentValues.put(DataHelper.EVENT_PROJECT_NAME, SoftwareInfoUtil.getDeviceName());
        if (TextUtils.isEmpty(App.RAM_SIZE)) {
            App.RAM_SIZE = SoftwareInfoUtil.getRamSize(getContext());
        }
        if (TextUtils.isEmpty(App.ROM_SIZE)) {
            App.ROM_SIZE = SoftwareInfoUtil.getRomSize(getContext());
        }
        if (TextUtils.isEmpty(App.DEVICE_COLOR)) {
            App.DEVICE_COLOR = SoftwareInfoUtil.getDeviceColor();
        }
        contentValues.put(DataHelper.EVENT_DEVICE_RAM_SIZE, App.RAM_SIZE);
        contentValues.put(DataHelper.EVENT_DEVICE_ROM_SIZE, App.ROM_SIZE);
        contentValues.put(DataHelper.EVENT_DEVICE_COLOR, App.DEVICE_COLOR);
    }

    private boolean userAgreeExperience() {
        return Settings.System.getInt(getContext().getContentResolver(), "nt_data_collection", 0) == 1;
    }

    private boolean isCarrier(String str) {
        return str != null && (str.contains("sim1_carrier") || str.contains("sim2_carrier"));
    }

    private void enforceWritePermission(String str) {
        if (getContext().checkCallingOrSelfPermission(str) != 0) {
            throw new SecurityException("Permission denial: writing to experience requires:" + str);
        }
    }

    public int delete(Uri uri, String str, String[] strArr) {
        Log.w(TAG, "delete action not supported");
        return 0;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        Log.w(TAG, "update action not supported");
        return 0;
    }

    private Uri getUriByType(int i) {
        if (i == 0) {
            return CONTENT_PRODUCT_URI;
        }
        if (i == 1) {
            return CONTENT_ACTIVATION_URI;
        }
        return CONTENT_QUALITY_URI;
    }

    public Bundle call(String str, String str2, Bundle bundle) {
        this.mPool.execute(new EventProvider$$ExternalSyntheticLambda0(this, str, str2, bundle));
        return super.call(str, str, bundle);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x006c, code lost:
        if (r9.contains(com.nothing.experience.datas.DataHelper.EVENT_DEVICE_ID2) != false) goto L_0x006e;
     */
    /* renamed from: callInner */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void m90lambda$call$0$comnothingexperiencedatasEventProvider(java.lang.String r13, java.lang.String r14, android.os.Bundle r15) {
        /*
            r12 = this;
            java.lang.String r14 = "event_time"
            java.lang.String r0 = "app_name"
            java.lang.String r1 = "app_version"
            java.lang.String r2 = "package_name"
            java.lang.String r3 = "event_name"
            java.lang.String r4 = "event_params_value_string_value"
            java.lang.String r5 = "event_params_key"
            java.lang.String r6 = "device_id"
            int r13 = java.lang.Integer.parseInt(r13)     // Catch:{ Exception -> 0x00a9 }
            android.net.Uri r7 = r12.getUriByType(r13)     // Catch:{ Exception -> 0x00a9 }
            android.content.ContentValues r8 = new android.content.ContentValues     // Catch:{ Exception -> 0x00a9 }
            r8.<init>()     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r9 = r15.getString(r5)     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r10 = r15.getString(r4)     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r11 = r15.getString(r3)     // Catch:{ Exception -> 0x00a9 }
            r8.put(r3, r11)     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r3 = r15.getString(r2)     // Catch:{ Exception -> 0x00a9 }
            r8.put(r2, r3)     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r2 = r15.getString(r1)     // Catch:{ Exception -> 0x00a9 }
            r8.put(r1, r2)     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r1 = r15.getString(r0)     // Catch:{ Exception -> 0x00a9 }
            r8.put(r0, r1)     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r15 = r15.getString(r14)     // Catch:{ Exception -> 0x00a9 }
            r8.put(r14, r15)     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r14 = "event_type"
            java.lang.Integer r15 = java.lang.Integer.valueOf(r13)     // Catch:{ Exception -> 0x00a9 }
            r8.put(r14, r15)     // Catch:{ Exception -> 0x00a9 }
            r8.put(r5, r9)     // Catch:{ Exception -> 0x00a9 }
            r8.put(r4, r10)     // Catch:{ Exception -> 0x00a9 }
            r14 = 1
            if (r13 != r14) goto L_0x00a5
            boolean r13 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Exception -> 0x00a9 }
            if (r13 != 0) goto L_0x00a5
            boolean r13 = r9.contains(r6)     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r14 = "device_id2"
            if (r13 != 0) goto L_0x006e
            boolean r13 = r9.contains(r14)     // Catch:{ Exception -> 0x00a9 }
            if (r13 == 0) goto L_0x00a5
        L_0x006e:
            java.util.List r13 = r12.generateEventParams(r9, r10)     // Catch:{ Exception -> 0x00a9 }
            if (r13 == 0) goto L_0x00a5
            int r15 = r13.size()     // Catch:{ Exception -> 0x00a9 }
            if (r15 <= 0) goto L_0x00a5
            java.util.Iterator r13 = r13.iterator()     // Catch:{ Exception -> 0x00a9 }
        L_0x007e:
            boolean r15 = r13.hasNext()     // Catch:{ Exception -> 0x00a9 }
            if (r15 == 0) goto L_0x00a5
            java.lang.Object r15 = r13.next()     // Catch:{ Exception -> 0x00a9 }
            com.nothing.experience.datas.EventParam r15 = (com.nothing.experience.datas.EventParam) r15     // Catch:{ Exception -> 0x00a9 }
            java.lang.String r0 = r15.key     // Catch:{ Exception -> 0x00a9 }
            boolean r0 = r6.equals(r0)     // Catch:{ Exception -> 0x00a9 }
            if (r0 == 0) goto L_0x0097
            java.lang.String r0 = r15.value     // Catch:{ Exception -> 0x00a9 }
            r8.put(r6, r0)     // Catch:{ Exception -> 0x00a9 }
        L_0x0097:
            java.lang.String r0 = r15.key     // Catch:{ Exception -> 0x00a9 }
            boolean r0 = r14.equals(r0)     // Catch:{ Exception -> 0x00a9 }
            if (r0 == 0) goto L_0x007e
            java.lang.String r15 = r15.value     // Catch:{ Exception -> 0x00a9 }
            r8.put(r14, r15)     // Catch:{ Exception -> 0x00a9 }
            goto L_0x007e
        L_0x00a5:
            r12.insert(r7, r8)     // Catch:{ Exception -> 0x00a9 }
            goto L_0x00ad
        L_0x00a9:
            r13 = move-exception
            r13.printStackTrace()
        L_0x00ad:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.experience.datas.EventProvider.m90lambda$call$0$comnothingexperiencedatasEventProvider(java.lang.String, java.lang.String, android.os.Bundle):void");
    }

    private List<EventParam> generateEventParams(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        String[] split = str.split("\\*");
        String[] split2 = str2.split("\\*");
        if (split.length != split2.length) {
            return null;
        }
        int length = split.length;
        for (int i = 0; i < length; i++) {
            EventParam eventParam = new EventParam();
            eventParam.key = split[i];
            String str3 = split2[i];
            eventParam.value = str3;
            if (str3 != null) {
                eventParam.value_type = str3.getClass().getSimpleName();
            }
            arrayList.add(eventParam);
        }
        return arrayList;
    }
}
