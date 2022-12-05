package com.nt.settings.glyphs.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.media.Utils;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import com.nt.settings.glyphs.utils.ContactsManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
/* loaded from: classes2.dex */
public class LedSettingUtils {
    private static final Uri CONTACT_LED_URI = Uri.parse("content://com.nt.ledsettings.glyphs.GlyphsContactLedContentProvider/glyphs_led");

    public static void setContactLed(Context context, String str, String str2, String str3, String str4, String str5, int i) {
        if (!isInstalled(context, "com.nt.ledsettings")) {
            return;
        }
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
        } else {
            context.getContentResolver().insert(CONTACT_LED_URI, contentValues);
        }
    }

    @SuppressLint({"Range"})
    public static ContactsManager.Contact queryCustomContactLedById(Context context, String str) {
        Cursor query;
        if (isInstalled(context, "com.nt.ledsettings") && !TextUtils.isEmpty(str) && (query = context.getContentResolver().query(CONTACT_LED_URI, null, "contact_id=?", new String[]{str}, "update_time DESC")) != null && query.getCount() > 0 && query.moveToNext()) {
            ContactsManager.Contact contact = new ContactsManager.Contact(query.getString(query.getColumnIndex("led_name")), query.getString(query.getColumnIndex("led_id")), query.getString(query.getColumnIndex("led_uri")), query.getString(query.getColumnIndex("ringtone_uri")), query.getString(query.getColumnIndex("contact_id")), query.getInt(query.getColumnIndex("ringtone_type")), query.getLong(query.getColumnIndex("update_time")));
            query.close();
            return contact;
        }
        return null;
    }

    private static Uri addCustomRingtone(Context context, Uri uri, String str, int i) {
        try {
            File uniqueExternalFile = Utils.getUniqueExternalFile(context, i == 1 ? Environment.DIRECTORY_RINGTONES : Environment.DIRECTORY_NOTIFICATIONS, FileUtils.buildValidFatFilename(getFileDisplayNameFromUri(context, uri)), str);
            if (uniqueExternalFile.exists()) {
                return MediaStore.scanFile(context.getContentResolver(), uniqueExternalFile);
            }
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = new FileOutputStream(uniqueExternalFile);
            try {
                FileUtils.copy(openInputStream, fileOutputStream);
                fileOutputStream.close();
                if (openInputStream != null) {
                    openInputStream.close();
                }
                return MediaStore.scanFile(context.getContentResolver(), uniqueExternalFile);
            } catch (Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri saveAsRingtone(Context context, String str, String str2, int i) {
        ContentValues contentValues = new ContentValues();
        File file = new File(str);
        Uri contentUriForPath = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        Cursor query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "_data=?", new String[]{file.getAbsolutePath()}, "is_music");
        long j = -1;
        if (query != null) {
            while (query.moveToNext()) {
                j = query.getLong(query.getColumnIndexOrThrow("_id"));
            }
            query.close();
        }
        String mimeType = getMimeType(file);
        contentValues.put("title", str2);
        contentValues.put("artist", "artist");
        contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis() / 1000));
        if (i == 1) {
            contentValues.put("is_ringtone", Boolean.TRUE);
        } else if (i == 1) {
            contentValues.put("is_notification", Boolean.TRUE);
        }
        if (j > 0) {
            context.getContentResolver().update(contentUriForPath, contentValues, null);
        } else {
            contentValues.put("mime_type", mimeType);
            contentValues.put("_data", file.getAbsolutePath());
            context.getContentResolver().insert(contentUriForPath, contentValues);
        }
        return addCustomRingtone(context, Uri.fromFile(file), mimeType, i);
    }

    private static String getMimeType(File file) {
        return URLConnection.getFileNameMap().getContentTypeFor(file.getName());
    }

    static String getFileDisplayNameFromUri(Context context, Uri uri) {
        String scheme = uri.getScheme();
        if ("file".equals(scheme)) {
            return uri.getLastPathSegment();
        }
        if ("content".equals(scheme)) {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_display_name"}, null, null, null);
            if (query != null) {
                try {
                    if (query.getCount() != 0) {
                        query.moveToFirst();
                        String string = query.getString(query.getColumnIndex("_display_name"));
                        query.close();
                        return string;
                    }
                } catch (Throwable th) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
        }
        return uri.toString();
    }

    private static boolean isInstalled(Context context, String str) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(str, 16384);
        } catch (Exception e) {
            Log.w("LedSettingUtils", "isAppExist exception:" + e.getMessage());
            packageInfo = null;
        }
        return packageInfo != null;
    }
}
