package sun.net.ext;

import java.net.SocketException;
import java.net.SocketOption;
import java.p026io.FileDescriptor;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ExtendedSocketOptions {
    public static final short SOCK_DGRAM = 2;
    public static final short SOCK_STREAM = 1;
    private static volatile ExtendedSocketOptions instance = new NoExtendedSocketOptions();
    private final Set<SocketOption<?>> options;

    public abstract Object getOption(FileDescriptor fileDescriptor, SocketOption<?> socketOption) throws SocketException;

    public abstract void setOption(FileDescriptor fileDescriptor, SocketOption<?> socketOption, Object obj) throws SocketException;

    public final boolean isOptionSupported(SocketOption<?> socketOption) {
        return options().contains(socketOption);
    }

    public final Set<SocketOption<?>> options() {
        return this.options;
    }

    public static final Set<SocketOption<?>> options(short s) {
        return getInstance().options0(s);
    }

    private Set<SocketOption<?>> options0(short s) {
        if (s == 1) {
            return (Set) this.options.stream().filter(new ExtendedSocketOptions$$ExternalSyntheticLambda1()).collect(Collectors.toUnmodifiableSet());
        }
        if (s == 2) {
            return (Set) this.options.stream().filter(new ExtendedSocketOptions$$ExternalSyntheticLambda0()).collect(Collectors.toUnmodifiableSet());
        }
        throw new IllegalArgumentException("Invalid socket option type");
    }

    static /* synthetic */ boolean lambda$options0$0(SocketOption socketOption) {
        return !socketOption.name().startsWith("TCP_");
    }

    static /* synthetic */ boolean lambda$options0$1(SocketOption socketOption) {
        return !socketOption.name().startsWith("UDP_");
    }

    protected ExtendedSocketOptions(Set<SocketOption<?>> set) {
        this.options = set;
    }

    public static final ExtendedSocketOptions getInstance() {
        return instance;
    }

    public static final void register(ExtendedSocketOptions extendedSocketOptions) {
        if (instance == null) {
            instance = extendedSocketOptions;
            return;
        }
        throw new InternalError("Attempting to reregister extended options");
    }

    static {
        try {
            Class.forName("jdk.net.ExtendedSocketOptions");
        } catch (ClassNotFoundException unused) {
        }
    }

    static final class NoExtendedSocketOptions extends ExtendedSocketOptions {
        NoExtendedSocketOptions() {
            super(Collections.emptySet());
        }

        public void setOption(FileDescriptor fileDescriptor, SocketOption<?> socketOption, Object obj) throws SocketException {
            throw new UnsupportedOperationException("no extended options: " + socketOption.name());
        }

        public Object getOption(FileDescriptor fileDescriptor, SocketOption<?> socketOption) throws SocketException {
            throw new UnsupportedOperationException("no extended options: " + socketOption.name());
        }
    }
}
