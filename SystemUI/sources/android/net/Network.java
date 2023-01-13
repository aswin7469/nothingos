package android.net;

import android.annotation.SystemApi;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.system.C0308Os;
import android.system.ErrnoException;
import android.system.OsConstants;
import com.nothing.p023os.device.DeviceConstant;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import libcore.net.http.Dns;
import libcore.net.http.HttpURLConnectionFactory;
import libcore.p030io.IoUtils;

public class Network implements Parcelable {
    public static final Parcelable.Creator<Network> CREATOR = new Parcelable.Creator<Network>() {
        public Network createFromParcel(Parcel parcel) {
            return new Network(parcel.readInt());
        }

        public Network[] newArray(int i) {
            return new Network[i];
        }
    };
    private static final long HANDLE_MAGIC = 3405697037L;
    private static final int HANDLE_MAGIC_SIZE = 32;
    private static final int USE_LOCAL_NAMESERVERS_FLAG = Integer.MIN_VALUE;
    private static final boolean httpKeepAlive;
    private static final long httpKeepAliveDurationMs = Long.parseLong(System.getProperty("http.keepAliveDuration", "300000"));
    private static final int httpMaxConnections;
    private final Object mLock;
    private volatile NetworkBoundSocketFactory mNetworkBoundSocketFactory;
    private final transient boolean mPrivateDnsBypass;
    private HttpURLConnectionFactory mUrlConnectionFactory;
    public final int netId;

    public int describeContents() {
        return 0;
    }

    static {
        boolean parseBoolean = Boolean.parseBoolean(System.getProperty("http.keepAlive", "true"));
        httpKeepAlive = parseBoolean;
        httpMaxConnections = parseBoolean ? Integer.parseInt(System.getProperty("http.maxConnections", DeviceConstant.NOISE_CANCELLATION_OFF)) : 0;
    }

    public Network(int i) {
        this(i, false);
    }

    public Network(int i, boolean z) {
        this.mNetworkBoundSocketFactory = null;
        this.mLock = new Object();
        this.netId = i;
        this.mPrivateDnsBypass = z;
    }

    @SystemApi
    public Network(Network network) {
        this(network.netId, network.mPrivateDnsBypass);
    }

    public InetAddress[] getAllByName(String str) throws UnknownHostException {
        return InetAddress.getAllByNameOnNet(str, getNetIdForResolv());
    }

    public InetAddress getByName(String str) throws UnknownHostException {
        return InetAddress.getByNameOnNet(str, getNetIdForResolv());
    }

    @SystemApi
    public Network getPrivateDnsBypassingCopy() {
        return new Network(this.netId, true);
    }

    @SystemApi
    public int getNetId() {
        return this.netId;
    }

    public int getNetIdForResolv() {
        if (this.mPrivateDnsBypass) {
            return this.netId | Integer.MIN_VALUE;
        }
        return this.netId;
    }

    private class NetworkBoundSocketFactory extends SocketFactory {
        private NetworkBoundSocketFactory() {
        }

        private Socket connectToHost(String str, int i, SocketAddress socketAddress) throws IOException {
            Socket createSocket;
            InetAddress[] allByName = Network.this.getAllByName(str);
            int i2 = 0;
            while (i2 < allByName.length) {
                try {
                    createSocket = createSocket();
                    if (socketAddress != null) {
                        createSocket.bind(socketAddress);
                    }
                    createSocket.connect(new InetSocketAddress(allByName[i2], i));
                    return createSocket;
                } catch (IOException e) {
                    if (i2 != allByName.length - 1) {
                        i2++;
                    } else {
                        throw e;
                    }
                } catch (Throwable th) {
                    IoUtils.closeQuietly(createSocket);
                    throw th;
                }
            }
            throw new UnknownHostException(str);
        }

        public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
            return connectToHost(str, i, new InetSocketAddress(inetAddress, i2));
        }

        public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
            Socket createSocket = createSocket();
            try {
                createSocket.bind(new InetSocketAddress(inetAddress2, i2));
                createSocket.connect(new InetSocketAddress(inetAddress, i));
                return createSocket;
            } catch (Throwable th) {
                IoUtils.closeQuietly(createSocket);
                throw th;
            }
        }

        public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
            Socket createSocket = createSocket();
            try {
                createSocket.connect(new InetSocketAddress(inetAddress, i));
                return createSocket;
            } catch (Throwable th) {
                IoUtils.closeQuietly(createSocket);
                throw th;
            }
        }

        public Socket createSocket(String str, int i) throws IOException {
            return connectToHost(str, i, (SocketAddress) null);
        }

        public Socket createSocket() throws IOException {
            Socket socket = new Socket();
            try {
                Network.this.bindSocket(socket);
                return socket;
            } catch (Throwable th) {
                IoUtils.closeQuietly(socket);
                throw th;
            }
        }
    }

    public SocketFactory getSocketFactory() {
        if (this.mNetworkBoundSocketFactory == null) {
            synchronized (this.mLock) {
                if (this.mNetworkBoundSocketFactory == null) {
                    this.mNetworkBoundSocketFactory = new NetworkBoundSocketFactory();
                }
            }
        }
        return this.mNetworkBoundSocketFactory;
    }

    private static HttpURLConnectionFactory createUrlConnectionFactory(Dns dns) {
        HttpURLConnectionFactory createInstance = HttpURLConnectionFactory.createInstance();
        createInstance.setDns(dns);
        createInstance.setNewConnectionPool(httpMaxConnections, httpKeepAliveDurationMs, TimeUnit.MILLISECONDS);
        return createInstance;
    }

    public URLConnection openConnection(URL url) throws IOException {
        Proxy proxy;
        ConnectivityManager instanceOrNull = ConnectivityManager.getInstanceOrNull();
        if (instanceOrNull != null) {
            ProxyInfo proxyForNetwork = instanceOrNull.getProxyForNetwork(this);
            if (proxyForNetwork != null) {
                proxy = proxyForNetwork.makeProxy();
            } else {
                proxy = Proxy.NO_PROXY;
            }
            return openConnection(url, proxy);
        }
        throw new IOException("No ConnectivityManager yet constructed, please construct one");
    }

    public URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        HttpURLConnectionFactory httpURLConnectionFactory;
        if (proxy != null) {
            synchronized (this.mLock) {
                if (this.mUrlConnectionFactory == null) {
                    this.mUrlConnectionFactory = createUrlConnectionFactory(new Network$$ExternalSyntheticLambda0(this));
                }
                httpURLConnectionFactory = this.mUrlConnectionFactory;
            }
            return httpURLConnectionFactory.openConnection(url, getSocketFactory(), proxy);
        }
        throw new IllegalArgumentException("proxy is null");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$openConnection$0$android-net-Network  reason: not valid java name */
    public /* synthetic */ List m1891lambda$openConnection$0$androidnetNetwork(String str) throws UnknownHostException {
        return Arrays.asList(getAllByName(str));
    }

    public void bindSocket(DatagramSocket datagramSocket) throws IOException {
        datagramSocket.getReuseAddress();
        ParcelFileDescriptor fromDatagramSocket = ParcelFileDescriptor.fromDatagramSocket(datagramSocket);
        try {
            bindSocket(fromDatagramSocket.getFileDescriptor());
            if (fromDatagramSocket != null) {
                fromDatagramSocket.close();
                return;
            }
            return;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public void bindSocket(Socket socket) throws IOException {
        socket.getReuseAddress();
        ParcelFileDescriptor fromSocket = ParcelFileDescriptor.fromSocket(socket);
        try {
            bindSocket(fromSocket.getFileDescriptor());
            if (fromSocket != null) {
                fromSocket.close();
                return;
            }
            return;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public void bindSocket(FileDescriptor fileDescriptor) throws IOException {
        try {
            if (((InetSocketAddress) C0308Os.getpeername(fileDescriptor)).getAddress().isAnyLocalAddress()) {
                int bindSocketToNetwork = NetworkUtils.bindSocketToNetwork(fileDescriptor, this.netId);
                if (bindSocketToNetwork != 0) {
                    throw new ErrnoException("Binding socket to network " + this.netId, -bindSocketToNetwork).rethrowAsSocketException();
                }
                return;
            }
            throw new SocketException("Socket is connected");
        } catch (ErrnoException e) {
            if (e.errno != OsConstants.ENOTCONN) {
                throw e.rethrowAsSocketException();
            }
        } catch (ClassCastException unused) {
            throw new SocketException("Only AF_INET/AF_INET6 sockets supported");
        }
    }

    public static Network fromNetworkHandle(long j) {
        if (j == 0) {
            throw new IllegalArgumentException("Network.fromNetworkHandle refusing to instantiate NETID_UNSET Network.");
        } else if ((UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER & j) == HANDLE_MAGIC) {
            int i = (int) (j >>> 32);
            return new Network(Integer.MAX_VALUE & i, (i & Integer.MIN_VALUE) != 0);
        } else {
            throw new IllegalArgumentException("Value passed to fromNetworkHandle() is not a network handle.");
        }
    }

    public long getNetworkHandle() {
        if (this.netId == 0) {
            return 0;
        }
        return (((long) getNetIdForResolv()) << 32) | HANDLE_MAGIC;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.netId);
    }

    public boolean equals(Object obj) {
        if ((obj instanceof Network) && this.netId == ((Network) obj).netId) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.netId * 11;
    }

    public String toString() {
        return Integer.toString(this.netId);
    }
}
