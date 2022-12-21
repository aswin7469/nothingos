package java.net;

import java.p026io.IOException;
import java.util.List;
import java.util.Map;
import sun.security.util.SecurityConstants;

public abstract class ResponseCache {
    private static ResponseCache theResponseCache;

    public abstract CacheResponse get(URI uri, String str, Map<String, List<String>> map) throws IOException;

    public abstract CacheRequest put(URI uri, URLConnection uRLConnection) throws IOException;

    public static synchronized ResponseCache getDefault() {
        ResponseCache responseCache;
        synchronized (ResponseCache.class) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(SecurityConstants.GET_RESPONSECACHE_PERMISSION);
            }
            responseCache = theResponseCache;
        }
        return responseCache;
    }

    public static synchronized void setDefault(ResponseCache responseCache) {
        synchronized (ResponseCache.class) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(SecurityConstants.SET_RESPONSECACHE_PERMISSION);
            }
            theResponseCache = responseCache;
        }
    }
}
