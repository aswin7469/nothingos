package sun.net.ftp;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ServiceConfigurationError;
import sun.net.ftp.impl.DefaultFtpClientProvider;

public abstract class FtpClientProvider {
    private static final Object lock = new Object();
    /* access modifiers changed from: private */
    public static FtpClientProvider provider;

    /* access modifiers changed from: private */
    public static boolean loadProviderAsService() {
        return false;
    }

    public abstract FtpClient createFtpClient();

    protected FtpClientProvider() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("ftpClientProvider"));
        }
    }

    /* access modifiers changed from: private */
    public static boolean loadProviderFromProperty() {
        String property = System.getProperty("sun.net.ftpClientProvider");
        if (property == null) {
            return false;
        }
        try {
            provider = (FtpClientProvider) Class.forName(property, true, (ClassLoader) null).newInstance();
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SecurityException e) {
            throw new ServiceConfigurationError(e.toString());
        }
    }

    public static FtpClientProvider provider() {
        synchronized (lock) {
            FtpClientProvider ftpClientProvider = provider;
            if (ftpClientProvider != null) {
                return ftpClientProvider;
            }
            FtpClientProvider ftpClientProvider2 = (FtpClientProvider) AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    if (FtpClientProvider.loadProviderFromProperty()) {
                        return FtpClientProvider.provider;
                    }
                    if (FtpClientProvider.loadProviderAsService()) {
                        return FtpClientProvider.provider;
                    }
                    FtpClientProvider.provider = new DefaultFtpClientProvider();
                    return FtpClientProvider.provider;
                }
            });
            return ftpClientProvider2;
        }
    }
}
