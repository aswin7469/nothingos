package android.net.nsd;

import android.net.Network;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.p026io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

public final class NsdServiceInfo implements Parcelable {
    public static final Parcelable.Creator<NsdServiceInfo> CREATOR = new Parcelable.Creator<NsdServiceInfo>() {
        public NsdServiceInfo createFromParcel(Parcel parcel) {
            NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
            nsdServiceInfo.mServiceName = parcel.readString();
            nsdServiceInfo.mServiceType = parcel.readString();
            if (parcel.readInt() == 1) {
                try {
                    nsdServiceInfo.mHost = InetAddress.getByAddress(parcel.createByteArray());
                } catch (UnknownHostException unused) {
                }
            }
            nsdServiceInfo.mPort = parcel.readInt();
            int readInt = parcel.readInt();
            int i = 0;
            while (true) {
                byte[] bArr = null;
                if (i < readInt) {
                    if (parcel.readInt() == 1) {
                        bArr = new byte[parcel.readInt()];
                        parcel.readByteArray(bArr);
                    }
                    nsdServiceInfo.mTxtRecord.put(parcel.readString(), bArr);
                    i++;
                } else {
                    nsdServiceInfo.mNetwork = (Network) parcel.readParcelable((ClassLoader) null, Network.class);
                    nsdServiceInfo.mInterfaceIndex = parcel.readInt();
                    return nsdServiceInfo;
                }
            }
        }

        public NsdServiceInfo[] newArray(int i) {
            return new NsdServiceInfo[i];
        }
    };
    private static final String TAG = "NsdServiceInfo";
    /* access modifiers changed from: private */
    public InetAddress mHost;
    /* access modifiers changed from: private */
    public int mInterfaceIndex;
    /* access modifiers changed from: private */
    public Network mNetwork;
    /* access modifiers changed from: private */
    public int mPort;
    /* access modifiers changed from: private */
    public String mServiceName;
    /* access modifiers changed from: private */
    public String mServiceType;
    /* access modifiers changed from: private */
    public final ArrayMap<String, byte[]> mTxtRecord = new ArrayMap<>();

    public int describeContents() {
        return 0;
    }

    public NsdServiceInfo() {
    }

    public NsdServiceInfo(String str, String str2) {
        this.mServiceName = str;
        this.mServiceType = str2;
    }

    public String getServiceName() {
        return this.mServiceName;
    }

    public void setServiceName(String str) {
        this.mServiceName = str;
    }

    public String getServiceType() {
        return this.mServiceType;
    }

    public void setServiceType(String str) {
        this.mServiceType = str;
    }

    public InetAddress getHost() {
        return this.mHost;
    }

    public void setHost(InetAddress inetAddress) {
        this.mHost = inetAddress;
    }

    public int getPort() {
        return this.mPort;
    }

    public void setPort(int i) {
        this.mPort = i;
    }

    public void setTxtRecords(byte[] bArr) {
        int i = 0;
        while (i < bArr.length) {
            int i2 = bArr[i] & 255;
            int i3 = i + 1;
            if (i2 != 0) {
                try {
                    if (i3 + i2 > bArr.length) {
                        Log.w(TAG, "Corrupt record length (pos = " + i3 + "): " + i2);
                        i2 = bArr.length - i3;
                    }
                    String str = null;
                    int i4 = 0;
                    byte[] bArr2 = null;
                    for (int i5 = i3; i5 < i3 + i2; i5++) {
                        if (str != null) {
                            if (bArr2 == null) {
                                bArr2 = new byte[((i2 - str.length()) - 1)];
                            }
                            bArr2[i4] = bArr[i5];
                            i4++;
                        } else if (bArr[i5] == 61) {
                            str = new String(bArr, i3, i5 - i3, StandardCharsets.US_ASCII);
                        }
                    }
                    if (str == null) {
                        str = new String(bArr, i3, i2, StandardCharsets.US_ASCII);
                    }
                    if (TextUtils.isEmpty(str)) {
                        throw new IllegalArgumentException("Invalid txt record (key is empty)");
                    } else if (!getAttributes().containsKey(str)) {
                        setAttribute(str, bArr2);
                        i = i3 + i2;
                    } else {
                        throw new IllegalArgumentException("Invalid txt record (duplicate key \"" + str + "\")");
                    }
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "While parsing txt records (pos = " + i3 + "): " + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException("Zero sized txt record");
            }
        }
    }

    public void setAttribute(String str, byte[] bArr) {
        if (!TextUtils.isEmpty(str)) {
            int i = 0;
            int i2 = 0;
            while (i2 < str.length()) {
                char charAt = str.charAt(i2);
                if (charAt < ' ' || charAt > '~') {
                    throw new IllegalArgumentException("Key strings must be printable US-ASCII");
                } else if (charAt != '=') {
                    i2++;
                } else {
                    throw new IllegalArgumentException("Key strings must not include '='");
                }
            }
            if (str.length() + (bArr == null ? 0 : bArr.length) < 255) {
                if (str.length() > 9) {
                    Log.w(TAG, "Key lengths > 9 are discouraged: " + str);
                }
                int txtRecordSize = getTxtRecordSize() + str.length();
                if (bArr != null) {
                    i = bArr.length;
                }
                int i3 = txtRecordSize + i + 2;
                if (i3 <= 1300) {
                    if (i3 > 400) {
                        Log.w(TAG, "Total length of all attributes exceeds 400 bytes; truncation may occur");
                    }
                    this.mTxtRecord.put(str, bArr);
                    return;
                }
                throw new IllegalArgumentException("Total length of attributes must be < 1300 bytes");
            }
            throw new IllegalArgumentException("Key length + value length must be < 255 bytes");
        }
        throw new IllegalArgumentException("Key cannot be empty");
    }

    public void setAttribute(String str, String str2) {
        byte[] bArr;
        if (str2 == null) {
            bArr = null;
            try {
                byte[] bArr2 = null;
            } catch (UnsupportedEncodingException unused) {
                throw new IllegalArgumentException("Value must be UTF-8");
            }
        } else {
            bArr = str2.getBytes("UTF-8");
        }
        setAttribute(str, bArr);
    }

    public void removeAttribute(String str) {
        this.mTxtRecord.remove(str);
    }

    public Map<String, byte[]> getAttributes() {
        return Collections.unmodifiableMap(this.mTxtRecord);
    }

    private int getTxtRecordSize() {
        int i;
        int i2 = 0;
        for (Map.Entry next : this.mTxtRecord.entrySet()) {
            int length = i2 + 2 + ((String) next.getKey()).length();
            byte[] bArr = (byte[]) next.getValue();
            if (bArr == null) {
                i = 0;
            } else {
                i = bArr.length;
            }
            i2 = length + i;
        }
        return i2;
    }

    public byte[] getTxtRecord() {
        int txtRecordSize = getTxtRecordSize();
        if (txtRecordSize == 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[txtRecordSize];
        int i = 0;
        for (Map.Entry next : this.mTxtRecord.entrySet()) {
            String str = (String) next.getKey();
            byte[] bArr2 = (byte[]) next.getValue();
            int i2 = i + 1;
            bArr[i] = (byte) (str.length() + (bArr2 == null ? 0 : bArr2.length) + 1);
            System.arraycopy((Object) str.getBytes(StandardCharsets.US_ASCII), 0, (Object) bArr, i2, str.length());
            int length = i2 + str.length();
            i = length + 1;
            bArr[length] = 61;
            if (bArr2 != null) {
                System.arraycopy((Object) bArr2, 0, (Object) bArr, i, bArr2.length);
                i += bArr2.length;
            }
        }
        return bArr;
    }

    public Network getNetwork() {
        return this.mNetwork;
    }

    public void setNetwork(Network network) {
        this.mNetwork = network;
    }

    public int getInterfaceIndex() {
        return this.mInterfaceIndex;
    }

    public void setInterfaceIndex(int i) {
        this.mInterfaceIndex = i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("name: ");
        sb.append(this.mServiceName);
        sb.append(", type: ");
        sb.append(this.mServiceType);
        sb.append(", host: ");
        sb.append((Object) this.mHost);
        sb.append(", port: ");
        sb.append(this.mPort);
        sb.append(", network: ");
        sb.append((Object) this.mNetwork);
        byte[] txtRecord = getTxtRecord();
        sb.append(", txtRecord: ");
        sb.append(new String(txtRecord, StandardCharsets.UTF_8));
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mServiceName);
        parcel.writeString(this.mServiceType);
        if (this.mHost != null) {
            parcel.writeInt(1);
            parcel.writeByteArray(this.mHost.getAddress());
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.mPort);
        parcel.writeInt(this.mTxtRecord.size());
        for (String next : this.mTxtRecord.keySet()) {
            byte[] bArr = this.mTxtRecord.get(next);
            if (bArr != null) {
                parcel.writeInt(1);
                parcel.writeInt(bArr.length);
                parcel.writeByteArray(bArr);
            } else {
                parcel.writeInt(0);
            }
            parcel.writeString(next);
        }
        parcel.writeParcelable(this.mNetwork, 0);
        parcel.writeInt(this.mInterfaceIndex);
    }
}
