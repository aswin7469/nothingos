package com.nothing.experience.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import okhttp3.HttpUrl;

public class SharedPreferencesUtils {
    public static final String KEY_ACTIVE_DONE = "active_done";
    public static final String KEY_ACTIVE_REPORTED = "active_reported";
    public static final String KEY_ID1 = "ID1";
    public static final String KEY_ID2 = "ID2";
    public static final String KEY_REPORT_TYPE = "report_type";
    public static final String KEY_SCHEDULE_TIME = "last_schedule_time";
    private static final String name = "experience_pref";
    private static SharedPreferencesUtils sInstance;
    private SharedPreferences.Editor editor = null;
    private Object object;
    private SharedPreferences preferences;

    public static SharedPreferencesUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (sInstance == null) {
                    sInstance = new SharedPreferencesUtils(context);
                }
            }
        }
        return sInstance;
    }

    private SharedPreferencesUtils(Context context) {
        this.preferences = context.getApplicationContext().getSharedPreferences(name, 0);
    }

    public synchronized void set(String str, Object obj) {
        if (this.editor == null) {
            this.editor = this.preferences.edit();
        }
        if (obj != null) {
            String simpleName = obj.getClass().getSimpleName();
            if ("String".equals(simpleName)) {
                this.editor.putString(str, (String) obj);
            } else if ("Integer".equals(simpleName)) {
                this.editor.putInt(str, ((Integer) obj).intValue());
            } else if ("Boolean".equals(simpleName)) {
                this.editor.putBoolean(str, ((Boolean) obj).booleanValue());
            } else if ("Float".equals(simpleName)) {
                this.editor.putFloat(str, ((Float) obj).floatValue());
            } else if ("Long".equals(simpleName)) {
                this.editor.putLong(str, ((Long) obj).longValue());
            } else if (obj instanceof Serializable) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    new ObjectOutputStream(byteArrayOutputStream).writeObject(obj);
                    this.editor.putString(str, Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
                    Log.d(getClass().getSimpleName(), "save object success");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(getClass().getSimpleName(), "save object error");
                }
            } else {
                throw new IllegalArgumentException(obj.getClass().getName() + " must implement interface Serializable");
            }
            this.editor.commit();
        }
    }

    public synchronized void remove(String str) {
        if (this.editor == null) {
            this.editor = this.preferences.edit();
        }
        this.editor.remove(str);
        this.editor.commit();
    }

    public Object get(String str, Object obj) {
        if (obj == null) {
            return getObject(str);
        }
        String simpleName = obj.getClass().getSimpleName();
        if ("String".equals(simpleName)) {
            return this.preferences.getString(str, (String) obj);
        }
        if ("Integer".equals(simpleName)) {
            return Integer.valueOf(this.preferences.getInt(str, ((Integer) obj).intValue()));
        }
        if ("Boolean".equals(simpleName)) {
            return Boolean.valueOf(this.preferences.getBoolean(str, ((Boolean) obj).booleanValue()));
        }
        if ("Float".equals(simpleName)) {
            return Float.valueOf(this.preferences.getFloat(str, ((Float) obj).floatValue()));
        }
        if ("Long".equals(simpleName)) {
            return Long.valueOf(this.preferences.getLong(str, ((Long) obj).longValue()));
        }
        return getObject(str);
    }

    public Object getObject(String str) {
        try {
            this.object = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(this.preferences.getString(str, HttpUrl.FRAGMENT_ENCODE_SET).getBytes(), 0))).readObject();
            Log.d(getClass().getSimpleName(), "Get object success");
            return this.object;
        } catch (Exception unused) {
            Log.e(getClass().getSimpleName(), "Get object is error");
            return null;
        }
    }
}
