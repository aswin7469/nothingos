package com.nothing.experience.datas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_BACKUP_SUFFIX = "-backup";
    private static final String DATABASE_NAME = "experience.db";
    private static final int DATABASE_VERSION = 21;
    public static final String EVENT_APP_NAME = "app_name";
    public static final String EVENT_BRAND_NAME = "brand_name";
    public static final String EVENT_BRAND_OS_VERSION = "brand_os_version";
    public static final String EVENT_DEVICE_CATEGORY = "device_category";
    public static final String EVENT_DEVICE_COLOR = "device_color";
    public static final String EVENT_DEVICE_ID = "device_id";
    public static final String EVENT_DEVICE_ID2 = "device_id2";
    public static final String EVENT_DEVICE_MODEL = "device_mode";
    public static final String EVENT_DEVICE_RAM_SIZE = "ram_size";
    public static final String EVENT_DEVICE_ROM_SIZE = "rom_size";
    public static final String EVENT_EVENT_TIME = "event_time";
    public static final String EVENT_ID = "event_id";
    public static final String EVENT_LANGUAGE = "language";
    public static final String EVENT_NAME = "event_name";
    public static final String EVENT_NETWORK_TYPE = "network_type";
    public static final String EVENT_OPERATOR_1 = "operator1";
    public static final String EVENT_OPERATOR_2 = "operator2";
    public static final String EVENT_PACKAGE_NAME = "package_name";
    public static final String EVENT_PACKAGE_VERSION = "app_version";
    public static final String EVENT_PARAMS = "event_params_key";
    public static final String EVENT_PARAMS_STRING_VALUE = "event_params_value_string_value";
    public static final String EVENT_PROJECT_NAME = "project_name";
    public static final String EVENT_SIM_COUNTRY_CODE = "sim_country_code";
    public static final String EVENT_SYSTEM_VERSION = "system_version";
    private static final String PROPERTY_SQL = " (event_id INTEGER PRIMARY KEY AUTOINCREMENT,event_name TEXT NOT NULL,event_params_key TEXT NOT NULL,event_params_value_string_value TEXT, app_name TEXT, project_name TEXT, package_name TEXT, app_version TEXT, brand_os_version TEXT, system_version TEXT, device_mode TEXT, device_id TEXT, device_id2 TEXT, device_category TEXT, ram_size TEXT, rom_size TEXT, device_color TEXT, operator1 TEXT, operator2 TEXT, language TEXT, event_time TEXT, network_type TEXT, sim_country_code TEXT, brand_name TEXT );";
    public static final String TABLE_ACTIVATION = "activation";
    public static final String TABLE_PRODUCT = "events";
    public static final String TABLE_QUALITY = "quality";
    private static DataHelper sInstance;
    private Context mContext;

    private DataHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 21);
        this.mContext = context;
    }

    public static DataHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataHelper(context);
        }
        return sInstance;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        createProductTable(sQLiteDatabase);
        createActivationTable(sQLiteDatabase);
        createQualityTable(sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("drop table if exists events");
        createProductTable(sQLiteDatabase);
        sQLiteDatabase.execSQL("drop table if exists activation");
        createActivationTable(sQLiteDatabase);
        sQLiteDatabase.execSQL("drop table if exists quality");
        createQualityTable(sQLiteDatabase);
    }

    private void createQualityTable(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE quality (event_id INTEGER PRIMARY KEY AUTOINCREMENT,event_name TEXT NOT NULL,event_params_key TEXT NOT NULL,event_params_value_string_value TEXT, app_name TEXT, project_name TEXT, package_name TEXT, app_version TEXT, brand_os_version TEXT, system_version TEXT, device_mode TEXT, device_id TEXT, device_id2 TEXT, device_category TEXT, ram_size TEXT, rom_size TEXT, device_color TEXT, operator1 TEXT, operator2 TEXT, language TEXT, event_time TEXT, network_type TEXT, sim_country_code TEXT, brand_name TEXT );");
    }

    private void createActivationTable(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE activation (event_id INTEGER PRIMARY KEY AUTOINCREMENT,event_name TEXT NOT NULL,event_params_key TEXT NOT NULL,event_params_value_string_value TEXT, app_name TEXT, project_name TEXT, package_name TEXT, app_version TEXT, brand_os_version TEXT, system_version TEXT, device_mode TEXT, device_id TEXT, device_id2 TEXT, device_category TEXT, ram_size TEXT, rom_size TEXT, device_color TEXT, operator1 TEXT, operator2 TEXT, language TEXT, event_time TEXT, network_type TEXT, sim_country_code TEXT, brand_name TEXT );");
    }

    private void createProductTable(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE events (event_id INTEGER PRIMARY KEY AUTOINCREMENT,event_name TEXT NOT NULL,event_params_key TEXT NOT NULL,event_params_value_string_value TEXT, app_name TEXT, project_name TEXT, package_name TEXT, app_version TEXT, brand_os_version TEXT, system_version TEXT, device_mode TEXT, device_id TEXT, device_id2 TEXT, device_category TEXT, ram_size TEXT, rom_size TEXT, device_color TEXT, operator1 TEXT, operator2 TEXT, language TEXT, event_time TEXT, network_type TEXT, sim_country_code TEXT, brand_name TEXT );");
    }

    private boolean isInMemory() {
        return getDatabaseName() == null;
    }

    public void dropDatabase() {
        close();
        if (!isInMemory()) {
            File databasePath = this.mContext.getDatabasePath(getDatabaseName());
            if (databasePath.exists()) {
                SQLiteDatabase.deleteDatabase(databasePath);
            }
        }
    }

    public void backupDatabase() {
        close();
        if (!isInMemory()) {
            File databasePath = this.mContext.getDatabasePath(getDatabaseName());
            if (databasePath.exists()) {
                Context context = this.mContext;
                File databasePath2 = context.getDatabasePath(getDatabaseName() + DATABASE_BACKUP_SUFFIX);
                if (!databasePath2.exists()) {
                    databasePath.renameTo(databasePath2);
                }
            }
        }
    }
}
