package com.nothing.settings.glyphs.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.ExternalRingtonesCursorWrapper;
import android.net.Uri;
import android.provider.MediaStore;
import com.android.internal.database.SortCursor;
import java.util.ArrayList;
import java.util.List;

public class RingtoneHelper {
    private static final String[] INTERNAL_COLUMNS = {"_id", "title", "title_key", "_display_name"};
    private Context mContext;
    private Cursor mCursor;
    private final List<String> mFilterColumns = new ArrayList();

    public RingtoneHelper(Context context) {
        this.mContext = context;
        setType(1);
    }

    public void setType(int i) {
        if (this.mCursor == null) {
            setFilterColumnsList(i);
            return;
        }
        throw new IllegalStateException("Setting filter columns should be done before querying for ringtones.");
    }

    private void setFilterColumnsList(int i) {
        List<String> list = this.mFilterColumns;
        list.clear();
        if ((i & 1) != 0) {
            list.add("is_ringtone");
        }
        if ((i & 2) != 0) {
            list.add("is_notification");
        }
        if ((i & 4) != 0) {
            list.add("is_alarm");
        }
    }

    public Cursor getCursor() {
        Cursor cursor = this.mCursor;
        if (cursor != null && cursor.requery()) {
            return this.mCursor;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(getInternalRingtones());
        SortCursor sortCursor = new SortCursor((Cursor[]) arrayList.toArray(new Cursor[arrayList.size()]), "title_key");
        this.mCursor = sortCursor;
        return sortCursor;
    }

    public Cursor getMediaRingtones() {
        return new ExternalRingtonesCursorWrapper(getMediaRingtones(this.mContext), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
    }

    private Cursor getMediaRingtones(Context context) {
        return query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, INTERNAL_COLUMNS, constructBooleanTrueWhereClause(this.mFilterColumns), (String[]) null, "title_key", context);
    }

    private Cursor getInternalRingtones() {
        return new ExternalRingtonesCursorWrapper(query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, INTERNAL_COLUMNS, constructBooleanTrueWhereClause(this.mFilterColumns), (String[]) null, "_display_name"), MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
    }

    private static String constructBooleanTrueWhereClause(List<String> list) {
        if (list == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int size = list.size() - 1; size >= 0; size--) {
            sb.append(list.get(size));
            sb.append("=1 or ");
        }
        if (list.size() > 0) {
            sb.setLength(sb.length() - 4);
        }
        sb.append(")");
        return sb.toString();
    }

    private Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return query(uri, strArr, str, strArr2, str2, this.mContext);
    }

    private Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, Context context) {
        return context.getContentResolver().query(uri, strArr, str, strArr2, str2);
    }
}
