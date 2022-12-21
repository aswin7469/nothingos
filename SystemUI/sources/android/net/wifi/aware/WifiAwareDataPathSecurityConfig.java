package android.net.wifi.aware;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Arrays;
import java.util.Objects;

public final class WifiAwareDataPathSecurityConfig implements Parcelable {
    public static final Parcelable.Creator<WifiAwareDataPathSecurityConfig> CREATOR = new Parcelable.Creator<WifiAwareDataPathSecurityConfig>() {
        public WifiAwareDataPathSecurityConfig createFromParcel(Parcel parcel) {
            return new WifiAwareDataPathSecurityConfig(parcel);
        }

        public WifiAwareDataPathSecurityConfig[] newArray(int i) {
            return new WifiAwareDataPathSecurityConfig[i];
        }
    };
    private final int mCipherSuite;
    private final String mPassphrase;
    private final byte[] mPmk;
    private final byte[] mPmkId;

    public int describeContents() {
        return 0;
    }

    public WifiAwareDataPathSecurityConfig(int i, byte[] bArr, byte[] bArr2, String str) {
        this.mCipherSuite = i;
        this.mPassphrase = str;
        this.mPmk = bArr;
        this.mPmkId = bArr2;
    }

    private WifiAwareDataPathSecurityConfig(Parcel parcel) {
        this.mPmk = parcel.createByteArray();
        this.mPassphrase = parcel.readString();
        this.mPmkId = parcel.createByteArray();
        this.mCipherSuite = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.mPmk);
        parcel.writeString(this.mPassphrase);
        parcel.writeByteArray(this.mPmkId);
        parcel.writeInt(this.mCipherSuite);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiAwareDataPathSecurityConfig)) {
            return false;
        }
        WifiAwareDataPathSecurityConfig wifiAwareDataPathSecurityConfig = (WifiAwareDataPathSecurityConfig) obj;
        if (this.mCipherSuite != wifiAwareDataPathSecurityConfig.mCipherSuite || !Arrays.equals(this.mPmk, wifiAwareDataPathSecurityConfig.mPmk) || !Objects.equals(this.mPassphrase, wifiAwareDataPathSecurityConfig.mPassphrase) || !Arrays.equals(this.mPmkId, wifiAwareDataPathSecurityConfig.mPmkId)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(Arrays.hashCode(this.mPmk)), this.mPassphrase, Integer.valueOf(Arrays.hashCode(this.mPmkId)), Integer.valueOf(this.mCipherSuite));
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder("WifiAwareDataPathSecurityConfig [cipherSuite=");
        sb.append(this.mCipherSuite);
        sb.append(", passphrase=");
        String str2 = "<null>";
        sb.append(TextUtils.isEmpty(this.mPassphrase) ? str2 : "<non-null>");
        sb.append(", PMK=");
        if (this.mPmk == null) {
            str = str2;
        } else {
            str = "<non-null>";
        }
        sb.append(str);
        sb.append(", PMKID=");
        if (this.mPmkId != null) {
            str2 = "<non-null>";
        }
        sb.append(str2);
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public boolean isValid() {
        int i = this.mCipherSuite;
        if (i == 1 || i == 2) {
            if (TextUtils.isEmpty(this.mPassphrase) && this.mPmk == null) {
                return false;
            }
            if ((!TextUtils.isEmpty(this.mPassphrase) && this.mPmk != null) || this.mPmkId != null) {
                return false;
            }
            if (WifiAwareUtils.validatePassphrase(this.mPassphrase) && this.mPmk == null) {
                return true;
            }
            if (!TextUtils.isEmpty(this.mPassphrase) || !WifiAwareUtils.validatePmk(this.mPmk)) {
                return false;
            }
            return true;
        } else if ((i == 4 || i == 8) && WifiAwareUtils.validatePmk(this.mPmk) && WifiAwareUtils.validatePmkId(this.mPmkId)) {
            return TextUtils.isEmpty(this.mPassphrase);
        } else {
            return false;
        }
    }

    public int getCipherSuite() {
        return this.mCipherSuite;
    }

    public byte[] getPmk() {
        return this.mPmk;
    }

    public byte[] getPmkId() {
        return this.mPmkId;
    }

    public String getPskPassphrase() {
        return this.mPassphrase;
    }

    public static final class Builder {
        private int mCipherSuite;
        private String mPassphrase;
        private byte[] mPmk;
        private byte[] mPmkId;

        public Builder(int i) {
            if (i == 1 || i == 2 || i == 4 || i == 8) {
                this.mCipherSuite = i;
                return;
            }
            throw new IllegalArgumentException("Invalid cipher suite");
        }

        public Builder setPskPassphrase(String str) {
            if (WifiAwareUtils.validatePassphrase(str)) {
                this.mPassphrase = str;
                return this;
            }
            throw new IllegalArgumentException("Passphrase must meet length requirements");
        }

        public Builder setPmk(byte[] bArr) {
            if (WifiAwareUtils.validatePmk(bArr)) {
                this.mPmk = bArr;
                return this;
            }
            throw new IllegalArgumentException("PMK must 32 bytes");
        }

        public Builder setPmkId(byte[] bArr) {
            if (WifiAwareUtils.validatePmkId(bArr)) {
                this.mPmkId = bArr;
                return this;
            }
            throw new IllegalArgumentException("PMKID must 16 bytes");
        }

        public WifiAwareDataPathSecurityConfig build() {
            String str = this.mPassphrase;
            if (str == null || this.mPmk == null) {
                int i = this.mCipherSuite;
                if (i == 1 || i == 2) {
                    if (TextUtils.isEmpty(str) && this.mPmk == null) {
                        throw new IllegalStateException("Must set either PMK or Passphrase for shared key cipher suite");
                    } else if (this.mPmkId != null) {
                        throw new IllegalStateException("PMKID should not set for shared key cipher suite");
                    }
                } else if (this.mPmk == null || this.mPmkId == null) {
                    throw new IllegalStateException("Must set both PMK and PMKID for public key cipher suite");
                } else if (!TextUtils.isEmpty(str)) {
                    throw new IllegalStateException("Passphrase is not support for public key cipher suite");
                }
                return new WifiAwareDataPathSecurityConfig(this.mCipherSuite, this.mPmk, this.mPmkId, this.mPassphrase);
            }
            throw new IllegalStateException("Can only specify a Passphrase or a PMK - not both!");
        }
    }
}
