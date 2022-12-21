package android.net.wifi.p2p.nsd;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Parcel;
import android.os.Parcelable;
import java.p026io.ByteArrayInputStream;
import java.p026io.DataInputStream;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WifiP2pServiceResponse implements Parcelable {
    public static final Parcelable.Creator<WifiP2pServiceResponse> CREATOR = new Parcelable.Creator<WifiP2pServiceResponse>() {
        public WifiP2pServiceResponse createFromParcel(Parcel parcel) {
            int readInt = parcel.readInt();
            int readInt2 = parcel.readInt();
            int readInt3 = parcel.readInt();
            byte[] bArr = null;
            WifiP2pDevice wifiP2pDevice = (WifiP2pDevice) parcel.readParcelable((ClassLoader) null);
            int readInt4 = parcel.readInt();
            if (readInt4 > 0) {
                bArr = new byte[readInt4];
                parcel.readByteArray(bArr);
            }
            byte[] bArr2 = bArr;
            if (readInt == 1) {
                return WifiP2pDnsSdServiceResponse.newInstance(readInt2, readInt3, wifiP2pDevice, bArr2);
            }
            if (readInt == 2) {
                return WifiP2pUpnpServiceResponse.newInstance(readInt2, readInt3, wifiP2pDevice, bArr2);
            }
            return new WifiP2pServiceResponse(readInt, readInt2, readInt3, wifiP2pDevice, bArr2);
        }

        public WifiP2pServiceResponse[] newArray(int i) {
            return new WifiP2pServiceResponse[i];
        }
    };
    private static int MAX_BUF_SIZE = 1024;
    protected byte[] mData;
    protected WifiP2pDevice mDevice;
    protected int mServiceType;
    protected int mStatus;
    protected int mTransId;

    public int describeContents() {
        return 0;
    }

    public static class Status {
        public static final int BAD_REQUEST = 3;
        public static final int REQUESTED_INFORMATION_NOT_AVAILABLE = 2;
        public static final int SERVICE_PROTOCOL_NOT_AVAILABLE = 1;
        public static final int SUCCESS = 0;

        public static String toString(int i) {
            return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "UNKNOWN" : "BAD_REQUEST" : "REQUESTED_INFORMATION_NOT_AVAILABLE" : "SERVICE_PROTOCOL_NOT_AVAILABLE" : "SUCCESS";
        }

        private Status() {
        }
    }

    protected WifiP2pServiceResponse(int i, int i2, int i3, WifiP2pDevice wifiP2pDevice, byte[] bArr) {
        this.mServiceType = i;
        this.mStatus = i2;
        this.mTransId = i3;
        this.mDevice = wifiP2pDevice;
        this.mData = bArr;
    }

    public int getServiceType() {
        return this.mServiceType;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public int getTransactionId() {
        return this.mTransId;
    }

    public byte[] getRawData() {
        return this.mData;
    }

    public WifiP2pDevice getSrcDevice() {
        return this.mDevice;
    }

    public void setSrcDevice(WifiP2pDevice wifiP2pDevice) {
        if (wifiP2pDevice != null) {
            this.mDevice = wifiP2pDevice;
        }
    }

    public static List<WifiP2pServiceResponse> newInstance(String str, byte[] bArr) {
        WifiP2pServiceResponse wifiP2pServiceResponse;
        ArrayList arrayList = new ArrayList();
        WifiP2pDevice wifiP2pDevice = new WifiP2pDevice();
        wifiP2pDevice.deviceAddress = str;
        if (bArr == null) {
            return null;
        }
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        while (dataInputStream.available() > 0) {
            try {
                int readUnsignedByte = (dataInputStream.readUnsignedByte() + (dataInputStream.readUnsignedByte() << 8)) - 3;
                int readUnsignedByte2 = dataInputStream.readUnsignedByte();
                int readUnsignedByte3 = dataInputStream.readUnsignedByte();
                int readUnsignedByte4 = dataInputStream.readUnsignedByte();
                if (readUnsignedByte < 0) {
                    return null;
                }
                if (readUnsignedByte == 0) {
                    if (readUnsignedByte4 == 0) {
                        arrayList.add(new WifiP2pServiceResponse(readUnsignedByte2, readUnsignedByte4, readUnsignedByte3, wifiP2pDevice, (byte[]) null));
                    }
                } else if (readUnsignedByte > MAX_BUF_SIZE) {
                    dataInputStream.skip((long) readUnsignedByte);
                } else {
                    byte[] bArr2 = new byte[readUnsignedByte];
                    dataInputStream.readFully(bArr2);
                    if (readUnsignedByte2 == 1) {
                        wifiP2pServiceResponse = WifiP2pDnsSdServiceResponse.newInstance(readUnsignedByte4, readUnsignedByte3, wifiP2pDevice, bArr2);
                    } else if (readUnsignedByte2 == 2) {
                        wifiP2pServiceResponse = WifiP2pUpnpServiceResponse.newInstance(readUnsignedByte4, readUnsignedByte3, wifiP2pDevice, bArr2);
                    } else {
                        wifiP2pServiceResponse = new WifiP2pServiceResponse(readUnsignedByte2, readUnsignedByte4, readUnsignedByte3, wifiP2pDevice, bArr2);
                    }
                    if (wifiP2pServiceResponse != null && wifiP2pServiceResponse.getStatus() == 0) {
                        arrayList.add(wifiP2pServiceResponse);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (arrayList.size() > 0) {
                    return arrayList;
                }
                return null;
            }
        }
        return arrayList;
    }

    private static byte[] hexStr2Bin(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[(str.length() / 2)];
        int i = 0;
        while (i < length) {
            int i2 = i * 2;
            try {
                bArr[i] = (byte) Integer.parseInt(str.substring(i2, i2 + 2), 16);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return bArr;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("serviceType:");
        stringBuffer.append(this.mServiceType);
        stringBuffer.append(" status:").append(Status.toString(this.mStatus));
        stringBuffer.append(" srcAddr:").append(this.mDevice.deviceAddress);
        stringBuffer.append(" data:").append(Arrays.toString(this.mData));
        return stringBuffer.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WifiP2pServiceResponse)) {
            return false;
        }
        WifiP2pServiceResponse wifiP2pServiceResponse = (WifiP2pServiceResponse) obj;
        if (wifiP2pServiceResponse.mServiceType != this.mServiceType || wifiP2pServiceResponse.mStatus != this.mStatus || !equals(wifiP2pServiceResponse.mDevice.deviceAddress, this.mDevice.deviceAddress) || !Arrays.equals(wifiP2pServiceResponse.mData, this.mData)) {
            return false;
        }
        return true;
    }

    private boolean equals(Object obj, Object obj2) {
        if (obj == null && obj2 == null) {
            return true;
        }
        if (obj != null) {
            return obj.equals(obj2);
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2 = (((((527 + this.mServiceType) * 31) + this.mStatus) * 31) + this.mTransId) * 31;
        int i3 = 0;
        if (this.mDevice.deviceAddress == null) {
            i = 0;
        } else {
            i = this.mDevice.deviceAddress.hashCode();
        }
        int i4 = (i2 + i) * 31;
        byte[] bArr = this.mData;
        if (bArr != null) {
            i3 = Arrays.hashCode(bArr);
        }
        return i4 + i3;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mServiceType);
        parcel.writeInt(this.mStatus);
        parcel.writeInt(this.mTransId);
        parcel.writeParcelable(this.mDevice, i);
        byte[] bArr = this.mData;
        if (bArr == null || bArr.length == 0) {
            parcel.writeInt(0);
            return;
        }
        parcel.writeInt(bArr.length);
        parcel.writeByteArray(this.mData);
    }
}
