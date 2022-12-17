package com.nothing.settings.glyphs.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class MusicUtils {
    public static int deleteRingtone(Context context, Uri uri) {
        return context.getContentResolver().delete(uri, (Bundle) null);
    }

    public static String getRingtonesPath(Context context, Uri uri) {
        Cursor query = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        String str = "";
        if (query != null) {
            if (query.moveToNext()) {
                str = query.getString(query.getColumnIndexOrThrow("_data"));
            }
            query.close();
        }
        return str;
    }

    public static String getRingtonesId(Context context, Uri uri) {
        String str = "";
        if (uri == null) {
            return str;
        }
        Cursor query = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        if (query != null) {
            if (query.moveToNext()) {
                str = query.getString(query.getColumnIndexOrThrow("_id"));
            }
            query.close();
        }
        return str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x003d A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getExternalRingtoneCount(android.content.Context r9, int r10) {
        /*
            java.lang.String r0 = "1"
            java.lang.String[] r1 = new java.lang.String[]{r0}
            r2 = 0
            r3 = 2
            if (r10 != r3) goto L_0x0013
            java.lang.String[] r1 = new java.lang.String[]{r0}
            java.lang.String r2 = "is_notification=?"
        L_0x0010:
            r7 = r1
            r6 = r2
            goto L_0x0026
        L_0x0013:
            r3 = 4
            if (r10 != r3) goto L_0x001d
            java.lang.String[] r1 = new java.lang.String[]{r0}
            java.lang.String r2 = "is_alarm=?"
            goto L_0x0010
        L_0x001d:
            r0 = 7
            if (r10 != r0) goto L_0x0023
            r6 = r2
            r7 = r6
            goto L_0x0026
        L_0x0023:
            java.lang.String r2 = "is_ringtone=?"
            goto L_0x0010
        L_0x0026:
            android.content.ContentResolver r3 = r9.getContentResolver()
            android.net.Uri r4 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            r5 = 0
            java.lang.String r8 = "is_ringtone"
            android.database.Cursor r9 = r3.query(r4, r5, r6, r7, r8)
            if (r9 == 0) goto L_0x003d
            int r10 = r9.getCount()
            r9.close()
            return r10
        L_0x003d:
            r9 = 0
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.glyphs.utils.MusicUtils.getExternalRingtoneCount(android.content.Context, int):int");
    }
}
