package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.IpSecManager;
import android.os.Binder;
import android.os.ServiceSpecificException;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.p026io.IOException;
import java.util.Objects;

public final class IpSecTransform implements AutoCloseable {
    public static final int ENCAP_ESPINUDP = 2;
    public static final int ENCAP_ESPINUDP_NON_IKE = 1;
    public static final int ENCAP_NONE = 0;
    public static final int MODE_TRANSPORT = 0;
    public static final int MODE_TUNNEL = 1;
    private static final String TAG = "IpSecTransform";
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final IpSecConfig mConfig;
    private final Context mContext;
    private int mResourceId;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EncapType {
    }

    public static class NattKeepaliveCallback {
        public static final int ERROR_HARDWARE_ERROR = 3;
        public static final int ERROR_HARDWARE_UNSUPPORTED = 2;
        public static final int ERROR_INVALID_NETWORK = 1;

        public void onError(int i) {
        }

        public void onStarted() {
        }

        public void onStopped() {
        }
    }

    public IpSecTransform(Context context, IpSecConfig ipSecConfig) {
        this.mContext = context;
        this.mConfig = new IpSecConfig(ipSecConfig);
        this.mResourceId = -1;
    }

    private IpSecManager getIpSecManager(Context context) {
        return (IpSecManager) context.getSystemService(IpSecManager.class);
    }

    private void checkResultStatus(int i) throws IOException, IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException {
        if (i == 0) {
            return;
        }
        if (i != 1) {
            if (i == 2) {
                Log.wtf(TAG, "Attempting to use an SPI that was somehow not reserved");
            }
            throw new IllegalStateException("Failed to Create a Transform with status code " + i);
        }
        throw new IpSecManager.ResourceUnavailableException("Failed to allocate a new IpSecTransform");
    }

    /* access modifiers changed from: private */
    public IpSecTransform activate() throws IOException, IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException {
        synchronized (this) {
            try {
                IpSecTransformResponse createTransform = getIpSecManager(this.mContext).createTransform(this.mConfig, new Binder(), this.mContext.getOpPackageName());
                checkResultStatus(createTransform.status);
                this.mResourceId = createTransform.resourceId;
                Log.d(TAG, "Added Transform with Id " + this.mResourceId);
                this.mCloseGuard.open("build");
            } catch (ServiceSpecificException e) {
                throw IpSecManager.rethrowUncheckedExceptionFromServiceSpecificException(e);
            } catch (Throwable th) {
                throw th;
            }
        }
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IpSecTransform)) {
            return false;
        }
        IpSecTransform ipSecTransform = (IpSecTransform) obj;
        if (!getConfig().equals(ipSecTransform.getConfig()) || this.mResourceId != ipSecTransform.mResourceId) {
            return false;
        }
        return true;
    }

    public void close() {
        Log.d(TAG, "Removing Transform with Id " + this.mResourceId);
        if (this.mResourceId == -1) {
            this.mCloseGuard.close();
            return;
        }
        try {
            getIpSecManager(this.mContext).deleteTransform(this.mResourceId);
        } catch (Exception e) {
            Log.e(TAG, "Failed to close " + this + ", Exception=" + e);
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

    /* access modifiers changed from: package-private */
    public IpSecConfig getConfig() {
        return this.mConfig;
    }

    public int getResourceId() {
        return this.mResourceId;
    }

    public static class Builder {
        private IpSecConfig mConfig = new IpSecConfig();
        private Context mContext;

        public Builder setEncryption(IpSecAlgorithm ipSecAlgorithm) {
            Objects.requireNonNull(ipSecAlgorithm);
            this.mConfig.setEncryption(ipSecAlgorithm);
            return this;
        }

        public Builder setAuthentication(IpSecAlgorithm ipSecAlgorithm) {
            Objects.requireNonNull(ipSecAlgorithm);
            this.mConfig.setAuthentication(ipSecAlgorithm);
            return this;
        }

        public Builder setAuthenticatedEncryption(IpSecAlgorithm ipSecAlgorithm) {
            Objects.requireNonNull(ipSecAlgorithm);
            this.mConfig.setAuthenticatedEncryption(ipSecAlgorithm);
            return this;
        }

        public Builder setIpv4Encapsulation(IpSecManager.UdpEncapsulationSocket udpEncapsulationSocket, int i) {
            Objects.requireNonNull(udpEncapsulationSocket);
            this.mConfig.setEncapType(2);
            if (udpEncapsulationSocket.getResourceId() != -1) {
                this.mConfig.setEncapSocketResourceId(udpEncapsulationSocket.getResourceId());
                this.mConfig.setEncapRemotePort(i);
                return this;
            }
            throw new IllegalArgumentException("Invalid UdpEncapsulationSocket");
        }

        public IpSecTransform buildTransportModeTransform(InetAddress inetAddress, IpSecManager.SecurityParameterIndex securityParameterIndex) throws IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException, IOException {
            Objects.requireNonNull(inetAddress);
            Objects.requireNonNull(securityParameterIndex);
            if (securityParameterIndex.getResourceId() != -1) {
                this.mConfig.setMode(0);
                this.mConfig.setSourceAddress(inetAddress.getHostAddress());
                this.mConfig.setSpiResourceId(securityParameterIndex.getResourceId());
                return new IpSecTransform(this.mContext, this.mConfig).activate();
            }
            throw new IllegalArgumentException("Invalid SecurityParameterIndex");
        }

        @SystemApi
        public IpSecTransform buildTunnelModeTransform(InetAddress inetAddress, IpSecManager.SecurityParameterIndex securityParameterIndex) throws IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException, IOException {
            Objects.requireNonNull(inetAddress);
            Objects.requireNonNull(securityParameterIndex);
            if (securityParameterIndex.getResourceId() != -1) {
                this.mConfig.setMode(1);
                this.mConfig.setSourceAddress(inetAddress.getHostAddress());
                this.mConfig.setSpiResourceId(securityParameterIndex.getResourceId());
                return new IpSecTransform(this.mContext, this.mConfig).activate();
            }
            throw new IllegalArgumentException("Invalid SecurityParameterIndex");
        }

        public Builder(Context context) {
            Objects.requireNonNull(context);
            this.mContext = context;
        }
    }

    public String toString() {
        return "IpSecTransform{resourceId=" + this.mResourceId + "}";
    }
}
