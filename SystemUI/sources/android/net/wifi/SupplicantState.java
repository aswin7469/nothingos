package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;

public enum SupplicantState implements Parcelable {
    DISCONNECTED,
    INTERFACE_DISABLED,
    INACTIVE,
    SCANNING,
    AUTHENTICATING,
    ASSOCIATING,
    ASSOCIATED,
    FOUR_WAY_HANDSHAKE,
    GROUP_HANDSHAKE,
    COMPLETED,
    DORMANT,
    UNINITIALIZED,
    INVALID;
    
    public static final Parcelable.Creator<SupplicantState> CREATOR = null;

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new Parcelable.Creator<SupplicantState>() {
            public SupplicantState createFromParcel(Parcel parcel) {
                return SupplicantState.valueOf(parcel.readString());
            }

            public SupplicantState[] newArray(int i) {
                return new SupplicantState[i];
            }
        };
    }

    public static boolean isValidState(SupplicantState supplicantState) {
        return (supplicantState == UNINITIALIZED || supplicantState == INVALID) ? false : true;
    }

    /* renamed from: android.net.wifi.SupplicantState$2 */
    static /* synthetic */ class C01512 {
        static final /* synthetic */ int[] $SwitchMap$android$net$wifi$SupplicantState = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|(3:25|26|28)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0090 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                android.net.wifi.SupplicantState[] r0 = android.net.wifi.SupplicantState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$net$wifi$SupplicantState = r0
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.AUTHENTICATING     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x001d }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.ASSOCIATING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x0028 }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.ASSOCIATED     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x0033 }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.FOUR_WAY_HANDSHAKE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x003e }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.GROUP_HANDSHAKE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x0049 }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.COMPLETED     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x0054 }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.DISCONNECTED     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x0060 }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.INTERFACE_DISABLED     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x006c }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.INACTIVE     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x0078 }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.SCANNING     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x0084 }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.DORMANT     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x0090 }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.UNINITIALIZED     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                int[] r0 = $SwitchMap$android$net$wifi$SupplicantState     // Catch:{ NoSuchFieldError -> 0x009c }
                android.net.wifi.SupplicantState r1 = android.net.wifi.SupplicantState.INVALID     // Catch:{ NoSuchFieldError -> 0x009c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.SupplicantState.C01512.<clinit>():void");
        }
    }

    public static boolean isHandshakeState(SupplicantState supplicantState) {
        switch (C01512.$SwitchMap$android$net$wifi$SupplicantState[supplicantState.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return true;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return false;
            default:
                throw new IllegalArgumentException("Unknown supplicant state");
        }
    }

    public static boolean isConnecting(SupplicantState supplicantState) {
        switch (C01512.$SwitchMap$android$net$wifi$SupplicantState[supplicantState.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return false;
            default:
                throw new IllegalArgumentException("Unknown supplicant state");
        }
    }

    public static boolean isDriverActive(SupplicantState supplicantState) {
        switch (C01512.$SwitchMap$android$net$wifi$SupplicantState[supplicantState.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
                return true;
            case 8:
            case 12:
            case 13:
                return false;
            default:
                throw new IllegalArgumentException("Unknown supplicant state");
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name());
    }
}
