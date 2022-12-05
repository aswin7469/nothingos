package com.nt.ledsettings.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/* loaded from: classes4.dex */
public class GlyphsLedDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_CARD_TABLE = "CREATE TABLE glyphs_led(led_name TEXT,led_id TEXT,led_uri TEXT,ringtone_uri TEXT,ringtone_type INTEGER,update_time LONG,contact_id TEXT);";
    private static final String DATABASE_NAME = "glyphs.db";
    private static final int DATABASE_VERSION = 3;
    public static final String LED_TABLE = "glyphs_led";
    private static final String TAG = "GlyphsLedDatabaseHelper";
    static GlyphsLedDatabaseHelper sCardDatabaseHelper;

    /* loaded from: classes4.dex */
    public interface LedColumns {
        public static final String CONTACT_ID = "contact_id";
        public static final String LED_ID = "led_id";
        public static final String LED_NAME = "led_name";
        public static final String LED_URI = "led_uri";
        public static final String RINGTONE_TYPE = "ringtone_type";
        public static final String RINGTONE_URI = "ringtone_uri";
        public static final String UPDATE_TIME = "update_time";
    }

    public GlyphsLedDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 3);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CARD_TABLE);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            Log.d(TAG, "Reconstructing DB from " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS glyphs_led");
            onCreate(db);
        }
    }

    public static synchronized GlyphsLedDatabaseHelper getInstance(Context context) {
        GlyphsLedDatabaseHelper glyphsLedDatabaseHelper;
        synchronized (GlyphsLedDatabaseHelper.class) {
            if (sCardDatabaseHelper == null) {
                sCardDatabaseHelper = new GlyphsLedDatabaseHelper(context.getApplicationContext());
            }
            glyphsLedDatabaseHelper = sCardDatabaseHelper;
        }
        return glyphsLedDatabaseHelper;
    }
}
