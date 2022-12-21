package android.net.nsd;

import android.net.connectivity.android.net.mdns.aidl.DiscoveryInfo;
import android.net.connectivity.android.net.mdns.aidl.GetAddressInfo;
import android.net.connectivity.android.net.mdns.aidl.IMDns;
import android.net.connectivity.android.net.mdns.aidl.IMDnsEventListener;
import android.net.connectivity.android.net.mdns.aidl.RegistrationInfo;
import android.net.connectivity.android.net.mdns.aidl.ResolutionInfo;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.Log;

public class MDnsManager {
    public static final String MDNS_SERVICE = "mdns";
    private static final int NETID_UNSET = 0;
    private static final int NO_RESULT = -1;
    private static final String TAG = "MDnsManager";
    private final IMDns mMdns;

    public MDnsManager(IMDns iMDns) {
        this.mMdns = iMDns;
    }

    public void startDaemon() {
        try {
            this.mMdns.startDaemon();
        } catch (RemoteException | ServiceSpecificException e) {
            Log.e(TAG, "Start mdns failed.", e);
        }
    }

    public void stopDaemon() {
        try {
            this.mMdns.stopDaemon();
        } catch (RemoteException | ServiceSpecificException e) {
            Log.e(TAG, "Stop mdns failed.", e);
        }
    }

    public boolean registerService(int i, String str, String str2, int i2, byte[] bArr, int i3) {
        try {
            this.mMdns.registerService(new RegistrationInfo(i, -1, str, str2, i2, bArr, i3));
            return true;
        } catch (RemoteException | ServiceSpecificException e) {
            Log.e(TAG, "Register service failed.", e);
            return false;
        }
    }

    public boolean discover(int i, String str, int i2) {
        try {
            this.mMdns.discover(new DiscoveryInfo(i, -1, "", str, "", i2, 0));
            return true;
        } catch (RemoteException | ServiceSpecificException e) {
            Log.e(TAG, "Discover service failed.", e);
            return false;
        }
    }

    public boolean resolve(int i, String str, String str2, String str3, int i2) {
        try {
            this.mMdns.resolve(new ResolutionInfo(i, -1, str, str2, str3, "", "", 0, new byte[0], i2));
            return true;
        } catch (RemoteException | ServiceSpecificException e) {
            Log.e(TAG, "Resolve service failed.", e);
            return false;
        }
    }

    public boolean getServiceAddress(int i, String str, int i2) {
        try {
            this.mMdns.getServiceAddress(new GetAddressInfo(i, -1, str, "", i2, 0));
            return true;
        } catch (RemoteException | ServiceSpecificException e) {
            Log.e(TAG, "Get service address failed.", e);
            return false;
        }
    }

    public boolean stopOperation(int i) {
        try {
            this.mMdns.stopOperation(i);
            return true;
        } catch (RemoteException | ServiceSpecificException e) {
            Log.e(TAG, "Stop operation failed.", e);
            return false;
        }
    }

    public void registerEventListener(IMDnsEventListener iMDnsEventListener) {
        try {
            this.mMdns.registerEventListener(iMDnsEventListener);
        } catch (RemoteException | ServiceSpecificException e) {
            Log.e(TAG, "Register listener failed.", e);
        }
    }

    public void unregisterEventListener(IMDnsEventListener iMDnsEventListener) {
        try {
            this.mMdns.unregisterEventListener(iMDnsEventListener);
        } catch (RemoteException | ServiceSpecificException e) {
            Log.e(TAG, "Unregister listener failed.", e);
        }
    }
}
