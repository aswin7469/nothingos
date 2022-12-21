package java.nio.channels.spi;

import java.net.ProtocolFamily;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import sun.nio.p033ch.DefaultSelectorProvider;

public abstract class SelectorProvider {
    private static final Object lock = new Object();
    /* access modifiers changed from: private */
    public static SelectorProvider provider;

    public Channel inheritedChannel() throws IOException {
        return null;
    }

    public abstract DatagramChannel openDatagramChannel() throws IOException;

    public abstract DatagramChannel openDatagramChannel(ProtocolFamily protocolFamily) throws IOException;

    public abstract Pipe openPipe() throws IOException;

    public abstract AbstractSelector openSelector() throws IOException;

    public abstract ServerSocketChannel openServerSocketChannel() throws IOException;

    public abstract SocketChannel openSocketChannel() throws IOException;

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        securityManager.checkPermission(new RuntimePermission("selectorProvider"));
        return null;
    }

    private SelectorProvider(Void voidR) {
    }

    protected SelectorProvider() {
        this(checkPermission());
    }

    /* access modifiers changed from: private */
    public static boolean loadProviderFromProperty() {
        String property = System.getProperty("java.nio.channels.spi.SelectorProvider");
        if (property == null) {
            return false;
        }
        try {
            provider = (SelectorProvider) Class.forName(property, true, ClassLoader.getSystemClassLoader()).newInstance();
            return true;
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
    public static boolean loadProviderAsService() {
        Iterator<S> it = ServiceLoader.load(SelectorProvider.class, ClassLoader.getSystemClassLoader()).iterator();
        do {
            try {
                if (!it.hasNext()) {
                    return false;
                }
                provider = (SelectorProvider) it.next();
                return true;
            } catch (ServiceConfigurationError e) {
                if (!(e.getCause() instanceof SecurityException)) {
                    throw e;
                }
            }
        } while (!(e.getCause() instanceof SecurityException));
        throw e;
    }

    public static SelectorProvider provider() {
        synchronized (lock) {
            SelectorProvider selectorProvider = provider;
            if (selectorProvider != null) {
                return selectorProvider;
            }
            SelectorProvider selectorProvider2 = (SelectorProvider) AccessController.doPrivileged(new PrivilegedAction<SelectorProvider>() {
                public SelectorProvider run() {
                    if (SelectorProvider.loadProviderFromProperty()) {
                        return SelectorProvider.provider;
                    }
                    if (SelectorProvider.loadProviderAsService()) {
                        return SelectorProvider.provider;
                    }
                    SelectorProvider.provider = DefaultSelectorProvider.create();
                    return SelectorProvider.provider;
                }
            });
            return selectorProvider2;
        }
    }
}
