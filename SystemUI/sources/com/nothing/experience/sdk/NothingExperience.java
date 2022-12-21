package com.nothing.experience.sdk;

import android.content.AttributionSource;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IContentProvider;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import java.util.Set;

public class NothingExperience {
    private static final String AUTHORITY = "com.nothing.experience";
    public static final Uri CONTENT_ACTIVATION_URI;
    private static final Uri CONTENT_PRODUCT_URI;
    public static final Uri CONTENT_QUALITY_URI;
    static final String EVENT_APP_NAME = "app_name";
    private static final String EVENT_DEVICE_ID = "device_id";
    private static final String EVENT_DEVICE_ID2 = "device_id2";
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
                string = context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return string;
    }

    public boolean logEvent(String str, Bundle bundle) {
        return logEventInner(str, bundle, 0);
    }

    public boolean logActivationEvent(String str, Bundle bundle) {
        return logEventInner(str, bundle, 1);
    }

    public boolean logQualityEvent(String str, Bundle bundle) {
        return logEventInner(str, bundle, 2);
    }

    private boolean logEventInner(String str, Bundle bundle, int i) {
        if (!TextUtils.isEmpty(str) && bundle != null && !bundle.isEmpty()) {
            Set<String> keySet = bundle.keySet();
            Bundle bundle2 = new Bundle();
            bundle2.putString(EVENT_NAME, str);
            bundle2.putString("package_name", this.mPackageName);
            bundle2.putString(EVENT_PACKAGE_VERSION, this.mPackageVersion);
            bundle2.putString(EVENT_APP_NAME, this.mAppName);
            bundle2.putString(EVENT_EVENT_TIME, String.valueOf(System.currentTimeMillis()));
            if (i == 1) {
                String string = bundle.getString(EVENT_DEVICE_ID);
                String string2 = bundle.getString(EVENT_DEVICE_ID2);
                if (!TextUtils.isEmpty(string)) {
                    bundle2.putString(EVENT_DEVICE_ID, string);
                }
                if (!TextUtils.isEmpty(string2)) {
                    bundle2.putString(EVENT_DEVICE_ID2, string2);
                }
            }
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (String str2 : keySet) {
                if (!str2.contains("*")) {
                    Object obj = bundle.get(str2);
                    if (obj != null && (obj instanceof String) && ((String) obj).contains("*")) {
                        throw new IllegalArgumentException("Your event value contains illegal character *");
                    } else if (!EVENT_DEVICE_ID.equals(str2) && !EVENT_DEVICE_ID2.equals(str2)) {
                        sb.append(str2);
                        sb.append("*");
                        sb2.append(obj);
                        sb2.append("*");
                    }
                } else {
                    throw new IllegalArgumentException("Your event key contains illegal character *");
                }
            }
            bundle2.putString(EVENT_PARAMS, sb.toString());
            bundle2.putString(EVENT_PARAMS_STRING_VALUE, sb2.toString());
            if (this.mEventRecorder == null) {
                this.mEventRecorder = this.mContext.getContentResolver();
            }
            try {
                Uri uri = sContentUriArray.get(i);
                IContentProvider acquireUnstableProvider = this.mEventRecorder.acquireUnstableProvider(uri.getAuthority());
                this.mContentProvider = acquireUnstableProvider;
                AttributionSource attributionSource = this.mEventRecorder.getAttributionSource();
                String authority = uri.getAuthority();
                acquireUnstableProvider.call(attributionSource, authority, "" + i, (String) null, bundle2);
            } catch (Exception unused) {
            }
        }
        return false;
    }
}
