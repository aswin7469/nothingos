package com.nothing.experience.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.nothing.experience.App;
import com.nothing.experience.datas.LocalData;
import com.nothing.experience.service.DataQuery;
import com.nothing.experience.service.DataQueryListener;

public class DeviceIdFactory {
    private static final String PREFS_FILE = "device_id.xml";
    private static final String PREFS_UUID = "uuid";
    public static final String WRONG_UUID = "0de8ddf24fd4424e2a0d29a21de4880e";
    public static String uuid;

    public static void loadDeviceid(final Context context, DataQuery dataQuery) {
        dataQuery.queryActivationLastItem(new DataQueryListener<LocalData>() {
            public void onSuccess(LocalData localData) {
                if (localData != null) {
                    DeviceIdFactory.updateDeviceIdWhenActive(context, localData.user_pseudo_id);
                }
                App.startCompleted = true;
            }

            public void onFail(String str) {
                App.startCompleted = true;
            }
        });
    }

    public static void updateDeviceIdWhenActive(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, 0);
        if (TextUtils.isEmpty(str)) {
            str = SoftwareInfoUtil.generateUniqueDeviceId(context);
        }
        if (SoftwareInfoUtil.shouldEncryptId()) {
            str = EncryptUtils.getEncryptForString(str);
        }
        uuid = str;
        sharedPreferences.edit().putString(PREFS_UUID, uuid).apply();
    }

    public String getDeviceUuid() {
        return uuid;
    }
}
