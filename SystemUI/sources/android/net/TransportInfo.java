package android.net;

import android.annotation.SystemApi;

public interface TransportInfo {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    long getApplicableRedactions() {
        return 0;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    TransportInfo makeCopy(long j) {
        return this;
    }
}
