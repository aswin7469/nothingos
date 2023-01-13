package jdk.net;

import java.net.SocketException;
import java.net.SocketOption;
import java.p026io.FileDescriptor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jdk.internal.misc.JavaIOFileDescriptorAccess;
import jdk.internal.misc.SharedSecrets;

public final class ExtendedSocketOptions {
    public static final SocketOption<SocketFlow> SO_FLOW_SLA = new ExtSocketOption("SO_FLOW_SLA", SocketFlow.class);
    public static final SocketOption<Integer> TCP_KEEPCOUNT = new ExtSocketOption("TCP_KEEPCOUNT", Integer.class);
    public static final SocketOption<Integer> TCP_KEEPIDLE = new ExtSocketOption("TCP_KEEPIDLE", Integer.class);
    public static final SocketOption<Integer> TCP_KEEPINTERVAL = new ExtSocketOption("TCP_KEEPINTERVAL", Integer.class);
    public static final SocketOption<Boolean> TCP_QUICKACK = new ExtSocketOption("TCP_QUICKACK", Boolean.class);
    private static final Set<SocketOption<?>> extendedOptions;
    private static final JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
    /* access modifiers changed from: private */
    public static final boolean flowSupported;
    private static final boolean keepAliveOptSupported;
    private static final PlatformSocketOptions platformSocketOptions;
    private static final boolean quickAckSupported;

    private static class ExtSocketOption<T> implements SocketOption<T> {
        private final String name;
        private final Class<T> type;

        ExtSocketOption(String str, Class<T> cls) {
            this.name = str;
            this.type = cls;
        }

        public String name() {
            return this.name;
        }

        public Class<T> type() {
            return this.type;
        }

        public String toString() {
            return this.name;
        }
    }

    private ExtendedSocketOptions() {
    }

    static {
        PlatformSocketOptions platformSocketOptions2 = PlatformSocketOptions.get();
        platformSocketOptions = platformSocketOptions2;
        flowSupported = platformSocketOptions2.flowSupported();
        quickAckSupported = platformSocketOptions2.quickAckSupported();
        keepAliveOptSupported = platformSocketOptions2.keepAliveOptionsSupported();
        Set<SocketOption<?>> options = options();
        extendedOptions = options;
        sun.net.ext.ExtendedSocketOptions.register(new sun.net.ext.ExtendedSocketOptions(options) {
            static final /* synthetic */ boolean $assertionsDisabled = false;

            static {
                Class<ExtendedSocketOptions> cls = ExtendedSocketOptions.class;
            }

            public void setOption(FileDescriptor fileDescriptor, SocketOption<?> socketOption, Object obj) throws SocketException {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkPermission(new NetworkPermission("setOption." + socketOption.name()));
                }
                if (fileDescriptor == null || !fileDescriptor.valid()) {
                    throw new SocketException("socket closed");
                } else if (socketOption == ExtendedSocketOptions.SO_FLOW_SLA) {
                    ExtendedSocketOptions.setFlowOption(fileDescriptor, (SocketFlow) ExtendedSocketOptions.checkValueType(obj, socketOption.type()));
                } else if (socketOption == ExtendedSocketOptions.TCP_QUICKACK) {
                    ExtendedSocketOptions.setQuickAckOption(fileDescriptor, ((Boolean) obj).booleanValue());
                } else if (socketOption == ExtendedSocketOptions.TCP_KEEPCOUNT) {
                    ExtendedSocketOptions.setTcpkeepAliveProbes(fileDescriptor, ((Integer) obj).intValue());
                } else if (socketOption == ExtendedSocketOptions.TCP_KEEPIDLE) {
                    ExtendedSocketOptions.setTcpKeepAliveTime(fileDescriptor, ((Integer) obj).intValue());
                } else if (socketOption == ExtendedSocketOptions.TCP_KEEPINTERVAL) {
                    ExtendedSocketOptions.setTcpKeepAliveIntvl(fileDescriptor, ((Integer) obj).intValue());
                } else {
                    throw new InternalError("Unexpected option " + socketOption);
                }
            }

            public Object getOption(FileDescriptor fileDescriptor, SocketOption<?> socketOption) throws SocketException {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkPermission(new NetworkPermission("getOption." + socketOption.name()));
                }
                if (fileDescriptor == null || !fileDescriptor.valid()) {
                    throw new SocketException("socket closed");
                } else if (socketOption == ExtendedSocketOptions.SO_FLOW_SLA) {
                    SocketFlow create = SocketFlow.create();
                    ExtendedSocketOptions.getFlowOption(fileDescriptor, create);
                    return create;
                } else if (socketOption == ExtendedSocketOptions.TCP_QUICKACK) {
                    return ExtendedSocketOptions.getQuickAckOption(fileDescriptor);
                } else {
                    if (socketOption == ExtendedSocketOptions.TCP_KEEPCOUNT) {
                        return Integer.valueOf(ExtendedSocketOptions.getTcpkeepAliveProbes(fileDescriptor));
                    }
                    if (socketOption == ExtendedSocketOptions.TCP_KEEPIDLE) {
                        return Integer.valueOf(ExtendedSocketOptions.getTcpKeepAliveTime(fileDescriptor));
                    }
                    if (socketOption == ExtendedSocketOptions.TCP_KEEPINTERVAL) {
                        return Integer.valueOf(ExtendedSocketOptions.getTcpKeepAliveIntvl(fileDescriptor));
                    }
                    throw new InternalError("Unexpected option " + socketOption);
                }
            }
        });
    }

    static Set<SocketOption<?>> options() {
        HashSet hashSet = new HashSet();
        if (flowSupported) {
            hashSet.add(SO_FLOW_SLA);
        }
        if (quickAckSupported) {
            hashSet.add(TCP_QUICKACK);
        }
        if (keepAliveOptSupported) {
            hashSet.addAll(Set.m1759of(TCP_KEEPCOUNT, TCP_KEEPIDLE, TCP_KEEPINTERVAL));
        }
        return Collections.unmodifiableSet(hashSet);
    }

    /* access modifiers changed from: private */
    public static <T> T checkValueType(Object obj, Class<?> cls) {
        if (cls.isAssignableFrom(obj.getClass())) {
            return obj;
        }
        throw new IllegalArgumentException("Found: " + obj.getClass() + ", Expected: " + cls);
    }

    /* access modifiers changed from: private */
    public static void setFlowOption(FileDescriptor fileDescriptor, SocketFlow socketFlow) throws SocketException {
        socketFlow.status(platformSocketOptions.setFlowOption(fdAccess.get(fileDescriptor), socketFlow.priority(), socketFlow.bandwidth()));
    }

    /* access modifiers changed from: private */
    public static void getFlowOption(FileDescriptor fileDescriptor, SocketFlow socketFlow) throws SocketException {
        socketFlow.status(platformSocketOptions.getFlowOption(fdAccess.get(fileDescriptor), socketFlow));
    }

    /* access modifiers changed from: private */
    public static void setQuickAckOption(FileDescriptor fileDescriptor, boolean z) throws SocketException {
        platformSocketOptions.setQuickAck(fdAccess.get(fileDescriptor), z);
    }

    /* access modifiers changed from: private */
    public static Object getQuickAckOption(FileDescriptor fileDescriptor) throws SocketException {
        return Boolean.valueOf(platformSocketOptions.getQuickAck(fdAccess.get(fileDescriptor)));
    }

    /* access modifiers changed from: private */
    public static void setTcpkeepAliveProbes(FileDescriptor fileDescriptor, int i) throws SocketException {
        platformSocketOptions.setTcpkeepAliveProbes(fdAccess.get(fileDescriptor), i);
    }

    /* access modifiers changed from: private */
    public static void setTcpKeepAliveTime(FileDescriptor fileDescriptor, int i) throws SocketException {
        platformSocketOptions.setTcpKeepAliveTime(fdAccess.get(fileDescriptor), i);
    }

    /* access modifiers changed from: private */
    public static void setTcpKeepAliveIntvl(FileDescriptor fileDescriptor, int i) throws SocketException {
        platformSocketOptions.setTcpKeepAliveIntvl(fdAccess.get(fileDescriptor), i);
    }

    /* access modifiers changed from: private */
    public static int getTcpkeepAliveProbes(FileDescriptor fileDescriptor) throws SocketException {
        return platformSocketOptions.getTcpkeepAliveProbes(fdAccess.get(fileDescriptor));
    }

    /* access modifiers changed from: private */
    public static int getTcpKeepAliveTime(FileDescriptor fileDescriptor) throws SocketException {
        return platformSocketOptions.getTcpKeepAliveTime(fdAccess.get(fileDescriptor));
    }

    /* access modifiers changed from: private */
    public static int getTcpKeepAliveIntvl(FileDescriptor fileDescriptor) throws SocketException {
        return platformSocketOptions.getTcpKeepAliveIntvl(fdAccess.get(fileDescriptor));
    }

    static class PlatformSocketOptions {
        private static final PlatformSocketOptions instance = create();

        /* access modifiers changed from: package-private */
        public boolean flowSupported() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean keepAliveOptionsSupported() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean quickAckSupported() {
            return false;
        }

        protected PlatformSocketOptions() {
        }

        private static PlatformSocketOptions newInstance(String str) {
            try {
                return (PlatformSocketOptions) Class.forName(str).getConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (ReflectiveOperationException e) {
                throw new AssertionError((Object) e);
            }
        }

        private static PlatformSocketOptions create() {
            return new PlatformSocketOptions();
        }

        static PlatformSocketOptions get() {
            return instance;
        }

        /* access modifiers changed from: package-private */
        public int setFlowOption(int i, int i2, long j) throws SocketException {
            throw new UnsupportedOperationException("unsupported socket option");
        }

        /* access modifiers changed from: package-private */
        public int getFlowOption(int i, SocketFlow socketFlow) throws SocketException {
            throw new UnsupportedOperationException("unsupported socket option");
        }

        /* access modifiers changed from: package-private */
        public void setQuickAck(int i, boolean z) throws SocketException {
            throw new UnsupportedOperationException("unsupported TCP_QUICKACK option");
        }

        /* access modifiers changed from: package-private */
        public boolean getQuickAck(int i) throws SocketException {
            throw new UnsupportedOperationException("unsupported TCP_QUICKACK option");
        }

        /* access modifiers changed from: package-private */
        public void setTcpkeepAliveProbes(int i, int i2) throws SocketException {
            throw new UnsupportedOperationException("unsupported TCP_KEEPCNT option");
        }

        /* access modifiers changed from: package-private */
        public void setTcpKeepAliveTime(int i, int i2) throws SocketException {
            throw new UnsupportedOperationException("unsupported TCP_KEEPIDLE option");
        }

        /* access modifiers changed from: package-private */
        public void setTcpKeepAliveIntvl(int i, int i2) throws SocketException {
            throw new UnsupportedOperationException("unsupported TCP_KEEPINTVL option");
        }

        /* access modifiers changed from: package-private */
        public int getTcpkeepAliveProbes(int i) throws SocketException {
            throw new UnsupportedOperationException("unsupported TCP_KEEPCNT option");
        }

        /* access modifiers changed from: package-private */
        public int getTcpKeepAliveTime(int i) throws SocketException {
            throw new UnsupportedOperationException("unsupported TCP_KEEPIDLE option");
        }

        /* access modifiers changed from: package-private */
        public int getTcpKeepAliveIntvl(int i) throws SocketException {
            throw new UnsupportedOperationException("unsupported TCP_KEEPINTVL option");
        }
    }
}
