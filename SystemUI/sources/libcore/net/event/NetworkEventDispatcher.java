package libcore.net.event;

import android.annotation.SystemApi;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class NetworkEventDispatcher {
    private static final NetworkEventDispatcher instance = new NetworkEventDispatcher();
    private final List<NetworkEventListener> listeners = new CopyOnWriteArrayList();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static NetworkEventDispatcher getInstance() {
        return instance;
    }

    public void addListener(NetworkEventListener networkEventListener) {
        if (networkEventListener != null) {
            this.listeners.add(networkEventListener);
            return;
        }
        throw new NullPointerException("toAdd == null");
    }

    public void removeListener(NetworkEventListener networkEventListener) {
        for (NetworkEventListener next : this.listeners) {
            if (next == networkEventListener) {
                this.listeners.remove((Object) next);
                return;
            }
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void dispatchNetworkConfigurationChange() {
        for (NetworkEventListener onNetworkConfigurationChanged : this.listeners) {
            try {
                onNetworkConfigurationChanged.onNetworkConfigurationChanged();
            } catch (RuntimeException e) {
                System.logI("Exception thrown during network event propagation", e);
            }
        }
    }
}
