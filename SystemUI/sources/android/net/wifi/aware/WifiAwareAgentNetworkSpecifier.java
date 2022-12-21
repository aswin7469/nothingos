package android.net.wifi.aware;

import android.net.NetworkSpecifier;
import android.net.wifi.util.HexEncoding;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class WifiAwareAgentNetworkSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<WifiAwareAgentNetworkSpecifier> CREATOR = new Parcelable.Creator<WifiAwareAgentNetworkSpecifier>() {
        public WifiAwareAgentNetworkSpecifier createFromParcel(Parcel parcel) {
            WifiAwareAgentNetworkSpecifier wifiAwareAgentNetworkSpecifier = new WifiAwareAgentNetworkSpecifier();
            for (Object obj : parcel.readArray((ClassLoader) null)) {
                wifiAwareAgentNetworkSpecifier.mNetworkSpecifiers.add((ByteArrayWrapper) obj);
            }
            return wifiAwareAgentNetworkSpecifier;
        }

        public WifiAwareAgentNetworkSpecifier[] newArray(int i) {
            return new WifiAwareAgentNetworkSpecifier[i];
        }
    };
    private static final String TAG = "WifiAwareAgentNs";
    private static final boolean VDBG = false;
    private MessageDigest mDigester;
    /* access modifiers changed from: private */
    public Set<ByteArrayWrapper> mNetworkSpecifiers = new HashSet();

    public int describeContents() {
        return 0;
    }

    public NetworkSpecifier redact() {
        return null;
    }

    public WifiAwareAgentNetworkSpecifier() {
        initialize();
    }

    public WifiAwareAgentNetworkSpecifier(WifiAwareNetworkSpecifier wifiAwareNetworkSpecifier) {
        initialize();
        this.mNetworkSpecifiers.add(convert(wifiAwareNetworkSpecifier));
    }

    public WifiAwareAgentNetworkSpecifier(WifiAwareNetworkSpecifier[] wifiAwareNetworkSpecifierArr) {
        initialize();
        for (WifiAwareNetworkSpecifier convert : wifiAwareNetworkSpecifierArr) {
            this.mNetworkSpecifiers.add(convert(convert));
        }
    }

    public boolean isEmpty() {
        return this.mNetworkSpecifiers.isEmpty();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(this.mNetworkSpecifiers.toArray());
    }

    public int hashCode() {
        return this.mNetworkSpecifiers.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WifiAwareAgentNetworkSpecifier)) {
            return false;
        }
        return this.mNetworkSpecifiers.equals(((WifiAwareAgentNetworkSpecifier) obj).mNetworkSpecifiers);
    }

    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR);
        for (ByteArrayWrapper byteArrayWrapper : this.mNetworkSpecifiers) {
            stringJoiner.add(byteArrayWrapper.toString());
        }
        return stringJoiner.toString();
    }

    public boolean canBeSatisfiedBy(NetworkSpecifier networkSpecifier) {
        if (!(networkSpecifier instanceof WifiAwareAgentNetworkSpecifier)) {
            return false;
        }
        WifiAwareAgentNetworkSpecifier wifiAwareAgentNetworkSpecifier = (WifiAwareAgentNetworkSpecifier) networkSpecifier;
        for (ByteArrayWrapper contains : this.mNetworkSpecifiers) {
            if (!wifiAwareAgentNetworkSpecifier.mNetworkSpecifiers.contains(contains)) {
                return false;
            }
        }
        return true;
    }

    public boolean satisfiesAwareNetworkSpecifier(WifiAwareNetworkSpecifier wifiAwareNetworkSpecifier) {
        return this.mNetworkSpecifiers.contains(convert(wifiAwareNetworkSpecifier));
    }

    private void initialize() {
        try {
            this.mDigester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException unused) {
            Log.e(TAG, "Can not instantiate a SHA-256 digester!? Will match nothing.");
        }
    }

    private ByteArrayWrapper convert(WifiAwareNetworkSpecifier wifiAwareNetworkSpecifier) {
        if (this.mDigester == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        wifiAwareNetworkSpecifier.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        this.mDigester.reset();
        this.mDigester.update(marshall);
        return new ByteArrayWrapper(this.mDigester.digest());
    }

    private static class ByteArrayWrapper implements Parcelable {
        public static final Parcelable.Creator<ByteArrayWrapper> CREATOR = new Parcelable.Creator<ByteArrayWrapper>() {
            public ByteArrayWrapper createFromParcel(Parcel parcel) {
                return new ByteArrayWrapper(parcel.createByteArray());
            }

            public ByteArrayWrapper[] newArray(int i) {
                return new ByteArrayWrapper[i];
            }
        };
        private byte[] mData;

        public int describeContents() {
            return 0;
        }

        ByteArrayWrapper(byte[] bArr) {
            this.mData = bArr;
        }

        public int hashCode() {
            return Arrays.hashCode(this.mData);
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ByteArrayWrapper)) {
                return false;
            }
            return Arrays.equals(((ByteArrayWrapper) obj).mData, this.mData);
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeByteArray(this.mData);
        }

        public String toString() {
            return new String(HexEncoding.encode(this.mData));
        }
    }
}
