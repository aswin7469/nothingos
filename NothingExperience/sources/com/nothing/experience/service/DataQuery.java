package com.nothing.experience.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.nothing.experience.App;
import com.nothing.experience.datas.AppInfo;
import com.nothing.experience.datas.DataHelper;
import com.nothing.experience.datas.DeviceInfo;
import com.nothing.experience.datas.Event;
import com.nothing.experience.datas.EventParam;
import com.nothing.experience.datas.LocalData;
import com.nothing.experience.network.ThreadPoolManager;
import com.nothing.experience.utils.DeviceIdFactory;
import com.nothing.experience.utils.SoftwareInfoUtil;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;

public class DataQuery {
    private static final String[] QUERY_ITEMS = {DataHelper.EVENT_ID, DataHelper.EVENT_NAME, DataHelper.EVENT_APP_NAME, DataHelper.EVENT_PARAMS, DataHelper.EVENT_PARAMS_STRING_VALUE, DataHelper.EVENT_PROJECT_NAME, DataHelper.EVENT_PACKAGE_NAME, DataHelper.EVENT_PACKAGE_VERSION, DataHelper.EVENT_BRAND_OS_VERSION, DataHelper.EVENT_SYSTEM_VERSION, DataHelper.EVENT_DEVICE_MODEL, DataHelper.EVENT_DEVICE_ID, DataHelper.EVENT_DEVICE_ID2, DataHelper.EVENT_OPERATOR_1, DataHelper.EVENT_OPERATOR_2, DataHelper.EVENT_DEVICE_CATEGORY, DataHelper.EVENT_DEVICE_RAM_SIZE, DataHelper.EVENT_DEVICE_ROM_SIZE, DataHelper.EVENT_DEVICE_COLOR, DataHelper.EVENT_LANGUAGE, DataHelper.EVENT_EVENT_TIME, DataHelper.EVENT_BRAND_NAME, DataHelper.EVENT_NETWORK_TYPE, DataHelper.EVENT_SIM_COUNTRY_CODE};
    private Context mContext;
    private SQLiteDatabase mDb;
    private DataHelper mHelper;
    private ThreadPoolManager mThreadManager = ThreadPoolManager.getQueryInstance();

    public DataQuery(Context context) {
        this.mContext = context;
        DataHelper instance = DataHelper.getInstance(context);
        this.mHelper = instance;
        this.mDb = instance.getWritableDatabase();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0020, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void deleteUploadedData(com.nothing.experience.datas.LocalData r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 == 0) goto L_0x001f
            java.util.List<com.nothing.experience.datas.Event> r0 = r3.events     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x001f
            java.util.List<com.nothing.experience.datas.Event> r0 = r3.events     // Catch:{ all -> 0x001c }
            int r0 = r0.size()     // Catch:{ all -> 0x001c }
            if (r0 != 0) goto L_0x0010
            goto L_0x001f
        L_0x0010:
            com.nothing.experience.network.ThreadPoolManager r0 = r2.mThreadManager     // Catch:{ all -> 0x001c }
            com.nothing.experience.service.DataQuery$$ExternalSyntheticLambda0 r1 = new com.nothing.experience.service.DataQuery$$ExternalSyntheticLambda0     // Catch:{ all -> 0x001c }
            r1.<init>(r2, r3)     // Catch:{ all -> 0x001c }
            r0.execute(r1)     // Catch:{ all -> 0x001c }
            monitor-exit(r2)
            return
        L_0x001c:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        L_0x001f:
            monitor-exit(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.experience.service.DataQuery.deleteUploadedData(com.nothing.experience.datas.LocalData):void");
    }

    /* renamed from: lambda$deleteUploadedData$0$com-nothing-experience-service-DataQuery */
    public /* synthetic */ void mo13945xd6a5a995(LocalData localData) {
        String str = LocalData.sTypeTableMapping.get(localData.dataType);
        for (Event event : localData.events) {
            this.mDb.delete(str, "event_id=?", new String[]{event.event_id + HttpUrl.FRAGMENT_ENCODE_SET});
        }
    }

    public synchronized void queryProductLastItem(DataQueryListener dataQueryListener) {
        queryProductItemByLimited(dataQueryListener, "1");
    }

    public void queryProductItemByLimited(DataQueryListener dataQueryListener, String str) {
        this.mThreadManager.execute(new DataQuery$$ExternalSyntheticLambda2(this, str, dataQueryListener));
    }

    /* renamed from: lambda$queryProductItemByLimited$1$com-nothing-experience-service-DataQuery */
    public /* synthetic */ void mo13947xddbc8da(String str, DataQueryListener dataQueryListener) {
        LocalData readDataListFromCursor = readDataListFromCursor(this.mDb.query(DataHelper.TABLE_PRODUCT, QUERY_ITEMS, (String) null, (String[]) null, (String) null, (String) null, "event_id DESC", str), 0, dataQueryListener);
        if (dataQueryListener != null) {
            dataQueryListener.onSuccess(readDataListFromCursor);
        }
    }

    public void queryActivationLastItem(DataQueryListener dataQueryListener) {
        queryActivationItemByLimited(dataQueryListener, "1");
    }

    public void queryActivationItemByLimited(DataQueryListener dataQueryListener, String str) {
        this.mThreadManager.execute(new DataQuery$$ExternalSyntheticLambda1(this, str, dataQueryListener));
    }

    /* renamed from: lambda$queryActivationItemByLimited$2$com-nothing-experience-service-DataQuery */
    public /* synthetic */ void mo13946xbd1eb56(String str, DataQueryListener dataQueryListener) {
        LocalData readDataListFromCursor = readDataListFromCursor(this.mDb.query(DataHelper.TABLE_ACTIVATION, QUERY_ITEMS, (String) null, (String[]) null, (String) null, (String) null, "event_id DESC", str), 1, dataQueryListener);
        if (dataQueryListener != null) {
            dataQueryListener.onSuccess(readDataListFromCursor);
        }
    }

    public void queryQualityLastItem(DataQueryListener dataQueryListener) {
        queryQualityItemByLimited(dataQueryListener, "1");
    }

    public void queryQualityItemByLimited(DataQueryListener dataQueryListener, String str) {
        this.mThreadManager.execute(new DataQuery$$ExternalSyntheticLambda3(this, str, dataQueryListener));
    }

    /* renamed from: lambda$queryQualityItemByLimited$3$com-nothing-experience-service-DataQuery */
    public /* synthetic */ void mo13948x19bdb728(String str, DataQueryListener dataQueryListener) {
        LocalData readDataListFromCursor = readDataListFromCursor(this.mDb.query(DataHelper.TABLE_QUALITY, QUERY_ITEMS, (String) null, (String[]) null, (String) null, (String) null, "event_id DESC", str), 2, dataQueryListener);
        if (dataQueryListener != null) {
            dataQueryListener.onSuccess(readDataListFromCursor);
        }
    }

    public void queryAllQualityItem(DataQueryListener dataQueryListener) {
        queryQualityItemByLimited(dataQueryListener, (String) null);
    }

    public void queryAllProductItem(DataQueryListener dataQueryListener) {
        queryProductItemByLimited(dataQueryListener, (String) null);
    }

    public void queryAllActivationItem(DataQueryListener dataQueryListener) {
        queryActivationItemByLimited(dataQueryListener, (String) null);
    }

    private void queryFail(DataQueryListener dataQueryListener) {
        if (dataQueryListener != null) {
            dataQueryListener.onFail("no data found");
        }
    }

    private LocalData readDataListFromCursor(Cursor cursor, int i, DataQueryListener dataQueryListener) {
        Cursor cursor2 = cursor;
        String str = null;
        if (cursor2 == null) {
            queryFail(dataQueryListener);
            return null;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        LocalData localData = new LocalData();
        ArrayList arrayList = new ArrayList();
        boolean z = false;
        boolean z2 = false;
        while (cursor.moveToNext()) {
            int i2 = cursor2.getInt(cursor2.getColumnIndex(DataHelper.EVENT_ID));
            String string = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_NAME));
            String string2 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_APP_NAME));
            String string3 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_PARAMS));
            String string4 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_PARAMS_STRING_VALUE));
            String string5 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_PROJECT_NAME));
            String string6 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_PACKAGE_NAME));
            String string7 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_PACKAGE_VERSION));
            LocalData localData2 = localData;
            String string8 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_BRAND_OS_VERSION));
            ArrayList arrayList2 = arrayList;
            String string9 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_SYSTEM_VERSION));
            String str2 = string3;
            String string10 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_DEVICE_MODEL));
            String str3 = string4;
            String string11 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_DEVICE_ID));
            String str4 = string;
            String string12 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_DEVICE_ID2));
            int i3 = i2;
            String string13 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_LANGUAGE));
            String str5 = string10;
            String string14 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_EVENT_TIME));
            String string15 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_BRAND_NAME));
            String str6 = string13;
            String string16 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_NETWORK_TYPE));
            String str7 = string8;
            String string17 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_SIM_COUNTRY_CODE));
            String str8 = string15;
            String string18 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_DEVICE_RAM_SIZE));
            String string19 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_DEVICE_ROM_SIZE));
            String string20 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_DEVICE_COLOR));
            String string21 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_OPERATOR_1));
            String str9 = string9;
            String string22 = cursor2.getString(cursor2.getColumnIndex(DataHelper.EVENT_OPERATOR_2));
            String oSCategory = SoftwareInfoUtil.getOSCategory();
            if (isWrongId(string11) || isWrongId(string12)) {
                arrayList = arrayList2;
                cursor2 = cursor;
                localData = localData2;
            } else {
                if (!z) {
                    LocalData localData3 = new LocalData();
                    localData3.project_name = string5;
                    localData3.network_type = string16;
                    localData3.sim_country_code = string17;
                    localData3.operator_1 = string21;
                    localData3.operator_2 = string22;
                    localData = localData3;
                    z = true;
                } else {
                    localData = localData2;
                }
                AppInfo appInfo = new AppInfo();
                appInfo.app_name = string2;
                appInfo.package_name = string6;
                appInfo.version = string7;
                if (str == null) {
                    str = string11;
                }
                if (!z2) {
                    deviceInfo.device_id = string11;
                    deviceInfo.device_id_2 = string12;
                    deviceInfo.os_version = str9;
                    deviceInfo.brand_name = str8;
                    deviceInfo.operating_system_version = str7;
                    deviceInfo.language = str6;
                    deviceInfo.model_name = str5;
                    deviceInfo.ram_size = string18;
                    deviceInfo.rom_size = string19;
                    deviceInfo.device_color = string20;
                    deviceInfo.category = oSCategory;
                    deviceInfo.is_locked = String.valueOf(SoftwareInfoUtil.isLocked());
                    if (App.VERSION_CODE >= 500) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(SoftwareInfoUtil.getReadableBuildInfo());
                        if (!SoftwareInfoUtil.isLocked()) {
                            sb.append("-unlocked");
                        }
                        deviceInfo.sw_build_info = sb.toString();
                    }
                    localData.device = deviceInfo;
                    z2 = true;
                }
                Event event = new Event();
                event.app_info = appInfo;
                event.event_id = String.valueOf(i3);
                event.event_timestamp = string14;
                event.event_name = str4;
                event.event_params = generateEventParams(str2, str3);
                arrayList = arrayList2;
                arrayList.add(event);
                cursor2 = cursor;
            }
        }
        localData.events = arrayList;
        localData.platform = "Android";
        localData.user_pseudo_id = str;
        localData.dataType = i;
        cursor.close();
        return localData;
    }

    private boolean isWrongId(String str) {
        return DeviceIdFactory.WRONG_UUID.equalsIgnoreCase(str);
    }

    private List<EventParam> generateEventParams(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            ArrayList arrayList = new ArrayList();
            String[] split = str.split("\\*");
            String[] split2 = str2.split("\\*");
            if (split == null || split2 == null || split.length != split2.length) {
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
        return null;
    }
}
