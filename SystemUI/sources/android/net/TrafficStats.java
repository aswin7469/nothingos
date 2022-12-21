package android.net;

import android.annotation.SystemApi;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.NetworkStats;
import android.os.Binder;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

public class TrafficStats {
    @Deprecated
    public static final long GB_IN_BYTES = 1073741824;
    @Deprecated
    public static final long KB_IN_BYTES = 1024;
    private static final String LOOPBACK_IFACE = "lo";
    @Deprecated
    public static final long MB_IN_BYTES = 1048576;
    @Deprecated
    public static final long PB_IN_BYTES = 1125899906842624L;
    /* access modifiers changed from: private */
    public static final String TAG = "TrafficStats";
    @SystemApi
    public static final int TAG_NETWORK_STACK_IMPERSONATION_RANGE_END = -113;
    @SystemApi
    public static final int TAG_NETWORK_STACK_IMPERSONATION_RANGE_START = -128;
    @SystemApi
    public static final int TAG_NETWORK_STACK_RANGE_END = -257;
    @SystemApi
    public static final int TAG_NETWORK_STACK_RANGE_START = -768;
    public static final int TAG_SYSTEM_APP = -251;
    public static final int TAG_SYSTEM_BACKUP = -253;
    public static final int TAG_SYSTEM_DOWNLOAD = -255;
    @SystemApi
    public static final int TAG_SYSTEM_IMPERSONATION_RANGE_END = -241;
    @SystemApi
    public static final int TAG_SYSTEM_IMPERSONATION_RANGE_START = -256;
    public static final int TAG_SYSTEM_MEDIA = -254;
    public static final int TAG_SYSTEM_PROBE = -190;
    public static final int TAG_SYSTEM_RESTORE = -252;
    @Deprecated
    public static final long TB_IN_BYTES = 1099511627776L;
    public static final int TYPE_RX_BYTES = 0;
    public static final int TYPE_RX_PACKETS = 1;
    public static final int TYPE_TCP_RX_PACKETS = 4;
    public static final int TYPE_TCP_TX_PACKETS = 5;
    public static final int TYPE_TX_BYTES = 2;
    public static final int TYPE_TX_PACKETS = 3;
    public static final int UID_REMOVED = -4;
    public static final int UID_TETHERING = -5;
    public static final int UNSUPPORTED = -1;
    private static NetworkStats sActiveProfilingStart;
    private static Object sProfilingLock = new Object();
    private static INetworkStatsService sStatsService;
    /* access modifiers changed from: private */
    public static ThreadLocal<UidTag> sThreadUidTag = new ThreadLocal<UidTag>() {
        /* access modifiers changed from: protected */
        public UidTag initialValue() {
            return new UidTag();
        }
    };

    private static long addIfSupported(long j) {
        if (j == -1) {
            return 0;
        }
        return j;
    }

    @Deprecated
    public static long getUidTcpRxBytes(int i) {
        return -1;
    }

    @Deprecated
    public static long getUidTcpRxSegments(int i) {
        return -1;
    }

    @Deprecated
    public static long getUidTcpTxBytes(int i) {
        return -1;
    }

    @Deprecated
    public static long getUidTcpTxSegments(int i) {
        return -1;
    }

    @Deprecated
    public static long getUidUdpRxBytes(int i) {
        return -1;
    }

    @Deprecated
    public static long getUidUdpRxPackets(int i) {
        return -1;
    }

    @Deprecated
    public static long getUidUdpTxBytes(int i) {
        return -1;
    }

    @Deprecated
    public static long getUidUdpTxPackets(int i) {
        return -1;
    }

    /* access modifiers changed from: private */
    public static native int native_tagSocketFd(FileDescriptor fileDescriptor, int i, int i2);

    /* access modifiers changed from: private */
    public static native int native_untagSocketFd(FileDescriptor fileDescriptor);

    static {
        System.loadLibrary("framework-connectivity-tiramisu-jni");
    }

    private static synchronized INetworkStatsService getStatsService() {
        INetworkStatsService iNetworkStatsService;
        synchronized (TrafficStats.class) {
            iNetworkStatsService = sStatsService;
            if (iNetworkStatsService == null) {
                throw new IllegalStateException("TrafficStats not initialized, uid=" + Binder.getCallingUid());
            }
        }
        return iNetworkStatsService;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static synchronized void init(Context context) {
        synchronized (TrafficStats.class) {
            if (sStatsService == null) {
                NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NetworkStatsManager.class);
                if (networkStatsManager == null) {
                    String str = TAG;
                    Log.e(str, "TrafficStats not initialized, uid=" + Binder.getCallingUid());
                    return;
                }
                sStatsService = networkStatsManager.getBinder();
                return;
            }
            throw new IllegalStateException("TrafficStats is already initialized, uid=" + Binder.getCallingUid());
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void attachSocketTagger() {
        dalvik.system.SocketTagger.set(new SocketTagger());
    }

    private static class SocketTagger extends dalvik.system.SocketTagger {
        private static final boolean LOGD = true;

        SocketTagger() {
        }

        public void tag(FileDescriptor fileDescriptor) throws SocketException {
            int r0;
            UidTag uidTag = (UidTag) TrafficStats.sThreadUidTag.get();
            String r02 = TrafficStats.TAG;
            Log.d(r02, "tagSocket(" + fileDescriptor.getInt$() + ") with statsTag=0x" + Integer.toHexString(uidTag.tag) + ", statsUid=" + uidTag.uid);
            if (uidTag.tag == -1) {
                StrictMode.noteUntaggedSocket();
            }
            if (!(uidTag.tag == -1 && uidTag.uid == -1) && (r0 = TrafficStats.native_tagSocketFd(fileDescriptor, uidTag.tag, uidTag.uid)) < 0) {
                String r1 = TrafficStats.TAG;
                Log.i(r1, "tagSocketFd(" + fileDescriptor.getInt$() + ", " + uidTag.tag + ", " + uidTag.uid + ") failed with errno" + r0);
            }
        }

        public void untag(FileDescriptor fileDescriptor) throws SocketException {
            int r3;
            String r32 = TrafficStats.TAG;
            Log.i(r32, "untagSocket(" + fileDescriptor.getInt$() + NavigationBarInflaterView.KEY_CODE_END);
            UidTag uidTag = (UidTag) TrafficStats.sThreadUidTag.get();
            if (!(uidTag.tag == -1 && uidTag.uid == -1) && (r3 = TrafficStats.native_untagSocketFd(fileDescriptor)) < 0) {
                String r0 = TrafficStats.TAG;
                Log.w(r0, "untagSocket(" + fileDescriptor.getInt$() + ") failed with errno " + r3);
            }
        }
    }

    private static class UidTag {
        public int tag;
        public int uid;

        private UidTag() {
            this.tag = -1;
            this.uid = -1;
        }
    }

    public static void setThreadStatsTag(int i) {
        getAndSetThreadStatsTag(i);
    }

    public static int getAndSetThreadStatsTag(int i) {
        int i2 = sThreadUidTag.get().tag;
        sThreadUidTag.get().tag = i;
        return i2;
    }

    @SystemApi
    public static void setThreadStatsTagBackup() {
        setThreadStatsTag(TAG_SYSTEM_BACKUP);
    }

    @SystemApi
    public static void setThreadStatsTagRestore() {
        setThreadStatsTag(TAG_SYSTEM_RESTORE);
    }

    @SystemApi
    public static void setThreadStatsTagApp() {
        setThreadStatsTag(TAG_SYSTEM_APP);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setThreadStatsTagDownload() {
        setThreadStatsTag(TAG_SYSTEM_DOWNLOAD);
    }

    public static int getThreadStatsTag() {
        return sThreadUidTag.get().tag;
    }

    public static void clearThreadStatsTag() {
        sThreadUidTag.get().tag = -1;
    }

    public static void setThreadStatsUid(int i) {
        sThreadUidTag.get().uid = i;
    }

    public static int getThreadStatsUid() {
        return sThreadUidTag.get().uid;
    }

    @Deprecated
    public static void setThreadStatsUidSelf() {
        setThreadStatsUid(Process.myUid());
    }

    public static void clearThreadStatsUid() {
        setThreadStatsUid(-1);
    }

    public static void tagSocket(Socket socket) throws SocketException {
        SocketTagger.get().tag(socket);
    }

    public static void untagSocket(Socket socket) throws SocketException {
        SocketTagger.get().untag(socket);
    }

    public static void tagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
        SocketTagger.get().tag(datagramSocket);
    }

    public static void untagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
        SocketTagger.get().untag(datagramSocket);
    }

    public static void tagFileDescriptor(FileDescriptor fileDescriptor) throws IOException {
        SocketTagger.get().tag(fileDescriptor);
    }

    public static void untagFileDescriptor(FileDescriptor fileDescriptor) throws IOException {
        SocketTagger.get().untag(fileDescriptor);
    }

    public static void startDataProfiling(Context context) {
        synchronized (sProfilingLock) {
            if (sActiveProfilingStart == null) {
                sActiveProfilingStart = getDataLayerSnapshotForUid(context);
            } else {
                throw new IllegalStateException("already profiling data");
            }
        }
    }

    public static NetworkStats stopDataProfiling(Context context) {
        NetworkStats subtract;
        synchronized (sProfilingLock) {
            if (sActiveProfilingStart != null) {
                subtract = NetworkStats.subtract(getDataLayerSnapshotForUid(context), sActiveProfilingStart, (NetworkStats.NonMonotonicObserver) null, null);
                sActiveProfilingStart = null;
            } else {
                throw new IllegalStateException("not profiling data");
            }
        }
        return subtract;
    }

    public static void incrementOperationCount(int i) {
        incrementOperationCount(getThreadStatsTag(), i);
    }

    public static void incrementOperationCount(int i, int i2) {
        try {
            getStatsService().incrementOperationCount(Process.myUid(), i, i2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void closeQuietly(INetworkStatsSession iNetworkStatsSession) {
        if (iNetworkStatsSession != null) {
            try {
                iNetworkStatsSession.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    public static long getMobileTxPackets() {
        long j = 0;
        for (String txPackets : getMobileIfaces()) {
            j += addIfSupported(getTxPackets(txPackets));
        }
        return j;
    }

    public static long getMobileRxPackets() {
        long j = 0;
        for (String rxPackets : getMobileIfaces()) {
            j += addIfSupported(getRxPackets(rxPackets));
        }
        return j;
    }

    public static long getMobileTxBytes() {
        long j = 0;
        for (String txBytes : getMobileIfaces()) {
            j += addIfSupported(getTxBytes(txBytes));
        }
        return j;
    }

    public static long getMobileRxBytes() {
        long j = 0;
        for (String rxBytes : getMobileIfaces()) {
            j += addIfSupported(getRxBytes(rxBytes));
        }
        return j;
    }

    public static long getMobileTcpRxPackets() {
        String[] mobileIfaces = getMobileIfaces();
        int length = mobileIfaces.length;
        long j = 0;
        int i = 0;
        while (i < length) {
            try {
                j += addIfSupported(getStatsService().getIfaceStats(mobileIfaces[i], 4));
                i++;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return j;
    }

    public static long getMobileTcpTxPackets() {
        String[] mobileIfaces = getMobileIfaces();
        int length = mobileIfaces.length;
        long j = 0;
        int i = 0;
        while (i < length) {
            try {
                j += addIfSupported(getStatsService().getIfaceStats(mobileIfaces[i], 5));
                i++;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return j;
    }

    public static long getTxPackets(String str) {
        try {
            return getStatsService().getIfaceStats(str, 3);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getRxPackets(String str) {
        try {
            return getStatsService().getIfaceStats(str, 1);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getTxBytes(String str) {
        try {
            return getStatsService().getIfaceStats(str, 2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getRxBytes(String str) {
        try {
            return getStatsService().getIfaceStats(str, 0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getLoopbackTxPackets() {
        try {
            return getStatsService().getIfaceStats(LOOPBACK_IFACE, 3);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getLoopbackRxPackets() {
        try {
            return getStatsService().getIfaceStats(LOOPBACK_IFACE, 1);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getLoopbackTxBytes() {
        try {
            return getStatsService().getIfaceStats(LOOPBACK_IFACE, 2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getLoopbackRxBytes() {
        try {
            return getStatsService().getIfaceStats(LOOPBACK_IFACE, 0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getTotalTxPackets() {
        try {
            return getStatsService().getTotalStats(3);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getTotalRxPackets() {
        try {
            return getStatsService().getTotalStats(1);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getTotalTxBytes() {
        try {
            return getStatsService().getTotalStats(2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getTotalRxBytes() {
        try {
            return getStatsService().getTotalStats(0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getUidTxBytes(int i) {
        try {
            return getStatsService().getUidStats(i, 2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getUidRxBytes(int i) {
        try {
            return getStatsService().getUidStats(i, 0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getUidTxPackets(int i) {
        try {
            return getStatsService().getUidStats(i, 3);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static long getUidRxPackets(int i) {
        try {
            return getStatsService().getUidStats(i, 1);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static NetworkStats getDataLayerSnapshotForUid(Context context) {
        try {
            return getStatsService().getDataLayerSnapshotForUid(Process.myUid());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static String[] getMobileIfaces() {
        try {
            return getStatsService().getMobileIfaces();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
