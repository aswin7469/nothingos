package android.telephony;

import android.provider.CalendarContract;
/* loaded from: classes3.dex */
public final class DisconnectCause {
    public static final int ACCESS_INFORMATION_DISCARDED = 120;
    public static final int ALREADY_DIALING = 72;
    public static final int ANSWERED_ELSEWHERE = 52;
    public static final int BEARER_CAPABILITY_NOT_AUTHORIZED = 110;
    public static final int BEARER_CAPABILITY_UNAVAILABLE = 83;
    public static final int BEARER_SERVICE_NOT_IMPLEMENTED = 85;
    public static final int BUSY = 4;
    public static final int CALLING_DISABLED = 74;
    public static final int CALL_BARRED = 20;
    public static final int CALL_FAIL_DESTINATION_OUT_OF_ORDER = 109;
    public static final int CALL_FAIL_NO_ANSWER_FROM_USER = 108;
    public static final int CALL_FAIL_NO_USER_RESPONDING = 107;
    public static final int CALL_PULLED = 51;
    public static final int CALL_REJECTED = 112;
    public static final int CANT_CALL_WHILE_RINGING = 73;
    public static final int CDMA_ACCESS_BLOCKED = 35;
    public static final int CDMA_ACCESS_FAILURE = 32;
    public static final int CDMA_ALREADY_ACTIVATED = 49;
    public static final int CDMA_CALL_LOST = 41;
    public static final int CDMA_DROP = 27;
    public static final int CDMA_INTERCEPT = 28;
    public static final int CDMA_LOCKED_UNTIL_POWER_CYCLE = 26;
    public static final int CDMA_NOT_EMERGENCY = 34;
    public static final int CDMA_PREEMPTED = 33;
    public static final int CDMA_REORDER = 29;
    public static final int CDMA_RETRY_ORDER = 31;
    public static final int CDMA_SO_REJECT = 30;
    public static final int CHANNEL_UNACCEPTABLE = 111;
    public static final int CONCURRENT_CALLS_NOT_POSSIBLE = 127;
    public static final int CONDITIONAL_IE_ERROR = 98;
    public static final int CONGESTION = 5;
    public static final int CS_RESTRICTED = 22;
    public static final int CS_RESTRICTED_EMERGENCY = 24;
    public static final int CS_RESTRICTED_NORMAL = 23;
    public static final int DATA_DISABLED = 54;
    public static final int DATA_LIMIT_REACHED = 55;
    public static final int DIALED_CALL_FORWARDING_WHILE_ROAMING = 57;
    public static final int DIALED_MMI = 39;
    public static final int DIAL_LOW_BATTERY = 62;
    public static final int DIAL_MODIFIED_TO_DIAL = 48;
    public static final int DIAL_MODIFIED_TO_DIAL_VIDEO = 66;
    public static final int DIAL_MODIFIED_TO_SS = 47;
    public static final int DIAL_MODIFIED_TO_USSD = 46;
    public static final int DIAL_VIDEO_MODIFIED_TO_DIAL = 69;
    public static final int DIAL_VIDEO_MODIFIED_TO_DIAL_VIDEO = 70;
    public static final int DIAL_VIDEO_MODIFIED_TO_SS = 67;
    public static final int DIAL_VIDEO_MODIFIED_TO_USSD = 68;
    public static final int EMERGENCY_CALL_OVER_WFC_NOT_AVAILABLE = 78;
    public static final int EMERGENCY_ONLY = 37;
    public static final int EMERGENCY_PERM_FAILURE = 64;
    public static final int EMERGENCY_TEMP_FAILURE = 63;
    public static final int ERROR_UNSPECIFIED = 36;
    public static final int EXITED_ECM = 42;
    public static final int FACILITY_REJECTED = 115;
    public static final int FDN_BLOCKED = 21;
    public static final int HO_NOT_FEASIBLE = 125;
    public static final int ICC_ERROR = 19;
    public static final int IMEI_NOT_ACCEPTED = 58;
    public static final int IMS_ACCESS_BLOCKED = 60;
    public static final int IMS_MERGED_SUCCESSFULLY = 45;
    public static final int IMS_SIP_ALTERNATE_EMERGENCY_CALL = 71;
    public static final int INCOMING_AUTO_REJECTED = 81;
    public static final int INCOMING_CALLS_BARRED_WITHIN_CUG = 82;
    public static final int INCOMING_MISSED = 1;
    public static final int INCOMING_REJECTED = 16;
    public static final int INCOMPATIBLE_DESTINATION = 91;
    public static final int INFORMATION_ELEMENT_NON_EXISTENT = 97;
    public static final int INTERWORKING_UNSPECIFIED = 102;
    public static final int INVALID_CREDENTIALS = 10;
    public static final int INVALID_MANDATORY_INFORMATION = 94;
    public static final int INVALID_NUMBER = 7;
    public static final int INVALID_TRANSACTION_IDENTIFIER = 89;
    public static final int INVALID_TRANSIT_NW_SELECTION = 92;
    public static final int LIMIT_EXCEEDED = 15;
    public static final int LOCAL = 3;
    public static final int LOCAL_LOW_BATTERY = 103;
    public static final int LOST_SIGNAL = 14;
    public static final int LOW_BATTERY = 61;
    public static final int MAXIMUM_NUMBER_OF_CALLS_REACHED = 53;
    public static final int MAXIMUM_VALID_VALUE = 127;
    public static final int MEDIA_TIMEOUT = 77;
    public static final int MESSAGE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE = 99;
    public static final int MESSAGE_TYPE_NON_IMPLEMENTED = 95;
    public static final int MESSAGE_TYPE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE = 96;
    public static final int MINIMUM_VALID_VALUE = 0;
    public static final int MMI = 6;
    public static final int NETWORK_OUT_OF_ORDER = 117;
    public static final int NON_SELECTED_USER_CLEARING = 126;
    public static final int NORMAL = 2;
    public static final int NORMAL_UNSPECIFIED = 65;
    public static final int NOT_DISCONNECTED = 0;
    public static final int NOT_VALID = -1;
    public static final int NO_CIRCUIT_AVAIL = 104;
    public static final int NO_PHONE_NUMBER_SUPPLIED = 38;
    public static final int NO_ROUTE_TO_DESTINATION = 105;
    public static final int NUMBER_CHANGED = 113;
    public static final int NUMBER_UNREACHABLE = 8;
    public static final int ONLY_DIGITAL_INFORMATION_BEARER_AVAILABLE = 87;
    public static final int OPERATOR_DETERMINED_BARRING = 106;
    public static final int OTASP_PROVISIONING_IN_PROCESS = 76;
    public static final int OUTGOING_CANCELED = 44;
    public static final int OUTGOING_EMERGENCY_CALL_PLACED = 80;
    public static final int OUTGOING_FAILURE = 43;
    public static final int OUT_OF_NETWORK = 11;
    public static final int OUT_OF_SERVICE = 18;
    public static final int POWER_OFF = 17;
    public static final int PREEMPTION = 114;
    public static final int PROTOCOL_ERROR_UNSPECIFIED = 101;
    public static final int QOS_UNAVAILABLE = 123;
    public static final int RECOVERY_ON_TIMER_EXPIRED = 100;
    public static final int REQUESTED_CIRCUIT_OR_CHANNEL_NOT_AVAILABLE = 121;
    public static final int REQUESTED_FACILITY_NOT_IMPLEMENTED = 86;
    public static final int REQUESTED_FACILITY_NOT_SUBSCRIBED = 124;
    public static final int RESOURCES_UNAVAILABLE_OR_UNSPECIFIED = 122;
    public static final int RESP_TO_STATUS_ENQUIRY = 116;
    public static final int SEMANTICALLY_INCORRECT_MESSAGE = 93;
    public static final int SERVER_ERROR = 12;
    public static final int SERVER_UNREACHABLE = 9;
    public static final int SERVICE_OPTION_NOT_AVAILABLE = 84;
    public static final int SERVICE_OR_OPTION_NOT_IMPLEMENTED = 88;
    public static final int SWITCHING_EQUIPMENT_CONGESTION = 119;
    public static final int TEMPORARY_FAILURE = 118;
    public static final int TIMED_OUT = 13;
    public static final int TOO_MANY_ONGOING_CALLS = 75;
    public static final int UNOBTAINABLE_NUMBER = 25;
    public static final int USER_NOT_MEMBER_OF_CUG = 90;
    public static final int VIDEO_CALL_NOT_ALLOWED_WHILE_TTY_ENABLED = 50;
    public static final int VOICEMAIL_NUMBER_MISSING = 40;
    public static final int WFC_SERVICE_NOT_AVAILABLE_IN_THIS_LOCATION = 79;
    public static final int WIFI_LOST = 59;

    private DisconnectCause() {
    }

    public static String toString(int cause) {
        switch (cause) {
            case 0:
                return "NOT_DISCONNECTED";
            case 1:
                return "INCOMING_MISSED";
            case 2:
                return "NORMAL";
            case 3:
                return CalendarContract.ACCOUNT_TYPE_LOCAL;
            case 4:
                return "BUSY";
            case 5:
                return "CONGESTION";
            case 6:
            case 56:
            default:
                return "INVALID: " + cause;
            case 7:
                return "INVALID_NUMBER";
            case 8:
                return "NUMBER_UNREACHABLE";
            case 9:
                return "SERVER_UNREACHABLE";
            case 10:
                return "INVALID_CREDENTIALS";
            case 11:
                return "OUT_OF_NETWORK";
            case 12:
                return "SERVER_ERROR";
            case 13:
                return "TIMED_OUT";
            case 14:
                return "LOST_SIGNAL";
            case 15:
                return "LIMIT_EXCEEDED";
            case 16:
                return "INCOMING_REJECTED";
            case 17:
                return "POWER_OFF";
            case 18:
                return "OUT_OF_SERVICE";
            case 19:
                return "ICC_ERROR";
            case 20:
                return "CALL_BARRED";
            case 21:
                return "FDN_BLOCKED";
            case 22:
                return "CS_RESTRICTED";
            case 23:
                return "CS_RESTRICTED_NORMAL";
            case 24:
                return "CS_RESTRICTED_EMERGENCY";
            case 25:
                return "UNOBTAINABLE_NUMBER";
            case 26:
                return "CDMA_LOCKED_UNTIL_POWER_CYCLE";
            case 27:
                return "CDMA_DROP";
            case 28:
                return "CDMA_INTERCEPT";
            case 29:
                return "CDMA_REORDER";
            case 30:
                return "CDMA_SO_REJECT";
            case 31:
                return "CDMA_RETRY_ORDER";
            case 32:
                return "CDMA_ACCESS_FAILURE";
            case 33:
                return "CDMA_PREEMPTED";
            case 34:
                return "CDMA_NOT_EMERGENCY";
            case 35:
                return "CDMA_ACCESS_BLOCKED";
            case 36:
                return "ERROR_UNSPECIFIED";
            case 37:
                return "EMERGENCY_ONLY";
            case 38:
                return "NO_PHONE_NUMBER_SUPPLIED";
            case 39:
                return "DIALED_MMI";
            case 40:
                return "VOICEMAIL_NUMBER_MISSING";
            case 41:
                return "CDMA_CALL_LOST";
            case 42:
                return "EXITED_ECM";
            case 43:
                return "OUTGOING_FAILURE";
            case 44:
                return "OUTGOING_CANCELED";
            case 45:
                return "IMS_MERGED_SUCCESSFULLY";
            case 46:
                return "DIAL_MODIFIED_TO_USSD";
            case 47:
                return "DIAL_MODIFIED_TO_SS";
            case 48:
                return "DIAL_MODIFIED_TO_DIAL";
            case 49:
                return "CDMA_ALREADY_ACTIVATED";
            case 50:
                return "VIDEO_CALL_NOT_ALLOWED_WHILE_TTY_ENABLED";
            case 51:
                return "CALL_PULLED";
            case 52:
                return "ANSWERED_ELSEWHERE";
            case 53:
                return "MAXIMUM_NUMER_OF_CALLS_REACHED";
            case 54:
                return "DATA_DISABLED";
            case 55:
                return "DATA_LIMIT_REACHED";
            case 57:
                return "DIALED_CALL_FORWARDING_WHILE_ROAMING";
            case 58:
                return "IMEI_NOT_ACCEPTED";
            case 59:
                return "WIFI_LOST";
            case 60:
                return "IMS_ACCESS_BLOCKED";
            case 61:
                return "LOW_BATTERY";
            case 62:
                return "DIAL_LOW_BATTERY";
            case 63:
                return "EMERGENCY_TEMP_FAILURE";
            case 64:
                return "EMERGENCY_PERM_FAILURE";
            case 65:
                return "NORMAL_UNSPECIFIED";
            case 66:
                return "DIAL_MODIFIED_TO_DIAL_VIDEO";
            case 67:
                return "DIAL_VIDEO_MODIFIED_TO_SS";
            case 68:
                return "DIAL_VIDEO_MODIFIED_TO_USSD";
            case 69:
                return "DIAL_VIDEO_MODIFIED_TO_DIAL";
            case 70:
                return "DIAL_VIDEO_MODIFIED_TO_DIAL_VIDEO";
            case 71:
                return "IMS_SIP_ALTERNATE_EMERGENCY_CALL";
            case 72:
                return "ALREADY_DIALING";
            case 73:
                return "CANT_CALL_WHILE_RINGING";
            case 74:
                return "CALLING_DISABLED";
            case 75:
                return "TOO_MANY_ONGOING_CALLS";
            case 76:
                return "OTASP_PROVISIONING_IN_PROCESS";
            case 77:
                return "MEDIA_TIMEOUT";
            case 78:
                return "EMERGENCY_CALL_OVER_WFC_NOT_AVAILABLE";
            case 79:
                return "WFC_SERVICE_NOT_AVAILABLE_IN_THIS_LOCATION";
            case 80:
                return "OUTGOING_EMERGENCY_CALL_PLACED";
            case 81:
                return "INCOMING_AUTO_REJECTED";
            case 82:
                return "INCOMING_CALLS_BARRED_WITHIN_CUG";
            case 83:
                return "BEARER_CAPABILITY_UNAVAILABLE";
            case 84:
                return "SERVICE_OPTION_NOT_AVAILABLE";
            case 85:
                return "BEARER_SERVICE_NOT_IMPLEMENTED";
            case 86:
                return "REQUESTED_FACILITY_NOT_IMPLEMENTED";
            case 87:
                return "ONLY_DIGITAL_INFORMATION_BEARER_AVAILABLE";
            case 88:
                return "SERVICE_OR_OPTION_NOT_IMPLEMENTED";
            case 89:
                return "INVALID_TRANSACTION_IDENTIFIER";
            case 90:
                return "USER_NOT_MEMBER_OF_CUG";
            case 91:
                return "INCOMPATIBLE_DESTINATION";
            case 92:
                return "INVALID_TRANSIT_NW_SELECTION";
            case 93:
                return "SEMANTICALLY_INCORRECT_MESSAGE";
            case 94:
                return "INVALID_MANDATORY_INFORMATION";
            case 95:
                return "MESSAGE_TYPE_NON_IMPLEMENTED";
            case 96:
                return "MESSAGE_TYPE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE";
            case 97:
                return "INFORMATION_ELEMENT_NON_EXISTENT";
            case 98:
                return "CONDITIONAL_IE_ERROR";
            case 99:
                return "MESSAGE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE";
            case 100:
                return "RECOVERY_ON_TIMER_EXPIRED";
            case 101:
                return "PROTOCOL_ERROR_UNSPECIFIED";
            case 102:
                return "INTERWORKING_UNSPECIFIED";
            case 103:
                return "LOCAL_LOW_BATTERY";
            case 104:
                return "NO_CIRCUIT_AVAIL";
            case 105:
                return "NO_ROUTE_TO_DESTINATION";
            case 106:
                return "OPERATOR_DETERMINED_BARRING";
            case 107:
                return "CALL_FAIL_NO_USER_RESPONDING";
            case 108:
                return "CALL_FAIL_NO_ANSWER_FROM_USER";
            case 109:
                return "CALL_FAIL_DESTINATION_OUT_OF_ORDER";
            case 110:
                return "BEARER_CAPABILITY_NOT_AUTHORIZED";
            case 111:
                return "CHANNEL_UNACCEPTABLE";
            case 112:
                return "CALL_REJECTED";
            case 113:
                return "NUMBER_CHANGED";
            case 114:
                return "PREEMPTION";
            case 115:
                return "FACILITY_REJECTED";
            case 116:
                return "RESP_TO_STATUS_ENQUIRY";
            case 117:
                return "NETWORK_OUT_OF_ORDER";
            case 118:
                return "TEMPORARY_FAILURE";
            case 119:
                return "SWITCHING_EQUIPMENT_CONGESTION";
            case 120:
                return "ACCESS_INFORMATION_DISCARDED";
            case 121:
                return "REQUESTED_CIRCUIT_OR_CHANNEL_NOT_AVAILABLE";
            case 122:
                return "RESOURCES_UNAVAILABLE_OR_UNSPECIFIED";
            case 123:
                return "QOS_UNAVAILABLE";
            case 124:
                return "REQUESTED_FACILITY_NOT_SUBSCRIBED";
            case 125:
                return "HO_NOT_FEASIBLE";
            case 126:
                return "NON_SELECTED_USER_CLEARING";
            case 127:
                return "CONCURRENT_CALLS_NOT_POSSIBLE";
        }
    }
}
