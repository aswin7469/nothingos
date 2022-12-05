package com.android.internal.util;

import android.os.WorkSource;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.StatsEvent;
import android.util.StatsLog;
import java.util.List;
/* loaded from: classes4.dex */
public class FrameworkStatsLog {
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED = 266;
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED__SERVICE_STATUS__DISABLED = 2;
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED__SERVICE_STATUS__ENABLED = 1;
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED__SERVICE_STATUS__UNKNOWN = 0;
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED__SHORTCUT_TYPE__A11Y_BUTTON = 1;
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED__SHORTCUT_TYPE__A11Y_BUTTON_LONG_PRESS = 4;
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED__SHORTCUT_TYPE__A11Y_FLOATING_MENU = 5;
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED__SHORTCUT_TYPE__TRIPLE_TAP = 3;
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED__SHORTCUT_TYPE__UNKNOWN_TYPE = 0;
    public static final int ACCESSIBILITY_SHORTCUT_REPORTED__SHORTCUT_TYPE__VOLUME_KEY = 2;
    public static final int ACTIVITY_FOREGROUND_STATE_CHANGED = 42;
    public static final int ACTIVITY_FOREGROUND_STATE_CHANGED__STATE__BACKGROUND = 0;
    public static final int ACTIVITY_FOREGROUND_STATE_CHANGED__STATE__FOREGROUND = 1;
    public static final int ACTIVITY_MANAGER_SLEEP_STATE_CHANGED = 14;
    public static final int ACTIVITY_MANAGER_SLEEP_STATE_CHANGED__STATE__ASLEEP = 1;
    public static final int ACTIVITY_MANAGER_SLEEP_STATE_CHANGED__STATE__AWAKE = 2;
    public static final int ACTIVITY_MANAGER_SLEEP_STATE_CHANGED__STATE__UNKNOWN = 0;
    public static final int ADB_CONNECTION_CHANGED = 144;
    public static final int ADB_CONNECTION_CHANGED__STATE__AUTOMATICALLY_ALLOWED = 4;
    public static final int ADB_CONNECTION_CHANGED__STATE__AWAITING_USER_APPROVAL = 1;
    public static final int ADB_CONNECTION_CHANGED__STATE__DENIED_INVALID_KEY = 5;
    public static final int ADB_CONNECTION_CHANGED__STATE__DENIED_VOLD_DECRYPT = 6;
    public static final int ADB_CONNECTION_CHANGED__STATE__DISCONNECTED = 7;
    public static final int ADB_CONNECTION_CHANGED__STATE__UNKNOWN = 0;
    public static final int ADB_CONNECTION_CHANGED__STATE__USER_ALLOWED = 2;
    public static final int ADB_CONNECTION_CHANGED__STATE__USER_DENIED = 3;
    public static final int ALARM_BATCH_DELIVERED = 367;
    public static final int ALARM_SCHEDULED = 368;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_BACKUP = 1008;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 1004;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_BOUND_TOP = 1020;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_CACHED_ACTIVITY = 1015;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 1016;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_CACHED_EMPTY = 1018;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_CACHED_RECENT = 1017;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_FOREGROUND_SERVICE = 1003;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_HEAVY_WEIGHT = 1012;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_HOME = 1013;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_IMPORTANT_BACKGROUND = 1006;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_IMPORTANT_FOREGROUND = 1005;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_LAST_ACTIVITY = 1014;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_NONEXISTENT = 1019;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_PERSISTENT = 1000;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_PERSISTENT_UI = 1001;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_RECEIVER = 1010;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_SERVICE = 1009;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_TOP = 1002;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_TOP_SLEEPING = 1011;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_TRANSIENT_BACKGROUND = 1007;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_UNKNOWN = 999;
    public static final int ALARM_SCHEDULED__CALLING_PROCESS_STATE__PROCESS_STATE_UNKNOWN_TO_PROTO = 998;
    public static final int ALARM_SCHEDULED__EXACT_ALARM_ALLOWED_REASON__ALLOW_LIST = 2;
    public static final int ALARM_SCHEDULED__EXACT_ALARM_ALLOWED_REASON__CHANGE_DISABLED = 3;
    public static final int ALARM_SCHEDULED__EXACT_ALARM_ALLOWED_REASON__NOT_APPLICABLE = 0;
    public static final int ALARM_SCHEDULED__EXACT_ALARM_ALLOWED_REASON__PERMISSION = 1;
    public static final byte ANNOTATION_ID_EXCLUSIVE_STATE = 4;
    public static final byte ANNOTATION_ID_IS_UID = 1;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD = 3;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD_FIRST_UID = 5;
    public static final byte ANNOTATION_ID_STATE_NESTED = 8;
    public static final byte ANNOTATION_ID_TRIGGER_STATE_RESET = 7;
    public static final byte ANNOTATION_ID_TRUNCATE_TIMESTAMP = 2;
    public static final int ANROCCURRED__ERROR_SOURCE__DATA_APP = 1;
    public static final int ANROCCURRED__ERROR_SOURCE__ERROR_SOURCE_UNKNOWN = 0;
    public static final int ANROCCURRED__ERROR_SOURCE__SYSTEM_APP = 2;
    public static final int ANROCCURRED__ERROR_SOURCE__SYSTEM_SERVER = 3;
    public static final int ANROCCURRED__FOREGROUND_STATE__BACKGROUND = 1;
    public static final int ANROCCURRED__FOREGROUND_STATE__FOREGROUND = 2;
    public static final int ANROCCURRED__FOREGROUND_STATE__UNKNOWN = 0;
    public static final int ANROCCURRED__IS_INSTANT_APP__FALSE = 1;
    public static final int ANROCCURRED__IS_INSTANT_APP__TRUE = 2;
    public static final int ANROCCURRED__IS_INSTANT_APP__UNAVAILABLE = 0;
    public static final int ANR_OCCURRED = 79;
    public static final int ANR_OCCURRED_PROCESSING_STARTED = 376;
    public static final int APPS_ON_EXTERNAL_STORAGE_INFO = 10057;
    public static final int APPS_ON_EXTERNAL_STORAGE_INFO__EXTERNAL_STORAGE_TYPE__OTHER = 3;
    public static final int APPS_ON_EXTERNAL_STORAGE_INFO__EXTERNAL_STORAGE_TYPE__SD_CARD = 1;
    public static final int APPS_ON_EXTERNAL_STORAGE_INFO__EXTERNAL_STORAGE_TYPE__UNKNOWN = 0;
    public static final int APPS_ON_EXTERNAL_STORAGE_INFO__EXTERNAL_STORAGE_TYPE__USB = 2;
    public static final int APP_COMPACTED = 115;
    public static final int APP_COMPACTED__ACTION__BFGS = 4;
    public static final int APP_COMPACTED__ACTION__FULL = 2;
    public static final int APP_COMPACTED__ACTION__PERSISTENT = 3;
    public static final int APP_COMPACTED__ACTION__SOME = 1;
    public static final int APP_COMPACTED__ACTION__UNKNOWN = 0;
    public static final int APP_COMPACTED__LAST_ACTION__BFGS = 4;
    public static final int APP_COMPACTED__LAST_ACTION__FULL = 2;
    public static final int APP_COMPACTED__LAST_ACTION__PERSISTENT = 3;
    public static final int APP_COMPACTED__LAST_ACTION__SOME = 1;
    public static final int APP_COMPACTED__LAST_ACTION__UNKNOWN = 0;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_BACKUP = 1008;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 1004;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_BOUND_TOP = 1020;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_CACHED_ACTIVITY = 1015;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 1016;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_CACHED_EMPTY = 1018;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_CACHED_RECENT = 1017;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_FOREGROUND_SERVICE = 1003;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_HEAVY_WEIGHT = 1012;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_HOME = 1013;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_IMPORTANT_BACKGROUND = 1006;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_IMPORTANT_FOREGROUND = 1005;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_LAST_ACTIVITY = 1014;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_NONEXISTENT = 1019;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_PERSISTENT = 1000;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_PERSISTENT_UI = 1001;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_RECEIVER = 1010;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_SERVICE = 1009;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_TOP = 1002;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_TOP_SLEEPING = 1011;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_TRANSIENT_BACKGROUND = 1007;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_UNKNOWN = 999;
    public static final int APP_COMPACTED__PROCESS_STATE__PROCESS_STATE_UNKNOWN_TO_PROTO = 998;
    public static final int APP_COMPATIBILITY_CHANGE_REPORTED = 228;
    public static final int APP_COMPATIBILITY_CHANGE_REPORTED__SOURCE__APP_PROCESS = 1;
    public static final int APP_COMPATIBILITY_CHANGE_REPORTED__SOURCE__SYSTEM_SERVER = 2;
    public static final int APP_COMPATIBILITY_CHANGE_REPORTED__SOURCE__UNKNOWN_SOURCE = 0;
    public static final int APP_COMPATIBILITY_CHANGE_REPORTED__STATE__DISABLED = 2;
    public static final int APP_COMPATIBILITY_CHANGE_REPORTED__STATE__ENABLED = 1;
    public static final int APP_COMPATIBILITY_CHANGE_REPORTED__STATE__LOGGED = 3;
    public static final int APP_COMPATIBILITY_CHANGE_REPORTED__STATE__UNKNOWN_STATE = 0;
    public static final int APP_CRASH_OCCURRED = 78;
    public static final int APP_CRASH_OCCURRED__ERROR_SOURCE__DATA_APP = 1;
    public static final int APP_CRASH_OCCURRED__ERROR_SOURCE__ERROR_SOURCE_UNKNOWN = 0;
    public static final int APP_CRASH_OCCURRED__ERROR_SOURCE__SYSTEM_APP = 2;
    public static final int APP_CRASH_OCCURRED__ERROR_SOURCE__SYSTEM_SERVER = 3;
    public static final int APP_CRASH_OCCURRED__FOREGROUND_STATE__BACKGROUND = 1;
    public static final int APP_CRASH_OCCURRED__FOREGROUND_STATE__FOREGROUND = 2;
    public static final int APP_CRASH_OCCURRED__FOREGROUND_STATE__UNKNOWN = 0;
    public static final int APP_CRASH_OCCURRED__IS_INSTANT_APP__FALSE = 1;
    public static final int APP_CRASH_OCCURRED__IS_INSTANT_APP__TRUE = 2;
    public static final int APP_CRASH_OCCURRED__IS_INSTANT_APP__UNAVAILABLE = 0;
    public static final int APP_DIED = 65;
    public static final int APP_DOWNGRADED = 128;
    public static final int APP_FREEZE_CHANGED = 254;
    public static final int APP_FREEZE_CHANGED__ACTION__FREEZE_APP = 1;
    public static final int APP_FREEZE_CHANGED__ACTION__UNFREEZE_APP = 2;
    public static final int APP_FREEZE_CHANGED__ACTION__UNKNOWN = 0;
    public static final int APP_INSTALL_ON_EXTERNAL_STORAGE_REPORTED = 181;
    public static final int APP_INSTALL_ON_EXTERNAL_STORAGE_REPORTED__STORAGE_TYPE__OTHER = 3;
    public static final int APP_INSTALL_ON_EXTERNAL_STORAGE_REPORTED__STORAGE_TYPE__SD_CARD = 1;
    public static final int APP_INSTALL_ON_EXTERNAL_STORAGE_REPORTED__STORAGE_TYPE__UNKNOWN = 0;
    public static final int APP_INSTALL_ON_EXTERNAL_STORAGE_REPORTED__STORAGE_TYPE__USB = 2;
    public static final int APP_MOVED_STORAGE_REPORTED = 183;
    public static final int APP_MOVED_STORAGE_REPORTED__EXTERNAL_STORAGE_TYPE__OTHER = 3;
    public static final int APP_MOVED_STORAGE_REPORTED__EXTERNAL_STORAGE_TYPE__SD_CARD = 1;
    public static final int APP_MOVED_STORAGE_REPORTED__EXTERNAL_STORAGE_TYPE__UNKNOWN = 0;
    public static final int APP_MOVED_STORAGE_REPORTED__EXTERNAL_STORAGE_TYPE__USB = 2;
    public static final int APP_MOVED_STORAGE_REPORTED__MOVE_TYPE__TO_EXTERNAL = 1;
    public static final int APP_MOVED_STORAGE_REPORTED__MOVE_TYPE__TO_INTERNAL = 2;
    public static final int APP_MOVED_STORAGE_REPORTED__MOVE_TYPE__UNKNOWN = 0;
    public static final int APP_OPS = 10060;
    public static final int APP_OPS__OP_ID__APP_OP_ACCEPT_HANDOVER = 74;
    public static final int APP_OPS__OP_ID__APP_OP_ACCESS_ACCESSIBILITY = 88;
    public static final int APP_OPS__OP_ID__APP_OP_ACCESS_MEDIA_LOCATION = 90;
    public static final int APP_OPS__OP_ID__APP_OP_ACCESS_NOTIFICATIONS = 25;
    public static final int APP_OPS__OP_ID__APP_OP_ACTIVATE_PLATFORM_VPN = 94;
    public static final int APP_OPS__OP_ID__APP_OP_ACTIVATE_VPN = 47;
    public static final int APP_OPS__OP_ID__APP_OP_ACTIVITY_RECOGNITION = 79;
    public static final int APP_OPS__OP_ID__APP_OP_ACTIVITY_RECOGNITION_SOURCE = 113;
    public static final int APP_OPS__OP_ID__APP_OP_ADD_VOICEMAIL = 52;
    public static final int APP_OPS__OP_ID__APP_OP_ANSWER_PHONE_CALLS = 69;
    public static final int APP_OPS__OP_ID__APP_OP_ASSIST_SCREENSHOT = 50;
    public static final int APP_OPS__OP_ID__APP_OP_ASSIST_STRUCTURE = 49;
    public static final int APP_OPS__OP_ID__APP_OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    public static final int APP_OPS__OP_ID__APP_OP_AUDIO_ALARM_VOLUME = 37;
    public static final int APP_OPS__OP_ID__APP_OP_AUDIO_BLUETOOTH_VOLUME = 39;
    public static final int APP_OPS__OP_ID__APP_OP_AUDIO_MASTER_VOLUME = 33;
    public static final int APP_OPS__OP_ID__APP_OP_AUDIO_MEDIA_VOLUME = 36;
    public static final int APP_OPS__OP_ID__APP_OP_AUDIO_NOTIFICATION_VOLUME = 38;
    public static final int APP_OPS__OP_ID__APP_OP_AUDIO_RING_VOLUME = 35;
    public static final int APP_OPS__OP_ID__APP_OP_AUDIO_VOICE_VOLUME = 34;
    public static final int APP_OPS__OP_ID__APP_OP_AUTO_REVOKE_MANAGED_BY_INSTALLER = 98;
    public static final int APP_OPS__OP_ID__APP_OP_AUTO_REVOKE_PERMISSIONS_IF_UNUSED = 97;
    public static final int APP_OPS__OP_ID__APP_OP_BIND_ACCESSIBILITY_SERVICE = 73;
    public static final int APP_OPS__OP_ID__APP_OP_BLUETOOTH_ADVERTISE = 114;
    public static final int APP_OPS__OP_ID__APP_OP_BLUETOOTH_CONNECT = 111;
    public static final int APP_OPS__OP_ID__APP_OP_BLUETOOTH_SCAN = 77;
    public static final int APP_OPS__OP_ID__APP_OP_BODY_SENSORS = 56;
    public static final int APP_OPS__OP_ID__APP_OP_CALL_PHONE = 13;
    public static final int APP_OPS__OP_ID__APP_OP_CAMERA = 26;
    public static final int APP_OPS__OP_ID__APP_OP_CHANGE_WIFI_STATE = 71;
    public static final int APP_OPS__OP_ID__APP_OP_COARSE_LOCATION = 0;
    public static final int APP_OPS__OP_ID__APP_OP_COARSE_LOCATION_SOURCE = 109;
    public static final int APP_OPS__OP_ID__APP_OP_DEPRECATED_1 = 96;
    public static final int APP_OPS__OP_ID__APP_OP_FINE_LOCATION = 1;
    public static final int APP_OPS__OP_ID__APP_OP_FINE_LOCATION_SOURCE = 108;
    public static final int APP_OPS__OP_ID__APP_OP_GET_ACCOUNTS = 62;
    public static final int APP_OPS__OP_ID__APP_OP_GET_USAGE_STATS = 43;
    public static final int APP_OPS__OP_ID__APP_OP_GPS = 2;
    public static final int APP_OPS__OP_ID__APP_OP_INSTANT_APP_START_FOREGROUND = 68;
    public static final int APP_OPS__OP_ID__APP_OP_INTERACT_ACROSS_PROFILES = 93;
    public static final int APP_OPS__OP_ID__APP_OP_LEGACY_STORAGE = 87;
    public static final int APP_OPS__OP_ID__APP_OP_LOADER_USAGE_STATS = 95;
    public static final int APP_OPS__OP_ID__APP_OP_MANAGE_CREDENTIALS = 104;
    public static final int APP_OPS__OP_ID__APP_OP_MANAGE_EXTERNAL_STORAGE = 92;
    public static final int APP_OPS__OP_ID__APP_OP_MANAGE_IPSEC_TUNNELS = 75;
    public static final int APP_OPS__OP_ID__APP_OP_MANAGE_MEDIA = 110;
    public static final int APP_OPS__OP_ID__APP_OP_MANAGE_ONGOING_CALLS = 103;
    public static final int APP_OPS__OP_ID__APP_OP_MOCK_LOCATION = 58;
    public static final int APP_OPS__OP_ID__APP_OP_MONITOR_HIGH_POWER_LOCATION = 42;
    public static final int APP_OPS__OP_ID__APP_OP_MONITOR_LOCATION = 41;
    public static final int APP_OPS__OP_ID__APP_OP_MUTE_MICROPHONE = 44;
    public static final int APP_OPS__OP_ID__APP_OP_NEIGHBORING_CELLS = 12;
    public static final int APP_OPS__OP_ID__APP_OP_NONE = -1;
    public static final int APP_OPS__OP_ID__APP_OP_NO_ISOLATED_STORAGE = 99;
    public static final int APP_OPS__OP_ID__APP_OP_PHONE_CALL_CAMERA = 101;
    public static final int APP_OPS__OP_ID__APP_OP_PHONE_CALL_MICROPHONE = 100;
    public static final int APP_OPS__OP_ID__APP_OP_PICTURE_IN_PICTURE = 67;
    public static final int APP_OPS__OP_ID__APP_OP_PLAY_AUDIO = 28;
    public static final int APP_OPS__OP_ID__APP_OP_POST_NOTIFICATION = 11;
    public static final int APP_OPS__OP_ID__APP_OP_PROCESS_OUTGOING_CALLS = 54;
    public static final int APP_OPS__OP_ID__APP_OP_PROJECT_MEDIA = 46;
    public static final int APP_OPS__OP_ID__APP_OP_QUERY_ALL_PACKAGES = 91;
    public static final int APP_OPS__OP_ID__APP_OP_READ_CALENDAR = 8;
    public static final int APP_OPS__OP_ID__APP_OP_READ_CALL_LOG = 6;
    public static final int APP_OPS__OP_ID__APP_OP_READ_CELL_BROADCASTS = 57;
    public static final int APP_OPS__OP_ID__APP_OP_READ_CLIPBOARD = 29;
    public static final int APP_OPS__OP_ID__APP_OP_READ_CONTACTS = 4;
    public static final int APP_OPS__OP_ID__APP_OP_READ_DEVICE_IDENTIFIERS = 89;
    public static final int APP_OPS__OP_ID__APP_OP_READ_EXTERNAL_STORAGE = 59;
    public static final int APP_OPS__OP_ID__APP_OP_READ_ICC_SMS = 21;
    public static final int APP_OPS__OP_ID__APP_OP_READ_MEDIA_AUDIO = 81;
    public static final int APP_OPS__OP_ID__APP_OP_READ_MEDIA_IMAGES = 85;
    public static final int APP_OPS__OP_ID__APP_OP_READ_MEDIA_VIDEO = 83;
    public static final int APP_OPS__OP_ID__APP_OP_READ_PHONE_NUMBERS = 65;
    public static final int APP_OPS__OP_ID__APP_OP_READ_PHONE_STATE = 51;
    public static final int APP_OPS__OP_ID__APP_OP_READ_SMS = 14;
    public static final int APP_OPS__OP_ID__APP_OP_RECEIVE_EMERGENCY_SMS = 17;
    public static final int APP_OPS__OP_ID__APP_OP_RECEIVE_MMS = 18;
    public static final int APP_OPS__OP_ID__APP_OP_RECEIVE_SMS = 16;
    public static final int APP_OPS__OP_ID__APP_OP_RECEIVE_WAP_PUSH = 19;
    public static final int APP_OPS__OP_ID__APP_OP_RECORD_AUDIO = 27;
    public static final int APP_OPS__OP_ID__APP_OP_RECORD_AUDIO_HOTWORD = 102;
    public static final int APP_OPS__OP_ID__APP_OP_RECORD_AUDIO_OUTPUT = 106;
    public static final int APP_OPS__OP_ID__APP_OP_RECORD_INCOMING_PHONE_AUDIO = 115;
    public static final int APP_OPS__OP_ID__APP_OP_REQUEST_DELETE_PACKAGES = 72;
    public static final int APP_OPS__OP_ID__APP_OP_REQUEST_INSTALL_PACKAGES = 66;
    public static final int APP_OPS__OP_ID__APP_OP_RUN_ANY_IN_BACKGROUND = 70;
    public static final int APP_OPS__OP_ID__APP_OP_RUN_IN_BACKGROUND = 63;
    public static final int APP_OPS__OP_ID__APP_OP_SCHEDULE_EXACT_ALARM = 107;
    public static final int APP_OPS__OP_ID__APP_OP_SEND_SMS = 20;
    public static final int APP_OPS__OP_ID__APP_OP_SMS_FINANCIAL_TRANSACTIONS = 80;
    public static final int APP_OPS__OP_ID__APP_OP_START_FOREGROUND = 76;
    public static final int APP_OPS__OP_ID__APP_OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int APP_OPS__OP_ID__APP_OP_TAKE_AUDIO_FOCUS = 32;
    public static final int APP_OPS__OP_ID__APP_OP_TAKE_MEDIA_BUTTONS = 31;
    public static final int APP_OPS__OP_ID__APP_OP_TOAST_WINDOW = 45;
    public static final int APP_OPS__OP_ID__APP_OP_TURN_SCREEN_ON = 61;
    public static final int APP_OPS__OP_ID__APP_OP_USE_BIOMETRIC = 78;
    public static final int APP_OPS__OP_ID__APP_OP_USE_FINGERPRINT = 55;
    public static final int APP_OPS__OP_ID__APP_OP_USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER = 105;
    public static final int APP_OPS__OP_ID__APP_OP_USE_SIP = 53;
    public static final int APP_OPS__OP_ID__APP_OP_UWB_RANGING = 112;
    public static final int APP_OPS__OP_ID__APP_OP_VIBRATE = 3;
    public static final int APP_OPS__OP_ID__APP_OP_WAKE_LOCK = 40;
    public static final int APP_OPS__OP_ID__APP_OP_WIFI_SCAN = 10;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_CALENDAR = 9;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_CALL_LOG = 7;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_CLIPBOARD = 30;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_CONTACTS = 5;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_EXTERNAL_STORAGE = 60;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_ICC_SMS = 22;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_MEDIA_AUDIO = 82;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_MEDIA_IMAGES = 86;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_MEDIA_VIDEO = 84;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_SETTINGS = 23;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_SMS = 15;
    public static final int APP_OPS__OP_ID__APP_OP_WRITE_WALLPAPER = 48;
    public static final int APP_PROCESS_DIED = 373;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_BACKGROUND = 400;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_CANT_SAVE_STATE = 350;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_CANT_SAVE_STATE_PRE_26 = 170;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_EMPTY = 500;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_FOREGROUND = 100;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_FOREGROUND_SERVICE = 125;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_GONE = 1000;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_PERCEPTIBLE = 230;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_PERCEPTIBLE_PRE_26 = 130;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_SERVICE = 300;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_TOP_SLEEPING = 325;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_TOP_SLEEPING_PRE_28 = 150;
    public static final int APP_PROCESS_DIED__IMPORTANCE__IMPORTANCE_VISIBLE = 200;
    public static final int APP_PROCESS_DIED__REASON__REASON_ANR = 6;
    public static final int APP_PROCESS_DIED__REASON__REASON_CRASH = 4;
    public static final int APP_PROCESS_DIED__REASON__REASON_CRASH_NATIVE = 5;
    public static final int APP_PROCESS_DIED__REASON__REASON_DEPENDENCY_DIED = 12;
    public static final int APP_PROCESS_DIED__REASON__REASON_EXCESSIVE_RESOURCE_USAGE = 9;
    public static final int APP_PROCESS_DIED__REASON__REASON_EXIT_SELF = 1;
    public static final int APP_PROCESS_DIED__REASON__REASON_INITIALIZATION_FAILURE = 7;
    public static final int APP_PROCESS_DIED__REASON__REASON_LOW_MEMORY = 3;
    public static final int APP_PROCESS_DIED__REASON__REASON_OTHER = 13;
    public static final int APP_PROCESS_DIED__REASON__REASON_PERMISSION_CHANGE = 8;
    public static final int APP_PROCESS_DIED__REASON__REASON_SIGNALED = 2;
    public static final int APP_PROCESS_DIED__REASON__REASON_UNKNOWN = 0;
    public static final int APP_PROCESS_DIED__REASON__REASON_USER_REQUESTED = 10;
    public static final int APP_PROCESS_DIED__REASON__REASON_USER_STOPPED = 11;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_CACHED_IDLE_FORCED_APP_STANDBY = 18;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_EXCESSIVE_CPU = 7;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_FREEZER_BINDER_IOCTL = 19;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_FREEZER_BINDER_TRANSACTION = 20;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_IMPERCEPTIBLE = 15;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_INVALID_START = 13;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_INVALID_STATE = 14;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_ISOLATED_NOT_NEEDED = 17;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_KILL_ALL_BG_EXCEPT = 10;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_KILL_ALL_FG = 9;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_KILL_PID = 12;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_KILL_UID = 11;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_LARGE_CACHED = 5;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_MEMORY_PRESSURE = 6;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_REMOVE_LRU = 16;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_SYSTEM_UPDATE_DONE = 8;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_TOO_MANY_CACHED = 2;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_TOO_MANY_EMPTY = 3;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_TRIM_EMPTY = 4;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_UNKNOWN = 0;
    public static final int APP_PROCESS_DIED__SUB_REASON__SUBREASON_WAIT_FOR_DEBUGGER = 1;
    public static final int APP_SIZE = 10027;
    public static final int APP_STANDBY_BUCKET_CHANGED = 258;
    public static final int APP_STANDBY_BUCKET_CHANGED__BUCKET__BUCKET_ACTIVE = 10;
    public static final int APP_STANDBY_BUCKET_CHANGED__BUCKET__BUCKET_EXEMPTED = 5;
    public static final int APP_STANDBY_BUCKET_CHANGED__BUCKET__BUCKET_FREQUENT = 30;
    public static final int APP_STANDBY_BUCKET_CHANGED__BUCKET__BUCKET_NEVER = 50;
    public static final int APP_STANDBY_BUCKET_CHANGED__BUCKET__BUCKET_RARE = 40;
    public static final int APP_STANDBY_BUCKET_CHANGED__BUCKET__BUCKET_RESTRICTED = 45;
    public static final int APP_STANDBY_BUCKET_CHANGED__BUCKET__BUCKET_UNKNOWN = 0;
    public static final int APP_STANDBY_BUCKET_CHANGED__BUCKET__BUCKET_WORKING_SET = 20;
    public static final int APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_DEFAULT = 256;
    public static final int APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_FORCED_BY_SYSTEM = 1536;
    public static final int APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_FORCED_BY_USER = 1024;
    public static final int APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_PREDICTED = 1280;
    public static final int APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_TIMEOUT = 512;
    public static final int APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_UNKNOWN = 0;
    public static final int APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_USAGE = 768;
    public static final int APP_START_CANCELED = 49;
    public static final int APP_START_CANCELED__TYPE__COLD = 3;
    public static final int APP_START_CANCELED__TYPE__HOT = 2;
    public static final int APP_START_CANCELED__TYPE__RELAUNCH = 4;
    public static final int APP_START_CANCELED__TYPE__UNKNOWN = 0;
    public static final int APP_START_CANCELED__TYPE__WARM = 1;
    public static final int APP_START_FULLY_DRAWN = 50;
    public static final int APP_START_FULLY_DRAWN__SOURCE_TYPE__LAUNCHER = 1;
    public static final int APP_START_FULLY_DRAWN__SOURCE_TYPE__LOCKSCREEN = 3;
    public static final int APP_START_FULLY_DRAWN__SOURCE_TYPE__NOTIFICATION = 2;
    public static final int APP_START_FULLY_DRAWN__SOURCE_TYPE__UNAVAILABLE = 0;
    public static final int APP_START_FULLY_DRAWN__TYPE__UNKNOWN = 0;
    public static final int APP_START_FULLY_DRAWN__TYPE__WITHOUT_BUNDLE = 2;
    public static final int APP_START_FULLY_DRAWN__TYPE__WITH_BUNDLE = 1;
    public static final int APP_START_MEMORY_STATE_CAPTURED = 55;
    public static final int APP_START_OCCURRED = 48;
    public static final int APP_START_OCCURRED__REASON__APP_TRANSITION_REASON_UNKNOWN = 0;
    public static final int APP_START_OCCURRED__REASON__APP_TRANSITION_RECENTS_ANIM = 5;
    public static final int APP_START_OCCURRED__REASON__APP_TRANSITION_SNAPSHOT = 4;
    public static final int APP_START_OCCURRED__REASON__APP_TRANSITION_SPLASH_SCREEN = 1;
    public static final int APP_START_OCCURRED__REASON__APP_TRANSITION_TIMEOUT = 3;
    public static final int APP_START_OCCURRED__REASON__APP_TRANSITION_WINDOWS_DRAWN = 2;
    public static final int APP_START_OCCURRED__SOURCE_TYPE__LAUNCHER = 1;
    public static final int APP_START_OCCURRED__SOURCE_TYPE__LOCKSCREEN = 3;
    public static final int APP_START_OCCURRED__SOURCE_TYPE__NOTIFICATION = 2;
    public static final int APP_START_OCCURRED__SOURCE_TYPE__RECENTS_ANIMATION = 4;
    public static final int APP_START_OCCURRED__SOURCE_TYPE__UNAVAILABLE = 0;
    public static final int APP_START_OCCURRED__TYPE__COLD = 3;
    public static final int APP_START_OCCURRED__TYPE__HOT = 2;
    public static final int APP_START_OCCURRED__TYPE__RELAUNCH = 4;
    public static final int APP_START_OCCURRED__TYPE__UNKNOWN = 0;
    public static final int APP_START_OCCURRED__TYPE__WARM = 1;
    public static final int APP_USAGE_EVENT_OCCURRED = 269;
    public static final int APP_USAGE_EVENT_OCCURRED__EVENT_TYPE__MOVE_TO_BACKGROUND = 2;
    public static final int APP_USAGE_EVENT_OCCURRED__EVENT_TYPE__MOVE_TO_FOREGROUND = 1;
    public static final int APP_USAGE_EVENT_OCCURRED__EVENT_TYPE__NONE = 0;
    public static final int ASSISTANT_INVOCATION_REPORTED = 281;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__AOD1 = 1;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__AOD2 = 2;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__APP_DEFAULT = 8;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__APP_FULLSCREEN = 10;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__APP_IMMERSIVE = 9;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__BOUNCER = 3;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__LAUNCHER_ALL_APPS = 7;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__LAUNCHER_HOME = 5;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__LAUNCHER_OVERVIEW = 6;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__UNKNOWN_DEVICE_STATE = 0;
    public static final int ASSISTANT_INVOCATION_REPORTED__DEVICE_STATE__UNLOCKED_LOCKSCREEN = 4;
    public static final int ATTENTION_MANAGER_SERVICE_RESULT_REPORTED = 143;
    public static final int ATTENTION_MANAGER_SERVICE_RESULT_REPORTED__ATTENTION_CHECK_RESULT__ATTENTION_FAILURE_CAMERA_PERMISSION_ABSENT = 6;
    public static final int ATTENTION_MANAGER_SERVICE_RESULT_REPORTED__ATTENTION_CHECK_RESULT__ATTENTION_FAILURE_CANCELLED = 3;
    public static final int ATTENTION_MANAGER_SERVICE_RESULT_REPORTED__ATTENTION_CHECK_RESULT__ATTENTION_FAILURE_PREEMPTED = 4;
    public static final int ATTENTION_MANAGER_SERVICE_RESULT_REPORTED__ATTENTION_CHECK_RESULT__ATTENTION_FAILURE_TIMED_OUT = 5;
    public static final int ATTENTION_MANAGER_SERVICE_RESULT_REPORTED__ATTENTION_CHECK_RESULT__ATTENTION_FAILURE_UNKNOWN = 2;
    public static final int ATTENTION_MANAGER_SERVICE_RESULT_REPORTED__ATTENTION_CHECK_RESULT__ATTENTION_SUCCESS_ABSENT = 0;
    public static final int ATTENTION_MANAGER_SERVICE_RESULT_REPORTED__ATTENTION_CHECK_RESULT__ATTENTION_SUCCESS_PRESENT = 1;
    public static final int ATTENTION_MANAGER_SERVICE_RESULT_REPORTED__ATTENTION_CHECK_RESULT__UNKNOWN = 20;
    public static final int ATTRIBUTED_APP_OPS = 10075;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ACCEPT_HANDOVER = 74;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ACCESS_ACCESSIBILITY = 88;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ACCESS_MEDIA_LOCATION = 90;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ACCESS_NOTIFICATIONS = 25;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ACTIVATE_PLATFORM_VPN = 94;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ACTIVATE_VPN = 47;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ACTIVITY_RECOGNITION = 79;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ACTIVITY_RECOGNITION_SOURCE = 113;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ADD_VOICEMAIL = 52;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ANSWER_PHONE_CALLS = 69;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ASSIST_SCREENSHOT = 50;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_ASSIST_STRUCTURE = 49;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUDIO_ALARM_VOLUME = 37;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUDIO_BLUETOOTH_VOLUME = 39;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUDIO_MASTER_VOLUME = 33;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUDIO_MEDIA_VOLUME = 36;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUDIO_NOTIFICATION_VOLUME = 38;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUDIO_RING_VOLUME = 35;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUDIO_VOICE_VOLUME = 34;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUTO_REVOKE_MANAGED_BY_INSTALLER = 98;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_AUTO_REVOKE_PERMISSIONS_IF_UNUSED = 97;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_BIND_ACCESSIBILITY_SERVICE = 73;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_BLUETOOTH_ADVERTISE = 114;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_BLUETOOTH_CONNECT = 111;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_BLUETOOTH_SCAN = 77;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_BODY_SENSORS = 56;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_CALL_PHONE = 13;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_CAMERA = 26;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_CHANGE_WIFI_STATE = 71;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_COARSE_LOCATION = 0;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_COARSE_LOCATION_SOURCE = 109;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_DEPRECATED_1 = 96;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_FINE_LOCATION = 1;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_FINE_LOCATION_SOURCE = 108;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_GET_ACCOUNTS = 62;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_GET_USAGE_STATS = 43;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_GPS = 2;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_INSTANT_APP_START_FOREGROUND = 68;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_INTERACT_ACROSS_PROFILES = 93;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_LEGACY_STORAGE = 87;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_LOADER_USAGE_STATS = 95;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_MANAGE_CREDENTIALS = 104;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_MANAGE_EXTERNAL_STORAGE = 92;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_MANAGE_IPSEC_TUNNELS = 75;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_MANAGE_MEDIA = 110;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_MANAGE_ONGOING_CALLS = 103;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_MOCK_LOCATION = 58;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_MONITOR_HIGH_POWER_LOCATION = 42;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_MONITOR_LOCATION = 41;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_MUTE_MICROPHONE = 44;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_NEIGHBORING_CELLS = 12;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_NONE = -1;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_NO_ISOLATED_STORAGE = 99;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_PHONE_CALL_CAMERA = 101;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_PHONE_CALL_MICROPHONE = 100;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_PICTURE_IN_PICTURE = 67;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_PLAY_AUDIO = 28;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_POST_NOTIFICATION = 11;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_PROCESS_OUTGOING_CALLS = 54;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_PROJECT_MEDIA = 46;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_QUERY_ALL_PACKAGES = 91;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_CALENDAR = 8;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_CALL_LOG = 6;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_CELL_BROADCASTS = 57;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_CLIPBOARD = 29;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_CONTACTS = 4;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_DEVICE_IDENTIFIERS = 89;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_EXTERNAL_STORAGE = 59;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_ICC_SMS = 21;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_MEDIA_AUDIO = 81;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_MEDIA_IMAGES = 85;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_MEDIA_VIDEO = 83;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_PHONE_NUMBERS = 65;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_PHONE_STATE = 51;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_READ_SMS = 14;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RECEIVE_EMERGENCY_SMS = 17;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RECEIVE_MMS = 18;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RECEIVE_SMS = 16;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RECEIVE_WAP_PUSH = 19;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RECORD_AUDIO = 27;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RECORD_AUDIO_HOTWORD = 102;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RECORD_AUDIO_OUTPUT = 106;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RECORD_INCOMING_PHONE_AUDIO = 115;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_REQUEST_DELETE_PACKAGES = 72;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_REQUEST_INSTALL_PACKAGES = 66;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RUN_ANY_IN_BACKGROUND = 70;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_RUN_IN_BACKGROUND = 63;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_SCHEDULE_EXACT_ALARM = 107;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_SEND_SMS = 20;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_SMS_FINANCIAL_TRANSACTIONS = 80;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_START_FOREGROUND = 76;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_TAKE_AUDIO_FOCUS = 32;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_TAKE_MEDIA_BUTTONS = 31;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_TOAST_WINDOW = 45;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_TURN_SCREEN_ON = 61;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_USE_BIOMETRIC = 78;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_USE_FINGERPRINT = 55;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER = 105;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_USE_SIP = 53;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_UWB_RANGING = 112;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_VIBRATE = 3;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WAKE_LOCK = 40;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WIFI_SCAN = 10;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_CALENDAR = 9;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_CALL_LOG = 7;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_CLIPBOARD = 30;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_CONTACTS = 5;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_EXTERNAL_STORAGE = 60;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_ICC_SMS = 22;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_MEDIA_AUDIO = 82;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_MEDIA_IMAGES = 86;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_MEDIA_VIDEO = 84;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_SETTINGS = 23;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_SMS = 15;
    public static final int ATTRIBUTED_APP_OPS__OP__APP_OP_WRITE_WALLPAPER = 48;
    public static final int AUDIO_STATE_CHANGED = 23;
    public static final int AUDIO_STATE_CHANGED__STATE__OFF = 0;
    public static final int AUDIO_STATE_CHANGED__STATE__ON = 1;
    public static final int AUDIO_STATE_CHANGED__STATE__RESET = 2;
    public static final int AUTH_DEPRECATED_APIUSED__DEPRECATED_API__API_BIOMETRIC_MANAGER_CAN_AUTHENTICATE = 4;
    public static final int AUTH_DEPRECATED_APIUSED__DEPRECATED_API__API_FINGERPRINT_MANAGER_AUTHENTICATE = 1;
    public static final int AUTH_DEPRECATED_APIUSED__DEPRECATED_API__API_FINGERPRINT_MANAGER_HAS_ENROLLED_FINGERPRINTS = 2;
    public static final int AUTH_DEPRECATED_APIUSED__DEPRECATED_API__API_FINGERPRINT_MANAGER_IS_HARDWARE_DETECTED = 3;
    public static final int AUTH_DEPRECATED_APIUSED__DEPRECATED_API__API_UNKNOWN = 0;
    public static final int AUTH_DEPRECATED_API_USED = 356;
    public static final int AUTH_ENROLL_ACTION_INVOKED = 355;
    public static final int AUTH_MANAGER_CAN_AUTHENTICATE_INVOKED = 354;
    public static final int AUTH_PROMPT_AUTHENTICATE_INVOKED = 353;
    public static final int AUTO_ROTATE_REPORTED = 328;
    public static final int AUTO_ROTATE_REPORTED__CURRENT_ORIENTATION__DEPRECATED = 1;
    public static final int AUTO_ROTATE_REPORTED__CURRENT_ORIENTATION__DISABLED = 6;
    public static final int AUTO_ROTATE_REPORTED__CURRENT_ORIENTATION__FAILURE = 8;
    public static final int AUTO_ROTATE_REPORTED__CURRENT_ORIENTATION__ROTATION_0 = 2;
    public static final int AUTO_ROTATE_REPORTED__CURRENT_ORIENTATION__ROTATION_180 = 4;
    public static final int AUTO_ROTATE_REPORTED__CURRENT_ORIENTATION__ROTATION_270 = 5;
    public static final int AUTO_ROTATE_REPORTED__CURRENT_ORIENTATION__ROTATION_90 = 3;
    public static final int AUTO_ROTATE_REPORTED__CURRENT_ORIENTATION__UNAVAILABLE = 7;
    public static final int AUTO_ROTATE_REPORTED__CURRENT_ORIENTATION__UNKNOWN = 0;
    public static final int AUTO_ROTATE_REPORTED__PROPOSED_ORIENTATION__DEPRECATED = 1;
    public static final int AUTO_ROTATE_REPORTED__PROPOSED_ORIENTATION__DISABLED = 6;
    public static final int AUTO_ROTATE_REPORTED__PROPOSED_ORIENTATION__FAILURE = 8;
    public static final int AUTO_ROTATE_REPORTED__PROPOSED_ORIENTATION__ROTATION_0 = 2;
    public static final int AUTO_ROTATE_REPORTED__PROPOSED_ORIENTATION__ROTATION_180 = 4;
    public static final int AUTO_ROTATE_REPORTED__PROPOSED_ORIENTATION__ROTATION_270 = 5;
    public static final int AUTO_ROTATE_REPORTED__PROPOSED_ORIENTATION__ROTATION_90 = 3;
    public static final int AUTO_ROTATE_REPORTED__PROPOSED_ORIENTATION__UNAVAILABLE = 7;
    public static final int AUTO_ROTATE_REPORTED__PROPOSED_ORIENTATION__UNKNOWN = 0;
    public static final int AUTO_ROTATE_REPORTED__RECOMMENDED_ORIENTATION__DEPRECATED = 1;
    public static final int AUTO_ROTATE_REPORTED__RECOMMENDED_ORIENTATION__DISABLED = 6;
    public static final int AUTO_ROTATE_REPORTED__RECOMMENDED_ORIENTATION__FAILURE = 8;
    public static final int AUTO_ROTATE_REPORTED__RECOMMENDED_ORIENTATION__ROTATION_0 = 2;
    public static final int AUTO_ROTATE_REPORTED__RECOMMENDED_ORIENTATION__ROTATION_180 = 4;
    public static final int AUTO_ROTATE_REPORTED__RECOMMENDED_ORIENTATION__ROTATION_270 = 5;
    public static final int AUTO_ROTATE_REPORTED__RECOMMENDED_ORIENTATION__ROTATION_90 = 3;
    public static final int AUTO_ROTATE_REPORTED__RECOMMENDED_ORIENTATION__UNAVAILABLE = 7;
    public static final int AUTO_ROTATE_REPORTED__RECOMMENDED_ORIENTATION__UNKNOWN = 0;
    public static final int BATTERY_CYCLE_COUNT = 10045;
    public static final int BATTERY_LEVEL = 10043;
    public static final int BATTERY_LEVEL_CHANGED = 30;
    public static final int BATTERY_SAVER_MODE_STATE_CHANGED = 20;
    public static final int BATTERY_SAVER_MODE_STATE_CHANGED__STATE__OFF = 0;
    public static final int BATTERY_SAVER_MODE_STATE_CHANGED__STATE__ON = 1;
    public static final int BATTERY_USAGE_STATS_BEFORE_RESET = 10111;
    public static final int BATTERY_USAGE_STATS_SINCE_RESET = 10112;
    public static final int BATTERY_USAGE_STATS_SINCE_RESET_USING_POWER_PROFILE_MODEL = 10113;
    public static final int BATTERY_VOLTAGE = 10030;
    public static final int BINDER_CALLS = 10022;
    public static final int BINDER_CALLS_EXCEPTIONS = 10023;
    public static final int BINDER_LATENCY_REPORTED = 342;
    public static final int BIOMETRIC_ACQUIRED = 87;
    public static final int BIOMETRIC_ACQUIRED__ACTION__ACTION_AUTHENTICATE = 2;
    public static final int BIOMETRIC_ACQUIRED__ACTION__ACTION_ENROLL = 1;
    public static final int BIOMETRIC_ACQUIRED__ACTION__ACTION_ENUMERATE = 3;
    public static final int BIOMETRIC_ACQUIRED__ACTION__ACTION_REMOVE = 4;
    public static final int BIOMETRIC_ACQUIRED__ACTION__ACTION_UNKNOWN = 0;
    public static final int BIOMETRIC_ACQUIRED__CLIENT__CLIENT_BIOMETRIC_PROMPT = 2;
    public static final int BIOMETRIC_ACQUIRED__CLIENT__CLIENT_FINGERPRINT_MANAGER = 3;
    public static final int BIOMETRIC_ACQUIRED__CLIENT__CLIENT_KEYGUARD = 1;
    public static final int BIOMETRIC_ACQUIRED__CLIENT__CLIENT_UNKNOWN = 0;
    public static final int BIOMETRIC_ACQUIRED__MODALITY__MODALITY_FACE = 4;
    public static final int BIOMETRIC_ACQUIRED__MODALITY__MODALITY_FINGERPRINT = 1;
    public static final int BIOMETRIC_ACQUIRED__MODALITY__MODALITY_IRIS = 2;
    public static final int BIOMETRIC_ACQUIRED__MODALITY__MODALITY_UNKNOWN = 0;
    public static final int BIOMETRIC_AUTHENTICATED = 88;
    public static final int BIOMETRIC_AUTHENTICATED__CLIENT__CLIENT_BIOMETRIC_PROMPT = 2;
    public static final int BIOMETRIC_AUTHENTICATED__CLIENT__CLIENT_FINGERPRINT_MANAGER = 3;
    public static final int BIOMETRIC_AUTHENTICATED__CLIENT__CLIENT_KEYGUARD = 1;
    public static final int BIOMETRIC_AUTHENTICATED__CLIENT__CLIENT_UNKNOWN = 0;
    public static final int BIOMETRIC_AUTHENTICATED__MODALITY__MODALITY_FACE = 4;
    public static final int BIOMETRIC_AUTHENTICATED__MODALITY__MODALITY_FINGERPRINT = 1;
    public static final int BIOMETRIC_AUTHENTICATED__MODALITY__MODALITY_IRIS = 2;
    public static final int BIOMETRIC_AUTHENTICATED__MODALITY__MODALITY_UNKNOWN = 0;
    public static final int BIOMETRIC_AUTHENTICATED__STATE__CONFIRMED = 3;
    public static final int BIOMETRIC_AUTHENTICATED__STATE__PENDING_CONFIRMATION = 2;
    public static final int BIOMETRIC_AUTHENTICATED__STATE__REJECTED = 1;
    public static final int BIOMETRIC_AUTHENTICATED__STATE__UNKNOWN = 0;
    public static final int BIOMETRIC_ENROLLED = 184;
    public static final int BIOMETRIC_ENROLLED__MODALITY__MODALITY_FACE = 4;
    public static final int BIOMETRIC_ENROLLED__MODALITY__MODALITY_FINGERPRINT = 1;
    public static final int BIOMETRIC_ENROLLED__MODALITY__MODALITY_IRIS = 2;
    public static final int BIOMETRIC_ENROLLED__MODALITY__MODALITY_UNKNOWN = 0;
    public static final int BIOMETRIC_ERROR_OCCURRED = 89;
    public static final int BIOMETRIC_ERROR_OCCURRED__ACTION__ACTION_AUTHENTICATE = 2;
    public static final int BIOMETRIC_ERROR_OCCURRED__ACTION__ACTION_ENROLL = 1;
    public static final int BIOMETRIC_ERROR_OCCURRED__ACTION__ACTION_ENUMERATE = 3;
    public static final int BIOMETRIC_ERROR_OCCURRED__ACTION__ACTION_REMOVE = 4;
    public static final int BIOMETRIC_ERROR_OCCURRED__ACTION__ACTION_UNKNOWN = 0;
    public static final int BIOMETRIC_ERROR_OCCURRED__CLIENT__CLIENT_BIOMETRIC_PROMPT = 2;
    public static final int BIOMETRIC_ERROR_OCCURRED__CLIENT__CLIENT_FINGERPRINT_MANAGER = 3;
    public static final int BIOMETRIC_ERROR_OCCURRED__CLIENT__CLIENT_KEYGUARD = 1;
    public static final int BIOMETRIC_ERROR_OCCURRED__CLIENT__CLIENT_UNKNOWN = 0;
    public static final int BIOMETRIC_ERROR_OCCURRED__MODALITY__MODALITY_FACE = 4;
    public static final int BIOMETRIC_ERROR_OCCURRED__MODALITY__MODALITY_FINGERPRINT = 1;
    public static final int BIOMETRIC_ERROR_OCCURRED__MODALITY__MODALITY_IRIS = 2;
    public static final int BIOMETRIC_ERROR_OCCURRED__MODALITY__MODALITY_UNKNOWN = 0;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED = 148;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED__ISSUE__ISSUE_CANCEL_TIMED_OUT = 4;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED__ISSUE__ISSUE_HAL_DEATH = 1;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED__ISSUE__ISSUE_UNKNOWN = 0;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED__ISSUE__ISSUE_UNKNOWN_TEMPLATE_ENROLLED_FRAMEWORK = 2;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED__ISSUE__ISSUE_UNKNOWN_TEMPLATE_ENROLLED_HAL = 3;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED__MODALITY__MODALITY_FACE = 4;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED__MODALITY__MODALITY_FINGERPRINT = 1;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED__MODALITY__MODALITY_IRIS = 2;
    public static final int BIOMETRIC_SYSTEM_HEALTH_ISSUE_DETECTED__MODALITY__MODALITY_UNKNOWN = 0;
    public static final int BLOB_COMMITTED = 298;
    public static final int BLOB_COMMITTED__RESULT__COUNT_LIMIT_EXCEEDED = 4;
    public static final int BLOB_COMMITTED__RESULT__DIGEST_MISMATCH = 3;
    public static final int BLOB_COMMITTED__RESULT__ERROR_DURING_COMMIT = 2;
    public static final int BLOB_COMMITTED__RESULT__SUCCESS = 1;
    public static final int BLOB_COMMITTED__RESULT__UNKNOWN = 0;
    public static final int BLOB_INFO = 10081;
    public static final int BLOB_LEASED = 299;
    public static final int BLOB_LEASED__RESULT__ACCESS_NOT_ALLOWED = 3;
    public static final int BLOB_LEASED__RESULT__BLOB_DNE = 2;
    public static final int BLOB_LEASED__RESULT__COUNT_LIMIT_EXCEEDED = 6;
    public static final int BLOB_LEASED__RESULT__DATA_SIZE_LIMIT_EXCEEDED = 5;
    public static final int BLOB_LEASED__RESULT__LEASE_EXPIRY_INVALID = 4;
    public static final int BLOB_LEASED__RESULT__SUCCESS = 1;
    public static final int BLOB_LEASED__RESULT__UNKNOWN = 0;
    public static final int BLOB_OPENED = 300;
    public static final int BLOB_OPENED__RESULT__ACCESS_NOT_ALLOWED = 3;
    public static final int BLOB_OPENED__RESULT__BLOB_DNE = 2;
    public static final int BLOB_OPENED__RESULT__SUCCESS = 1;
    public static final int BLOB_OPENED__RESULT__UNKNOWN = 0;
    public static final int BLUETOOTH_ACTIVITY_INFO = 10007;
    public static final int BLUETOOTH_BYTES_TRANSFER = 10006;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED = 67;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_AIRPLANE_MODE = 2;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_APPLICATION_REQUEST = 1;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_CRASH = 7;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_DISALLOWED = 3;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_FACTORY_RESET = 10;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_INIT_FLAGS_CHANGED = 11;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_RESTARTED = 4;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_RESTORE_USER_SETTING = 9;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_START_ERROR = 5;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_SYSTEM_BOOT = 6;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_UNSPECIFIED = 0;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__REASON__ENABLE_DISABLE_REASON_USER_SWITCH = 8;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__STATE__DISABLED = 2;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__STATE__ENABLED = 1;
    public static final int BLUETOOTH_ENABLED_STATE_CHANGED__STATE__UNKNOWN = 0;
    public static final int BOOT_TIME_EVENT_DURATION_REPORTED = 239;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__ABSOLUTE_BOOT_TIME = 1;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__ANDROID_INIT_STAGE_1 = 19;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__BOOTLOADER_FIRST_STAGE_EXEC = 2;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__BOOTLOADER_FIRST_STAGE_LOAD = 3;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__BOOTLOADER_KERNEL_LOAD = 4;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__BOOTLOADER_SECOND_STAGE_EXEC = 5;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__BOOTLOADER_SECOND_STAGE_LOAD = 6;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__BOOTLOADER_TOTAL = 8;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__BOOTLOADER_UI_WAIT = 7;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__COLDBOOT_WAIT = 16;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__FACTORY_RESET_TIME_SINCE_RESET = 18;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__MOUNT_DEFAULT_DURATION = 10;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__MOUNT_EARLY_DURATION = 11;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__MOUNT_LATE_DURATION = 12;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__OTA_PACKAGE_MANAGER_DATA_APP_AVG_SCAN_TIME = 14;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__OTA_PACKAGE_MANAGER_INIT_TIME = 13;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__OTA_PACKAGE_MANAGER_SYSTEM_APP_AVG_SCAN_TIME = 15;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__SELINUX_INIT = 17;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__SHUTDOWN_DURATION = 9;
    public static final int BOOT_TIME_EVENT_DURATION__EVENT__UNKNOWN = 0;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED = 240;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__ANDROID_INIT_STAGE_1 = 1;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__BOOT_COMPLETE = 2;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__BOOT_COMPLETE_ENCRYPTION = 3;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__BOOT_COMPLETE_NO_ENCRYPTION = 4;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__BOOT_COMPLETE_POST_DECRYPT = 5;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__FACTORY_RESET_BOOT_COMPLETE = 6;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__FACTORY_RESET_BOOT_COMPLETE_NO_ENCRYPTION = 7;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__FACTORY_RESET_BOOT_COMPLETE_POST_DECRYPT = 8;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__FRAMEWORK_BOOT_COMPLETED = 13;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__FRAMEWORK_LOCKED_BOOT_COMPLETED = 12;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__LAUNCHER_SHOWN = 22;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__LAUNCHER_START = 21;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__OTA_BOOT_COMPLETE = 9;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__OTA_BOOT_COMPLETE_NO_ENCRYPTION = 10;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__OTA_BOOT_COMPLETE_POST_DECRYPT = 11;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__PACKAGE_MANAGER_INIT_READY = 15;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__PACKAGE_MANAGER_INIT_START = 14;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__POST_DECRYPT = 16;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__SECONDARY_ZYGOTE_INIT_START = 18;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__SYSTEM_SERVER_INIT_START = 19;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__SYSTEM_SERVER_READY = 20;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__UNKNOWN = 0;
    public static final int BOOT_TIME_EVENT_ELAPSED_TIME__EVENT__ZYGOTE_INIT_START = 17;
    public static final int BOOT_TIME_EVENT_ERROR_CODE_REPORTED = 242;
    public static final int BOOT_TIME_EVENT_ERROR_CODE__EVENT__FACTORY_RESET_CURRENT_TIME_FAILURE = 1;
    public static final int BOOT_TIME_EVENT_ERROR_CODE__EVENT__FS_MGR_FS_STAT_DATA_PARTITION = 3;
    public static final int BOOT_TIME_EVENT_ERROR_CODE__EVENT__SHUTDOWN_UMOUNT_STAT = 2;
    public static final int BOOT_TIME_EVENT_ERROR_CODE__EVENT__UNKNOWN = 0;
    public static final int BROADCAST_DISPATCH_LATENCY_REPORTED = 142;
    public static final int BUBBLE_DEVELOPER_ERROR_REPORTED = 173;
    public static final int BUBBLE_DEVELOPER_ERROR_REPORTED__ERROR__ACTIVITY_INFO_MISSING = 1;
    public static final int BUBBLE_DEVELOPER_ERROR_REPORTED__ERROR__ACTIVITY_INFO_NOT_RESIZABLE = 2;
    public static final int BUBBLE_DEVELOPER_ERROR_REPORTED__ERROR__DOCUMENT_LAUNCH_NOT_ALWAYS = 3;
    public static final int BUBBLE_DEVELOPER_ERROR_REPORTED__ERROR__UNKNOWN = 0;
    public static final int BUBBLE_UICHANGED__ACTION__COLLAPSED = 4;
    public static final int BUBBLE_UICHANGED__ACTION__DISMISSED = 5;
    public static final int BUBBLE_UICHANGED__ACTION__EXPANDED = 3;
    public static final int BUBBLE_UICHANGED__ACTION__FLYOUT = 16;
    public static final int BUBBLE_UICHANGED__ACTION__HEADER_GO_TO_APP = 8;
    public static final int BUBBLE_UICHANGED__ACTION__HEADER_GO_TO_SETTINGS = 9;
    public static final int BUBBLE_UICHANGED__ACTION__PERMISSION_DIALOG_SHOWN = 12;
    public static final int BUBBLE_UICHANGED__ACTION__PERMISSION_OPT_IN = 10;
    public static final int BUBBLE_UICHANGED__ACTION__PERMISSION_OPT_OUT = 11;
    public static final int BUBBLE_UICHANGED__ACTION__POSTED = 1;
    public static final int BUBBLE_UICHANGED__ACTION__STACK_DISMISSED = 6;
    public static final int BUBBLE_UICHANGED__ACTION__STACK_EXPANDED = 15;
    public static final int BUBBLE_UICHANGED__ACTION__STACK_MOVED = 7;
    public static final int BUBBLE_UICHANGED__ACTION__SWIPE_LEFT = 13;
    public static final int BUBBLE_UICHANGED__ACTION__SWIPE_RIGHT = 14;
    public static final int BUBBLE_UICHANGED__ACTION__UNKNOWN = 0;
    public static final int BUBBLE_UICHANGED__ACTION__UPDATED = 2;
    public static final int BUBBLE_UI_CHANGED = 149;
    public static final int BUILD_INFORMATION = 10044;
    public static final int BYTES_TRANSFER_BY_TAG_AND_METERED = 10083;
    public static final int CACHED_KILL_REPORTED = 17;
    public static final int CAMERA_ACTION_EVENT = 227;
    public static final int CAMERA_ACTION_EVENT__ACTION__CLOSE = 2;
    public static final int CAMERA_ACTION_EVENT__ACTION__OPEN = 1;
    public static final int CAMERA_ACTION_EVENT__ACTION__SESSION = 3;
    public static final int CAMERA_ACTION_EVENT__ACTION__UNKNOWN_ACTION = 0;
    public static final int CAMERA_ACTION_EVENT__FACING__BACK = 1;
    public static final int CAMERA_ACTION_EVENT__FACING__EXTERNAL = 3;
    public static final int CAMERA_ACTION_EVENT__FACING__FRONT = 2;
    public static final int CAMERA_ACTION_EVENT__FACING__UNKNOWN = 0;
    public static final int CAMERA_STATE_CHANGED = 25;
    public static final int CAMERA_STATE_CHANGED__STATE__OFF = 0;
    public static final int CAMERA_STATE_CHANGED__STATE__ON = 1;
    public static final int CAMERA_STATE_CHANGED__STATE__RESET = 2;
    public static final int CATEGORY_SIZE = 10028;
    public static final int CATEGORY_SIZE__CATEGORY__APP_CACHE_SIZE = 3;
    public static final int CATEGORY_SIZE__CATEGORY__APP_DATA_SIZE = 2;
    public static final int CATEGORY_SIZE__CATEGORY__APP_SIZE = 1;
    public static final int CATEGORY_SIZE__CATEGORY__AUDIO = 6;
    public static final int CATEGORY_SIZE__CATEGORY__DOWNLOADS = 7;
    public static final int CATEGORY_SIZE__CATEGORY__OTHER = 9;
    public static final int CATEGORY_SIZE__CATEGORY__PHOTOS = 4;
    public static final int CATEGORY_SIZE__CATEGORY__SYSTEM = 8;
    public static final int CATEGORY_SIZE__CATEGORY__UNKNOWN = 0;
    public static final int CATEGORY_SIZE__CATEGORY__VIDEOS = 5;
    public static final int CHARGING_STATE_CHANGED = 31;
    public static final int CHARGING_STATE_CHANGED__STATE__BATTERY_STATUS_CHARGING = 2;
    public static final int CHARGING_STATE_CHANGED__STATE__BATTERY_STATUS_DISCHARGING = 3;
    public static final int CHARGING_STATE_CHANGED__STATE__BATTERY_STATUS_FULL = 5;
    public static final int CHARGING_STATE_CHANGED__STATE__BATTERY_STATUS_INVALID = 0;
    public static final int CHARGING_STATE_CHANGED__STATE__BATTERY_STATUS_NOT_CHARGING = 4;
    public static final int CHARGING_STATE_CHANGED__STATE__BATTERY_STATUS_UNKNOWN = 1;
    public static final int CONNECTIVITY_STATE_CHANGED = 98;
    public static final int CONNECTIVITY_STATE_CHANGED__STATE__CONNECTED = 1;
    public static final int CONNECTIVITY_STATE_CHANGED__STATE__DISCONNECTED = 2;
    public static final int CONNECTIVITY_STATE_CHANGED__STATE__UNKNOWN = 0;
    public static final int CONTENT_CAPTURE_CALLER_MISMATCH_REPORTED = 206;
    public static final int CONTENT_CAPTURE_FLUSHED = 209;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS = 207;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__ACCEPT_DATA_SHARE_REQUEST = 7;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__DATA_SHARE_ERROR_CLIENT_PIPE_FAIL = 12;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__DATA_SHARE_ERROR_CONCURRENT_REQUEST = 14;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__DATA_SHARE_ERROR_EMPTY_DATA = 11;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__DATA_SHARE_ERROR_IOEXCEPTION = 10;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__DATA_SHARE_ERROR_SERVICE_PIPE_FAIL = 13;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__DATA_SHARE_ERROR_TIMEOUT_INTERRUPTED = 15;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__DATA_SHARE_WRITE_FINISHED = 9;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__ON_CONNECTED = 1;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__ON_DATA_SHARE_REQUEST = 6;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__ON_DISCONNECTED = 2;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__ON_USER_DATA_REMOVED = 5;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__REJECT_DATA_SHARE_REQUEST = 8;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__SET_DISABLED = 4;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__SET_WHITELIST = 3;
    public static final int CONTENT_CAPTURE_SERVICE_EVENTS__EVENT__UNKNOWN = 0;
    public static final int CONTENT_CAPTURE_SESSION_EVENTS = 208;
    public static final int CONTENT_CAPTURE_SESSION_EVENTS__EVENT__ON_SESSION_FINISHED = 2;
    public static final int CONTENT_CAPTURE_SESSION_EVENTS__EVENT__ON_SESSION_STARTED = 1;
    public static final int CONTENT_CAPTURE_SESSION_EVENTS__EVENT__SESSION_NOT_CREATED = 3;
    public static final int CONTENT_CAPTURE_SESSION_EVENTS__EVENT__UNKNOWN = 0;
    public static final int COOLING_DEVICE = 10059;
    public static final int COOLING_DEVICE__DEVICE_LOCATION__BATTERY = 1;
    public static final int COOLING_DEVICE__DEVICE_LOCATION__COMPONENT = 6;
    public static final int COOLING_DEVICE__DEVICE_LOCATION__CPU = 2;
    public static final int COOLING_DEVICE__DEVICE_LOCATION__FAN = 0;
    public static final int COOLING_DEVICE__DEVICE_LOCATION__GPU = 3;
    public static final int COOLING_DEVICE__DEVICE_LOCATION__MODEM = 4;
    public static final int COOLING_DEVICE__DEVICE_LOCATION__NPU = 5;
    public static final int CPU_ACTIVE_TIME = 10016;
    public static final int CPU_CLUSTER_TIME = 10017;
    public static final int CPU_CYCLES_PER_THREAD_GROUP_CLUSTER = 10098;
    public static final int CPU_CYCLES_PER_THREAD_GROUP_CLUSTER__THREAD_GROUP__SURFACE_FLINGER = 3;
    public static final int CPU_CYCLES_PER_THREAD_GROUP_CLUSTER__THREAD_GROUP__SYSTEM_SERVER = 2;
    public static final int CPU_CYCLES_PER_THREAD_GROUP_CLUSTER__THREAD_GROUP__SYSTEM_SERVER_BINDER = 1;
    public static final int CPU_CYCLES_PER_THREAD_GROUP_CLUSTER__THREAD_GROUP__UNKNOWN_THREAD_GROUP = 0;
    public static final int CPU_CYCLES_PER_UID_CLUSTER = 10096;
    public static final int CPU_TIME_PER_CLUSTER_FREQ = 10095;
    public static final int CPU_TIME_PER_THREAD_FREQ = 10037;
    public static final int CPU_TIME_PER_UID = 10009;
    public static final int CPU_TIME_PER_UID_FREQ = 10010;
    public static final int DANGEROUS_PERMISSION_STATE = 10050;
    public static final int DANGEROUS_PERMISSION_STATE_SAMPLED = 10067;
    public static final int DATA_USAGE_BYTES_TRANSFER = 10082;
    public static final int DATA_USAGE_BYTES_TRANSFER__OPPORTUNISTIC_DATA_SUB__ALL = 1;
    public static final int DATA_USAGE_BYTES_TRANSFER__OPPORTUNISTIC_DATA_SUB__NOT_OPPORTUNISTIC = 3;
    public static final int DATA_USAGE_BYTES_TRANSFER__OPPORTUNISTIC_DATA_SUB__OPPORTUNISTIC = 2;
    public static final int DATA_USAGE_BYTES_TRANSFER__OPPORTUNISTIC_DATA_SUB__UNKNOWN = 0;
    public static final int DEBUG_ELAPSED_CLOCK = 10046;
    public static final int DEBUG_ELAPSED_CLOCK__TYPE__ALWAYS_PRESENT = 1;
    public static final int DEBUG_ELAPSED_CLOCK__TYPE__PRESENT_ON_ODD_PULLS = 2;
    public static final int DEBUG_ELAPSED_CLOCK__TYPE__TYPE_UNKNOWN = 0;
    public static final int DEBUG_FAILING_ELAPSED_CLOCK = 10047;
    public static final int DEFERRED_JOB_STATS_REPORTED = 85;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER = 10041;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__AMBIENT_DISPLAY = 0;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__BLUETOOTH = 2;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__CAMERA = 3;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__CELL = 4;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__FLASHLIGHT = 5;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__IDLE = 6;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__MEMORY = 7;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__OVERCOUNTED = 8;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__PHONE = 9;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__SCREEN = 10;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__UNACCOUNTED = 11;
    public static final int DEVICE_CALCULATED_POWER_BLAME_OTHER__DRAIN_TYPE__WIFI = 13;
    public static final int DEVICE_CALCULATED_POWER_BLAME_UID = 10040;
    public static final int DEVICE_CALCULATED_POWER_USE = 10039;
    public static final int DEVICE_IDLE_MODE_STATE_CHANGED = 21;
    public static final int DEVICE_IDLE_MODE_STATE_CHANGED__STATE__DEVICE_IDLE_MODE_DEEP = 2;
    public static final int DEVICE_IDLE_MODE_STATE_CHANGED__STATE__DEVICE_IDLE_MODE_LIGHT = 1;
    public static final int DEVICE_IDLE_MODE_STATE_CHANGED__STATE__DEVICE_IDLE_MODE_OFF = 0;
    public static final int DEVICE_IDLING_MODE_STATE_CHANGED = 22;
    public static final int DEVICE_IDLING_MODE_STATE_CHANGED__STATE__DEVICE_IDLE_MODE_DEEP = 2;
    public static final int DEVICE_IDLING_MODE_STATE_CHANGED__STATE__DEVICE_IDLE_MODE_LIGHT = 1;
    public static final int DEVICE_IDLING_MODE_STATE_CHANGED__STATE__DEVICE_IDLE_MODE_OFF = 0;
    public static final int DEVICE_POLICY_EVENT = 103;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ADD_ACCOUNT = 202;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ADD_ACCOUNT_EXPLICITLY = 203;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ADD_CROSS_PROFILE_INTENT_FILTER = 48;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ADD_CROSS_PROFILE_WIDGET_PROVIDER = 49;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ADD_PERSISTENT_PREFERRED_ACTIVITY = 52;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ADD_USER_RESTRICTION = 12;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ALLOW_MODIFICATION_OF_ADMIN_CONFIGURED_NETWORKS = 132;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__BIND_CROSS_PROFILE_SERVICE = 151;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CAN_INTERACT_ACROSS_PROFILES_FALSE_NO_PROFILES = 147;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CAN_INTERACT_ACROSS_PROFILES_FALSE_PERMISSION = 146;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CAN_INTERACT_ACROSS_PROFILES_TRUE = 145;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CAN_REQUEST_INTERACT_ACROSS_PROFILES_FALSE_NO_PROFILES = 142;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CAN_REQUEST_INTERACT_ACROSS_PROFILES_FALSE_PERMISSION = 144;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CAN_REQUEST_INTERACT_ACROSS_PROFILES_FALSE_WHITELIST = 143;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CAN_REQUEST_INTERACT_ACROSS_PROFILES_TRUE = 141;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CHOOSE_PRIVATE_KEY_ALIAS = 22;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__COMP_TO_ORG_OWNED_PO_MIGRATED = 137;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREATE_CROSS_PROFILE_INTENT = 148;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_CREDENTIAL_FOUND_IN_POLICY = 183;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_GENERATE_KEY_PAIR_FAILED = 185;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_INSTALL_KEY_PAIR_FAILED = 184;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_POLICY_LOOKUP_FAILED = 186;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_REMOVED = 187;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_REQUEST_ACCEPTED = 180;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_REQUEST_DENIED = 181;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_REQUEST_FAILED = 182;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_REQUEST_NAME = 178;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_REQUEST_POLICY = 179;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_APPS_GET_TARGET_USER_PROFILES = 125;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_APPS_START_ACTIVITY_AS_USER = 126;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_ADMIN_RESTRICTED = 164;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_INSTALL_BANNER_CLICKED = 168;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_INSTALL_BANNER_NO_INTENT_CLICKED = 169;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_LAUNCHED_FROM_APP = 162;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_LAUNCHED_FROM_SETTINGS = 163;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_MISSING_INSTALL_BANNER_INTENT = 167;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_MISSING_PERSONAL_APP = 166;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_MISSING_WORK_APP = 165;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_PERMISSION_REVOKED = 172;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_USER_CONSENTED = 170;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__CROSS_PROFILE_SETTINGS_PAGE_USER_DECLINED_CONSENT = 171;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__DOCSUI_EMPTY_STATE_NO_PERMISSION = 173;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__DOCSUI_EMPTY_STATE_QUIET_MODE = 174;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__DOCSUI_LAUNCH_OTHER_APP = 175;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__DOCSUI_PICK_RESULT = 176;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__DO_USER_INFO_CLICKED = 57;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ENABLE_SYSTEM_APP = 64;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ENABLE_SYSTEM_APP_WITH_INTENT = 65;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ESTABLISH_VPN = 118;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__GENERATE_KEY_PAIR = 59;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__GET_ACCOUNT_AUTH_TOKEN = 204;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__GET_CROSS_PROFILE_PACKAGES = 140;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__GET_USER_PASSWORD_COMPLEXITY_LEVEL = 72;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__GET_WIFI_MAC_ADDRESS = 54;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__INSTALL_CA_CERT = 21;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__INSTALL_EXISTING_PACKAGE = 66;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__INSTALL_KEY_PAIR = 20;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__INSTALL_PACKAGE = 112;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__INSTALL_SYSTEM_UPDATE = 73;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__INSTALL_SYSTEM_UPDATE_ERROR = 74;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__IS_ACTIVE_PASSWORD_SUFFICIENT_FOR_DEVICE = 189;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__IS_MANAGED_KIOSK = 75;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__IS_MANAGED_PROFILE = 149;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__IS_UNATTENDED_MANAGED_KIOSK = 76;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__LOCK_NOW = 10;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__ON_LOCK_TASK_MODE_ENTERING = 69;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PLATFORM_PROVISIONING_COPY_ACCOUNT_MS = 190;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PLATFORM_PROVISIONING_COPY_ACCOUNT_STATUS = 193;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PLATFORM_PROVISIONING_CREATE_PROFILE_MS = 191;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PLATFORM_PROVISIONING_ERROR = 194;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PLATFORM_PROVISIONING_PARAM = 197;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PLATFORM_PROVISIONING_START_PROFILE_MS = 192;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_ACTION = 94;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_CANCELLED = 101;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_COPY_ACCOUNT_STATUS = 103;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_COPY_ACCOUNT_TASK_MS = 96;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_CREATE_PROFILE_TASK_MS = 97;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_DOWNLOAD_PACKAGE_TASK_MS = 99;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_DPC_INSTALLED_BY_PACKAGE = 85;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_DPC_PACKAGE_NAME = 84;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_DPC_SETUP_COMPLETED = 153;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_DPC_SETUP_STARTED = 152;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_ENCRYPT_DEVICE_ACTIVITY_TIME_MS = 88;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_ENTRY_POINT_ADB = 82;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_ENTRY_POINT_CLOUD_ENROLLMENT = 81;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_ENTRY_POINT_NFC = 79;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_ENTRY_POINT_QR_CODE = 80;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_ENTRY_POINT_TRUSTED_SOURCE = 83;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_ERROR = 102;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_EXTRAS = 95;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_FINALIZATION_ACTIVITY_TIME_MS = 92;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_FLOW_TYPE = 124;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_INSTALL_PACKAGE_TASK_MS = 100;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_IS_LANDSCAPE = 200;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_IS_NIGHT_MODE = 201;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_MANAGED_PROFILE_ON_FULLY_MANAGED_DEVICE = 77;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_NETWORK_TYPE = 93;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_ORGANIZATION_OWNED_MANAGED_PROFILE = 154;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_PERSISTENT_DEVICE_OWNER = 78;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_POST_ENCRYPTION_ACTIVITY_TIME_MS = 91;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_PREPARE_COMPLETED = 123;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_PREPARE_STARTED = 122;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_PREPARE_TOTAL_TIME_MS = 121;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_PREPROVISIONING_ACTIVITY_TIME_MS = 87;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_PROVISIONING_ACTIVITY_TIME_MS = 86;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_PROVISION_FULLY_MANAGED_DEVICE_TASK_MS = 196;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_PROVISION_MANAGED_PROFILE_TASK_MS = 195;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_SESSION_COMPLETED = 106;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_SESSION_STARTED = 105;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_START_PROFILE_TASK_MS = 98;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_TERMS_ACTIVITY_TIME_MS = 107;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_TERMS_COUNT = 108;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_TERMS_READ = 109;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_TOTAL_TASK_TIME_MS = 104;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_TRAMPOLINE_ACTIVITY_TIME_MS = 90;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__PROVISIONING_WEB_ACTIVITY_TIME_MS = 89;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__QUERY_DETAILS = 33;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__QUERY_SUMMARY = 32;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__QUERY_SUMMARY_FOR_DEVICE = 116;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__QUERY_SUMMARY_FOR_USER = 31;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__REBOOT = 34;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__REMOVE_CROSS_PROFILE_WIDGET_PROVIDER = 117;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__REMOVE_KEY_PAIR = 23;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__REMOVE_USER_RESTRICTION = 13;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__REQUEST_BUGREPORT = 53;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__REQUEST_QUIET_MODE_ENABLED = 55;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RESET_PASSWORD = 205;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RESET_PASSWORD_WITH_TOKEN = 206;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RESOLVER_AUTOLAUNCH_CROSS_PROFILE_TARGET = 161;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RESOLVER_CROSS_PROFILE_TARGET_OPENED = 155;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RESOLVER_EMPTY_STATE_NO_APPS_RESOLVED = 160;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RESOLVER_EMPTY_STATE_NO_SHARING_TO_PERSONAL = 158;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RESOLVER_EMPTY_STATE_NO_SHARING_TO_WORK = 159;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RESOLVER_EMPTY_STATE_WORK_APPS_DISABLED = 157;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RESOLVER_SWITCH_TABS = 156;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RETRIEVE_NETWORK_LOGS = 120;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RETRIEVE_PRE_REBOOT_SECURITY_LOGS = 17;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__RETRIEVE_SECURITY_LOGS = 16;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SEPARATE_PROFILE_CHALLENGE_CHANGED = 110;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_ALWAYS_ON_VPN_PACKAGE = 26;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_APPLICATION_HIDDEN = 63;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_APPLICATION_RESTRICTIONS = 62;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_AUTO_TIME = 127;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_AUTO_TIME_REQUIRED = 36;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_AUTO_TIME_ZONE = 128;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_BLUETOOTH_CONTACT_SHARING_DISABLED = 47;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_CAMERA_DISABLED = 30;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_CERT_INSTALLER_PACKAGE = 25;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_COMMON_CRITERIA_MODE = 131;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_CROSS_PROFILE_CALENDAR_PACKAGES = 70;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_CROSS_PROFILE_CALLER_ID_DISABLED = 46;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_CROSS_PROFILE_CONTACTS_SEARCH_DISABLED = 45;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_CROSS_PROFILE_PACKAGES = 138;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_DEVICE_OWNER_LOCK_SCREEN_INFO = 42;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_FACTORY_RESET_PROTECTION = 130;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_GLOBAL_SETTING = 111;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_INTERACT_ACROSS_PROFILES_APP_OP = 139;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_KEEP_UNINSTALLED_PACKAGES = 61;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_KEYGUARD_DISABLED = 37;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_KEYGUARD_DISABLED_FEATURES = 9;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_KEY_PAIR_CERTIFICATE = 60;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_LOCKTASK_MODE_ENABLED = 51;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_LONG_SUPPORT_MESSAGE = 44;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF = 136;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_MASTER_VOLUME_MUTED = 35;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_NETWORK_LOGGING_ENABLED = 119;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_ORGANIZATION_COLOR = 39;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_ORGANIZATION_ID = 188;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PACKAGES_SUSPENDED = 68;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PASSWORD_COMPLEXITY = 177;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PASSWORD_MINIMUM_LENGTH = 2;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PASSWORD_MINIMUM_LETTERS = 5;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PASSWORD_MINIMUM_LOWER_CASE = 6;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PASSWORD_MINIMUM_NON_LETTER = 4;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PASSWORD_MINIMUM_NUMERIC = 3;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PASSWORD_MINIMUM_SYMBOLS = 8;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PASSWORD_MINIMUM_UPPER_CASE = 7;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PASSWORD_QUALITY = 1;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PERMISSION_GRANT_STATE = 19;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PERMISSION_POLICY = 18;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PERMITTED_ACCESSIBILITY_SERVICES = 28;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PERMITTED_INPUT_METHODS = 27;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PERSONAL_APPS_SUSPENDED = 135;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PREFERENTIAL_NETWORK_SERVICE_ENABLED = 199;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_PROFILE_NAME = 40;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_SCREEN_CAPTURE_DISABLED = 29;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_SECURE_SETTING = 14;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_SECURITY_LOGGING_ENABLED = 15;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_SHORT_SUPPORT_MESSAGE = 43;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_STATUS_BAR_DISABLED = 38;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_SYSTEM_UPDATE_POLICY = 50;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_TIME = 133;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_TIME_ZONE = 134;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_UNINSTALL_BLOCKED = 67;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_USB_DATA_SIGNALING = 198;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_USER_CONTROL_DISABLED_PACKAGES = 129;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__SET_USER_ICON = 41;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__START_ACTIVITY_BY_INTENT = 150;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__TRANSFER_OWNERSHIP = 58;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__UNINSTALL_CA_CERTS = 24;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__UNINSTALL_PACKAGE = 113;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__WIFI_SERVICE_ADD_NETWORK_SUGGESTIONS = 114;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__WIFI_SERVICE_ADD_OR_UPDATE_NETWORK = 115;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__WIPE_DATA_WITH_REASON = 11;
    public static final int DEVICE_POLICY_EVENT__EVENT_ID__WORK_PROFILE_LOCATION_CHANGED = 56;
    public static final int DEVICE_ROTATED = 333;
    public static final int DEVICE_ROTATED_DATA = 10097;
    public static final int DEVICE_ROTATED_DATA__PROPOSED_ORIENTATION__ROTATION_0 = 1;
    public static final int DEVICE_ROTATED_DATA__PROPOSED_ORIENTATION__ROTATION_180 = 3;
    public static final int DEVICE_ROTATED_DATA__PROPOSED_ORIENTATION__ROTATION_270 = 4;
    public static final int DEVICE_ROTATED_DATA__PROPOSED_ORIENTATION__ROTATION_90 = 2;
    public static final int DEVICE_ROTATED_DATA__PROPOSED_ORIENTATION__UNKNOWN = 0;
    public static final int DEVICE_ROTATED__PROPOSED_ORIENTATION__ROTATION_0 = 1;
    public static final int DEVICE_ROTATED__PROPOSED_ORIENTATION__ROTATION_180 = 3;
    public static final int DEVICE_ROTATED__PROPOSED_ORIENTATION__ROTATION_270 = 4;
    public static final int DEVICE_ROTATED__PROPOSED_ORIENTATION__ROTATION_90 = 2;
    public static final int DEVICE_ROTATED__PROPOSED_ORIENTATION__UNKNOWN = 0;
    public static final int DEVICE_ROTATED__ROTATION_EVENT_TYPE__ACTUAL_EVENT = 2;
    public static final int DEVICE_ROTATED__ROTATION_EVENT_TYPE__DATA_READY = 3;
    public static final int DEVICE_ROTATED__ROTATION_EVENT_TYPE__PREINDICATION = 1;
    public static final int DEVICE_ROTATED__ROTATION_EVENT_TYPE__UNKNOWN = 0;
    public static final int DEVICE_STATE_CHANGED = 350;
    public static final int DIRECTORY_USAGE = 10026;
    public static final int DIRECTORY_USAGE__DIRECTORY__CACHE = 2;
    public static final int DIRECTORY_USAGE__DIRECTORY__DATA = 1;
    public static final int DIRECTORY_USAGE__DIRECTORY__SYSTEM = 3;
    public static final int DIRECTORY_USAGE__DIRECTORY__UNKNOWN = 0;
    public static final int DISK_IO = 10032;
    public static final int DISK_STATS = 10025;
    public static final int DISPLAY_WAKE_REPORTED = 282;
    public static final int DNDMODE_PROTO__ZEN_MODE__ROOT_CONFIG = -1;
    public static final int DNDMODE_PROTO__ZEN_MODE__ZEN_MODE_ALARMS = 3;
    public static final int DNDMODE_PROTO__ZEN_MODE__ZEN_MODE_IMPORTANT_INTERRUPTIONS = 1;
    public static final int DNDMODE_PROTO__ZEN_MODE__ZEN_MODE_NO_INTERRUPTIONS = 2;
    public static final int DNDMODE_PROTO__ZEN_MODE__ZEN_MODE_OFF = 0;
    public static final int DND_MODE_RULE = 10084;
    public static final int EXCESSIVE_CPU_USAGE_REPORTED = 16;
    public static final int EXCLUSION_RECT_STATE_CHANGED = 223;
    public static final int EXCLUSION_RECT_STATE_CHANGED__X_LOCATION__DEFAULT_LOCATION = 0;
    public static final int EXCLUSION_RECT_STATE_CHANGED__X_LOCATION__LEFT = 1;
    public static final int EXCLUSION_RECT_STATE_CHANGED__X_LOCATION__RIGHT = 2;
    public static final int EXTERNAL_STORAGE_INFO = 10053;
    public static final int EXTERNAL_STORAGE_INFO__STORAGE_TYPE__OTHER = 3;
    public static final int EXTERNAL_STORAGE_INFO__STORAGE_TYPE__SD_CARD = 1;
    public static final int EXTERNAL_STORAGE_INFO__STORAGE_TYPE__UNKNOWN = 0;
    public static final int EXTERNAL_STORAGE_INFO__STORAGE_TYPE__USB = 2;
    public static final int EXTERNAL_STORAGE_INFO__VOLUME_TYPE__OTHER = 3;
    public static final int EXTERNAL_STORAGE_INFO__VOLUME_TYPE__PRIVATE = 2;
    public static final int EXTERNAL_STORAGE_INFO__VOLUME_TYPE__PUBLIC = 1;
    public static final int EXTERNAL_STORAGE_INFO__VOLUME_TYPE__UNKNOWN = 0;
    public static final int FACE_DOWN_REPORTED = 337;
    public static final int FACE_DOWN_REPORTED__FACE_DOWN_RESPONSE__SCREEN_OFF = 4;
    public static final int FACE_DOWN_REPORTED__FACE_DOWN_RESPONSE__UNFLIP = 2;
    public static final int FACE_DOWN_REPORTED__FACE_DOWN_RESPONSE__UNKNOWN = 1;
    public static final int FACE_DOWN_REPORTED__FACE_DOWN_RESPONSE__USER_INTERACTION = 3;
    public static final int FACE_SETTINGS = 10058;
    public static final int FDTRACK_EVENT_OCCURRED = 364;
    public static final int FDTRACK_EVENT_OCCURRED__EVENT__ABORTING = 3;
    public static final int FDTRACK_EVENT_OCCURRED__EVENT__DISABLED = 1;
    public static final int FDTRACK_EVENT_OCCURRED__EVENT__ENABLED = 2;
    public static final int FLAG_FLIP_UPDATE_OCCURRED = 101;
    public static final int FLASHLIGHT_STATE_CHANGED = 26;
    public static final int FLASHLIGHT_STATE_CHANGED__STATE__OFF = 0;
    public static final int FLASHLIGHT_STATE_CHANGED__STATE__ON = 1;
    public static final int FLASHLIGHT_STATE_CHANGED__STATE__RESET = 2;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED = 256;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_MODE__MODE_ALLOWED = 1;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_MODE__MODE_FOREGROUND = 3;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_MODE__MODE_IGNORED = 2;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_MODE__MODE_UNKNOWN = 0;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ACCEPT_HANDOVER = 74;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ACCESS_ACCESSIBILITY = 88;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ACCESS_MEDIA_LOCATION = 90;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ACCESS_NOTIFICATIONS = 25;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ACTIVATE_PLATFORM_VPN = 94;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ACTIVATE_VPN = 47;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ACTIVITY_RECOGNITION = 79;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ACTIVITY_RECOGNITION_SOURCE = 113;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ADD_VOICEMAIL = 52;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ANSWER_PHONE_CALLS = 69;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ASSIST_SCREENSHOT = 50;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_ASSIST_STRUCTURE = 49;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUDIO_ALARM_VOLUME = 37;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUDIO_BLUETOOTH_VOLUME = 39;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUDIO_MASTER_VOLUME = 33;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUDIO_MEDIA_VOLUME = 36;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUDIO_NOTIFICATION_VOLUME = 38;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUDIO_RING_VOLUME = 35;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUDIO_VOICE_VOLUME = 34;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUTO_REVOKE_MANAGED_BY_INSTALLER = 98;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_AUTO_REVOKE_PERMISSIONS_IF_UNUSED = 97;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_BIND_ACCESSIBILITY_SERVICE = 73;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_BLUETOOTH_ADVERTISE = 114;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_BLUETOOTH_CONNECT = 111;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_BLUETOOTH_SCAN = 77;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_BODY_SENSORS = 56;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_CALL_PHONE = 13;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_CAMERA = 26;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_CHANGE_WIFI_STATE = 71;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_COARSE_LOCATION = 0;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_COARSE_LOCATION_SOURCE = 109;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_DEPRECATED_1 = 96;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_FINE_LOCATION = 1;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_FINE_LOCATION_SOURCE = 108;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_GET_ACCOUNTS = 62;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_GET_USAGE_STATS = 43;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_GPS = 2;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_INSTANT_APP_START_FOREGROUND = 68;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_INTERACT_ACROSS_PROFILES = 93;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_LEGACY_STORAGE = 87;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_LOADER_USAGE_STATS = 95;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_MANAGE_CREDENTIALS = 104;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_MANAGE_EXTERNAL_STORAGE = 92;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_MANAGE_IPSEC_TUNNELS = 75;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_MANAGE_MEDIA = 110;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_MANAGE_ONGOING_CALLS = 103;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_MOCK_LOCATION = 58;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_MONITOR_HIGH_POWER_LOCATION = 42;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_MONITOR_LOCATION = 41;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_MUTE_MICROPHONE = 44;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_NEIGHBORING_CELLS = 12;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_NONE = -1;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_NO_ISOLATED_STORAGE = 99;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_PHONE_CALL_CAMERA = 101;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_PHONE_CALL_MICROPHONE = 100;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_PICTURE_IN_PICTURE = 67;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_PLAY_AUDIO = 28;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_POST_NOTIFICATION = 11;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_PROCESS_OUTGOING_CALLS = 54;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_PROJECT_MEDIA = 46;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_QUERY_ALL_PACKAGES = 91;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_CALENDAR = 8;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_CALL_LOG = 6;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_CELL_BROADCASTS = 57;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_CLIPBOARD = 29;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_CONTACTS = 4;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_DEVICE_IDENTIFIERS = 89;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_EXTERNAL_STORAGE = 59;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_ICC_SMS = 21;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_MEDIA_AUDIO = 81;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_MEDIA_IMAGES = 85;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_MEDIA_VIDEO = 83;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_PHONE_NUMBERS = 65;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_PHONE_STATE = 51;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_READ_SMS = 14;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RECEIVE_EMERGENCY_SMS = 17;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RECEIVE_MMS = 18;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RECEIVE_SMS = 16;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RECEIVE_WAP_PUSH = 19;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RECORD_AUDIO = 27;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RECORD_AUDIO_HOTWORD = 102;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RECORD_AUDIO_OUTPUT = 106;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RECORD_INCOMING_PHONE_AUDIO = 115;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_REQUEST_DELETE_PACKAGES = 72;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_REQUEST_INSTALL_PACKAGES = 66;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RUN_ANY_IN_BACKGROUND = 70;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_RUN_IN_BACKGROUND = 63;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_SCHEDULE_EXACT_ALARM = 107;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_SEND_SMS = 20;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_SMS_FINANCIAL_TRANSACTIONS = 80;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_START_FOREGROUND = 76;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_TAKE_AUDIO_FOCUS = 32;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_TAKE_MEDIA_BUTTONS = 31;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_TOAST_WINDOW = 45;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_TURN_SCREEN_ON = 61;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_USE_BIOMETRIC = 78;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_USE_FINGERPRINT = 55;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER = 105;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_USE_SIP = 53;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_UWB_RANGING = 112;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_VIBRATE = 3;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WAKE_LOCK = 40;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WIFI_SCAN = 10;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_CALENDAR = 9;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_CALL_LOG = 7;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_CLIPBOARD = 30;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_CONTACTS = 5;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_EXTERNAL_STORAGE = 60;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_ICC_SMS = 22;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_MEDIA_AUDIO = 82;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_MEDIA_IMAGES = 86;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_MEDIA_VIDEO = 84;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_SETTINGS = 23;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_SMS = 15;
    public static final int FOREGROUND_SERVICE_APP_OP_SESSION_ENDED__APP_OP_NAME__APP_OP_WRITE_WALLPAPER = 48;
    public static final int FOREGROUND_SERVICE_STATE_CHANGED = 60;
    public static final int FOREGROUND_SERVICE_STATE_CHANGED__STATE__DENIED = 3;
    public static final int FOREGROUND_SERVICE_STATE_CHANGED__STATE__ENTER = 1;
    public static final int FOREGROUND_SERVICE_STATE_CHANGED__STATE__EXIT = 2;
    public static final int FULL_BATTERY_CAPACITY = 10020;
    public static final int GLOBAL_HIBERNATED_APPS = 10109;
    public static final int GNSS_CONFIGURATION_REPORTED = 132;
    public static final int GNSS_CONFIGURATION_REPORTED__A_GLONASS_POS_PROTOCOL_SELECT__LPP_UPLANE = 4;
    public static final int GNSS_CONFIGURATION_REPORTED__A_GLONASS_POS_PROTOCOL_SELECT__RRC_CPLANE = 1;
    public static final int GNSS_CONFIGURATION_REPORTED__A_GLONASS_POS_PROTOCOL_SELECT__RRLP_CPLANE = 2;
    public static final int GNSS_CONFIGURATION_REPORTED__GPS_LOCK__MO = 1;
    public static final int GNSS_CONFIGURATION_REPORTED__GPS_LOCK__NI = 2;
    public static final int GNSS_CONFIGURATION_REPORTED__LPP_PROFILE__CONTROL_PLANE = 2;
    public static final int GNSS_CONFIGURATION_REPORTED__LPP_PROFILE__USER_PLANE = 1;
    public static final int GNSS_CONFIGURATION_REPORTED__SUPL_MODE__MSA = 2;
    public static final int GNSS_CONFIGURATION_REPORTED__SUPL_MODE__MSB = 1;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED = 131;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__PROTOCOL_STACK__CTRL_PLANE = 0;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__PROTOCOL_STACK__IMS = 10;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__PROTOCOL_STACK__OTHER_PROTOCOL_STACK = 100;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__PROTOCOL_STACK__SIM = 11;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__PROTOCOL_STACK__SUPL = 1;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__REQUESTOR__AUTOMOBILE_CLIENT = 20;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__REQUESTOR__CARRIER = 0;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__REQUESTOR__GNSS_CHIPSET_VENDOR = 12;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__REQUESTOR__MODEM_CHIPSET_VENDOR = 11;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__REQUESTOR__OEM = 10;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__REQUESTOR__OTHER_CHIPSET_VENDOR = 13;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__REQUESTOR__OTHER_REQUESTOR = 100;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__RESPONSE_TYPE__ACCEPTED_LOCATION_PROVIDED = 2;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__RESPONSE_TYPE__ACCEPTED_NO_LOCATION_PROVIDED = 1;
    public static final int GNSS_NFW_NOTIFICATION_REPORTED__RESPONSE_TYPE__REJECTED = 0;
    public static final int GNSS_NI_EVENT_REPORTED = 124;
    public static final int GNSS_NI_EVENT_REPORTED__DEFAULT_RESPONSE__RESPONSE_ACCEPT = 1;
    public static final int GNSS_NI_EVENT_REPORTED__DEFAULT_RESPONSE__RESPONSE_DENY = 2;
    public static final int GNSS_NI_EVENT_REPORTED__DEFAULT_RESPONSE__RESPONSE_NORESP = 3;
    public static final int GNSS_NI_EVENT_REPORTED__EVENT_TYPE__NI_REQUEST = 1;
    public static final int GNSS_NI_EVENT_REPORTED__EVENT_TYPE__NI_RESPONSE = 2;
    public static final int GNSS_NI_EVENT_REPORTED__EVENT_TYPE__UNKNOWN = 0;
    public static final int GNSS_NI_EVENT_REPORTED__NI_TYPE__EMERGENCY_SUPL = 4;
    public static final int GNSS_NI_EVENT_REPORTED__NI_TYPE__UMTS_CTRL_PLANE = 3;
    public static final int GNSS_NI_EVENT_REPORTED__NI_TYPE__UMTS_SUPL = 2;
    public static final int GNSS_NI_EVENT_REPORTED__NI_TYPE__VOICE = 1;
    public static final int GNSS_NI_EVENT_REPORTED__REQUESTOR_ID_ENCODING__ENC_NONE = 0;
    public static final int GNSS_NI_EVENT_REPORTED__REQUESTOR_ID_ENCODING__ENC_SUPL_GSM_DEFAULT = 1;
    public static final int GNSS_NI_EVENT_REPORTED__REQUESTOR_ID_ENCODING__ENC_SUPL_UCS2 = 3;
    public static final int GNSS_NI_EVENT_REPORTED__REQUESTOR_ID_ENCODING__ENC_SUPL_UTF8 = 2;
    public static final int GNSS_NI_EVENT_REPORTED__REQUESTOR_ID_ENCODING__ENC_UNKNOWN = -1;
    public static final int GNSS_NI_EVENT_REPORTED__TEXT_ENCODING__ENC_NONE = 0;
    public static final int GNSS_NI_EVENT_REPORTED__TEXT_ENCODING__ENC_SUPL_GSM_DEFAULT = 1;
    public static final int GNSS_NI_EVENT_REPORTED__TEXT_ENCODING__ENC_SUPL_UCS2 = 3;
    public static final int GNSS_NI_EVENT_REPORTED__TEXT_ENCODING__ENC_SUPL_UTF8 = 2;
    public static final int GNSS_NI_EVENT_REPORTED__TEXT_ENCODING__ENC_UNKNOWN = -1;
    public static final int GNSS_NI_EVENT_REPORTED__USER_RESPONSE__RESPONSE_ACCEPT = 1;
    public static final int GNSS_NI_EVENT_REPORTED__USER_RESPONSE__RESPONSE_DENY = 2;
    public static final int GNSS_NI_EVENT_REPORTED__USER_RESPONSE__RESPONSE_NORESP = 3;
    public static final int GNSS_POWER_STATS = 10101;
    public static final int GNSS_STATS = 10074;
    public static final int GPS_SCAN_STATE_CHANGED = 6;
    public static final int GPS_SCAN_STATE_CHANGED__STATE__OFF = 0;
    public static final int GPS_SCAN_STATE_CHANGED__STATE__ON = 1;
    public static final int GPS_SIGNAL_QUALITY_CHANGED = 69;
    public static final int GPS_SIGNAL_QUALITY_CHANGED__LEVEL__GPS_SIGNAL_QUALITY_GOOD = 1;
    public static final int GPS_SIGNAL_QUALITY_CHANGED__LEVEL__GPS_SIGNAL_QUALITY_POOR = 0;
    public static final int GPS_SIGNAL_QUALITY_CHANGED__LEVEL__GPS_SIGNAL_QUALITY_UNKNOWN = -1;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED = 309;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__AUDIO_SYSTEM = 5;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__LOGICAL_ADDRESS_UNKNOWN = -1;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__PLAYBACK_DEVICE_1 = 4;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__PLAYBACK_DEVICE_2 = 8;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__PLAYBACK_DEVICE_3 = 11;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__RECORDING_DEVICE_1 = 1;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__RECORDING_DEVICE_2 = 2;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__RECORDING_DEVICE_3 = 9;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__RESERVED_1 = 12;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__RESERVED_2 = 13;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__SPECIFIC_USE = 14;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__TUNER_1 = 3;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__TUNER_2 = 6;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__TUNER_3 = 7;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__TUNER_4 = 10;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__TV = 0;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__ACTIVE_SOURCE_LOGICAL_ADDRESS__UNREGISTERED_OR_BROADCAST = 15;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__LOCAL_RELATIONSHIP__ANCESTOR = 2;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__LOCAL_RELATIONSHIP__DESCENDANT = 3;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__LOCAL_RELATIONSHIP__DIFFERENT_BRANCH = 1;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__LOCAL_RELATIONSHIP__RELATIONSHIP_TO_ACTIVE_SOURCE_UNKNOWN = 0;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__LOCAL_RELATIONSHIP__SAME = 5;
    public static final int HDMI_CEC_ACTIVE_SOURCE_CHANGED__LOCAL_RELATIONSHIP__SIBLING = 4;
    public static final int HDMI_CEC_MESSAGE_REPORTED = 310;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__AUDIO_SYSTEM = 5;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__LOGICAL_ADDRESS_UNKNOWN = -1;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__PLAYBACK_DEVICE_1 = 4;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__PLAYBACK_DEVICE_2 = 8;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__PLAYBACK_DEVICE_3 = 11;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__RECORDING_DEVICE_1 = 1;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__RECORDING_DEVICE_2 = 2;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__RECORDING_DEVICE_3 = 9;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__RESERVED_1 = 12;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__RESERVED_2 = 13;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__SPECIFIC_USE = 14;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__TUNER_1 = 3;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__TUNER_2 = 6;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__TUNER_3 = 7;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__TUNER_4 = 10;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__TV = 0;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DESTINATION_LOGICAL_ADDRESS__UNREGISTERED_OR_BROADCAST = 15;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DIRECTION__INCOMING = 3;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DIRECTION__MESSAGE_DIRECTION_OTHER = 1;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DIRECTION__MESSAGE_DIRECTION_UNKNOWN = 0;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DIRECTION__OUTGOING = 2;
    public static final int HDMI_CEC_MESSAGE_REPORTED__DIRECTION__TO_SELF = 4;
    public static final int HDMI_CEC_MESSAGE_REPORTED__FEATURE_ABORT_REASON__CANNOT_PROVIDE_SOURCE = 12;
    public static final int HDMI_CEC_MESSAGE_REPORTED__FEATURE_ABORT_REASON__FEATURE_ABORT_REASON_UNKNOWN = 0;
    public static final int HDMI_CEC_MESSAGE_REPORTED__FEATURE_ABORT_REASON__INVALID_OPERAND = 13;
    public static final int HDMI_CEC_MESSAGE_REPORTED__FEATURE_ABORT_REASON__NOT_IN_CORRECT_MODE_TO_RESPOND = 11;
    public static final int HDMI_CEC_MESSAGE_REPORTED__FEATURE_ABORT_REASON__REFUSED = 14;
    public static final int HDMI_CEC_MESSAGE_REPORTED__FEATURE_ABORT_REASON__UNABLE_TO_DETERMINE = 15;
    public static final int HDMI_CEC_MESSAGE_REPORTED__FEATURE_ABORT_REASON__UNRECOGNIZED_OPCODE = 10;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__AUDIO_SYSTEM = 5;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__LOGICAL_ADDRESS_UNKNOWN = -1;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__PLAYBACK_DEVICE_1 = 4;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__PLAYBACK_DEVICE_2 = 8;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__PLAYBACK_DEVICE_3 = 11;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__RECORDING_DEVICE_1 = 1;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__RECORDING_DEVICE_2 = 2;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__RECORDING_DEVICE_3 = 9;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__RESERVED_1 = 12;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__RESERVED_2 = 13;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__SPECIFIC_USE = 14;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__TUNER_1 = 3;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__TUNER_2 = 6;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__TUNER_3 = 7;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__TUNER_4 = 10;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__TV = 0;
    public static final int HDMI_CEC_MESSAGE_REPORTED__INITIATOR_LOGICAL_ADDRESS__UNREGISTERED_OR_BROADCAST = 15;
    public static final int HDMI_CEC_MESSAGE_REPORTED__SEND_MESSAGE_RESULT__BUSY = 12;
    public static final int HDMI_CEC_MESSAGE_REPORTED__SEND_MESSAGE_RESULT__FAIL = 13;
    public static final int HDMI_CEC_MESSAGE_REPORTED__SEND_MESSAGE_RESULT__NACK = 11;
    public static final int HDMI_CEC_MESSAGE_REPORTED__SEND_MESSAGE_RESULT__SEND_MESSAGE_RESULT_UNKNOWN = 0;
    public static final int HDMI_CEC_MESSAGE_REPORTED__SEND_MESSAGE_RESULT__SUCCESS = 10;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__DOWN = 258;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__EXIT = 269;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__LEFT = 259;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__LEFT_DOWN = 264;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__LEFT_UP = 263;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__NUMBER = 2;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__POWER = 320;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__POWER_OFF = 364;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__POWER_ON = 365;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__POWER_TOGGLE = 363;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__RIGHT = 260;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__RIGHT_DOWN = 262;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__RIGHT_UP = 261;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__SELECT = 256;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__UP = 257;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__USER_CONTROL_PRESSED_COMMAND_OTHER = 1;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__USER_CONTROL_PRESSED_COMMAND_UNKNOWN = 0;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__VOLUME_DOWN = 322;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__VOLUME_MUTE = 323;
    public static final int HDMI_CEC_MESSAGE_REPORTED__USER_CONTROL_PRESSED_COMMAND__VOLUME_UP = 321;
    public static final int HIDDEN_API_USED = 178;
    public static final int HIDDEN_API_USED__ACCESS_METHOD__JNI = 2;
    public static final int HIDDEN_API_USED__ACCESS_METHOD__LINKING = 3;
    public static final int HIDDEN_API_USED__ACCESS_METHOD__NONE = 0;
    public static final int HIDDEN_API_USED__ACCESS_METHOD__REFLECTION = 1;
    public static final int INPUTDEVICE_REGISTERED = 351;
    public static final int INSTALLED_INCREMENTAL_PACKAGE = 10114;
    public static final int INTEGRITY_CHECK_RESULT_REPORTED = 247;
    public static final int INTEGRITY_CHECK_RESULT_REPORTED__RESPONSE__ALLOWED = 1;
    public static final int INTEGRITY_CHECK_RESULT_REPORTED__RESPONSE__FORCE_ALLOWED = 3;
    public static final int INTEGRITY_CHECK_RESULT_REPORTED__RESPONSE__REJECTED = 2;
    public static final int INTEGRITY_CHECK_RESULT_REPORTED__RESPONSE__UNKNOWN = 0;
    public static final int INTEGRITY_RULES_PUSHED = 248;
    public static final int INTERACTIVE_STATE_CHANGED = 33;
    public static final int INTERACTIVE_STATE_CHANGED__STATE__OFF = 0;
    public static final int INTERACTIVE_STATE_CHANGED__STATE__ON = 1;
    public static final int ION_HEAP_SIZE = 10070;
    public static final int ISOLATED_UID_CHANGED = 43;
    public static final int ISOLATED_UID_CHANGED__EVENT__CREATED = 1;
    public static final int ISOLATED_UID_CHANGED__EVENT__REMOVED = 0;
    public static final int KERNEL_WAKELOCK = 10004;
    public static final int KERNEL_WAKEUP_REPORTED = 36;
    public static final int KEYSTORE2_ATOM_WITH_OVERFLOW = 10121;
    public static final int KEYSTORE2_CRASH_STATS = 10125;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO = 10119;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__SECURITY_LEVEL__SECURITY_LEVEL_KEYSTORE = 4;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__SECURITY_LEVEL__SECURITY_LEVEL_SOFTWARE = 1;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__SECURITY_LEVEL__SECURITY_LEVEL_STRONGBOX = 3;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__SECURITY_LEVEL__SECURITY_LEVEL_TRUSTED_ENVIRONMENT = 2;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__SECURITY_LEVEL__SECURITY_LEVEL_UNSPECIFIED = 0;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__USER_AUTH_TYPE__ANY = 5;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__USER_AUTH_TYPE__AUTH_TYPE_UNSPECIFIED = 0;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__USER_AUTH_TYPE__FINGERPRINT = 3;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__USER_AUTH_TYPE__NONE = 1;
    public static final int KEYSTORE2_KEY_CREATION_WITH_AUTH_INFO__USER_AUTH_TYPE__PASSWORD = 2;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO = 10118;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__ALGORITHM__AES = 32;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__ALGORITHM__ALGORITHM_UNSPECIFIED = 0;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__ALGORITHM__EC = 3;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__ALGORITHM__HMAC = 128;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__ALGORITHM__RSA = 1;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__ALGORITHM__TRIPLE_DES = 33;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__EC_CURVE__EC_CURVE_UNSPECIFIED = 0;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__EC_CURVE__P_224 = 1;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__EC_CURVE__P_256 = 2;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__EC_CURVE__P_384 = 3;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__EC_CURVE__P_521 = 4;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__KEY_ORIGIN__DERIVED = 2;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__KEY_ORIGIN__GENERATED = 1;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__KEY_ORIGIN__IMPORTED = 3;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__KEY_ORIGIN__ORIGIN_UNSPECIFIED = 0;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__KEY_ORIGIN__RESERVED = 4;
    public static final int KEYSTORE2_KEY_CREATION_WITH_GENERAL_INFO__KEY_ORIGIN__SECURELY_IMPORTED = 5;
    public static final int KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO = 10120;
    public static final int KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO__ALGORITHM__AES = 32;
    public static final int KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO__ALGORITHM__ALGORITHM_UNSPECIFIED = 0;
    public static final int KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO__ALGORITHM__EC = 3;
    public static final int KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO__ALGORITHM__HMAC = 128;
    public static final int KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO__ALGORITHM__RSA = 1;
    public static final int KEYSTORE2_KEY_CREATION_WITH_PURPOSE_AND_MODES_INFO__ALGORITHM__TRIPLE_DES = 33;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO = 10123;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__OUTCOME__ABORT = 3;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__OUTCOME__DROPPED = 1;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__OUTCOME__ERROR = 5;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__OUTCOME__OUTCOME_UNSPECIFIED = 0;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__OUTCOME__PRUNED = 4;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__OUTCOME__SUCCESS = 2;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__SECURITY_LEVEL__SECURITY_LEVEL_KEYSTORE = 4;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__SECURITY_LEVEL__SECURITY_LEVEL_SOFTWARE = 1;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__SECURITY_LEVEL__SECURITY_LEVEL_STRONGBOX = 3;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__SECURITY_LEVEL__SECURITY_LEVEL_TRUSTED_ENVIRONMENT = 2;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_GENERAL_INFO__SECURITY_LEVEL__SECURITY_LEVEL_UNSPECIFIED = 0;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO = 10122;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO__PURPOSE__AGREE_KEY = 7;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO__PURPOSE__ATTEST_KEY = 8;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO__PURPOSE__DECRYPT = 2;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO__PURPOSE__ENCRYPT = 1;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO__PURPOSE__KEY_PURPOSE_UNSPECIFIED = 0;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO__PURPOSE__SIGN = 3;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO__PURPOSE__VERIFY = 4;
    public static final int KEYSTORE2_KEY_OPERATION_WITH_PURPOSE_AND_MODES_INFO__PURPOSE__WRAP_KEY = 6;
    public static final int KEYSTORE2_STORAGE_STATS = 10103;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__AUTH_TOKEN = 11;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__BLOB_ENTRY = 4;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__BLOB_ENTRY_KEY_ENTRY_ID_INDEX = 5;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__BLOB_METADATA = 12;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__BLOB_METADATA_BLOB_ENTRY_ID_INDEX = 13;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__DATABASE = 15;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__GRANT = 10;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__KEY_ENTRY = 1;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__KEY_ENTRY_DOMAIN_NAMESPACE_INDEX = 3;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__KEY_ENTRY_ID_INDEX = 2;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__KEY_METADATA = 8;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__KEY_METADATA_KEY_ENTRY_ID_INDEX = 9;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__KEY_PARAMETER = 6;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__KEY_PARAMETER_KEY_ENTRY_ID_INDEX = 7;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__LEGACY_STORAGE = 16;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__METADATA = 14;
    public static final int KEYSTORE2_STORAGE_STATS__STORAGE_TYPE__STORAGE_UNSPECIFIED = 0;
    public static final int KEY_VALUE_PAIRS_ATOM = 83;
    public static final int LMK_KILL_OCCURRED = 51;
    public static final int LMK_KILL_OCCURRED__REASON__DIRECT_RECL_AND_THRASHING = 6;
    public static final int LMK_KILL_OCCURRED__REASON__LOW_FILECACHE_AFTER_THRASHING = 8;
    public static final int LMK_KILL_OCCURRED__REASON__LOW_MEM_AND_SWAP = 4;
    public static final int LMK_KILL_OCCURRED__REASON__LOW_MEM_AND_SWAP_UTIL = 7;
    public static final int LMK_KILL_OCCURRED__REASON__LOW_MEM_AND_THRASHING = 5;
    public static final int LMK_KILL_OCCURRED__REASON__LOW_SWAP_AND_THRASHING = 3;
    public static final int LMK_KILL_OCCURRED__REASON__NOT_RESPONDING = 2;
    public static final int LMK_KILL_OCCURRED__REASON__PRESSURE_AFTER_KILL = 1;
    public static final int LMK_KILL_OCCURRED__REASON__UNKNOWN = 0;
    public static final int LMK_STATE_CHANGED = 54;
    public static final int LMK_STATE_CHANGED__STATE__START = 1;
    public static final int LMK_STATE_CHANGED__STATE__STOP = 2;
    public static final int LMK_STATE_CHANGED__STATE__UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED = 210;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__ACTIVIY_IMPORTANCE__IMPORTANCE_BACKGROUND = 3;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__ACTIVIY_IMPORTANCE__IMPORTANCE_FORGROUND_SERVICE = 2;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__ACTIVIY_IMPORTANCE__IMPORTANCE_TOP = 1;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__ACTIVIY_IMPORTANCE__IMPORTANCE_UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__API_IN_USE__API_ADD_GNSS_MEASUREMENTS_LISTENER = 2;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__API_IN_USE__API_REGISTER_GNSS_STATUS_CALLBACK = 3;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__API_IN_USE__API_REQUEST_GEOFENCE = 4;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__API_IN_USE__API_REQUEST_LOCATION_UPDATES = 1;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__API_IN_USE__API_SEND_EXTRA_COMMAND = 5;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__API_IN_USE__API_UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_EXPIRE_IN__EXPIRATION_BETWEEN_0_AND_20_SEC = 1;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_EXPIRE_IN__EXPIRATION_BETWEEN_10_MIN_AND_1_HOUR = 4;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_EXPIRE_IN__EXPIRATION_BETWEEN_1_MIN_AND_10_MIN = 3;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_EXPIRE_IN__EXPIRATION_BETWEEN_20_SEC_AND_1_MIN = 2;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_EXPIRE_IN__EXPIRATION_LARGER_THAN_1_HOUR = 5;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_EXPIRE_IN__EXPIRATION_NO_EXPIRY = 6;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_EXPIRE_IN__EXPIRATION_UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_INTERVAL__INTERVAL_BETWEEN_0_SEC_AND_1_SEC = 1;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_INTERVAL__INTERVAL_BETWEEN_10_MIN_AND_1_HOUR = 5;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_INTERVAL__INTERVAL_BETWEEN_1_MIN_AND_10_MIN = 4;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_INTERVAL__INTERVAL_BETWEEN_1_SEC_AND_5_SEC = 2;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_INTERVAL__INTERVAL_BETWEEN_5_SEC_AND_1_MIN = 3;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_INTERVAL__INTERVAL_LARGER_THAN_1_HOUR = 6;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_INTERVAL__INTERVAL_UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_RADIUS__RADIUS_BETWEEN_0_AND_100 = 1;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_RADIUS__RADIUS_BETWEEN_1000_AND_10000 = 5;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_RADIUS__RADIUS_BETWEEN_100_AND_200 = 2;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_RADIUS__RADIUS_BETWEEN_200_AND_300 = 3;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_RADIUS__RADIUS_BETWEEN_300_AND_1000 = 4;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_RADIUS__RADIUS_LARGER_THAN_100000 = 6;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_RADIUS__RADIUS_NEGATIVE = 7;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_RADIUS__RADIUS_UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_SMALLEST_DISPLACEMENT__DISTANCE_BETWEEN_0_AND_100 = 2;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_SMALLEST_DISPLACEMENT__DISTANCE_LARGER_THAN_100 = 3;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_SMALLEST_DISPLACEMENT__DISTANCE_UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__BUCKETIZED_SMALLEST_DISPLACEMENT__DISTANCE_ZERO = 1;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__CALLBACK_TYPE__CALLBACK_LISTENER = 2;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__CALLBACK_TYPE__CALLBACK_NOT_APPLICABLE = 1;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__CALLBACK_TYPE__CALLBACK_PENDING_INTENT = 3;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__CALLBACK_TYPE__CALLBACK_UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__PROVIDER__PROVIDER_FUSED = 4;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__PROVIDER__PROVIDER_GPS = 2;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__PROVIDER__PROVIDER_NETWORK = 1;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__PROVIDER__PROVIDER_PASSIVE = 3;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__PROVIDER__PROVIDER_UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__QUALITY__ACCURACY_BLOCK = 102;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__QUALITY__ACCURACY_CITY = 104;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__QUALITY__ACCURACY_FINE = 100;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__QUALITY__POWER_HIGH = 203;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__QUALITY__POWER_LOW = 201;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__QUALITY__POWER_NONE = 200;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__QUALITY__QUALITY_UNKNOWN = 0;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__STATE__USAGE_ENDED = 1;
    public static final int LOCATION_MANAGER_API_USAGE_REPORTED__STATE__USAGE_STARTED = 0;
    public static final int LOCATION_TIME_ZONE_PROVIDER_STATE_CHANGED = 359;
    public static final int LOCATION_TIME_ZONE_PROVIDER_STATE_CHANGED__STATE__CERTAIN = 2;
    public static final int LOCATION_TIME_ZONE_PROVIDER_STATE_CHANGED__STATE__DESTROYED = 6;
    public static final int LOCATION_TIME_ZONE_PROVIDER_STATE_CHANGED__STATE__INITIALIZING = 1;
    public static final int LOCATION_TIME_ZONE_PROVIDER_STATE_CHANGED__STATE__PERM_FAILED = 5;
    public static final int LOCATION_TIME_ZONE_PROVIDER_STATE_CHANGED__STATE__STOPPED = 4;
    public static final int LOCATION_TIME_ZONE_PROVIDER_STATE_CHANGED__STATE__UNCERTAIN = 3;
    public static final int LOCATION_TIME_ZONE_PROVIDER_STATE_CHANGED__STATE__UNKNOWN = 0;
    public static final int LONG_PARTIAL_WAKELOCK_STATE_CHANGED = 11;
    public static final int LONG_PARTIAL_WAKELOCK_STATE_CHANGED__STATE__OFF = 0;
    public static final int LONG_PARTIAL_WAKELOCK_STATE_CHANGED__STATE__ON = 1;
    public static final int LOOPER_STATS = 10024;
    public static final int LOW_MEM_REPORTED = 81;
    public static final int LOW_STORAGE_STATE_CHANGED = 130;
    public static final int LOW_STORAGE_STATE_CHANGED__STATE__OFF = 1;
    public static final int LOW_STORAGE_STATE_CHANGED__STATE__ON = 2;
    public static final int LOW_STORAGE_STATE_CHANGED__STATE__UNKNOWN = 0;
    public static final int MAGNIFICATION_MODE_WITH_IME_ON_REPORTED = 346;
    public static final int MAGNIFICATION_MODE_WITH_IME_ON_REPORTED__ACTIVATED_MODE__MAGNIFICATION_ALL = 3;
    public static final int MAGNIFICATION_MODE_WITH_IME_ON_REPORTED__ACTIVATED_MODE__MAGNIFICATION_FULL_SCREEN = 1;
    public static final int MAGNIFICATION_MODE_WITH_IME_ON_REPORTED__ACTIVATED_MODE__MAGNIFICATION_UNKNOWN_MODE = 0;
    public static final int MAGNIFICATION_MODE_WITH_IME_ON_REPORTED__ACTIVATED_MODE__MAGNIFICATION_WINDOW = 2;
    public static final int MAGNIFICATION_USAGE_REPORTED = 345;
    public static final int MAGNIFICATION_USAGE_REPORTED__ACTIVATED_MODE__MAGNIFICATION_ALL = 3;
    public static final int MAGNIFICATION_USAGE_REPORTED__ACTIVATED_MODE__MAGNIFICATION_FULL_SCREEN = 1;
    public static final int MAGNIFICATION_USAGE_REPORTED__ACTIVATED_MODE__MAGNIFICATION_UNKNOWN_MODE = 0;
    public static final int MAGNIFICATION_USAGE_REPORTED__ACTIVATED_MODE__MAGNIFICATION_WINDOW = 2;
    public static final int MEDIA_CODEC_REPORTED = 378;
    public static final int MEDIA_CODEC_STATE_CHANGED = 24;
    public static final int MEDIA_CODEC_STATE_CHANGED__STATE__OFF = 0;
    public static final int MEDIA_CODEC_STATE_CHANGED__STATE__ON = 1;
    public static final int MEDIA_CODEC_STATE_CHANGED__STATE__RESET = 2;
    public static final int MEMORY_FACTOR_STATE_CHANGED = 15;
    public static final int MEMORY_FACTOR_STATE_CHANGED__FACTOR__CRITICAL = 4;
    public static final int MEMORY_FACTOR_STATE_CHANGED__FACTOR__LOW = 3;
    public static final int MEMORY_FACTOR_STATE_CHANGED__FACTOR__MEMORY_UNKNOWN = 0;
    public static final int MEMORY_FACTOR_STATE_CHANGED__FACTOR__MODERATE = 2;
    public static final int MEMORY_FACTOR_STATE_CHANGED__FACTOR__NORMAL = 1;
    public static final int MOBILE_BYTES_TRANSFER = 10002;
    public static final int MOBILE_BYTES_TRANSFER_BY_FG_BG = 10003;
    public static final int MOBILE_RADIO_POWER_STATE_CHANGED = 12;
    public static final int MOBILE_RADIO_POWER_STATE_CHANGED__STATE__DATA_CONNECTION_POWER_STATE_HIGH = 3;
    public static final int MOBILE_RADIO_POWER_STATE_CHANGED__STATE__DATA_CONNECTION_POWER_STATE_LOW = 1;
    public static final int MOBILE_RADIO_POWER_STATE_CHANGED__STATE__DATA_CONNECTION_POWER_STATE_MEDIUM = 2;
    public static final int MOBILE_RADIO_POWER_STATE_CHANGED__STATE__DATA_CONNECTION_POWER_STATE_UNKNOWN = Integer.MAX_VALUE;
    public static final int MODEM_ACTIVITY_INFO = 10012;
    public static final int NOTIFICATION_CHANNEL_MODIFIED = 246;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__IMPORTANCE__IMPORTANCE_DEFAULT = 3;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__IMPORTANCE__IMPORTANCE_HIGH = 4;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__IMPORTANCE__IMPORTANCE_IMPORTANT_CONVERSATION = 5;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__IMPORTANCE__IMPORTANCE_LOW = 2;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__IMPORTANCE__IMPORTANCE_MIN = 1;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__IMPORTANCE__IMPORTANCE_NONE = 0;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__IMPORTANCE__IMPORTANCE_UNSPECIFIED = -1000;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__OLD_IMPORTANCE__IMPORTANCE_DEFAULT = 3;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__OLD_IMPORTANCE__IMPORTANCE_HIGH = 4;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__OLD_IMPORTANCE__IMPORTANCE_IMPORTANT_CONVERSATION = 5;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__OLD_IMPORTANCE__IMPORTANCE_LOW = 2;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__OLD_IMPORTANCE__IMPORTANCE_MIN = 1;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__OLD_IMPORTANCE__IMPORTANCE_NONE = 0;
    public static final int NOTIFICATION_CHANNEL_MODIFIED__OLD_IMPORTANCE__IMPORTANCE_UNSPECIFIED = -1000;
    public static final int NOTIFICATION_REMOTE_VIEWS = 10066;
    public static final int NOTIFICATION_REPORTED = 244;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_ASST__IMPORTANCE_DEFAULT = 3;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_ASST__IMPORTANCE_HIGH = 4;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_ASST__IMPORTANCE_IMPORTANT_CONVERSATION = 5;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_ASST__IMPORTANCE_LOW = 2;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_ASST__IMPORTANCE_MIN = 1;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_ASST__IMPORTANCE_NONE = 0;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_ASST__IMPORTANCE_UNSPECIFIED = -1000;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL_SOURCE__IMPORTANCE_EXPLANATION_APP = 1;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL_SOURCE__IMPORTANCE_EXPLANATION_APP_PRE_CHANNELS = 5;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL_SOURCE__IMPORTANCE_EXPLANATION_ASST = 3;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL_SOURCE__IMPORTANCE_EXPLANATION_SYSTEM = 4;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL_SOURCE__IMPORTANCE_EXPLANATION_UNKNOWN = 0;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL_SOURCE__IMPORTANCE_EXPLANATION_USER = 2;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL__IMPORTANCE_DEFAULT = 3;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL__IMPORTANCE_HIGH = 4;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL__IMPORTANCE_IMPORTANT_CONVERSATION = 5;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL__IMPORTANCE_LOW = 2;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL__IMPORTANCE_MIN = 1;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL__IMPORTANCE_NONE = 0;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_INITIAL__IMPORTANCE_UNSPECIFIED = -1000;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_SOURCE__IMPORTANCE_EXPLANATION_APP = 1;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_SOURCE__IMPORTANCE_EXPLANATION_APP_PRE_CHANNELS = 5;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_SOURCE__IMPORTANCE_EXPLANATION_ASST = 3;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_SOURCE__IMPORTANCE_EXPLANATION_SYSTEM = 4;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_SOURCE__IMPORTANCE_EXPLANATION_UNKNOWN = 0;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE_SOURCE__IMPORTANCE_EXPLANATION_USER = 2;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE__IMPORTANCE_DEFAULT = 3;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE__IMPORTANCE_HIGH = 4;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE__IMPORTANCE_IMPORTANT_CONVERSATION = 5;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE__IMPORTANCE_LOW = 2;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE__IMPORTANCE_MIN = 1;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE__IMPORTANCE_NONE = 0;
    public static final int NOTIFICATION_REPORTED__IMPORTANCE__IMPORTANCE_UNSPECIFIED = -1000;
    public static final int NUM_FACES_ENROLLED = 10048;
    public static final int NUM_FINGERPRINTS_ENROLLED = 10031;
    public static final int OEM_MANAGED_BYTES_TRANSFER = 10100;
    public static final int ON_DEVICE_POWER_MEASUREMENT = 10038;
    public static final int OVERLAY_STATE_CHANGED = 59;
    public static final int OVERLAY_STATE_CHANGED__STATE__ENTERED = 1;
    public static final int OVERLAY_STATE_CHANGED__STATE__EXITED = 2;
    public static final int PACKAGE_INSTALLER_V2_REPORTED = 263;
    public static final int PACKAGE_NOTIFICATION_CHANNEL_GROUP_PREFERENCES = 10073;
    public static final int PACKAGE_NOTIFICATION_CHANNEL_PREFERENCES = 10072;
    public static final int PACKAGE_NOTIFICATION_PREFERENCES = 10071;
    public static final int PACKET_WAKEUP_OCCURRED = 44;
    public static final int PENDING_ALARM_INFO = 10106;
    public static final int PHONE_SERVICE_STATE_CHANGED = 94;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_GOOD = 3;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_GREAT = 4;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_MODERATE = 2;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_POOR = 1;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_ABSENT = 1;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_CARD_IO_ERROR = 8;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_CARD_RESTRICTED = 9;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_LOADED = 10;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_NETWORK_LOCKED = 4;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_NOT_READY = 6;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_PERM_DISABLED = 7;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_PIN_REQUIRED = 2;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_PRESENT = 11;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_PUK_REQUIRED = 3;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_READY = 5;
    public static final int PHONE_SERVICE_STATE_CHANGED__SIM_STATE__SIM_STATE_UNKNOWN = 0;
    public static final int PHONE_SERVICE_STATE_CHANGED__STATE__SERVICE_STATE_EMERGENCY_ONLY = 2;
    public static final int PHONE_SERVICE_STATE_CHANGED__STATE__SERVICE_STATE_IN_SERVICE = 0;
    public static final int PHONE_SERVICE_STATE_CHANGED__STATE__SERVICE_STATE_OUT_OF_SERVICE = 1;
    public static final int PHONE_SERVICE_STATE_CHANGED__STATE__SERVICE_STATE_POWER_OFF = 3;
    public static final int PHONE_SIGNAL_STRENGTH_CHANGED = 40;
    public static final int PHONE_SIGNAL_STRENGTH_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_GOOD = 3;
    public static final int PHONE_SIGNAL_STRENGTH_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_GREAT = 4;
    public static final int PHONE_SIGNAL_STRENGTH_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_MODERATE = 2;
    public static final int PHONE_SIGNAL_STRENGTH_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    public static final int PHONE_SIGNAL_STRENGTH_CHANGED__SIGNAL_STRENGTH__SIGNAL_STRENGTH_POOR = 1;
    public static final int PHONE_STATE_CHANGED = 95;
    public static final int PHONE_STATE_CHANGED__STATE__OFF = 0;
    public static final int PHONE_STATE_CHANGED__STATE__ON = 1;
    public static final int PICTURE_IN_PICTURE_STATE_CHANGED = 52;
    public static final int PICTURE_IN_PICTURE_STATE_CHANGED__STATE__DISMISSED = 4;
    public static final int PICTURE_IN_PICTURE_STATE_CHANGED__STATE__ENTERED = 1;
    public static final int PICTURE_IN_PICTURE_STATE_CHANGED__STATE__EXPANDED_TO_FULL_SCREEN = 2;
    public static final int PICTURE_IN_PICTURE_STATE_CHANGED__STATE__MINIMIZED = 3;
    public static final int PLUGGED_STATE_CHANGED = 32;
    public static final int PLUGGED_STATE_CHANGED__STATE__BATTERY_PLUGGED_AC = 1;
    public static final int PLUGGED_STATE_CHANGED__STATE__BATTERY_PLUGGED_NONE = 0;
    public static final int PLUGGED_STATE_CHANGED__STATE__BATTERY_PLUGGED_USB = 2;
    public static final int PLUGGED_STATE_CHANGED__STATE__BATTERY_PLUGGED_WIRELESS = 4;
    public static final int POWER_PROFILE = 10033;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION = 381;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__ACTION__ACTION_UNKNOWN = 0;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__ACTION__TOGGLE_OFF = 2;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__ACTION__TOGGLE_ON = 1;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__SENSOR__CAMERA = 2;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__SENSOR__MICROPHONE = 1;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__SENSOR__SENSOR_UNKNOWN = 0;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__SOURCE__DIALOG = 1;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__SOURCE__QS_TILE = 3;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__SOURCE__SETTINGS = 2;
    public static final int PRIVACY_SENSOR_TOGGLE_INTERACTION__SOURCE__SOURCE_UNKNOWN = 0;
    public static final int PRIVACY_TOGGLE_DIALOG_INTERACTION = 382;
    public static final int PRIVACY_TOGGLE_DIALOG_INTERACTION__ACTION__ACTION_UNKNOWN = 0;
    public static final int PRIVACY_TOGGLE_DIALOG_INTERACTION__ACTION__CANCEL = 2;
    public static final int PRIVACY_TOGGLE_DIALOG_INTERACTION__ACTION__ENABLE = 1;
    public static final int PROCESS_CPU_TIME = 10035;
    public static final int PROCESS_DMABUF_MEMORY = 10105;
    public static final int PROCESS_LIFE_CYCLE_STATE_CHANGED = 28;
    public static final int PROCESS_LIFE_CYCLE_STATE_CHANGED__STATE__CRASHED = 2;
    public static final int PROCESS_LIFE_CYCLE_STATE_CHANGED__STATE__FINISHED = 0;
    public static final int PROCESS_LIFE_CYCLE_STATE_CHANGED__STATE__STARTED = 1;
    public static final int PROCESS_MEMORY_HIGH_WATER_MARK = 10042;
    public static final int PROCESS_MEMORY_SNAPSHOT = 10064;
    public static final int PROCESS_MEMORY_STATE = 10013;
    public static final int PROCESS_MEMORY_STAT_REPORTED = 18;
    public static final int PROCESS_MEMORY_STAT_REPORTED__TYPE__ADD_PSS_EXTERNAL = 3;
    public static final int PROCESS_MEMORY_STAT_REPORTED__TYPE__ADD_PSS_EXTERNAL_SLOW = 4;
    public static final int PROCESS_MEMORY_STAT_REPORTED__TYPE__ADD_PSS_INTERNAL_ALL_MEM = 1;
    public static final int PROCESS_MEMORY_STAT_REPORTED__TYPE__ADD_PSS_INTERNAL_ALL_POLL = 2;
    public static final int PROCESS_MEMORY_STAT_REPORTED__TYPE__ADD_PSS_INTERNAL_SINGLE = 0;
    public static final int PROCESS_START_TIME = 169;
    public static final int PROCESS_START_TIME__TYPE__COLD = 3;
    public static final int PROCESS_START_TIME__TYPE__HOT = 2;
    public static final int PROCESS_START_TIME__TYPE__UNKNOWN = 0;
    public static final int PROCESS_START_TIME__TYPE__WARM = 1;
    public static final int PROCESS_STATE_CHANGED = 3;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_BACKUP = 1008;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 1004;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_BOUND_TOP = 1020;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_CACHED_ACTIVITY = 1015;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 1016;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_CACHED_EMPTY = 1018;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_CACHED_RECENT = 1017;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_FOREGROUND_SERVICE = 1003;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_HEAVY_WEIGHT = 1012;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_HOME = 1013;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_IMPORTANT_BACKGROUND = 1006;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_IMPORTANT_FOREGROUND = 1005;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_LAST_ACTIVITY = 1014;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_NONEXISTENT = 1019;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_PERSISTENT = 1000;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_PERSISTENT_UI = 1001;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_RECEIVER = 1010;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_SERVICE = 1009;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_TOP = 1002;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_TOP_SLEEPING = 1011;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_TRANSIENT_BACKGROUND = 1007;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_UNKNOWN = 999;
    public static final int PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_UNKNOWN_TO_PROTO = 998;
    public static final int PROCESS_SYSTEM_ION_HEAP_SIZE = 10061;
    public static final int PROC_STATS = 10029;
    public static final int PROC_STATS_PKG_PROC = 10034;
    public static final int RANKING_SELECTED = 260;
    public static final int REBOOT_ESCROW_LSKF_CAPTURE_REPORTED = 340;
    public static final int REBOOT_ESCROW_PREPARATION_REPORTED = 339;
    public static final int REBOOT_ESCROW_PREPARATION_REPORTED__RESULT__ROR_NEED_PREPARATION = 0;
    public static final int REBOOT_ESCROW_PREPARATION_REPORTED__RESULT__ROR_SKIP_PREPARATION_AND_NOTIFY = 1;
    public static final int REBOOT_ESCROW_PREPARATION_REPORTED__RESULT__ROR_SKIP_PREPARATION_NOT_NOTIFY = 2;
    public static final int REBOOT_ESCROW_REBOOT_REPORTED = 341;
    public static final int REBOOT_ESCROW_RECOVERY_REPORTED = 238;
    public static final int REBOOT_ESCROW_RECOVERY_REPORTED__TYPE__HAL = 1;
    public static final int REBOOT_ESCROW_RECOVERY_REPORTED__TYPE__SERVER_BASED = 2;
    public static final int REBOOT_ESCROW_RECOVERY_REPORTED__TYPE__UNKNOWN = 0;
    public static final int REBOOT_ESCROW_RECOVERY_REPORTED__VBMETA_DIGEST_STATUS__MATCH_EXPECTED_SLOT = 0;
    public static final int REBOOT_ESCROW_RECOVERY_REPORTED__VBMETA_DIGEST_STATUS__MATCH_FALLBACK_SLOT = 1;
    public static final int REBOOT_ESCROW_RECOVERY_REPORTED__VBMETA_DIGEST_STATUS__MISMATCH = 2;
    public static final int REMAINING_BATTERY_CAPACITY = 10019;
    public static final int RESCUE_PARTY_RESET_REPORTED = 122;
    public static final int RESOURCE_CONFIGURATION_CHANGED = 66;
    public static final int RKP_ERROR_STATS = 10124;
    public static final int RKP_ERROR_STATS__RKP_ERROR__FALL_BACK_DURING_HYBRID = 2;
    public static final int RKP_ERROR_STATS__RKP_ERROR__OUT_OF_KEYS = 1;
    public static final int RKP_ERROR_STATS__RKP_ERROR__RKP_ERROR_UNSPECIFIED = 0;
    public static final int RKP_POOL_STATS = 10104;
    public static final int RKP_POOL_STATS__SECURITY_LEVEL__SECURITY_LEVEL_KEYSTORE = 4;
    public static final int RKP_POOL_STATS__SECURITY_LEVEL__SECURITY_LEVEL_SOFTWARE = 1;
    public static final int RKP_POOL_STATS__SECURITY_LEVEL__SECURITY_LEVEL_STRONGBOX = 3;
    public static final int RKP_POOL_STATS__SECURITY_LEVEL__SECURITY_LEVEL_TRUSTED_ENVIRONMENT = 2;
    public static final int RKP_POOL_STATS__SECURITY_LEVEL__SECURITY_LEVEL_UNSPECIFIED = 0;
    public static final int ROLE_HOLDER = 10049;
    public static final int RUNTIME_APP_OP_ACCESS = 10069;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ACCEPT_HANDOVER = 74;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ACCESS_ACCESSIBILITY = 88;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ACCESS_MEDIA_LOCATION = 90;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ACCESS_NOTIFICATIONS = 25;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ACTIVATE_PLATFORM_VPN = 94;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ACTIVATE_VPN = 47;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ACTIVITY_RECOGNITION = 79;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ACTIVITY_RECOGNITION_SOURCE = 113;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ADD_VOICEMAIL = 52;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ANSWER_PHONE_CALLS = 69;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ASSIST_SCREENSHOT = 50;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_ASSIST_STRUCTURE = 49;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUDIO_ALARM_VOLUME = 37;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUDIO_BLUETOOTH_VOLUME = 39;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUDIO_MASTER_VOLUME = 33;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUDIO_MEDIA_VOLUME = 36;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUDIO_NOTIFICATION_VOLUME = 38;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUDIO_RING_VOLUME = 35;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUDIO_VOICE_VOLUME = 34;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUTO_REVOKE_MANAGED_BY_INSTALLER = 98;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_AUTO_REVOKE_PERMISSIONS_IF_UNUSED = 97;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_BIND_ACCESSIBILITY_SERVICE = 73;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_BLUETOOTH_ADVERTISE = 114;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_BLUETOOTH_CONNECT = 111;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_BLUETOOTH_SCAN = 77;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_BODY_SENSORS = 56;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_CALL_PHONE = 13;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_CAMERA = 26;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_CHANGE_WIFI_STATE = 71;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_COARSE_LOCATION = 0;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_COARSE_LOCATION_SOURCE = 109;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_DEPRECATED_1 = 96;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_FINE_LOCATION = 1;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_FINE_LOCATION_SOURCE = 108;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_GET_ACCOUNTS = 62;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_GET_USAGE_STATS = 43;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_GPS = 2;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_INSTANT_APP_START_FOREGROUND = 68;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_INTERACT_ACROSS_PROFILES = 93;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_LEGACY_STORAGE = 87;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_LOADER_USAGE_STATS = 95;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_MANAGE_CREDENTIALS = 104;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_MANAGE_EXTERNAL_STORAGE = 92;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_MANAGE_IPSEC_TUNNELS = 75;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_MANAGE_MEDIA = 110;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_MANAGE_ONGOING_CALLS = 103;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_MOCK_LOCATION = 58;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_MONITOR_HIGH_POWER_LOCATION = 42;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_MONITOR_LOCATION = 41;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_MUTE_MICROPHONE = 44;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_NEIGHBORING_CELLS = 12;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_NONE = -1;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_NO_ISOLATED_STORAGE = 99;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_PHONE_CALL_CAMERA = 101;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_PHONE_CALL_MICROPHONE = 100;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_PICTURE_IN_PICTURE = 67;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_PLAY_AUDIO = 28;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_POST_NOTIFICATION = 11;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_PROCESS_OUTGOING_CALLS = 54;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_PROJECT_MEDIA = 46;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_QUERY_ALL_PACKAGES = 91;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_CALENDAR = 8;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_CALL_LOG = 6;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_CELL_BROADCASTS = 57;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_CLIPBOARD = 29;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_CONTACTS = 4;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_DEVICE_IDENTIFIERS = 89;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_EXTERNAL_STORAGE = 59;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_ICC_SMS = 21;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_MEDIA_AUDIO = 81;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_MEDIA_IMAGES = 85;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_MEDIA_VIDEO = 83;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_PHONE_NUMBERS = 65;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_PHONE_STATE = 51;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_READ_SMS = 14;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RECEIVE_EMERGENCY_SMS = 17;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RECEIVE_MMS = 18;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RECEIVE_SMS = 16;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RECEIVE_WAP_PUSH = 19;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RECORD_AUDIO = 27;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RECORD_AUDIO_HOTWORD = 102;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RECORD_AUDIO_OUTPUT = 106;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RECORD_INCOMING_PHONE_AUDIO = 115;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_REQUEST_DELETE_PACKAGES = 72;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_REQUEST_INSTALL_PACKAGES = 66;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RUN_ANY_IN_BACKGROUND = 70;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_RUN_IN_BACKGROUND = 63;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_SCHEDULE_EXACT_ALARM = 107;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_SEND_SMS = 20;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_SMS_FINANCIAL_TRANSACTIONS = 80;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_START_FOREGROUND = 76;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_TAKE_AUDIO_FOCUS = 32;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_TAKE_MEDIA_BUTTONS = 31;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_TOAST_WINDOW = 45;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_TURN_SCREEN_ON = 61;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_USE_BIOMETRIC = 78;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_USE_FINGERPRINT = 55;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER = 105;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_USE_SIP = 53;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_UWB_RANGING = 112;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_VIBRATE = 3;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WAKE_LOCK = 40;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WIFI_SCAN = 10;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_CALENDAR = 9;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_CALL_LOG = 7;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_CLIPBOARD = 30;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_CONTACTS = 5;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_EXTERNAL_STORAGE = 60;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_ICC_SMS = 22;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_MEDIA_AUDIO = 82;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_MEDIA_IMAGES = 86;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_MEDIA_VIDEO = 84;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_SETTINGS = 23;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_SMS = 15;
    public static final int RUNTIME_APP_OP_ACCESS__OP__APP_OP_WRITE_WALLPAPER = 48;
    public static final int RUNTIME_APP_OP_ACCESS__SAMPLING_STRATEGY__BOOT_TIME_SAMPLING = 3;
    public static final int RUNTIME_APP_OP_ACCESS__SAMPLING_STRATEGY__DEFAULT = 0;
    public static final int RUNTIME_APP_OP_ACCESS__SAMPLING_STRATEGY__RARELY_USED = 2;
    public static final int RUNTIME_APP_OP_ACCESS__SAMPLING_STRATEGY__UNIFORM = 1;
    public static final int RUNTIME_APP_OP_ACCESS__SAMPLING_STRATEGY__UNIFORM_OPS = 4;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED = 150;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_BACKGROUND_NOT_RESTRICTED = 11;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_BATTERY_NOT_LOW = 2;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_CHARGING = 1;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_CONNECTIVITY = 7;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_CONTENT_TRIGGER = 8;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_DEADLINE = 5;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_DEVICE_NOT_DOZING = 9;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_IDLE = 6;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_STORAGE_NOT_LOW = 3;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_TIMING_DELAY = 4;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_UNKNOWN = 0;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_WITHIN_EXPEDITED_JOB_QUOTA = 12;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__CONSTRAINT__CONSTRAINT_WITHIN_QUOTA = 10;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__STATE__SATISFIED = 2;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__STATE__UNKNOWN = 0;
    public static final int SCHEDULED_JOB_CONSTRAINT_CHANGED__STATE__UNSATISFIED = 1;
    public static final int SCHEDULED_JOB_STATE_CHANGED = 8;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_CANCELLED = 0;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_CONSTRAINTS_NOT_SATISFIED = 1;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_DATA_CLEARED = 8;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_DEVICE_IDLE = 4;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_DEVICE_THERMAL = 5;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_PREEMPT = 2;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_RESTRICTED_BUCKET = 6;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_RTC_UPDATED = 9;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_SUCCESSFUL_FINISH = 10;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_TIMEOUT = 3;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_UNINSTALL = 7;
    public static final int SCHEDULED_JOB_STATE_CHANGED__INTERNAL_STOP_REASON__INTERNAL_STOP_REASON_UNKNOWN = -1;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_APP_STANDBY = 12;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_BACKGROUND_RESTRICTION = 11;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_CANCELLED_BY_APP = 1;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_CONSTRAINT_BATTERY_NOT_LOW = 5;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_CONSTRAINT_CHARGING = 6;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_CONSTRAINT_CONNECTIVITY = 7;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_CONSTRAINT_DEVICE_IDLE = 8;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_CONSTRAINT_STORAGE_NOT_LOW = 9;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_DEVICE_STATE = 4;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_PREEMPT = 2;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_QUOTA = 10;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_SYSTEM_PROCESSING = 14;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_TIMEOUT = 3;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_UNDEFINED = 0;
    public static final int SCHEDULED_JOB_STATE_CHANGED__PUBLIC_STOP_REASON__STOP_REASON_USER = 13;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STANDBY_BUCKET__ACTIVE = 0;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STANDBY_BUCKET__FREQUENT = 2;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STANDBY_BUCKET__NEVER = 4;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STANDBY_BUCKET__RARE = 3;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STANDBY_BUCKET__RESTRICTED = 5;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STANDBY_BUCKET__UNKNOWN = -1;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STANDBY_BUCKET__WORKING_SET = 1;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STATE__FINISHED = 0;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STATE__SCHEDULED = 2;
    public static final int SCHEDULED_JOB_STATE_CHANGED__STATE__STARTED = 1;
    public static final int SCREEN_BRIGHTNESS_CHANGED = 9;
    public static final int SCREEN_STATE_CHANGED = 29;
    public static final int SCREEN_STATE_CHANGED__STATE__DISPLAY_STATE_DOZE = 3;
    public static final int SCREEN_STATE_CHANGED__STATE__DISPLAY_STATE_DOZE_SUSPEND = 4;
    public static final int SCREEN_STATE_CHANGED__STATE__DISPLAY_STATE_OFF = 1;
    public static final int SCREEN_STATE_CHANGED__STATE__DISPLAY_STATE_ON = 2;
    public static final int SCREEN_STATE_CHANGED__STATE__DISPLAY_STATE_ON_SUSPEND = 6;
    public static final int SCREEN_STATE_CHANGED__STATE__DISPLAY_STATE_UNKNOWN = 0;
    public static final int SCREEN_STATE_CHANGED__STATE__DISPLAY_STATE_VR = 5;
    public static final int SCREEN_TIMEOUT_EXTENSION_REPORTED = 168;
    public static final int SENSOR_STATE_CHANGED = 5;
    public static final int SENSOR_STATE_CHANGED__STATE__OFF = 0;
    public static final int SENSOR_STATE_CHANGED__STATE__ON = 1;
    public static final int SERVICE_LAUNCH_REPORTED = 100;
    public static final int SERVICE_STATE_CHANGED = 99;
    public static final int SERVICE_STATE_CHANGED__STATE__START = 1;
    public static final int SERVICE_STATE_CHANGED__STATE__STOP = 2;
    public static final int SETTING_CHANGED = 41;
    public static final int SETTING_CHANGED__REASON__DELETED = 2;
    public static final int SETTING_CHANGED__REASON__UPDATED = 1;
    public static final int SETTING_SNAPSHOT = 10080;
    public static final int SETTING_SNAPSHOT__TYPE__ASSIGNED_BOOL_TYPE = 1;
    public static final int SETTING_SNAPSHOT__TYPE__ASSIGNED_FLOAT_TYPE = 3;
    public static final int SETTING_SNAPSHOT__TYPE__ASSIGNED_INT_TYPE = 2;
    public static final int SETTING_SNAPSHOT__TYPE__ASSIGNED_STRING_TYPE = 4;
    public static final int SETTING_SNAPSHOT__TYPE__NOTASSIGNED = 0;
    public static final int SHARESHEET_STARTED = 259;
    public static final int SHARESHEET_STARTED__INTENT_TYPE__INTENT_ACTION_EDIT = 2;
    public static final int SHARESHEET_STARTED__INTENT_TYPE__INTENT_ACTION_IMAGE_CAPTURE = 6;
    public static final int SHARESHEET_STARTED__INTENT_TYPE__INTENT_ACTION_MAIN = 7;
    public static final int SHARESHEET_STARTED__INTENT_TYPE__INTENT_ACTION_SEND = 3;
    public static final int SHARESHEET_STARTED__INTENT_TYPE__INTENT_ACTION_SENDTO = 4;
    public static final int SHARESHEET_STARTED__INTENT_TYPE__INTENT_ACTION_SEND_MULTIPLE = 5;
    public static final int SHARESHEET_STARTED__INTENT_TYPE__INTENT_ACTION_VIEW = 1;
    public static final int SHARESHEET_STARTED__INTENT_TYPE__INTENT_DEFAULT = 0;
    public static final int SHARESHEET_STARTED__PREVIEW_TYPE__CONTENT_PREVIEW_FILE = 2;
    public static final int SHARESHEET_STARTED__PREVIEW_TYPE__CONTENT_PREVIEW_IMAGE = 1;
    public static final int SHARESHEET_STARTED__PREVIEW_TYPE__CONTENT_PREVIEW_TEXT = 3;
    public static final int SHARESHEET_STARTED__PREVIEW_TYPE__CONTENT_PREVIEW_TYPE_UNKNOWN = 0;
    public static final int SHUTDOWN_SEQUENCE_REPORTED = 56;
    public static final int SIGNED_CONFIG_REPORTED = 123;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__APPLIED = 1;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__BASE64_FAILURE_CONFIG = 2;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__BASE64_FAILURE_SIGNATURE = 3;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__INVALID_CONFIG = 5;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__NOT_APPLICABLE = 8;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__OLD_CONFIG = 6;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__SECURITY_EXCEPTION = 4;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__SIGNATURE_CHECK_FAILED = 7;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__SIGNATURE_CHECK_FAILED_PROD_KEY_ABSENT = 9;
    public static final int SIGNED_CONFIG_REPORTED__STATUS__UNKNOWN_STATUS = 0;
    public static final int SIGNED_CONFIG_REPORTED__TYPE__GLOBAL_SETTINGS = 1;
    public static final int SIGNED_CONFIG_REPORTED__TYPE__UNKNOWN_TYPE = 0;
    public static final int SIGNED_CONFIG_REPORTED__VERIFIED_WITH__DEBUG = 1;
    public static final int SIGNED_CONFIG_REPORTED__VERIFIED_WITH__NO_KEY = 0;
    public static final int SIGNED_CONFIG_REPORTED__VERIFIED_WITH__PRODUCTION = 2;
    public static final int SUBSYSTEM_SLEEP_STATE = 10005;
    public static final int SYNC_STATE_CHANGED = 7;
    public static final int SYNC_STATE_CHANGED__STATE__OFF = 0;
    public static final int SYNC_STATE_CHANGED__STATE__ON = 1;
    public static final int SYSTEM_ELAPSED_REALTIME = 10014;
    public static final int SYSTEM_ION_HEAP_SIZE = 10056;
    public static final int SYSTEM_MEMORY = 10092;
    public static final int SYSTEM_SERVER_WATCHDOG_OCCURRED = 185;
    public static final int SYSTEM_UPTIME = 10015;
    public static final int TEMPERATURE = 10021;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_BATTERY = 2;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_BCL_CURRENT = 7;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_BCL_PERCENTAGE = 8;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_BCL_VOLTAGE = 6;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_CPU = 0;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_GPU = 1;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_NPU = 9;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_POWER_AMPLIFIER = 5;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_SKIN = 3;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_UNKNOWN = -1;
    public static final int TEMPERATURE__SENSOR_LOCATION__TEMPERATURE_TYPE_USB_PORT = 4;
    public static final int TEMPERATURE__SEVERITY__CRITICAL = 4;
    public static final int TEMPERATURE__SEVERITY__EMERGENCY = 5;
    public static final int TEMPERATURE__SEVERITY__LIGHT = 1;
    public static final int TEMPERATURE__SEVERITY__MODERATE = 2;
    public static final int TEMPERATURE__SEVERITY__NONE = 0;
    public static final int TEMPERATURE__SEVERITY__SEVERE = 3;
    public static final int TEMPERATURE__SEVERITY__SHUTDOWN = 6;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED = 189;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_BATTERY = 2;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_BCL_CURRENT = 7;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_BCL_PERCENTAGE = 8;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_BCL_VOLTAGE = 6;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_CPU = 0;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_GPU = 1;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_NPU = 9;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_POWER_AMPLIFIER = 5;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_SKIN = 3;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_UNKNOWN = -1;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SENSOR_TYPE__TEMPERATURE_TYPE_USB_PORT = 4;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SEVERITY__CRITICAL = 4;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SEVERITY__EMERGENCY = 5;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SEVERITY__LIGHT = 1;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SEVERITY__MODERATE = 2;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SEVERITY__NONE = 0;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SEVERITY__SEVERE = 3;
    public static final int THERMAL_THROTTLING_SEVERITY_STATE_CHANGED__SEVERITY__SHUTDOWN = 6;
    public static final int TIF_TUNE_CHANGED = 327;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__CREATED = 1;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__RELEASED = 4;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__SURFACE_ATTACHED = 2;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__SURFACE_DETACHED = 3;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__TIF_TUNE_STATE_UNKNOWN = 0;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__TUNE_STARTED = 5;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_AVAILABLE = 6;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_AUDIO_ONLY = 104;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_BUFFERING = 103;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_BLACKOUT = 116;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_CARD_INVALID = 115;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_CARD_MUTE = 114;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_INSUFFICIENT_OUTPUT_PROTECTION = 107;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_LICENSE_EXPIRED = 110;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_NEED_ACTIVATION = 111;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_NEED_PAIRING = 112;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_NO_CARD = 113;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_NO_LICENSE = 109;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_PVR_RECORDING_NOT_ALLOWED = 108;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_REBOOTING = 117;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_CAS_UNKNOWN = 118;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_INSUFFICIENT_RESOURCE = 106;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_NOT_CONNECTED = 105;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_TUNING = 101;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_UNKNOWN = 100;
    public static final int TIF_TUNE_STATE_CHANGED__STATE__VIDEO_UNAVAILABLE_REASON_WEAK_SIGNAL = 102;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__COMPONENT = 1004;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__COMPOSITE = 1001;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__DISPLAY_PORT = 1008;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__DVI = 1006;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__HDMI = 1007;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__OTHER = 1000;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__SCART = 1003;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__SVIDEO = 1002;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__TIF_INPUT_TYPE_UNKNOWN = 0;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__TUNER = 1;
    public static final int TIF_TUNE_STATE_CHANGED__TYPE__VGA = 1005;
    public static final int TIMEOUT_AUTO_EXTENDED_REPORTED = 365;
    public static final int TIMEOUT_AUTO_EXTENDED_REPORTED__OUTCOME__POWER_BUTTON = 1;
    public static final int TIMEOUT_AUTO_EXTENDED_REPORTED__OUTCOME__TIMEOUT = 2;
    public static final int TIMEOUT_AUTO_EXTENDED_REPORTED__OUTCOME__UNKNOWN = 0;
    public static final int TIME_ZONE_DATA_INFO = 10052;
    public static final int TIME_ZONE_DETECTOR_STATE = 10102;
    public static final int TIME_ZONE_DETECTOR_STATE__DETECTION_MODE__GEO = 3;
    public static final int TIME_ZONE_DETECTOR_STATE__DETECTION_MODE__MANUAL = 1;
    public static final int TIME_ZONE_DETECTOR_STATE__DETECTION_MODE__TELEPHONY = 2;
    public static final int TIME_ZONE_DETECTOR_STATE__DETECTION_MODE__UNKNOWN = 0;
    public static final int TOMB_STONE_OCCURRED = 186;
    public static final int TOUCH_GESTURE_CLASSIFIED = 177;
    public static final int TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__DEEP_PRESS = 4;
    public static final int TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__DOUBLE_TAP = 2;
    public static final int TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__LONG_PRESS = 3;
    public static final int TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__SCROLL = 5;
    public static final int TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__SINGLE_TAP = 1;
    public static final int TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__UNKNOWN_CLASSIFICATION = 0;
    public static final int TV_CAS_SESSION_OPEN_STATUS = 280;
    public static final int TV_CAS_SESSION_OPEN_STATUS__STATE__FAILED = 2;
    public static final int TV_CAS_SESSION_OPEN_STATUS__STATE__SUCCEEDED = 1;
    public static final int TV_CAS_SESSION_OPEN_STATUS__STATE__UNKNOWN = 0;
    public static final int TV_TUNER_DVR_STATUS = 279;
    public static final int TV_TUNER_DVR_STATUS__STATE__STARTED = 1;
    public static final int TV_TUNER_DVR_STATUS__STATE__STOPPED = 2;
    public static final int TV_TUNER_DVR_STATUS__STATE__UNKNOWN_STATE = 0;
    public static final int TV_TUNER_DVR_STATUS__TYPE__PLAYBACK = 1;
    public static final int TV_TUNER_DVR_STATUS__TYPE__RECORD = 2;
    public static final int TV_TUNER_DVR_STATUS__TYPE__UNKNOWN_TYPE = 0;
    public static final int TV_TUNER_STATE_CHANGED = 276;
    public static final int TV_TUNER_STATE_CHANGED__STATE__LOCKED = 2;
    public static final int TV_TUNER_STATE_CHANGED__STATE__NOT_LOCKED = 3;
    public static final int TV_TUNER_STATE_CHANGED__STATE__SCANNING = 5;
    public static final int TV_TUNER_STATE_CHANGED__STATE__SCAN_STOPPED = 6;
    public static final int TV_TUNER_STATE_CHANGED__STATE__SIGNAL_LOST = 4;
    public static final int TV_TUNER_STATE_CHANGED__STATE__TUNING = 1;
    public static final int TV_TUNER_STATE_CHANGED__STATE__UNKNOWN = 0;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_CHECK_CREDENTIAL = 4;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_CHECK_CREDENTIAL_UNLOCKED = 5;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_EXPAND_PANEL = 1;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_FACE_WAKE_AND_UNLOCK = 8;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_FINGERPRINT_WAKE_AND_UNLOCK = 3;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_LOCKSCREEN_UNLOCK = 12;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_ROTATE_SCREEN = 7;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_ROTATE_SCREEN_CAMERA_CHECK = 10;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_ROTATE_SCREEN_SENSOR = 11;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_START_RECENTS_ANIMATION = 9;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_TOGGLE_RECENTS = 2;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__ACTION_TURN_ON_SCREEN = 6;
    public static final int UIACTION_LATENCY_REPORTED__ACTION__UNKNOWN = 0;
    public static final int UID_PROCESS_STATE_CHANGED = 27;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_BACKUP = 1008;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 1004;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_BOUND_TOP = 1020;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_CACHED_ACTIVITY = 1015;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 1016;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_CACHED_EMPTY = 1018;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_CACHED_RECENT = 1017;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_FOREGROUND_SERVICE = 1003;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_HEAVY_WEIGHT = 1012;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_HOME = 1013;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_IMPORTANT_BACKGROUND = 1006;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_IMPORTANT_FOREGROUND = 1005;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_LAST_ACTIVITY = 1014;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_NONEXISTENT = 1019;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_PERSISTENT = 1000;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_PERSISTENT_UI = 1001;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_RECEIVER = 1010;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_SERVICE = 1009;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_TOP = 1002;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_TOP_SLEEPING = 1011;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_TRANSIENT_BACKGROUND = 1007;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_UNKNOWN = 999;
    public static final int UID_PROCESS_STATE_CHANGED__STATE__PROCESS_STATE_UNKNOWN_TO_PROTO = 998;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LAUNCHER_ALL_APPS_SCROLL = 27;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LAUNCHER_APP_CLOSE_TO_HOME = 10;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LAUNCHER_APP_CLOSE_TO_PIP = 11;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LAUNCHER_APP_LAUNCH_FROM_ICON = 9;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LAUNCHER_APP_LAUNCH_FROM_RECENTS = 8;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LAUNCHER_APP_LAUNCH_FROM_WIDGET = 28;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LAUNCHER_OPEN_ALL_APPS = 26;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LAUNCHER_QUICK_SWITCH = 12;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LOCKSCREEN_PASSWORD_APPEAR = 18;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LOCKSCREEN_PASSWORD_DISAPPEAR = 21;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LOCKSCREEN_PATTERN_APPEAR = 19;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LOCKSCREEN_PATTERN_DISAPPEAR = 22;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LOCKSCREEN_PIN_APPEAR = 20;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LOCKSCREEN_PIN_DISAPPEAR = 23;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LOCKSCREEN_TRANSITION_FROM_AOD = 24;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LOCKSCREEN_TRANSITION_TO_AOD = 25;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__LOCKSCREEN_UNLOCK_ANIMATION = 30;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__NOTIFICATION_SHADE_SWIPE = 1;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SETTINGS_PAGE_SCROLL = 29;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_APP_LAUNCH = 17;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_APP_LAUNCH_FROM_HISTORY_BUTTON = 31;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_APP_LAUNCH_FROM_MEDIA_PLAYER = 32;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_APP_LAUNCH_FROM_QS_TILE = 33;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_APP_LAUNCH_FROM_SETTINGS_BUTTON = 34;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_EXPAND_COLLAPSE_LOCK = 2;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_HEADS_UP_APPEAR = 13;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_HEADS_UP_DISAPPEAR = 14;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_NOTIFICATION_ADD = 15;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_NOTIFICATION_REMOVE = 16;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_QS_EXPAND_COLLAPSE = 6;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_QS_SCROLL_SWIPE = 7;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_ROW_EXPAND = 4;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_ROW_SWIPE = 5;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__SHADE_SCROLL_FLING = 3;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__STATUS_BAR_APP_LAUNCH_FROM_CALL_CHIP = 35;
    public static final int UIINTERACTION_FRAME_INFO_REPORTED__INTERACTION_TYPE__UNKNOWN = 0;
    public static final int UI_ACTION_LATENCY_REPORTED = 306;
    public static final int UI_EVENT_REPORTED = 90;
    public static final int UI_INTERACTION_FRAME_INFO_REPORTED = 305;
    public static final int USB_CONNECTOR_STATE_CHANGED = 70;
    public static final int USB_CONNECTOR_STATE_CHANGED__STATE__STATE_CONNECTED = 1;
    public static final int USB_CONNECTOR_STATE_CHANGED__STATE__STATE_DISCONNECTED = 0;
    public static final int USB_CONTAMINANT_REPORTED = 146;
    public static final int USB_CONTAMINANT_REPORTED__STATUS__CONTAMINANT_STATUS_DETECTED = 4;
    public static final int USB_CONTAMINANT_REPORTED__STATUS__CONTAMINANT_STATUS_DISABLED = 2;
    public static final int USB_CONTAMINANT_REPORTED__STATUS__CONTAMINANT_STATUS_NOT_DETECTED = 3;
    public static final int USB_CONTAMINANT_REPORTED__STATUS__CONTAMINANT_STATUS_NOT_SUPPORTED = 1;
    public static final int USB_CONTAMINANT_REPORTED__STATUS__CONTAMINANT_STATUS_UNKNOWN = 0;
    public static final int USB_DEVICE_ATTACHED = 77;
    public static final int USB_DEVICE_ATTACHED__STATE__STATE_CONNECTED = 1;
    public static final int USB_DEVICE_ATTACHED__STATE__STATE_DISCONNECTED = 0;
    public static final int USERSPACE_REBOOT_REPORTED = 243;
    public static final int USERSPACE_REBOOT_REPORTED__OUTCOME__FAILED_SHUTDOWN_SEQUENCE_ABORTED = 2;
    public static final int USERSPACE_REBOOT_REPORTED__OUTCOME__FAILED_USERDATA_REMOUNT = 3;
    public static final int USERSPACE_REBOOT_REPORTED__OUTCOME__FAILED_USERSPACE_REBOOT_WATCHDOG_TRIGGERED = 4;
    public static final int USERSPACE_REBOOT_REPORTED__OUTCOME__OUTCOME_UNKNOWN = 0;
    public static final int USERSPACE_REBOOT_REPORTED__OUTCOME__SUCCESS = 1;
    public static final int USERSPACE_REBOOT_REPORTED__USER_ENCRYPTION_STATE__LOCKED = 2;
    public static final int USERSPACE_REBOOT_REPORTED__USER_ENCRYPTION_STATE__UNLOCKED = 1;
    public static final int USERSPACE_REBOOT_REPORTED__USER_ENCRYPTION_STATE__USER_ENCRYPTION_STATE_UNKNOWN = 0;
    public static final int USER_LEVEL_HIBERNATED_APPS = 10107;
    public static final int USER_LEVEL_HIBERNATION_STATE_CHANGED = 370;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED = 265;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__EVENT__CREATE_USER = 3;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__EVENT__START_USER = 2;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__EVENT__SWITCH_USER = 1;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__EVENT__UNKNOWN = 0;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__EVENT__UNLOCKED_USER = 6;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__EVENT__UNLOCKING_USER = 5;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__EVENT__USER_RUNNING_LOCKED = 4;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__STATE__BEGIN = 1;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__STATE__FINISH = 2;
    public static final int USER_LIFECYCLE_EVENT_OCCURRED__STATE__NONE = 0;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED = 264;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__JOURNEY__UNKNOWN = 0;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__JOURNEY__USER_CREATE = 4;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__JOURNEY__USER_START = 3;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__JOURNEY__USER_SWITCH_FG = 2;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__JOURNEY__USER_SWITCH_UI = 1;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__USER_TYPE__FULL_DEMO = 4;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__USER_TYPE__FULL_GUEST = 3;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__USER_TYPE__FULL_RESTRICTED = 5;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__USER_TYPE__FULL_SECONDARY = 2;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__USER_TYPE__FULL_SYSTEM = 1;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__USER_TYPE__PROFILE_CLONE = 8;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__USER_TYPE__PROFILE_MANAGED = 6;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__USER_TYPE__SYSTEM_HEADLESS = 7;
    public static final int USER_LIFECYCLE_JOURNEY_REPORTED__USER_TYPE__TYPE_UNKNOWN = 0;
    public static final int VIBRATOR_STATE_CHANGED = 84;
    public static final int VIBRATOR_STATE_CHANGED__STATE__OFF = 0;
    public static final int VIBRATOR_STATE_CHANGED__STATE__ON = 1;
    public static final int VMSTAT = 10117;
    public static final int WAKELOCK_STATE_CHANGED = 10;
    public static final int WAKELOCK_STATE_CHANGED__STATE__ACQUIRE = 1;
    public static final int WAKELOCK_STATE_CHANGED__STATE__CHANGE_ACQUIRE = 3;
    public static final int WAKELOCK_STATE_CHANGED__STATE__CHANGE_RELEASE = 2;
    public static final int WAKELOCK_STATE_CHANGED__STATE__RELEASE = 0;
    public static final int WAKELOCK_STATE_CHANGED__TYPE__DOZE_WAKE_LOCK = 64;
    public static final int WAKELOCK_STATE_CHANGED__TYPE__DRAW_WAKE_LOCK = 128;
    public static final int WAKELOCK_STATE_CHANGED__TYPE__FULL_WAKE_LOCK = 26;
    public static final int WAKELOCK_STATE_CHANGED__TYPE__PARTIAL_WAKE_LOCK = 1;
    public static final int WAKELOCK_STATE_CHANGED__TYPE__PROXIMITY_SCREEN_OFF_WAKE_LOCK = 32;
    public static final int WAKELOCK_STATE_CHANGED__TYPE__SCREEN_BRIGHT_WAKE_LOCK = 10;
    public static final int WAKELOCK_STATE_CHANGED__TYPE__SCREEN_DIM_WAKE_LOCK = 6;
    public static final int WAKEUP_ALARM_OCCURRED = 35;
    public static final int WAKEUP_ALARM_OCCURRED__APP_STANDBY_BUCKET__BUCKET_ACTIVE = 10;
    public static final int WAKEUP_ALARM_OCCURRED__APP_STANDBY_BUCKET__BUCKET_EXEMPTED = 5;
    public static final int WAKEUP_ALARM_OCCURRED__APP_STANDBY_BUCKET__BUCKET_FREQUENT = 30;
    public static final int WAKEUP_ALARM_OCCURRED__APP_STANDBY_BUCKET__BUCKET_NEVER = 50;
    public static final int WAKEUP_ALARM_OCCURRED__APP_STANDBY_BUCKET__BUCKET_RARE = 40;
    public static final int WAKEUP_ALARM_OCCURRED__APP_STANDBY_BUCKET__BUCKET_RESTRICTED = 45;
    public static final int WAKEUP_ALARM_OCCURRED__APP_STANDBY_BUCKET__BUCKET_UNKNOWN = 0;
    public static final int WAKEUP_ALARM_OCCURRED__APP_STANDBY_BUCKET__BUCKET_WORKING_SET = 20;
    public static final int WALL_CLOCK_TIME_SHIFTED = 45;
    public static final int WATCHDOG_ROLLBACK_OCCURRED = 147;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_APP_CRASH = 3;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_APP_NOT_RESPONDING = 4;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_EXPLICIT_HEALTH_CHECK = 2;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_NATIVE_CRASH = 1;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_NATIVE_CRASH_DURING_BOOT = 5;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_UNKNOWN = 0;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_BOOT_TRIGGERED = 4;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_FAILURE = 3;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_INITIATE = 1;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_SUCCESS = 2;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__UNKNOWN = 0;
    public static final int WIFI_ACTIVITY_INFO = 10011;
    public static final int WIFI_BYTES_TRANSFER = 10000;
    public static final int WIFI_BYTES_TRANSFER_BY_FG_BG = 10001;
    public static final int WIFI_ENABLED_STATE_CHANGED = 113;
    public static final int WIFI_ENABLED_STATE_CHANGED__STATE__OFF = 0;
    public static final int WIFI_ENABLED_STATE_CHANGED__STATE__ON = 1;
    public static final int WIFI_RADIO_POWER_STATE_CHANGED = 13;
    public static final int WIFI_RADIO_POWER_STATE_CHANGED__STATE__DATA_CONNECTION_POWER_STATE_HIGH = 3;
    public static final int WIFI_RADIO_POWER_STATE_CHANGED__STATE__DATA_CONNECTION_POWER_STATE_LOW = 1;
    public static final int WIFI_RADIO_POWER_STATE_CHANGED__STATE__DATA_CONNECTION_POWER_STATE_MEDIUM = 2;
    public static final int WIFI_RADIO_POWER_STATE_CHANGED__STATE__DATA_CONNECTION_POWER_STATE_UNKNOWN = Integer.MAX_VALUE;
    public static final int WIFI_RUNNING_STATE_CHANGED = 114;
    public static final int WIFI_RUNNING_STATE_CHANGED__STATE__OFF = 0;
    public static final int WIFI_RUNNING_STATE_CHANGED__STATE__ON = 1;
    public static final int WTFOCCURRED__ERROR_SOURCE__DATA_APP = 1;
    public static final int WTFOCCURRED__ERROR_SOURCE__ERROR_SOURCE_UNKNOWN = 0;
    public static final int WTFOCCURRED__ERROR_SOURCE__SYSTEM_APP = 2;
    public static final int WTFOCCURRED__ERROR_SOURCE__SYSTEM_SERVER = 3;
    public static final int WTF_OCCURRED = 80;

    public static void write(int code) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, byte[] arg1, float arg2, int arg3, int arg4, int arg5, float arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeByteArray(arg1 == null ? new byte[0] : arg1);
        builder.writeFloat(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeFloat(arg6);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, int arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        if (12 == code) {
            builder.addBooleanAnnotation((byte) 2, true);
        }
        if (23 == code) {
            builder.addBooleanAnnotation((byte) 2, true);
        }
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        builder.writeInt(arg2);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, int arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, int arg2, int arg3, String arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeString(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, int arg2, long arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        builder.writeInt(arg2);
        builder.writeLong(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, int arg2, String arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        if (10 == code) {
            builder.addBooleanAnnotation((byte) 5, true);
        }
        builder.writeInt(arg2);
        if (10 == code) {
            builder.addBooleanAnnotation((byte) 3, true);
        }
        builder.writeString(arg3);
        if (10 == code) {
            builder.addBooleanAnnotation((byte) 3, true);
        }
        builder.writeInt(arg4);
        if (10 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, true);
        }
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, int arg2, String arg3, int arg4, int arg5, int arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        if (327 == code) {
            builder.addBooleanAnnotation((byte) 5, true);
        }
        builder.writeInt(arg2);
        if (327 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        builder.writeString(arg3);
        if (327 == code) {
            builder.addBooleanAnnotation((byte) 3, true);
        }
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        if (327 == code) {
            builder.addBooleanAnnotation((byte) 3, true);
        }
        builder.writeInt(arg6);
        if (327 == code) {
            builder.addBooleanAnnotation((byte) 3, true);
        }
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, String arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, String arg2, int arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, String arg2, int arg3, int arg4, int arg5, int arg6, boolean arg7, boolean arg8, boolean arg9, boolean arg10, boolean arg11, boolean arg12, boolean arg13, boolean arg14, boolean arg15, boolean arg16, int arg17) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeBoolean(arg7);
        builder.writeBoolean(arg8);
        builder.writeBoolean(arg9);
        builder.writeBoolean(arg10);
        builder.writeBoolean(arg11);
        builder.writeBoolean(arg12);
        builder.writeBoolean(arg13);
        builder.writeBoolean(arg14);
        builder.writeBoolean(arg15);
        builder.writeBoolean(arg16);
        builder.writeInt(arg17);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int[] uid, String[] tag, String arg2, String arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeAttributionChain(uid == null ? new int[0] : uid, tag == null ? new String[0] : tag);
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, boolean arg1, boolean arg2, boolean arg3, boolean arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeBoolean(arg1);
        builder.writeBoolean(arg2);
        builder.writeBoolean(arg3);
        builder.writeBoolean(arg4);
        builder.writeInt(arg5);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, boolean arg1, int arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeBoolean(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, boolean arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeBoolean(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, boolean arg1, String arg2, long arg3, int arg4, long arg5, int arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeBoolean(arg1);
        builder.writeString(arg2);
        builder.writeLong(arg3);
        builder.writeInt(arg4);
        builder.writeLong(arg5);
        builder.writeInt(arg6);
        if (263 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, boolean arg1, String arg2, long arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeBoolean(arg1);
        builder.writeString(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, boolean arg1, String arg2, String arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeBoolean(arg1);
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        if (40 == code) {
            builder.addBooleanAnnotation((byte) 2, true);
        }
        builder.writeInt(arg1);
        if (14 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        if (15 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
        }
        if (20 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        if (21 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        if (22 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        if (29 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        if (31 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        if (32 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, boolean arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (350 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        builder.writeBoolean(arg2);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6, int arg7, boolean arg8, int arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (368 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeBoolean(arg2);
        builder.writeBoolean(arg3);
        builder.writeBoolean(arg4);
        builder.writeBoolean(arg5);
        builder.writeBoolean(arg6);
        builder.writeInt(arg7);
        builder.writeBoolean(arg8);
        builder.writeInt(arg9);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (27 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
            builder.addBooleanAnnotation((byte) 3, true);
        }
        if (276 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg2);
        if (27 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, boolean arg3, boolean arg4, boolean arg5, int arg6, long arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeBoolean(arg3);
        builder.writeBoolean(arg4);
        builder.writeBoolean(arg5);
        builder.writeInt(arg6);
        builder.writeLong(arg7);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, boolean arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeBoolean(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, boolean arg3, int arg4, boolean arg5, int arg6, long arg7, boolean arg8, int arg9, float arg10) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeBoolean(arg3);
        builder.writeInt(arg4);
        builder.writeBoolean(arg5);
        builder.writeInt(arg6);
        builder.writeLong(arg7);
        builder.writeBoolean(arg8);
        builder.writeInt(arg9);
        builder.writeFloat(arg10);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, boolean arg3, int arg4, int arg5, int arg6, int arg7, boolean arg8, int arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeBoolean(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeBoolean(arg8);
        builder.writeInt(arg9);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, boolean arg3, int arg4, int arg5, int arg6, int arg7, boolean arg8, long arg9, int arg10) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeBoolean(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeBoolean(arg8);
        builder.writeLong(arg9);
        builder.writeInt(arg10);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (280 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (339 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (340 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, int arg3, boolean arg4, boolean arg5, boolean arg6, int arg7, int arg8, String arg9, String arg10, int arg11, int arg12, boolean arg13, boolean arg14, int arg15) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeBoolean(arg4);
        builder.writeBoolean(arg5);
        builder.writeBoolean(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeString(arg9);
        builder.writeString(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeBoolean(arg13);
        builder.writeBoolean(arg14);
        builder.writeInt(arg15);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, int arg3, int arg4, boolean arg5, boolean arg6, int arg7, int arg8) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        if (341 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeBoolean(arg5);
        builder.writeBoolean(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, int arg3, int arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (256 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (279 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (310 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, int arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeLong(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, int arg3, String arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeString(arg4);
        builder.writeInt(arg5);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, int arg3, String arg4, String arg5, boolean arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeString(arg4);
        builder.writeString(arg5);
        builder.writeBoolean(arg6);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, long arg3, boolean arg4, int arg5, float arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeLong(arg3);
        builder.writeBoolean(arg4);
        builder.writeInt(arg5);
        builder.writeFloat(arg6);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, float arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13, int arg14, int arg15, int arg16, int arg17) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeFloat(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeInt(arg13);
        builder.writeInt(arg14);
        builder.writeInt(arg15);
        builder.writeInt(arg16);
        builder.writeInt(arg17);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, String arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeString(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, String arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        if (90 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, String arg3, int arg4, int arg5, boolean arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        if (281 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeBoolean(arg6);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, String arg3, int arg4, int arg5, int arg6, boolean arg7, int arg8, boolean arg9, boolean arg10) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        if (246 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeBoolean(arg7);
        builder.writeInt(arg8);
        builder.writeBoolean(arg9);
        builder.writeBoolean(arg10);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, String arg3, int arg4, int arg5, int arg6, int arg7, int arg8, boolean arg9, String arg10, int arg11, int arg12, int arg13, int arg14, int arg15, int arg16, int arg17, int arg18, int arg19, int arg20, float arg21) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        if (244 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeBoolean(arg9);
        builder.writeString(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeInt(arg13);
        builder.writeInt(arg14);
        builder.writeInt(arg15);
        builder.writeInt(arg16);
        builder.writeInt(arg17);
        builder.writeInt(arg18);
        builder.writeInt(arg19);
        builder.writeInt(arg20);
        builder.writeFloat(arg21);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, String arg3, int arg4, int arg5, int arg6, int arg7, long arg8, int arg9, int arg10, int arg11, int arg12, String arg13) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeLong(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeString(arg13);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, String arg3, int arg4, long arg5, int arg6, int arg7, String arg8, String arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (169 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeLong(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeString(arg8);
        builder.writeString(arg9);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, int arg2, String arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeString(arg3);
        builder.writeLong(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, long arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeLong(arg2);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, long arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeLong(arg2);
        builder.writeInt(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, long arg2, int arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (228 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeLong(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, long arg2, long arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, long arg2, long arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (298 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (299 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (300 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, long arg2, long arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, long arg2, long arg3, long arg4, long arg5, long arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, boolean arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (59 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
            builder.addBooleanAnnotation((byte) 3, true);
        }
        builder.writeString(arg2);
        if (59 == code) {
            builder.addBooleanAnnotation((byte) 3, true);
        }
        builder.writeBoolean(arg3);
        builder.writeInt(arg4);
        if (59 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (28 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (52 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, boolean arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (178 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeBoolean(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, boolean arg4, int arg5, int arg6, int arg7, int arg8, int arg9, boolean arg10, boolean arg11, int arg12, int arg13, int arg14) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (60 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeBoolean(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeBoolean(arg10);
        builder.writeBoolean(arg11);
        builder.writeInt(arg12);
        builder.writeInt(arg13);
        builder.writeInt(arg14);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, boolean arg4, long arg5, byte[] arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeBoolean(arg4);
        builder.writeLong(arg5);
        builder.writeByteArray(arg6 == null ? new byte[0] : arg6);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (373 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, int arg4, String arg5, byte[] arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeString(arg5);
        builder.writeByteArray(arg6 == null ? new byte[0] : arg6);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, int arg10, int arg11, int arg12, int arg13, int arg14, int arg15) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (51 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeInt(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeInt(arg13);
        builder.writeInt(arg14);
        builder.writeInt(arg15);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, int arg13, long arg14, int arg15, int arg16, long arg17, long arg18) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeLong(arg10);
        builder.writeLong(arg11);
        builder.writeLong(arg12);
        builder.writeInt(arg13);
        builder.writeLong(arg14);
        builder.writeInt(arg15);
        builder.writeInt(arg16);
        builder.writeLong(arg17);
        builder.writeLong(arg18);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, String arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (49 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeString(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, String arg4, boolean arg5, long arg6, int arg7, int arg8, int arg9, int arg10, boolean arg11, boolean arg12, int arg13) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (50 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeString(arg4);
        builder.writeBoolean(arg5);
        builder.writeLong(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeBoolean(arg11);
        builder.writeBoolean(arg12);
        builder.writeInt(arg13);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, String arg4, int arg5, int arg6, boolean arg7, int arg8, int arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeString(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeBoolean(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, String arg4, String arg5, boolean arg6, long arg7, int arg8, int arg9, int arg10, int arg11, int arg12, String arg13, int arg14, int arg15, int arg16, int arg17, boolean arg18, boolean arg19, boolean arg20, int arg21, long arg22) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (48 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeString(arg4);
        builder.writeString(arg5);
        builder.writeBoolean(arg6);
        builder.writeLong(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeString(arg13);
        builder.writeInt(arg14);
        builder.writeInt(arg15);
        builder.writeInt(arg16);
        builder.writeInt(arg17);
        builder.writeBoolean(arg18);
        builder.writeBoolean(arg19);
        builder.writeBoolean(arg20);
        builder.writeInt(arg21);
        builder.writeLong(arg22);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, int arg3, String arg4, String arg5, String arg6, int arg7, int arg8, int arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (44 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeString(arg4);
        builder.writeString(arg5);
        builder.writeString(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, long arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (70 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
            builder.addBooleanAnnotation((byte) 8, false);
        }
        builder.writeString(arg2);
        if (70 == code) {
            builder.addBooleanAnnotation((byte) 3, true);
        }
        builder.writeLong(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (100 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (42 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (99 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (269 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, int arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (80 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, int arg4, long arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeLong(arg5);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, int arg4, String arg5, int arg6, int arg7, int arg8, boolean arg9, float arg10, long arg11, int arg12, int arg13, boolean arg14, long arg15, long arg16, int arg17, int arg18, int arg19, long arg20, int arg21, long arg22) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (78 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeString(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeBoolean(arg9);
        builder.writeFloat(arg10);
        builder.writeLong(arg11);
        builder.writeInt(arg12);
        builder.writeInt(arg13);
        builder.writeBoolean(arg14);
        builder.writeLong(arg15);
        builder.writeLong(arg16);
        builder.writeInt(arg17);
        builder.writeInt(arg18);
        builder.writeInt(arg19);
        builder.writeLong(arg20);
        builder.writeInt(arg21);
        builder.writeLong(arg22);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeLong(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, long arg4, long arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, long arg4, long arg5, long arg6, int arg7, long arg8, long arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeInt(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, long arg4, long arg5, long arg6, long arg7, long arg8) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (55 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, String arg2, String arg3, String arg4, int arg5, int arg6, int arg7, String arg8, boolean arg9, float arg10, long arg11, int arg12, int arg13, boolean arg14, long arg15, long arg16, int arg17, int arg18, int arg19, long arg20, int arg21, long arg22) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (79 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeString(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeString(arg8);
        builder.writeBoolean(arg9);
        builder.writeFloat(arg10);
        builder.writeLong(arg11);
        builder.writeInt(arg12);
        builder.writeInt(arg13);
        builder.writeBoolean(arg14);
        builder.writeLong(arg15);
        builder.writeLong(arg16);
        builder.writeInt(arg17);
        builder.writeInt(arg18);
        builder.writeInt(arg19);
        builder.writeLong(arg20);
        builder.writeInt(arg21);
        builder.writeLong(arg22);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, SparseArray<Object> valueMap) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        int count = valueMap.size();
        SparseIntArray intMap = null;
        SparseLongArray longMap = null;
        SparseArray<String> stringMap = null;
        SparseArray<Float> floatMap = null;
        for (int i = 0; i < count; i++) {
            int key = valueMap.keyAt(i);
            Object value = valueMap.valueAt(i);
            if (value instanceof Integer) {
                if (intMap == null) {
                    intMap = new SparseIntArray();
                }
                intMap.put(key, ((Integer) value).intValue());
            } else if (value instanceof Long) {
                if (longMap == null) {
                    longMap = new SparseLongArray();
                }
                longMap.put(key, ((Long) value).longValue());
            } else if (value instanceof String) {
                if (stringMap == null) {
                    stringMap = new SparseArray<>();
                }
                stringMap.put(key, (String) value);
            } else if (value instanceof Float) {
                if (floatMap == null) {
                    floatMap = new SparseArray<>();
                }
                floatMap.put(key, (Float) value);
            }
        }
        builder.writeKeyValuePairs(intMap, longMap, stringMap, floatMap);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, long arg1) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        if (65 == code) {
            builder.addBooleanAnnotation((byte) 4, true);
        }
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, long arg1, int arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, long arg1, int arg2, int arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, long arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, long arg1, int arg2, String arg3, int arg4, String arg5, int arg6, boolean arg7, int arg8, int arg9, int arg10, long arg11, long arg12, boolean arg13, int arg14, byte[] arg15, byte[] arg16, byte[] arg17, byte[] arg18, byte[] arg19) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeInt(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeString(arg5);
        builder.writeInt(arg6);
        builder.writeBoolean(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeLong(arg11);
        builder.writeLong(arg12);
        builder.writeBoolean(arg13);
        builder.writeInt(arg14);
        builder.writeByteArray(arg15 == null ? new byte[0] : arg15);
        builder.writeByteArray(arg16 == null ? new byte[0] : arg16);
        builder.writeByteArray(arg17 == null ? new byte[0] : arg17);
        builder.writeByteArray(arg18 == null ? new byte[0] : arg18);
        builder.writeByteArray(arg19 == null ? new byte[0] : arg19);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, long arg1, long arg2, int arg3, boolean arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeLong(arg2);
        builder.writeInt(arg3);
        builder.writeBoolean(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, long arg1, String arg2, long arg3, long arg4, String arg5, String arg6, String arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13, int arg14, int arg15, int arg16, int arg17, int arg18, String arg19, long arg20, long arg21, long arg22, long arg23, long arg24, int arg25, int arg26, String arg27, int arg28, long arg29, long arg30, String arg31, int arg32, int arg33, long arg34, long arg35, long arg36, long arg37, long arg38, int arg39, float arg40, float arg41, float arg42, int arg43, int arg44, int arg45, int arg46, int arg47, int arg48, int arg49, int arg50, int arg51, int arg52, int arg53, int arg54, int arg55, int arg56, int arg57) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeString(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeString(arg5);
        builder.writeString(arg6);
        builder.writeString(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeInt(arg13);
        builder.writeInt(arg14);
        builder.writeInt(arg15);
        builder.writeInt(arg16);
        builder.writeInt(arg17);
        builder.writeInt(arg18);
        builder.writeString(arg19);
        builder.writeLong(arg20);
        builder.writeLong(arg21);
        builder.writeLong(arg22);
        builder.writeLong(arg23);
        builder.writeLong(arg24);
        builder.writeInt(arg25);
        builder.writeInt(arg26);
        builder.writeString(arg27);
        builder.writeInt(arg28);
        builder.writeLong(arg29);
        builder.writeLong(arg30);
        builder.writeString(arg31);
        builder.writeInt(arg32);
        builder.writeInt(arg33);
        builder.writeLong(arg34);
        builder.writeLong(arg35);
        builder.writeLong(arg36);
        builder.writeLong(arg37);
        builder.writeLong(arg38);
        builder.writeInt(arg39);
        builder.writeFloat(arg40);
        builder.writeFloat(arg41);
        builder.writeFloat(arg42);
        builder.writeInt(arg43);
        builder.writeInt(arg44);
        builder.writeInt(arg45);
        builder.writeInt(arg46);
        builder.writeInt(arg47);
        builder.writeInt(arg48);
        builder.writeInt(arg49);
        builder.writeInt(arg50);
        builder.writeInt(arg51);
        builder.writeInt(arg52);
        builder.writeInt(arg53);
        builder.writeInt(arg54);
        builder.writeInt(arg55);
        builder.writeInt(arg56);
        builder.writeInt(arg57);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, int arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, int arg2, boolean arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeBoolean(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, int arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, int arg2, int arg3, int arg4, boolean arg5, boolean arg6, int arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeBoolean(arg5);
        builder.writeBoolean(arg6);
        builder.writeInt(arg7);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, int arg2, int arg3, int arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, int arg2, int arg3, int arg4, int arg5, String arg6, int arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeString(arg6);
        builder.writeInt(arg7);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, int arg2, int arg3, float arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeFloat(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, int arg2, String arg3, int arg4, int arg5, int arg6, boolean arg7, int arg8, int arg9, boolean arg10, int arg11, int arg12, String arg13) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeBoolean(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeBoolean(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeString(arg13);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, int arg2, String arg3, int arg4, String arg5, int arg6, boolean arg7, boolean arg8, boolean arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeString(arg5);
        builder.writeInt(arg6);
        builder.writeBoolean(arg7);
        builder.writeBoolean(arg8);
        builder.writeBoolean(arg9);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, long arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeLong(arg2);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, long arg2, long arg3, boolean arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeBoolean(arg4);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, String arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeString(arg2);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, String arg2, int arg3, int arg4, int arg5, int arg6, float arg7, float arg8, boolean arg9, boolean arg10, boolean arg11) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeFloat(arg7);
        builder.writeFloat(arg8);
        builder.writeBoolean(arg9);
        builder.writeBoolean(arg10);
        builder.writeBoolean(arg11);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, String arg2, long arg3, String arg4, int arg5, boolean arg6, boolean arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeString(arg2);
        builder.writeLong(arg3);
        builder.writeString(arg4);
        builder.writeInt(arg5);
        builder.writeBoolean(arg6);
        builder.writeBoolean(arg7);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write(int code, String arg1, String arg2, String arg3, String arg4, String arg5, boolean arg6, int arg7, int arg8) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeString(arg4);
        builder.writeString(arg5);
        builder.writeBoolean(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.usePooledBuffer();
        StatsLog.write(builder.build());
    }

    public static void write_non_chained(int code, int arg1, String arg2, int arg3) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3);
    }

    public static void write_non_chained(int code, int arg1, String arg2, int arg3, int arg4) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3, arg4);
    }

    public static void write_non_chained(int code, int arg1, String arg2, int arg3, int arg4, String arg5) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3, arg4, arg5);
    }

    public static void write_non_chained(int code, int arg1, String arg2, int arg3, long arg4) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3, arg4);
    }

    public static void write_non_chained(int code, int arg1, String arg2, int arg3, String arg4, int arg5) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3, arg4, arg5);
    }

    public static void write_non_chained(int code, int arg1, String arg2, int arg3, String arg4, int arg5, int arg6, int arg7) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3, arg4, arg5, arg6, arg7);
    }

    public static void write_non_chained(int code, int arg1, String arg2, String arg3, int arg4) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3, arg4);
    }

    public static void write_non_chained(int code, int arg1, String arg2, String arg3, int arg4, int arg5) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3, arg4, arg5);
    }

    public static void write_non_chained(int code, int arg1, String arg2, String arg3, int arg4, int arg5, int arg6, int arg7, boolean arg8, boolean arg9, boolean arg10, boolean arg11, boolean arg12, boolean arg13, boolean arg14, boolean arg15, boolean arg16, boolean arg17, int arg18) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
    }

    public static void write_non_chained(int code, int arg1, String arg2, String arg3, String arg4, int arg5) {
        write(code, new int[]{arg1}, new String[]{arg2}, arg3, arg4, arg5);
    }

    public static StatsEvent buildStatsEvent(int code, byte[] arg1) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeByteArray(arg1 == null ? new byte[0] : arg1);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, byte[] arg1, int arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeByteArray(arg1 == null ? new byte[0] : arg1);
        builder.writeInt(arg2);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, boolean arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeBoolean(arg1);
        builder.writeBoolean(arg2);
        builder.writeBoolean(arg3);
        builder.writeBoolean(arg4);
        builder.writeBoolean(arg5);
        builder.writeBoolean(arg6);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, boolean arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5, int arg6, int arg7, byte[] arg8, byte[] arg9, byte[] arg10) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeBoolean(arg1);
        builder.writeBoolean(arg2);
        builder.writeBoolean(arg3);
        builder.writeBoolean(arg4);
        builder.writeBoolean(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeByteArray(arg8 == null ? new byte[0] : arg8);
        builder.writeByteArray(arg9 == null ? new byte[0] : arg9);
        builder.writeByteArray(arg10 == null ? new byte[0] : arg10);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10114 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, boolean arg2, boolean arg3, int arg4, String arg5, int arg6, byte[] arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeBoolean(arg2);
        builder.writeBoolean(arg3);
        builder.writeInt(arg4);
        builder.writeString(arg5);
        builder.writeInt(arg6);
        if (10084 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeByteArray(arg7 == null ? new byte[0] : arg7);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, boolean arg2, int arg3, int arg4, long arg5, long arg6, long arg7, long arg8) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10100 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeBoolean(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, boolean arg2, int arg3, long arg4, long arg5, long arg6, long arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        if (10083 == code) {
            builder.addBooleanAnnotation((byte) 2, true);
        }
        builder.writeInt(arg1);
        if (10083 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeBoolean(arg2);
        builder.writeInt(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, boolean arg2, long arg3, long arg4, long arg5, long arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        if (10003 == code) {
            builder.addBooleanAnnotation((byte) 2, true);
        }
        builder.writeInt(arg1);
        if (10001 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (10003 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeBoolean(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, boolean arg3, int arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeBoolean(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, int arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10071 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, int arg3, int arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, int arg3, int arg4, int arg5, boolean arg6, int arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeBoolean(arg6);
        builder.writeInt(arg7);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeInt(arg13);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, int arg3, String arg4, String arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13, int arg14, int arg15, int arg16, int arg17, int arg18, int arg19, int arg20, int arg21) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10037 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeString(arg4);
        builder.writeString(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeInt(arg11);
        builder.writeInt(arg12);
        builder.writeInt(arg13);
        builder.writeInt(arg14);
        builder.writeInt(arg15);
        builder.writeInt(arg16);
        builder.writeInt(arg17);
        builder.writeInt(arg18);
        builder.writeInt(arg19);
        builder.writeInt(arg20);
        builder.writeInt(arg21);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, long arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10010 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (10017 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg2);
        builder.writeLong(arg3);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, long arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeInt(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, int arg2, long arg3, long arg4, long arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10096 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeInt(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, long arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10016 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (10040 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeLong(arg2);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, long arg2, long arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10006 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (10009 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, long arg2, long arg3, long arg4, long arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        if (10002 == code) {
            builder.addBooleanAnnotation((byte) 2, true);
        }
        builder.writeInt(arg1);
        if (10000 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        if (10002 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, long arg2, long arg3, long arg4, long arg5, int arg6, String arg7, String arg8, int arg9, int arg10, boolean arg11) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        if (10082 == code) {
            builder.addBooleanAnnotation((byte) 2, true);
        }
        builder.writeInt(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeInt(arg6);
        builder.writeString(arg7);
        builder.writeString(arg8);
        builder.writeInt(arg9);
        builder.writeInt(arg10);
        builder.writeBoolean(arg11);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10032 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeLong(arg10);
        builder.writeLong(arg11);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, int arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, int arg3, int arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10061 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10105 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10064 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        builder.writeInt(arg8);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, int arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, boolean arg10) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10060 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeBoolean(arg10);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, int arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, int arg11) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10013 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeLong(arg10);
        builder.writeInt(arg11);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, long arg3, int arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10042 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeLong(arg3);
        builder.writeInt(arg4);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, long arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10035 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, String arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10049 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, String arg3, int arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, boolean arg11, int arg12) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10075 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeInt(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeLong(arg10);
        builder.writeBoolean(arg11);
        builder.writeInt(arg12);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, String arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, boolean arg13, int arg14) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10022 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeLong(arg10);
        builder.writeLong(arg11);
        builder.writeLong(arg12);
        builder.writeBoolean(arg13);
        builder.writeInt(arg14);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, String arg3, String arg4, boolean arg5, int arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10073 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeString(arg4);
        builder.writeBoolean(arg5);
        builder.writeInt(arg6);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, String arg3, String arg4, int arg5, int arg6, boolean arg7, boolean arg8, boolean arg9, boolean arg10) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10072 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeString(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        builder.writeBoolean(arg7);
        builder.writeBoolean(arg8);
        builder.writeBoolean(arg9);
        builder.writeBoolean(arg10);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, String arg3, String arg4, long arg5, long arg6, long arg7, long arg8, long arg9, boolean arg10, long arg11, long arg12, long arg13, long arg14, long arg15) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10024 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeString(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeBoolean(arg10);
        builder.writeLong(arg11);
        builder.writeLong(arg12);
        builder.writeLong(arg13);
        builder.writeLong(arg14);
        builder.writeLong(arg15);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, int arg1, String arg2, String arg3, String arg4, String arg5, int arg6, int arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        if (10069 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeString(arg4);
        builder.writeString(arg5);
        builder.writeInt(arg6);
        builder.writeInt(arg7);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, long arg1) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, long arg1, boolean arg2, int arg3) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeBoolean(arg2);
        builder.writeInt(arg3);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, long arg1, int arg2, long arg3, long arg4, long arg5, long arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeInt(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, long arg1, long arg2, long arg3, byte[] arg4, byte[] arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeByteArray(arg4 == null ? new byte[0] : arg4);
        builder.writeByteArray(arg5 == null ? new byte[0] : arg5);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, long arg1, long arg2, long arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, long arg1, long arg2, long arg3, long arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeInt(arg5);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeLong(arg10);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeLong(arg10);
        builder.writeLong(arg11);
        builder.writeLong(arg12);
        builder.writeLong(arg13);
        builder.writeLong(arg14);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15, long arg16) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        builder.writeLong(arg6);
        builder.writeLong(arg7);
        builder.writeLong(arg8);
        builder.writeLong(arg9);
        builder.writeLong(arg10);
        builder.writeLong(arg11);
        builder.writeLong(arg12);
        builder.writeLong(arg13);
        builder.writeLong(arg14);
        builder.writeLong(arg15);
        builder.writeLong(arg16);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, String arg1) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, String arg1, int arg2, boolean arg3, int arg4, int arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        if (10067 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeBoolean(arg3);
        builder.writeInt(arg4);
        builder.writeInt(arg5);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, String arg1, int arg2, boolean arg3, int arg4, float arg5, String arg6, int arg7) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeBoolean(arg3);
        builder.writeInt(arg4);
        builder.writeFloat(arg5);
        builder.writeString(arg6);
        builder.writeInt(arg7);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, String arg1, int arg2, int arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeLong(arg4);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, String arg1, int arg2, String arg3, boolean arg4, int arg5, int arg6) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeInt(arg2);
        if (10050 == code) {
            builder.addBooleanAnnotation((byte) 1, true);
        }
        builder.writeString(arg3);
        builder.writeBoolean(arg4);
        builder.writeInt(arg5);
        builder.writeInt(arg6);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, String arg1, long arg2) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeLong(arg2);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, String arg1, long arg2, long arg3, long arg4, long arg5) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeLong(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        builder.writeLong(arg5);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, String arg1, String arg2, long arg3, long arg4) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeString(arg2);
        builder.writeLong(arg3);
        builder.writeLong(arg4);
        return builder.build();
    }

    public static StatsEvent buildStatsEvent(int code, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9) {
        StatsEvent.Builder builder = StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeString(arg1);
        builder.writeString(arg2);
        builder.writeString(arg3);
        builder.writeString(arg4);
        builder.writeString(arg5);
        builder.writeString(arg6);
        builder.writeString(arg7);
        builder.writeString(arg8);
        builder.writeString(arg9);
        return builder.build();
    }

    public static void write(int code, WorkSource ws, int arg2) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2);
            }
        }
    }

    public static void write(int code, WorkSource ws, int arg2, int arg3) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2, arg3);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2, arg3);
            }
        }
    }

    public static void write(int code, WorkSource ws, int arg2, int arg3, String arg4) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2, arg3, arg4);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2, arg3, arg4);
            }
        }
    }

    public static void write(int code, WorkSource ws, int arg2, long arg3) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2, arg3);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2, arg3);
            }
        }
    }

    public static void write(int code, WorkSource ws, int arg2, String arg3, int arg4) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2, arg3, arg4);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2, arg3, arg4);
            }
        }
    }

    public static void write(int code, WorkSource ws, int arg2, String arg3, int arg4, int arg5, int arg6) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2, arg3, arg4, arg5, arg6);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2, arg3, arg4, arg5, arg6);
            }
        }
    }

    public static void write(int code, WorkSource ws, String arg2, int arg3) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2, arg3);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2, arg3);
            }
        }
    }

    public static void write(int code, WorkSource ws, String arg2, int arg3, int arg4) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2, arg3, arg4);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2, arg3, arg4);
            }
        }
    }

    public static void write(int code, WorkSource ws, String arg2, int arg3, int arg4, int arg5, int arg6, boolean arg7, boolean arg8, boolean arg9, boolean arg10, boolean arg11, boolean arg12, boolean arg13, boolean arg14, boolean arg15, boolean arg16, int arg17) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
            }
        }
    }

    public static void write(int code, WorkSource ws, String arg2, String arg3, int arg4) {
        for (int i = 0; i < ws.size(); i++) {
            write_non_chained(code, ws.getUid(i), ws.getPackageName(i), arg2, arg3, arg4);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (WorkSource.WorkChain wc : workChains) {
                write(code, wc.getUids(), wc.getTags(), arg2, arg3, arg4);
            }
        }
    }
}
