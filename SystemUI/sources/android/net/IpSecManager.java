package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.AndroidException;
import android.util.Log;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import dalvik.system.CloseGuard;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.Objects;

public class IpSecManager {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int DIRECTION_FWD = 2;
    public static final int DIRECTION_IN = 0;
    public static final int DIRECTION_OUT = 1;
    public static final int INVALID_RESOURCE_ID = -1;
    public static final int INVALID_SECURITY_PARAMETER_INDEX = 0;
    private static final String TAG = "IpSecManager";
    private final Context mContext;
    private final IIpSecService mService;

    @Retention(RetentionPolicy.SOURCE)
    public @interface PolicyDirection {
    }

    public interface Status {

        /* renamed from: OK */
        public static final int f40OK = 0;
        public static final int RESOURCE_UNAVAILABLE = 1;
        public static final int SPI_UNAVAILABLE = 2;
    }

    public void removeTunnelModeTransform(Network network, IpSecTransform ipSecTransform) {
    }

    public static final class SpiUnavailableException extends AndroidException {
        private final int mSpi;

        SpiUnavailableException(String str, int i) {
            super(str + " (spi: " + i + NavigationBarInflaterView.KEY_CODE_END);
            this.mSpi = i;
        }

        public int getSpi() {
            return this.mSpi;
        }
    }

    public static final class ResourceUnavailableException extends AndroidException {
        ResourceUnavailableException(String str) {
            super(str);
        }
    }

    public static final class SecurityParameterIndex implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private final InetAddress mDestinationAddress;
        private int mResourceId;
        private final IIpSecService mService;
        private int mSpi;

        public int getSpi() {
            return this.mSpi;
        }

        public void close() {
            try {
                this.mService.releaseSecurityParameterIndex(this.mResourceId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Exception e2) {
                Log.e(IpSecManager.TAG, "Failed to close " + this + ", Exception=" + e2);
            } catch (Throwable th) {
                this.mResourceId = -1;
                this.mCloseGuard.close();
                throw th;
            }
            this.mResourceId = -1;
            this.mCloseGuard.close();
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            CloseGuard closeGuard = this.mCloseGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        }

        private SecurityParameterIndex(IIpSecService iIpSecService, InetAddress inetAddress, int i) throws ResourceUnavailableException, SpiUnavailableException {
            CloseGuard closeGuard = CloseGuard.get();
            this.mCloseGuard = closeGuard;
            this.mSpi = 0;
            this.mResourceId = -1;
            this.mService = iIpSecService;
            this.mDestinationAddress = inetAddress;
            try {
                IpSecSpiResponse allocateSecurityParameterIndex = iIpSecService.allocateSecurityParameterIndex(inetAddress.getHostAddress(), i, new Binder());
                if (allocateSecurityParameterIndex != null) {
                    int i2 = allocateSecurityParameterIndex.status;
                    if (i2 == 0) {
                        this.mSpi = allocateSecurityParameterIndex.spi;
                        int i3 = allocateSecurityParameterIndex.resourceId;
                        this.mResourceId = i3;
                        if (this.mSpi == 0) {
                            throw new RuntimeException("Invalid SPI returned by IpSecService: " + i2);
                        } else if (i3 != -1) {
                            closeGuard.open("open");
                        } else {
                            throw new RuntimeException("Invalid Resource ID returned by IpSecService: " + i2);
                        }
                    } else if (i2 == 1) {
                        throw new ResourceUnavailableException("No more SPIs may be allocated by this requester.");
                    } else if (i2 != 2) {
                        throw new RuntimeException("Unknown status returned by IpSecService: " + i2);
                    } else {
                        throw new SpiUnavailableException("Requested SPI is unavailable", i);
                    }
                } else {
                    throw new NullPointerException("Received null response from IpSecService");
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public int getResourceId() {
            return this.mResourceId;
        }

        public String toString() {
            return "SecurityParameterIndex{spi=" + this.mSpi + ",resourceId=" + this.mResourceId + "}";
        }
    }

    public SecurityParameterIndex allocateSecurityParameterIndex(InetAddress inetAddress) throws ResourceUnavailableException {
        try {
            return new SecurityParameterIndex(this.mService, inetAddress, 0);
        } catch (ServiceSpecificException e) {
            throw rethrowUncheckedExceptionFromServiceSpecificException(e);
        } catch (SpiUnavailableException unused) {
            throw new ResourceUnavailableException("No SPIs available");
        }
    }

    public SecurityParameterIndex allocateSecurityParameterIndex(InetAddress inetAddress, int i) throws SpiUnavailableException, ResourceUnavailableException {
        if (i != 0) {
            try {
                return new SecurityParameterIndex(this.mService, inetAddress, i);
            } catch (ServiceSpecificException e) {
                throw rethrowUncheckedExceptionFromServiceSpecificException(e);
            }
        } else {
            throw new IllegalArgumentException("Requested SPI must be a valid (non-zero) SPI");
        }
    }

    public void applyTransportModeTransform(Socket socket, int i, IpSecTransform ipSecTransform) throws IOException {
        socket.getSoLinger();
        applyTransportModeTransform(socket.getFileDescriptor$(), i, ipSecTransform);
    }

    public void applyTransportModeTransform(DatagramSocket datagramSocket, int i, IpSecTransform ipSecTransform) throws IOException {
        applyTransportModeTransform(datagramSocket.getFileDescriptor$(), i, ipSecTransform);
    }

    public void applyTransportModeTransform(FileDescriptor fileDescriptor, int i, IpSecTransform ipSecTransform) throws IOException {
        ParcelFileDescriptor dup;
        try {
            dup = ParcelFileDescriptor.dup(fileDescriptor);
            this.mService.applyTransportModeTransform(dup, i, ipSecTransform.getResourceId());
            if (dup != null) {
                dup.close();
                return;
            }
            return;
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public void removeTransportModeTransforms(Socket socket) throws IOException {
        socket.getSoLinger();
        removeTransportModeTransforms(socket.getFileDescriptor$());
    }

    public void removeTransportModeTransforms(DatagramSocket datagramSocket) throws IOException {
        removeTransportModeTransforms(datagramSocket.getFileDescriptor$());
    }

    public void removeTransportModeTransforms(FileDescriptor fileDescriptor) throws IOException {
        ParcelFileDescriptor dup;
        try {
            dup = ParcelFileDescriptor.dup(fileDescriptor);
            this.mService.removeTransportModeTransforms(dup);
            if (dup != null) {
                dup.close();
                return;
            }
            return;
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static final class UdpEncapsulationSocket implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private final ParcelFileDescriptor mPfd;
        private final int mPort;
        private int mResourceId;
        private final IIpSecService mService;

        private UdpEncapsulationSocket(IIpSecService iIpSecService, int i) throws ResourceUnavailableException, IOException {
            this.mResourceId = -1;
            CloseGuard closeGuard = CloseGuard.get();
            this.mCloseGuard = closeGuard;
            this.mService = iIpSecService;
            try {
                IpSecUdpEncapResponse openUdpEncapsulationSocket = iIpSecService.openUdpEncapsulationSocket(i, new Binder());
                int i2 = openUdpEncapsulationSocket.status;
                if (i2 == 0) {
                    this.mResourceId = openUdpEncapsulationSocket.resourceId;
                    this.mPort = openUdpEncapsulationSocket.port;
                    this.mPfd = openUdpEncapsulationSocket.fileDescriptor;
                    closeGuard.open("constructor");
                } else if (i2 != 1) {
                    throw new RuntimeException("Unknown status returned by IpSecService: " + openUdpEncapsulationSocket.status);
                } else {
                    throw new ResourceUnavailableException("No more Sockets may be allocated by this requester.");
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public FileDescriptor getFileDescriptor() {
            ParcelFileDescriptor parcelFileDescriptor = this.mPfd;
            if (parcelFileDescriptor == null) {
                return null;
            }
            return parcelFileDescriptor.getFileDescriptor();
        }

        public int getPort() {
            return this.mPort;
        }

        public void close() throws IOException {
            try {
                this.mService.closeUdpEncapsulationSocket(this.mResourceId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Exception e2) {
                Log.e(IpSecManager.TAG, "Failed to close " + this + ", Exception=" + e2);
            } catch (Throwable th) {
                this.mResourceId = -1;
                this.mCloseGuard.close();
                throw th;
            }
            this.mResourceId = -1;
            this.mCloseGuard.close();
            try {
                this.mPfd.close();
            } catch (IOException e3) {
                Log.e(IpSecManager.TAG, "Failed to close UDP Encapsulation Socket with Port= " + this.mPort);
                throw e3;
            }
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            CloseGuard closeGuard = this.mCloseGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public int getResourceId() {
            return this.mResourceId;
        }

        public String toString() {
            return "UdpEncapsulationSocket{port=" + this.mPort + ",resourceId=" + this.mResourceId + "}";
        }
    }

    public UdpEncapsulationSocket openUdpEncapsulationSocket(int i) throws IOException, ResourceUnavailableException {
        if (i != 0) {
            try {
                return new UdpEncapsulationSocket(this.mService, i);
            } catch (ServiceSpecificException e) {
                throw rethrowCheckedExceptionFromServiceSpecificException(e);
            }
        } else {
            throw new IllegalArgumentException("Specified port must be a valid port number!");
        }
    }

    public UdpEncapsulationSocket openUdpEncapsulationSocket() throws IOException, ResourceUnavailableException {
        try {
            return new UdpEncapsulationSocket(this.mService, 0);
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        }
    }

    @SystemApi
    public static final class IpSecTunnelInterface implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private String mInterfaceName;
        private final InetAddress mLocalAddress;
        private final String mOpPackageName;
        private final InetAddress mRemoteAddress;
        private int mResourceId;
        private final IIpSecService mService;
        private final Network mUnderlyingNetwork;

        public String getInterfaceName() {
            return this.mInterfaceName;
        }

        @SystemApi
        public void addAddress(InetAddress inetAddress, int i) throws IOException {
            try {
                this.mService.addAddressToTunnelInterface(this.mResourceId, new LinkAddress(inetAddress, i), this.mOpPackageName);
            } catch (ServiceSpecificException e) {
                throw IpSecManager.rethrowCheckedExceptionFromServiceSpecificException(e);
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        }

        @SystemApi
        public void removeAddress(InetAddress inetAddress, int i) throws IOException {
            try {
                this.mService.removeAddressFromTunnelInterface(this.mResourceId, new LinkAddress(inetAddress, i), this.mOpPackageName);
            } catch (ServiceSpecificException e) {
                throw IpSecManager.rethrowCheckedExceptionFromServiceSpecificException(e);
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        }

        public void setUnderlyingNetwork(Network network) throws IOException {
            try {
                this.mService.setNetworkForTunnelInterface(this.mResourceId, network, this.mOpPackageName);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        private IpSecTunnelInterface(Context context, IIpSecService iIpSecService, InetAddress inetAddress, InetAddress inetAddress2, Network network) throws ResourceUnavailableException, IOException {
            CloseGuard closeGuard = CloseGuard.get();
            this.mCloseGuard = closeGuard;
            this.mResourceId = -1;
            String opPackageName = context.getOpPackageName();
            this.mOpPackageName = opPackageName;
            this.mService = iIpSecService;
            this.mLocalAddress = inetAddress;
            this.mRemoteAddress = inetAddress2;
            this.mUnderlyingNetwork = network;
            try {
                IpSecTunnelInterfaceResponse createTunnelInterface = iIpSecService.createTunnelInterface(inetAddress.getHostAddress(), inetAddress2.getHostAddress(), network, new Binder(), opPackageName);
                int i = createTunnelInterface.status;
                if (i == 0) {
                    this.mResourceId = createTunnelInterface.resourceId;
                    this.mInterfaceName = createTunnelInterface.interfaceName;
                    closeGuard.open("constructor");
                } else if (i != 1) {
                    throw new RuntimeException("Unknown status returned by IpSecService: " + createTunnelInterface.status);
                } else {
                    throw new ResourceUnavailableException("No more tunnel interfaces may be allocated by this requester.");
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void close() {
            try {
                this.mService.deleteTunnelInterface(this.mResourceId, this.mOpPackageName);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Exception e2) {
                Log.e(IpSecManager.TAG, "Failed to close " + this + ", Exception=" + e2);
            } catch (Throwable th) {
                this.mResourceId = -1;
                this.mCloseGuard.close();
                throw th;
            }
            this.mResourceId = -1;
            this.mCloseGuard.close();
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            CloseGuard closeGuard = this.mCloseGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        }

        public int getResourceId() {
            return this.mResourceId;
        }

        public String toString() {
            return "IpSecTunnelInterface{ifname=" + this.mInterfaceName + ",resourceId=" + this.mResourceId + "}";
        }
    }

    @SystemApi
    public IpSecTunnelInterface createIpSecTunnelInterface(InetAddress inetAddress, InetAddress inetAddress2, Network network) throws ResourceUnavailableException, IOException {
        try {
            return new IpSecTunnelInterface(this.mContext, this.mService, inetAddress, inetAddress2, network);
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        }
    }

    @SystemApi
    public void applyTunnelModeTransform(IpSecTunnelInterface ipSecTunnelInterface, int i, IpSecTransform ipSecTransform) throws IOException {
        try {
            this.mService.applyTunnelModeTransform(ipSecTunnelInterface.getResourceId(), i, ipSecTransform.getResourceId(), this.mContext.getOpPackageName());
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    public IpSecTransformResponse createTransform(IpSecConfig ipSecConfig, IBinder iBinder, String str) {
        try {
            return this.mService.createTransform(ipSecConfig, iBinder, str);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void deleteTransform(int i) {
        try {
            this.mService.deleteTransform(i);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public IpSecManager(Context context, IIpSecService iIpSecService) {
        this.mContext = context;
        this.mService = (IIpSecService) Objects.requireNonNull(iIpSecService, "missing service");
    }

    private static void maybeHandleServiceSpecificException(ServiceSpecificException serviceSpecificException) {
        if (serviceSpecificException.errorCode == OsConstants.EINVAL) {
            throw new IllegalArgumentException((Throwable) serviceSpecificException);
        } else if (serviceSpecificException.errorCode == OsConstants.EAGAIN) {
            throw new IllegalStateException((Throwable) serviceSpecificException);
        } else if (serviceSpecificException.errorCode == OsConstants.EOPNOTSUPP || serviceSpecificException.errorCode == OsConstants.EPROTONOSUPPORT) {
            throw new UnsupportedOperationException((Throwable) serviceSpecificException);
        }
    }

    static RuntimeException rethrowUncheckedExceptionFromServiceSpecificException(ServiceSpecificException serviceSpecificException) {
        maybeHandleServiceSpecificException(serviceSpecificException);
        throw new RuntimeException((Throwable) serviceSpecificException);
    }

    static IOException rethrowCheckedExceptionFromServiceSpecificException(ServiceSpecificException serviceSpecificException) throws IOException {
        maybeHandleServiceSpecificException(serviceSpecificException);
        throw new ErrnoException("IpSec encountered errno=" + serviceSpecificException.errorCode, serviceSpecificException.errorCode).rethrowAsIOException();
    }
}
