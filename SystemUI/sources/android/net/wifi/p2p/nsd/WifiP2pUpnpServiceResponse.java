package android.net.wifi.p2p.nsd;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.nsd.WifiP2pServiceResponse;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.List;

public class WifiP2pUpnpServiceResponse extends WifiP2pServiceResponse {
    private List<String> mUniqueServiceNames;
    private int mVersion;

    public int getVersion() {
        return this.mVersion;
    }

    public List<String> getUniqueServiceNames() {
        return this.mUniqueServiceNames;
    }

    protected WifiP2pUpnpServiceResponse(int i, int i2, WifiP2pDevice wifiP2pDevice, byte[] bArr) {
        super(2, i, i2, wifiP2pDevice, bArr);
        if (!parse()) {
            throw new IllegalArgumentException("Malformed upnp service response");
        }
    }

    private boolean parse() {
        if (this.mData == null) {
            return true;
        }
        if (this.mData.length < 1) {
            return false;
        }
        this.mVersion = this.mData[0] & 255;
        String[] split = new String(this.mData, 1, this.mData.length - 1).split(NavigationBarInflaterView.BUTTON_SEPARATOR);
        this.mUniqueServiceNames = new ArrayList();
        for (String add : split) {
            this.mUniqueServiceNames.add(add);
        }
        return true;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("serviceType:UPnP(");
        stringBuffer.append(this.mServiceType).append(") status:");
        stringBuffer.append(WifiP2pServiceResponse.Status.toString(this.mStatus));
        stringBuffer.append(" srcAddr:").append(this.mDevice.deviceAddress);
        stringBuffer.append(" version:").append(String.format("%02x", Integer.valueOf(this.mVersion)));
        List<String> list = this.mUniqueServiceNames;
        if (list != null) {
            for (String append : list) {
                stringBuffer.append(" usn:").append(append);
            }
        }
        return stringBuffer.toString();
    }

    static WifiP2pUpnpServiceResponse newInstance(int i, int i2, WifiP2pDevice wifiP2pDevice, byte[] bArr) {
        if (i != 0) {
            return new WifiP2pUpnpServiceResponse(i, i2, wifiP2pDevice, (byte[]) null);
        }
        try {
            return new WifiP2pUpnpServiceResponse(i, i2, wifiP2pDevice, bArr);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
