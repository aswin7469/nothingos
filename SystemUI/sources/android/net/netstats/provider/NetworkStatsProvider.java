package android.net.netstats.provider;

import android.annotation.SystemApi;
import android.net.NetworkStats;
import android.net.netstats.provider.INetworkStatsProvider;
import android.os.RemoteException;

@SystemApi
public abstract class NetworkStatsProvider {
    public static final int QUOTA_UNLIMITED = -1;
    private final INetworkStatsProvider mProviderBinder = new INetworkStatsProvider.Stub() {
        public void onRequestStatsUpdate(int i) {
            NetworkStatsProvider.this.onRequestStatsUpdate(i);
        }

        public void onSetAlert(long j) {
            NetworkStatsProvider.this.onSetAlert(j);
        }

        public void onSetWarningAndLimit(String str, long j, long j2) {
            NetworkStatsProvider.this.onSetWarningAndLimit(str, j, j2);
        }
    };
    private INetworkStatsProviderCallback mProviderCbBinder;

    public abstract void onRequestStatsUpdate(int i);

    public abstract void onSetAlert(long j);

    public abstract void onSetLimit(String str, long j);

    public INetworkStatsProvider getProviderBinder() {
        return this.mProviderBinder;
    }

    public void setProviderCallbackBinder(INetworkStatsProviderCallback iNetworkStatsProviderCallback) {
        if (this.mProviderCbBinder == null) {
            this.mProviderCbBinder = iNetworkStatsProviderCallback;
            return;
        }
        throw new IllegalArgumentException("provider is already registered");
    }

    public INetworkStatsProviderCallback getProviderCallbackBinder() {
        return this.mProviderCbBinder;
    }

    public INetworkStatsProviderCallback getProviderCallbackBinderOrThrow() {
        INetworkStatsProviderCallback iNetworkStatsProviderCallback = this.mProviderCbBinder;
        if (iNetworkStatsProviderCallback != null) {
            return iNetworkStatsProviderCallback;
        }
        throw new IllegalStateException("the provider is not registered");
    }

    public void notifyStatsUpdated(int i, NetworkStats networkStats, NetworkStats networkStats2) {
        try {
            getProviderCallbackBinderOrThrow().notifyStatsUpdated(i, networkStats, networkStats2);
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public void notifyAlertReached() {
        try {
            getProviderCallbackBinderOrThrow().notifyAlertReached();
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public void notifyWarningReached() {
        try {
            getProviderCallbackBinderOrThrow().notifyWarningReached();
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public void notifyLimitReached() {
        try {
            getProviderCallbackBinderOrThrow().notifyLimitReached();
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public void onSetWarningAndLimit(String str, long j, long j2) {
        onSetLimit(str, j2);
    }
}
