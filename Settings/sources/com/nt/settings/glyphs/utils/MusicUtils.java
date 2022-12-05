package com.nt.settings.glyphs.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class MusicUtils {
    public static List<Song> getMusicData(Context context) {
        ArrayList arrayList = new ArrayList();
        Cursor query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "is_music=?", new String[]{"1"}, "is_music");
        if (query != null) {
            while (query.moveToNext()) {
                Song song = new Song();
                song.title = query.getString(query.getColumnIndexOrThrow("title"));
                song.song = query.getString(query.getColumnIndexOrThrow("_display_name"));
                song.singer = query.getString(query.getColumnIndexOrThrow("artist"));
                song.path = query.getString(query.getColumnIndexOrThrow("_data"));
                song.duration = query.getInt(query.getColumnIndexOrThrow("duration"));
                song.size = query.getLong(query.getColumnIndexOrThrow("_size"));
                boolean z = true;
                song.isRingtone = query.getInt(query.getColumnIndexOrThrow("is_ringtone")) > 0;
                if (query.getInt(query.getColumnIndexOrThrow("is_notification")) <= 0) {
                    z = false;
                }
                song.isNotificationRing = z;
                if (song.size > 0) {
                    arrayList.add(song);
                }
            }
            query.close();
        }
        return arrayList;
    }

    public static int deleteRingtone(Context context, Uri uri) {
        return context.getContentResolver().delete(uri, null);
    }

    public static String getRingtonesPath(Context context, Uri uri) {
        Cursor query = context.getContentResolver().query(uri, null, null, null, null);
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
        Cursor query = context.getContentResolver().query(uri, null, null, null, null);
        if (query != null) {
            if (query.moveToNext()) {
                str = query.getString(query.getColumnIndexOrThrow("_id"));
            }
            query.close();
        }
        return str;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x003d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int getExternalRingtoneCount(Context context, int i) {
        String str;
        String str2;
        String[] strArr;
        Cursor query;
        String[] strArr2 = {"1"};
        if (i == 2) {
            strArr2 = new String[]{"1"};
            str = "is_notification=?";
        } else if (i == 4) {
            strArr2 = new String[]{"1"};
            str = "is_alarm=?";
        } else if (i == 7) {
            str2 = null;
            strArr = null;
            query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, str2, strArr, "is_ringtone");
            if (query != null) {
                return 0;
            }
            int count = query.getCount();
            query.close();
            return count;
        } else {
            str = "is_ringtone=?";
        }
        strArr = strArr2;
        str2 = str;
        query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, str2, strArr, "is_ringtone");
        if (query != null) {
        }
    }

    public static String getRingtoneTitle(Context context, Uri uri) {
        Cursor query = context.getContentResolver().query(uri, null, null, null, null);
        if (query == null || !query.moveToNext()) {
            return "";
        }
        String string = query.getString(query.getColumnIndex("title"));
        query.close();
        return string;
    }

    /* loaded from: classes2.dex */
    public static class Song {
        public int duration;
        public boolean isChecked;
        public boolean isNotificationRing;
        public boolean isRingtone;
        public String path;
        public String singer;
        public long size;
        public String song;
        public String title;

        public String toString() {
            return "Song{singer='" + this.singer + "', song='" + this.song + "', path='" + this.path + "', duration=" + this.duration + ", size=" + this.size + '}';
        }
    }
}
