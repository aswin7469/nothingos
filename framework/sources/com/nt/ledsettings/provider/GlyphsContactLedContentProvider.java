package com.nt.ledsettings.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
/* loaded from: classes4.dex */
public class GlyphsContactLedContentProvider extends ContentProvider {
    private static final String GLYPHS_CONTACT_LED_AUTHORITY = "com.nt.ledsettings.glyphs.GlyphsContactLedContentProvider";
    public static final int LED_URI_CODE = 0;
    private static final String TAG = "GlyphsContactLedContent";
    private static final UriMatcher uriMatcher;
    private GlyphsLedDatabaseHelper mDBHelper;

    static {
        UriMatcher uriMatcher2 = new UriMatcher(-1);
        uriMatcher = uriMatcher2;
        uriMatcher2.addURI(GLYPHS_CONTACT_LED_AUTHORITY, GlyphsLedDatabaseHelper.LED_TABLE, 0);
    }

    private String getTableName(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 0:
                return GlyphsLedDatabaseHelper.LED_TABLE;
            default:
                return null;
        }
    }

    @Override // android.content.ContentProvider, android.content.ContentInterface
    public Bundle call(String authority, String method, String arg, Bundle extras) {
        int uid = Binder.getCallingUid();
        Log.d(TAG, "calling pid/uid" + Binder.getCallingPid() + "/" + uid);
        return new Bundle();
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        this.mDBHelper = GlyphsLedDatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(tableName);
        SQLiteDatabase database = this.mDBHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override // android.content.ContentProvider, android.content.ContentInterface
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        SQLiteDatabase database = this.mDBHelper.getReadableDatabase();
        database.insert(tableName, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        SQLiteDatabase database = this.mDBHelper.getReadableDatabase();
        int count = database.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int row = this.mDBHelper.getReadableDatabase().update(tableName, values, selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }
}
