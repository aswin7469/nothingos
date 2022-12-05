package com.nothing.experience.sdk;

import android.content.AttributionSource;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IContentProvider;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import java.util.Set;
/* loaded from: classes4.dex */
public class NothingExperience {
    private static final String AUTHORITY = "com.nothing.experience";
    public static final Uri CONTENT_ACTIVATION_URI;
    private static final Uri CONTENT_PRODUCT_URI;
    public static final Uri CONTENT_QUALITY_URI;
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
    public static NothingExperience instance;
    private static SparseArray<Uri> sContentUriArray;
    private String mAppName;
    private IContentProvider mContentProvider;
    private Context mContext;
    private ContentResolver mEventRecorder;
    private String mPackageName;
    private String mPackageVersion;

    static {
        Uri parse = Uri.parse("content://com.nothing.experience/events");
        CONTENT_PRODUCT_URI = parse;
        Uri parse2 = Uri.parse("content://com.nothing.experience/quality");
        CONTENT_QUALITY_URI = parse2;
        Uri parse3 = Uri.parse("content://com.nothing.experience/activation");
        CONTENT_ACTIVATION_URI = parse3;
        SparseArray<Uri> sparseArray = new SparseArray<>();
        sContentUriArray = sparseArray;
        sparseArray.put(0, parse);
        sContentUriArray.put(1, parse3);
        sContentUriArray.put(2, parse2);
    }

    private NothingExperience(Context context) {
        this.mContext = context;
        this.mEventRecorder = context.getContentResolver();
        this.mPackageName = context.getPackageName();
        String appName = getAppName(context);
        this.mAppName = appName;
        if (TextUtils.isEmpty(appName)) {
            this.mAppName = "Android";
        }
        try {
            this.mPackageVersion = context.getPackageManager().getPackageInfo(this.mPackageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            this.mPackageVersion = "0.0.0";
            e.printStackTrace();
        }
    }

    public static NothingExperience getInstance(Context context) {
        if (instance == null) {
            synchronized (NothingExperience.class) {
                if (instance == null) {
                    instance = new NothingExperience(context);
                }
            }
        }
        return instance;
    }

    public static synchronized String getAppName(Context context) {
        String string;
        synchronized (NothingExperience.class) {
            try {
                PackageManager packageManager = context.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                int labelRes = packageInfo.applicationInfo.labelRes;
                string = context.getResources().getString(labelRes);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return string;
    }

    public boolean logEvent(String eventName, Bundle eventParams) {
        return logEventInner(eventName, eventParams, 0);
    }

    public boolean logActivationEvent(String eventName, Bundle eventParams) {
        return logEventInner(eventName, eventParams, 1);
    }

    public boolean logQualityEvent(String eventName, Bundle eventParams) {
        return logEventInner(eventName, eventParams, 2);
    }

    private boolean logEventInner(String eventName, Bundle eventParam, int eventType) {
        if (!TextUtils.isEmpty(eventName) && eventParam != null && !eventParam.isEmpty()) {
            Set<String> paramsKeys = eventParam.keySet();
            Bundle realParams = new Bundle();
            realParams.putString(EVENT_NAME, eventName);
            realParams.putString("package_name", this.mPackageName);
            realParams.putString(EVENT_PACKAGE_VERSION, this.mPackageVersion);
            realParams.putString(EVENT_APP_NAME, this.mAppName);
            realParams.putString(EVENT_EVENT_TIME, String.valueOf(System.currentTimeMillis()));
            StringBuilder keyString = new StringBuilder();
            StringBuilder valueString = new StringBuilder();
            for (String key : paramsKeys) {
                if (key.contains("*")) {
                    throw new IllegalArgumentException("Your event key contains illegal character *");
                }
                Object val = eventParam.get(key);
                if (val != null && (val instanceof String) && ((String) val).contains("*")) {
                    throw new IllegalArgumentException("Your event value contains illegal character *");
                }
                keyString.append(key);
                keyString.append("*");
                valueString.append(val);
                valueString.append("*");
            }
            realParams.putString(EVENT_PARAMS, keyString.toString());
            realParams.putString(EVENT_PARAMS_STRING_VALUE, valueString.toString());
            if (this.mEventRecorder == null) {
                this.mEventRecorder = this.mContext.getContentResolver();
            }
            try {
                Uri contentUri = sContentUriArray.get(eventType);
                IContentProvider acquireProvider = this.mEventRecorder.acquireProvider(contentUri.getAuthority());
                this.mContentProvider = acquireProvider;
                AttributionSource attributionSource = this.mEventRecorder.getAttributionSource();
                String authority = contentUri.getAuthority();
                acquireProvider.call(attributionSource, authority, "" + eventType, null, realParams);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return 0 != 0;
        }
        return false;
    }
}
