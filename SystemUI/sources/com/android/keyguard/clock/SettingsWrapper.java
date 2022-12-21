package com.android.keyguard.clock;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsWrapper {
    private static final String CLOCK_FIELD = "clock";
    private static final String CUSTOM_CLOCK_FACE = "lock_screen_custom_clock_face";
    private static final String DOCKED_CLOCK_FACE = "docked_clock_face";
    private static final String TAG = "ClockFaceSettings";
    private final ContentResolver mContentResolver;
    private final Migration mMigration;

    interface Migration {
        void migrate(String str, int i);
    }

    SettingsWrapper(ContentResolver contentResolver) {
        this(contentResolver, new Migrator(contentResolver));
    }

    SettingsWrapper(ContentResolver contentResolver, Migration migration) {
        this.mContentResolver = contentResolver;
        this.mMigration = migration;
    }

    /* access modifiers changed from: package-private */
    public String getLockScreenCustomClockFace(int i) {
        return decode(Settings.Secure.getStringForUser(this.mContentResolver, CUSTOM_CLOCK_FACE, i), i);
    }

    /* access modifiers changed from: package-private */
    public String getDockedClockFace(int i) {
        return Settings.Secure.getStringForUser(this.mContentResolver, DOCKED_CLOCK_FACE, i);
    }

    /* access modifiers changed from: package-private */
    public String decode(String str, int i) {
        if (str == null) {
            return str;
        }
        try {
            try {
                return new JSONObject(str).getString("clock");
            } catch (JSONException e) {
                Log.e(TAG, "JSON object does not contain clock field.", e);
                return null;
            }
        } catch (JSONException e2) {
            Log.e(TAG, "Settings value is not valid JSON", e2);
            this.mMigration.migrate(str, i);
            return str;
        }
    }

    private static final class Migrator implements Migration {
        private final ContentResolver mContentResolver;

        Migrator(ContentResolver contentResolver) {
            this.mContentResolver = contentResolver;
        }

        public void migrate(String str, int i) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("clock", (Object) str);
                Settings.Secure.putStringForUser(this.mContentResolver, SettingsWrapper.CUSTOM_CLOCK_FACE, jSONObject.toString(), i);
            } catch (JSONException e) {
                Log.e(SettingsWrapper.TAG, "Failed migrating settings value to JSON format", e);
            }
        }
    }
}
