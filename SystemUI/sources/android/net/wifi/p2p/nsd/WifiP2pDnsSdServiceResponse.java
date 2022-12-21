package android.net.wifi.p2p.nsd;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.nsd.WifiP2pServiceResponse;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.p026io.ByteArrayInputStream;
import java.p026io.DataInputStream;
import java.p026io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WifiP2pDnsSdServiceResponse extends WifiP2pServiceResponse {
    private static final Map<Integer, String> sVmpack;
    private String mDnsQueryName;
    private int mDnsType;
    private String mInstanceName;
    private final HashMap<String, String> mTxtRecord = new HashMap<>();
    private int mVersion;

    static {
        HashMap hashMap = new HashMap();
        sVmpack = hashMap;
        hashMap.put(12, "_tcp.local.");
        hashMap.put(17, "local.");
        hashMap.put(28, "_udp.local.");
    }

    public String getDnsQueryName() {
        return this.mDnsQueryName;
    }

    public int getDnsType() {
        return this.mDnsType;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public String getInstanceName() {
        return this.mInstanceName;
    }

    public Map<String, String> getTxtRecord() {
        return this.mTxtRecord;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("serviceType:DnsSd(");
        stringBuffer.append(this.mServiceType).append(") status:");
        stringBuffer.append(WifiP2pServiceResponse.Status.toString(this.mStatus));
        stringBuffer.append(" srcAddr:").append(this.mDevice.deviceAddress);
        stringBuffer.append(" version:").append(String.format("%02x", Integer.valueOf(this.mVersion)));
        stringBuffer.append(" dnsName:").append(this.mDnsQueryName);
        stringBuffer.append(" TxtRecord:");
        for (String next : this.mTxtRecord.keySet()) {
            stringBuffer.append(" key:").append(next).append(" value:").append(this.mTxtRecord.get(next));
        }
        if (this.mInstanceName != null) {
            stringBuffer.append(" InsName:").append(this.mInstanceName);
        }
        return stringBuffer.toString();
    }

    protected WifiP2pDnsSdServiceResponse(int i, int i2, WifiP2pDevice wifiP2pDevice, byte[] bArr) {
        super(1, i, i2, wifiP2pDevice, bArr);
        if (!parse()) {
            throw new IllegalArgumentException("Malformed bonjour service response");
        }
    }

    private boolean parse() {
        if (this.mData == null) {
            return true;
        }
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.mData));
        String readDnsName = readDnsName(dataInputStream);
        this.mDnsQueryName = readDnsName;
        if (readDnsName == null) {
            return false;
        }
        try {
            this.mDnsType = dataInputStream.readUnsignedShort();
            this.mVersion = dataInputStream.readUnsignedByte();
            int i = this.mDnsType;
            if (i == 12) {
                String readDnsName2 = readDnsName(dataInputStream);
                if (readDnsName2 == null || readDnsName2.length() <= this.mDnsQueryName.length()) {
                    return false;
                }
                this.mInstanceName = readDnsName2.substring(0, (readDnsName2.length() - this.mDnsQueryName.length()) - 1);
                return true;
            } else if (i == 16) {
                return readTxtData(dataInputStream);
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String readDnsName(DataInputStream dataInputStream) {
        StringBuffer stringBuffer = new StringBuffer();
        HashMap hashMap = new HashMap(sVmpack);
        if (this.mDnsQueryName != null) {
            hashMap.put(39, this.mDnsQueryName);
        }
        while (true) {
            try {
                int readUnsignedByte = dataInputStream.readUnsignedByte();
                if (readUnsignedByte == 0) {
                    return stringBuffer.toString();
                }
                if (readUnsignedByte == 192) {
                    String str = (String) hashMap.get(Integer.valueOf(dataInputStream.readUnsignedByte()));
                    if (str == null) {
                        return null;
                    }
                    stringBuffer.append(str);
                    return stringBuffer.toString();
                }
                byte[] bArr = new byte[readUnsignedByte];
                dataInputStream.readFully(bArr);
                stringBuffer.append(new String(bArr));
                stringBuffer.append(BaseIconCache.EMPTY_CLASS_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private boolean readTxtData(DataInputStream dataInputStream) {
        while (true) {
            try {
                if (dataInputStream.available() <= 0) {
                    break;
                }
                int readUnsignedByte = dataInputStream.readUnsignedByte();
                if (readUnsignedByte == 0) {
                    break;
                }
                byte[] bArr = new byte[readUnsignedByte];
                dataInputStream.readFully(bArr);
                String[] split = new String(bArr).split("=");
                if (split.length != 2) {
                    return false;
                }
                this.mTxtRecord.put(split[0], split[1]);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    static WifiP2pDnsSdServiceResponse newInstance(int i, int i2, WifiP2pDevice wifiP2pDevice, byte[] bArr) {
        if (i != 0) {
            return new WifiP2pDnsSdServiceResponse(i, i2, wifiP2pDevice, (byte[]) null);
        }
        try {
            return new WifiP2pDnsSdServiceResponse(i, i2, wifiP2pDevice, bArr);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
