package android.net;

import android.net.connectivity.com.android.modules.utils.build.SdkLevel;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.EnumMap;

@Deprecated
public class NetworkInfo implements Parcelable {
    public static final Parcelable.Creator<NetworkInfo> CREATOR = new Parcelable.Creator<NetworkInfo>() {
        public NetworkInfo createFromParcel(Parcel parcel) {
            NetworkInfo networkInfo = new NetworkInfo(parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readString());
            networkInfo.mState = State.valueOf(parcel.readString());
            networkInfo.mDetailedState = DetailedState.valueOf(parcel.readString());
            boolean z = true;
            networkInfo.mIsFailover = parcel.readInt() != 0;
            networkInfo.mIsAvailable = parcel.readInt() != 0;
            if (parcel.readInt() == 0) {
                z = false;
            }
            networkInfo.mIsRoaming = z;
            networkInfo.mReason = parcel.readString();
            networkInfo.mExtraInfo = parcel.readString();
            return networkInfo;
        }

        public NetworkInfo[] newArray(int i) {
            return new NetworkInfo[i];
        }
    };
    private static final EnumMap<DetailedState, State> stateMap;
    /* access modifiers changed from: private */
    public DetailedState mDetailedState;
    /* access modifiers changed from: private */
    public String mExtraInfo;
    /* access modifiers changed from: private */
    public boolean mIsAvailable;
    /* access modifiers changed from: private */
    public boolean mIsFailover;
    /* access modifiers changed from: private */
    public boolean mIsRoaming;
    private int mNetworkType;
    /* access modifiers changed from: private */
    public String mReason;
    /* access modifiers changed from: private */
    public State mState;
    private int mSubtype;
    private String mSubtypeName;
    private String mTypeName;

    @Deprecated
    public enum DetailedState {
        IDLE,
        SCANNING,
        CONNECTING,
        AUTHENTICATING,
        OBTAINING_IPADDR,
        CONNECTED,
        SUSPENDED,
        DISCONNECTING,
        DISCONNECTED,
        FAILED,
        BLOCKED,
        VERIFYING_POOR_LINK,
        CAPTIVE_PORTAL_CHECK
    }

    @Deprecated
    public enum State {
        CONNECTING,
        CONNECTED,
        SUSPENDED,
        DISCONNECTING,
        DISCONNECTED,
        UNKNOWN
    }

    public int describeContents() {
        return 0;
    }

    static {
        EnumMap<DetailedState, State> enumMap = new EnumMap<>(DetailedState.class);
        stateMap = enumMap;
        enumMap.put(DetailedState.IDLE, State.DISCONNECTED);
        enumMap.put(DetailedState.SCANNING, State.DISCONNECTED);
        enumMap.put(DetailedState.CONNECTING, State.CONNECTING);
        enumMap.put(DetailedState.AUTHENTICATING, State.CONNECTING);
        enumMap.put(DetailedState.OBTAINING_IPADDR, State.CONNECTING);
        enumMap.put(DetailedState.VERIFYING_POOR_LINK, State.CONNECTING);
        enumMap.put(DetailedState.CAPTIVE_PORTAL_CHECK, State.CONNECTING);
        enumMap.put(DetailedState.CONNECTED, State.CONNECTED);
        enumMap.put(DetailedState.SUSPENDED, State.SUSPENDED);
        enumMap.put(DetailedState.DISCONNECTING, State.DISCONNECTING);
        enumMap.put(DetailedState.DISCONNECTED, State.DISCONNECTED);
        enumMap.put(DetailedState.FAILED, State.DISCONNECTED);
        enumMap.put(DetailedState.BLOCKED, State.DISCONNECTED);
    }

    public NetworkInfo(int i, int i2, String str, String str2) {
        if (ConnectivityManager.isNetworkTypeValid(i) || i == -1) {
            this.mNetworkType = i;
            this.mSubtype = i2;
            this.mTypeName = str;
            this.mSubtypeName = str2;
            setDetailedState(DetailedState.IDLE, (String) null, (String) null);
            this.mState = State.UNKNOWN;
            return;
        }
        throw new IllegalArgumentException("Invalid network type: " + i);
    }

    public NetworkInfo(NetworkInfo networkInfo) {
        if (networkInfo != null || SdkLevel.isAtLeastT()) {
            synchronized (networkInfo) {
                this.mNetworkType = networkInfo.mNetworkType;
                this.mSubtype = networkInfo.mSubtype;
                this.mTypeName = networkInfo.mTypeName;
                this.mSubtypeName = networkInfo.mSubtypeName;
                this.mState = networkInfo.mState;
                this.mDetailedState = networkInfo.mDetailedState;
                this.mReason = networkInfo.mReason;
                this.mExtraInfo = networkInfo.mExtraInfo;
                this.mIsFailover = networkInfo.mIsFailover;
                this.mIsAvailable = networkInfo.mIsAvailable;
                this.mIsRoaming = networkInfo.mIsRoaming;
            }
        }
    }

    @Deprecated
    public int getType() {
        int i;
        synchronized (this) {
            i = this.mNetworkType;
        }
        return i;
    }

    @Deprecated
    public void setType(int i) {
        synchronized (this) {
            this.mNetworkType = i;
        }
    }

    @Deprecated
    public int getSubtype() {
        int i;
        synchronized (this) {
            i = this.mSubtype;
        }
        return i;
    }

    public void setSubtype(int i, String str) {
        synchronized (this) {
            this.mSubtype = i;
            this.mSubtypeName = str;
        }
    }

    @Deprecated
    public String getTypeName() {
        String str;
        synchronized (this) {
            str = this.mTypeName;
        }
        return str;
    }

    @Deprecated
    public String getSubtypeName() {
        String str;
        synchronized (this) {
            str = this.mSubtypeName;
        }
        return str;
    }

    @Deprecated
    public boolean isConnectedOrConnecting() {
        boolean z;
        synchronized (this) {
            if (this.mState != State.CONNECTED) {
                if (this.mState != State.CONNECTING) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    @Deprecated
    public boolean isConnected() {
        boolean z;
        synchronized (this) {
            z = this.mState == State.CONNECTED;
        }
        return z;
    }

    @Deprecated
    public boolean isAvailable() {
        boolean z;
        synchronized (this) {
            z = this.mIsAvailable;
        }
        return z;
    }

    @Deprecated
    public void setIsAvailable(boolean z) {
        synchronized (this) {
            this.mIsAvailable = z;
        }
    }

    @Deprecated
    public boolean isFailover() {
        boolean z;
        synchronized (this) {
            z = this.mIsFailover;
        }
        return z;
    }

    @Deprecated
    public void setFailover(boolean z) {
        synchronized (this) {
            this.mIsFailover = z;
        }
    }

    @Deprecated
    public boolean isRoaming() {
        boolean z;
        synchronized (this) {
            z = this.mIsRoaming;
        }
        return z;
    }

    @Deprecated
    public void setRoaming(boolean z) {
        synchronized (this) {
            this.mIsRoaming = z;
        }
    }

    @Deprecated
    public State getState() {
        State state;
        synchronized (this) {
            state = this.mState;
        }
        return state;
    }

    @Deprecated
    public DetailedState getDetailedState() {
        DetailedState detailedState;
        synchronized (this) {
            detailedState = this.mDetailedState;
        }
        return detailedState;
    }

    @Deprecated
    public void setDetailedState(DetailedState detailedState, String str, String str2) {
        synchronized (this) {
            this.mDetailedState = detailedState;
            State state = stateMap.get(detailedState);
            this.mState = state;
            this.mReason = str;
            this.mExtraInfo = str2;
            if (state == null) {
                if (SdkLevel.isAtLeastT()) {
                    throw new NullPointerException("Unknown DetailedState : " + detailedState);
                }
            }
        }
    }

    @Deprecated
    public void setExtraInfo(String str) {
        synchronized (this) {
            this.mExtraInfo = str;
        }
    }

    public String getReason() {
        String str;
        synchronized (this) {
            str = this.mReason;
        }
        return str;
    }

    @Deprecated
    public String getExtraInfo() {
        String str;
        synchronized (this) {
            str = this.mExtraInfo;
        }
        return str;
    }

    public String toString() {
        String sb;
        synchronized (this) {
            StringBuilder sb2 = new StringBuilder("[type: ");
            sb2.append(getTypeName());
            sb2.append(NavigationBarInflaterView.SIZE_MOD_START);
            sb2.append(getSubtypeName());
            sb2.append("], state: ");
            sb2.append((Object) this.mState);
            sb2.append("/");
            sb2.append((Object) this.mDetailedState);
            sb2.append(", reason: ");
            String str = this.mReason;
            if (str == null) {
                str = "(unspecified)";
            }
            sb2.append(str);
            sb2.append(", extra: ");
            String str2 = this.mExtraInfo;
            if (str2 == null) {
                str2 = "(none)";
            }
            sb2.append(str2);
            sb2.append(", failover: ");
            sb2.append(this.mIsFailover);
            sb2.append(", available: ");
            sb2.append(this.mIsAvailable);
            sb2.append(", roaming: ");
            sb2.append(this.mIsRoaming);
            sb2.append(NavigationBarInflaterView.SIZE_MOD_END);
            sb = sb2.toString();
        }
        return sb;
    }

    public String toShortString() {
        String sb;
        synchronized (this) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getTypeName());
            String subtypeName = getSubtypeName();
            if (!TextUtils.isEmpty(subtypeName)) {
                sb2.append(NavigationBarInflaterView.SIZE_MOD_START);
                sb2.append(subtypeName);
                sb2.append(NavigationBarInflaterView.SIZE_MOD_END);
            }
            sb2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb2.append((Object) this.mDetailedState);
            if (this.mIsRoaming) {
                sb2.append(" ROAMING");
            }
            if (this.mExtraInfo != null) {
                sb2.append(" extra: ");
                sb2.append(this.mExtraInfo);
            }
            sb = sb2.toString();
        }
        return sb;
    }

    public void writeToParcel(Parcel parcel, int i) {
        synchronized (this) {
            parcel.writeInt(this.mNetworkType);
            parcel.writeInt(this.mSubtype);
            parcel.writeString(this.mTypeName);
            parcel.writeString(this.mSubtypeName);
            parcel.writeString(this.mState.name());
            parcel.writeString(this.mDetailedState.name());
            int i2 = 1;
            parcel.writeInt(this.mIsFailover ? 1 : 0);
            parcel.writeInt(this.mIsAvailable ? 1 : 0);
            if (!this.mIsRoaming) {
                i2 = 0;
            }
            parcel.writeInt(i2);
            parcel.writeString(this.mReason);
            parcel.writeString(this.mExtraInfo);
        }
    }
}
