package android.net;

import android.os.ParcelFileDescriptor;
import android.system.C0308Os;
import android.system.ErrnoException;
import android.util.Log;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.p026io.FileDescriptor;
import java.util.Objects;

public class QosSocketFilter extends QosFilter {
    private static final String TAG = "QosSocketFilter";
    private final QosSocketInfo mQosSocketInfo;

    public QosSocketFilter(QosSocketInfo qosSocketInfo) {
        Objects.requireNonNull(qosSocketInfo, "qosSocketInfo must be non-null");
        this.mQosSocketInfo = qosSocketInfo;
    }

    public QosSocketInfo getQosSocketInfo() {
        return this.mQosSocketInfo;
    }

    public int validate() {
        InetSocketAddress addressFromFileDescriptor = getAddressFromFileDescriptor();
        if (addressFromFileDescriptor == null) {
            return 2;
        }
        return !addressFromFileDescriptor.equals(this.mQosSocketInfo.getLocalSocketAddress()) ? 4 : 0;
    }

    private InetSocketAddress getAddressFromFileDescriptor() {
        FileDescriptor fileDescriptor;
        ParcelFileDescriptor parcelFileDescriptor = this.mQosSocketInfo.getParcelFileDescriptor();
        if (parcelFileDescriptor == null || (fileDescriptor = parcelFileDescriptor.getFileDescriptor()) == null) {
            return null;
        }
        try {
            SocketAddress socketAddress = C0308Os.getsockname(fileDescriptor);
            if (socketAddress instanceof InetSocketAddress) {
                return (InetSocketAddress) socketAddress;
            }
            return null;
        } catch (ErrnoException e) {
            Log.e(TAG, "getAddressFromFileDescriptor: getLocalAddress exception", e);
            return null;
        }
    }

    public Network getNetwork() {
        return this.mQosSocketInfo.getNetwork();
    }

    public boolean matchesLocalAddress(InetAddress inetAddress, int i, int i2) {
        if (this.mQosSocketInfo.getLocalSocketAddress() == null) {
            return false;
        }
        return matchesAddress(this.mQosSocketInfo.getLocalSocketAddress(), inetAddress, i, i2);
    }

    public boolean matchesRemoteAddress(InetAddress inetAddress, int i, int i2) {
        if (this.mQosSocketInfo.getRemoteSocketAddress() == null) {
            return false;
        }
        return matchesAddress(this.mQosSocketInfo.getRemoteSocketAddress(), inetAddress, i, i2);
    }

    public static boolean matchesAddress(InetSocketAddress inetSocketAddress, InetAddress inetAddress, int i, int i2) {
        return i <= inetSocketAddress.getPort() && i2 >= inetSocketAddress.getPort() && inetSocketAddress.getAddress().equals(inetAddress);
    }
}
