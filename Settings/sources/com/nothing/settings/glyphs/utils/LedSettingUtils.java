package com.nothing.settings.glyphs.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.nothing.settings.glyphs.utils.ContactsManager;

public class LedSettingUtils {
    private static final Uri CONTACT_LED_URI = Uri.parse("content://com.nt.ledsettings.glyphs.GlyphsContactLedContentProvider/glyphs_led");

    public static void setContactLed(Context context, String str, String str2, String str3, String str4, String str5, int i) {
        if (isInstalled(context)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("led_name", str);
            contentValues.put("led_id", str2);
            contentValues.put("led_uri", str3);
            contentValues.put("contact_id", str5);
            contentValues.put("ringtone_uri", str4);
            contentValues.put("ringtone_type", Integer.valueOf(i));
            contentValues.put("update_time", Long.valueOf(System.currentTimeMillis()));
            if (queryCustomContactLedById(context, str5) != null) {
                context.getContentResolver().update(CONTACT_LED_URI, contentValues, "contact_id=?", new String[]{str5});
                return;
            }
            context.getContentResolver().insert(CONTACT_LED_URI, contentValues);
        }
    }

    @SuppressLint({"Range"})
    public static ContactsManager.Contact queryCustomContactLedById(Context context, String str) {
        Cursor query;
        if (!isInstalled(context) || TextUtils.isEmpty(str) || (query = context.getContentResolver().query(CONTACT_LED_URI, (String[]) null, "contact_id=?", new String[]{str}, "update_time DESC")) == null || query.getCount() <= 0 || !query.moveToNext()) {
            return null;
        }
        ContactsManager.Contact contact = new ContactsManager.Contact(query.getString(query.getColumnIndex("led_name")), query.getString(query.getColumnIndex("led_id")), query.getString(query.getColumnIndex("led_uri")), query.getString(query.getColumnIndex("ringtone_uri")), query.getString(query.getColumnIndex("contact_id")), query.getInt(query.getColumnIndex("ringtone_type")), query.getLong(query.getColumnIndex("update_time")));
        query.close();
        return contact;
    }

    private static boolean isInstalled(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.nt.ledsettings", 16384);
        } catch (Exception e) {
            Log.w("LedSettingUtils", "isAppExist exception:" + e.getMessage());
            packageInfo = null;
        }
        return packageInfo != null;
    }
}
