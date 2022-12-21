package android.net;

import android.annotation.SystemApi;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class TestNetworkManager {
    private static final boolean BRING_UP = true;
    public static final String CLAT_INTERFACE_PREFIX = "v4-";
    private static final LinkAddress[] NO_ADDRS = new LinkAddress[0];
    private static final String TAG = "TestNetworkManager";
    private static final boolean TAP = false;
    public static final String TEST_TAP_PREFIX = "testtap";
    public static final String TEST_TUN_PREFIX = "testtun";
    private static final boolean TUN = true;
    private final ITestNetworkManager mService;

    public TestNetworkManager(ITestNetworkManager iTestNetworkManager) {
        this.mService = (ITestNetworkManager) Objects.requireNonNull(iTestNetworkManager, "missing ITestNetworkManager");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void teardownTestNetwork(Network network) {
        try {
            this.mService.teardownTestNetwork(network.netId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void setupTestNetwork(String str, LinkProperties linkProperties, boolean z, int[] iArr, IBinder iBinder) {
        try {
            this.mService.setupTestNetwork(str, linkProperties, z, iArr, iBinder);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setupTestNetwork(LinkProperties linkProperties, boolean z, IBinder iBinder) {
        Objects.requireNonNull(linkProperties, "Invalid LinkProperties");
        setupTestNetwork(linkProperties.getInterfaceName(), linkProperties, z, new int[0], iBinder);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setupTestNetwork(String str, IBinder iBinder) {
        setupTestNetwork(str, (LinkProperties) null, true, new int[0], iBinder);
    }

    public void setupTestNetwork(String str, int[] iArr, IBinder iBinder) {
        setupTestNetwork(str, (LinkProperties) null, true, iArr, iBinder);
    }

    @Deprecated
    public TestNetworkInterface createTunInterface(LinkAddress[] linkAddressArr) {
        return createTunInterface((Collection<LinkAddress>) Arrays.asList(linkAddressArr));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public TestNetworkInterface createTunInterface(Collection<LinkAddress> collection) {
        try {
            return this.mService.createInterface(true, true, (LinkAddress[]) collection.toArray((T[]) new LinkAddress[collection.size()]), (String) null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public TestNetworkInterface createTapInterface() {
        try {
            return this.mService.createInterface(false, true, NO_ADDRS, (String) null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public TestNetworkInterface createTapInterface(boolean z) {
        try {
            return this.mService.createInterface(false, z, NO_ADDRS, (String) null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public TestNetworkInterface createTapInterface(boolean z, String str) {
        try {
            return this.mService.createInterface(false, z, NO_ADDRS, str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
