package android.nearby;

import android.annotation.SystemApi;

@SystemApi
public interface ScanCallback {
    void onDiscovered(NearbyDevice nearbyDevice);

    void onLost(NearbyDevice nearbyDevice);

    void onUpdated(NearbyDevice nearbyDevice);
}
