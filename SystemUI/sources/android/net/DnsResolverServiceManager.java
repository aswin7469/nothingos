package android.net;

import android.os.IBinder;

public class DnsResolverServiceManager {
    public static final String DNS_RESOLVER_SERVICE = "dnsresolver";
    private final IBinder mResolver;

    DnsResolverServiceManager(IBinder iBinder) {
        this.mResolver = iBinder;
    }

    public IBinder getService() {
        return this.mResolver;
    }
}
