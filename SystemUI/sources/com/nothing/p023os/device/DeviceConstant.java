package com.nothing.p023os.device;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, mo65043d2 = {"Lcom/nothing/os/device/DeviceConstant;", "", "()V", "Companion", "osConnect_SnapshotRelease"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* renamed from: com.nothing.os.device.DeviceConstant */
/* compiled from: DeviceConstant.kt */
public final class DeviceConstant {
    public static final int CONNECT = 0;
    public static final String CONTROL_ACTION = "com.nothing.os.device.intent.action.BIND_DEVICE_SERVICE";
    public static final String CONTROL_CLS_NAME = "com.nothing.os.device.DeviceControlService";
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int DISCONNECT = 2;
    public static final int ERROR_NOT_IN_CASE = -2;
    public static final int ERROR_NO_PERMISSION = -1;
    public static final int ERROR_UN_SUPPORT_DEVICE = -10;
    public static final int GET_ANC_LEVEL = 6;
    public static final int GET_BATTERY = 4;
    public static final int GET_EAR_BITMAP = 3;
    public static final int GET_FUNCTION_LIST = 1;
    public static final int GET_MODEL_ID_AND_MAC = 7;
    public static final int GET_NT_DEVICE_NAME = 5;
    public static final int IS_SUPPORT_ANC = 9;
    public static final String KEY_BATTERY_CASE = "KEY_BATTERY_CASE";
    public static final String KEY_BATTERY_CASE_MODE = "KEY_BATTERY_CASE_MODE";
    public static final String KEY_BATTERY_LEFT = "KEY_BATTERY_LEFT";
    public static final String KEY_BATTERY_LEFT_MODE = "KEY_BATTERY_LEFT_MODE";
    public static final String KEY_BATTERY_RIGHT = "KEY_BATTERY_RIGHT";
    public static final String KEY_BATTERY_RIGHT_MODE = "KEY_BATTERY_RIGHT_MODE";
    public static final String KEY_BITMAP = "KEY_BITMAP";
    public static final String KEY_EAR_CONNECTED = "KEY_EAR_CONNECTED";
    public static final String KEY_FORM_PAGE = "KEY_FORM_PAGE";
    public static final String KEY_FUNCTION_LIST = "KEY_FUNCTION_LIST";
    public static final String KEY_IS_AIRPODS = "KEY_IS_AIRPODS";
    public static final String KEY_MAC_ADDRESS = "device_address";
    public static final String KEY_MODEL_ID = "KEY_MODEL_ID";
    public static final String KEY_SCAN_RESULT = "KEY_SCAN_RESULT";
    public static final String KEY_VALUE_BOOLEAN = "KEY_VALUE_BOOLEAN";
    public static final String KEY_VALUE_INT = "KEY_VALUE_INT";
    public static final String KEY_VALUE_STRING = "KEY_VALUE_STRING";
    public static final String NOISE_CANCELLATION_ADAPTIVE = "4";
    public static final String NOISE_CANCELLATION_HIGH = "1";
    public static final String NOISE_CANCELLATION_LOW = "3";
    public static final String NOISE_CANCELLATION_MID = "2";
    public static final String NOISE_CANCELLATION_OFF = "5";
    public static final String NOISE_CANCELLATION_TRANSPARENCY = "7";
    public static final int ORDER_ADVANCED_FEATURES = 701;
    public static final int ORDER_ANC = 610;
    public static final int ORDER_EQUALIZER = 620;
    public static final int ORDER_FIND_DEVICE = 720;
    public static final int ORDER_FIRMWARE_UPDATE = 1310;
    public static final int ORDER_GESTURE_CONTROLS = 651;
    public static final int ORDER_IN_EAR_DETECTION = 710;
    public static final int ORDER_LEFT_EARBUD = 660;
    public static final int ORDER_LOW_LATENCY = 715;
    public static final int ORDER_RIGHT_EARBUD = 670;
    public static final int ORDER_SEE_MORE = 730;
    public static final int ORDER_SELECT_AIRPODS_MODEL = 1320;
    public static final int ORDER_SERIAL_NUMBER = 1300;
    public static final int ORDER_SOUND = 601;
    public static final String PACKAGE = "com.nothing.smartcenter";
    public static final int PARSE_AIR_PODS = 10;
    public static final int SET_MAC_ADDRESS = 8;
    public static final int STYLE_ANC = 2;
    public static final int STYLE_ITEM = 4;
    public static final int STYLE_ITEM_REMOTE = 3;
    public static final int STYLE_ITEM_SWITCH = 5;
    public static final int STYLE_MODULE = 1;
    public static final int STYLE_SEE_MORE = 6;

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b;\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006A"}, mo65043d2 = {"Lcom/nothing/os/device/DeviceConstant$Companion;", "", "()V", "CONNECT", "", "CONTROL_ACTION", "", "CONTROL_CLS_NAME", "DISCONNECT", "ERROR_NOT_IN_CASE", "ERROR_NO_PERMISSION", "ERROR_UN_SUPPORT_DEVICE", "GET_ANC_LEVEL", "GET_BATTERY", "GET_EAR_BITMAP", "GET_FUNCTION_LIST", "GET_MODEL_ID_AND_MAC", "GET_NT_DEVICE_NAME", "IS_SUPPORT_ANC", "KEY_BATTERY_CASE", "KEY_BATTERY_CASE_MODE", "KEY_BATTERY_LEFT", "KEY_BATTERY_LEFT_MODE", "KEY_BATTERY_RIGHT", "KEY_BATTERY_RIGHT_MODE", "KEY_BITMAP", "KEY_EAR_CONNECTED", "KEY_FORM_PAGE", "KEY_FUNCTION_LIST", "KEY_IS_AIRPODS", "KEY_MAC_ADDRESS", "KEY_MODEL_ID", "KEY_SCAN_RESULT", "KEY_VALUE_BOOLEAN", "KEY_VALUE_INT", "KEY_VALUE_STRING", "NOISE_CANCELLATION_ADAPTIVE", "NOISE_CANCELLATION_HIGH", "NOISE_CANCELLATION_LOW", "NOISE_CANCELLATION_MID", "NOISE_CANCELLATION_OFF", "NOISE_CANCELLATION_TRANSPARENCY", "ORDER_ADVANCED_FEATURES", "ORDER_ANC", "ORDER_EQUALIZER", "ORDER_FIND_DEVICE", "ORDER_FIRMWARE_UPDATE", "ORDER_GESTURE_CONTROLS", "ORDER_IN_EAR_DETECTION", "ORDER_LEFT_EARBUD", "ORDER_LOW_LATENCY", "ORDER_RIGHT_EARBUD", "ORDER_SEE_MORE", "ORDER_SELECT_AIRPODS_MODEL", "ORDER_SERIAL_NUMBER", "ORDER_SOUND", "PACKAGE", "PARSE_AIR_PODS", "SET_MAC_ADDRESS", "STYLE_ANC", "STYLE_ITEM", "STYLE_ITEM_REMOTE", "STYLE_ITEM_SWITCH", "STYLE_MODULE", "STYLE_SEE_MORE", "osConnect_SnapshotRelease"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
    /* renamed from: com.nothing.os.device.DeviceConstant$Companion */
    /* compiled from: DeviceConstant.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
