package java.nio.channels.spi;

import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import sun.nio.p033ch.DefaultAsynchronousChannelProvider;

public abstract class AsynchronousChannelProvider {
    public abstract AsynchronousChannelGroup openAsynchronousChannelGroup(int i, ThreadFactory threadFactory) throws IOException;

    public abstract AsynchronousChannelGroup openAsynchronousChannelGroup(ExecutorService executorService, int i) throws IOException;

    public abstract AsynchronousServerSocketChannel openAsynchronousServerSocketChannel(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException;

    public abstract AsynchronousSocketChannel openAsynchronousSocketChannel(AsynchronousChannelGroup asynchronousChannelGroup) throws IOException;

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        securityManager.checkPermission(new RuntimePermission("asynchronousChannelProvider"));
        return null;
    }

    private AsynchronousChannelProvider(Void voidR) {
    }

    protected AsynchronousChannelProvider() {
        this(checkPermission());
    }

    private static class ProviderHolder {
        static final AsynchronousChannelProvider provider = load();

        private ProviderHolder() {
        }

        private static AsynchronousChannelProvider load() {
            return (AsynchronousChannelProvider) AccessController.doPrivileged(new PrivilegedAction<AsynchronousChannelProvider>() {
                public AsynchronousChannelProvider run() {
                    AsynchronousChannelProvider r0 = ProviderHolder.loadProviderFromProperty();
                    if (r0 != null) {
                        return r0;
                    }
                    AsynchronousChannelProvider r02 = ProviderHolder.loadProviderAsService();
                    if (r02 != null) {
                        return r02;
                    }
                    return DefaultAsynchronousChannelProvider.create();
                }
            });
        }

        /* access modifiers changed from: private */
        public static AsynchronousChannelProvider loadProviderFromProperty() {
            String property = System.getProperty("java.nio.channels.spi.AsynchronousChannelProvider");
            if (property == null) {
                return null;
            }
            try {
                return (AsynchronousChannelProvider) Class.forName(property, true, ClassLoader.getSystemClassLoader()).newInstance();
            } catch (ClassNotFoundException e) {
                throw new ServiceConfigurationError((String) null, e);
            } catch (IllegalAccessException e2) {
                throw new ServiceConfigurationError((String) null, e2);
            } catch (InstantiationException e3) {
                throw new ServiceConfigurationError((String) null, e3);
            } catch (SecurityException e4) {
                throw new ServiceConfigurationError((String) null, e4);
            }
        }

        /* access modifiers changed from: private */
        public static AsynchronousChannelProvider loadProviderAsService() {
            Iterator<S> it = ServiceLoader.load(AsynchronousChannelProvider.class, ClassLoader.getSystemClassLoader()).iterator();
            do {
                try {
                    if (it.hasNext()) {
                        return (AsynchronousChannelProvider) it.next();
                    }
                    return null;
                } catch (ServiceConfigurationError e) {
                    if (!(e.getCause() instanceof SecurityException)) {
                        throw e;
                    }
                }
            } while (!(e.getCause() instanceof SecurityException));
            throw e;
        }
    }

    public static AsynchronousChannelProvider provider() {
        return ProviderHolder.provider;
    }
}
