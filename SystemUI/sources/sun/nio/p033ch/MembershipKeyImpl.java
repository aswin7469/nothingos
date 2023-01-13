package sun.nio.p033ch;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.channels.MembershipKey;
import java.nio.channels.MulticastChannel;
import java.p026io.IOException;
import java.util.HashSet;
import kotlin.text.Typography;

/* renamed from: sun.nio.ch.MembershipKeyImpl */
class MembershipKeyImpl extends MembershipKey {
    private HashSet<InetAddress> blockedSet;

    /* renamed from: ch */
    private final MulticastChannel f885ch;
    private final InetAddress group;
    private final NetworkInterface interf;
    private volatile boolean invalid;
    private final InetAddress source;
    private final Object stateLock;

    private MembershipKeyImpl(MulticastChannel multicastChannel, InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        this.stateLock = new Object();
        this.f885ch = multicastChannel;
        this.group = inetAddress;
        this.interf = networkInterface;
        this.source = inetAddress2;
    }

    /* renamed from: sun.nio.ch.MembershipKeyImpl$Type4 */
    static class Type4 extends MembershipKeyImpl {
        private final int groupAddress;
        private final int interfAddress;
        private final int sourceAddress;

        Type4(MulticastChannel multicastChannel, InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, int i, int i2, int i3) {
            super(multicastChannel, inetAddress, networkInterface, inetAddress2);
            this.groupAddress = i;
            this.interfAddress = i2;
            this.sourceAddress = i3;
        }

        /* access modifiers changed from: package-private */
        public int groupAddress() {
            return this.groupAddress;
        }

        /* access modifiers changed from: package-private */
        public int interfaceAddress() {
            return this.interfAddress;
        }

        /* access modifiers changed from: package-private */
        public int source() {
            return this.sourceAddress;
        }
    }

    /* renamed from: sun.nio.ch.MembershipKeyImpl$Type6 */
    static class Type6 extends MembershipKeyImpl {
        private final byte[] groupAddress;
        private final int index;
        private final byte[] sourceAddress;

        Type6(MulticastChannel multicastChannel, InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, byte[] bArr, int i, byte[] bArr2) {
            super(multicastChannel, inetAddress, networkInterface, inetAddress2);
            this.groupAddress = bArr;
            this.index = i;
            this.sourceAddress = bArr2;
        }

        /* access modifiers changed from: package-private */
        public byte[] groupAddress() {
            return this.groupAddress;
        }

        /* access modifiers changed from: package-private */
        public int index() {
            return this.index;
        }

        /* access modifiers changed from: package-private */
        public byte[] source() {
            return this.sourceAddress;
        }
    }

    public boolean isValid() {
        return !this.invalid;
    }

    /* access modifiers changed from: package-private */
    public void invalidate() {
        this.invalid = true;
    }

    public void drop() {
        ((DatagramChannelImpl) this.f885ch).drop(this);
    }

    public MulticastChannel channel() {
        return this.f885ch;
    }

    public InetAddress group() {
        return this.group;
    }

    public NetworkInterface networkInterface() {
        return this.interf;
    }

    public InetAddress sourceAddress() {
        return this.source;
    }

    public MembershipKey block(InetAddress inetAddress) throws IOException {
        if (this.source == null) {
            synchronized (this.stateLock) {
                HashSet<InetAddress> hashSet = this.blockedSet;
                if (hashSet != null && hashSet.contains(inetAddress)) {
                    return this;
                }
                ((DatagramChannelImpl) this.f885ch).block(this, inetAddress);
                if (this.blockedSet == null) {
                    this.blockedSet = new HashSet<>();
                }
                this.blockedSet.add(inetAddress);
                return this;
            }
        }
        throw new IllegalStateException("key is source-specific");
    }

    public MembershipKey unblock(InetAddress inetAddress) {
        synchronized (this.stateLock) {
            HashSet<InetAddress> hashSet = this.blockedSet;
            if (hashSet == null || !hashSet.contains(inetAddress)) {
                throw new IllegalStateException("not blocked");
            }
            ((DatagramChannelImpl) this.f885ch).unblock(this, inetAddress);
            this.blockedSet.remove(inetAddress);
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append((char) Typography.less);
        sb.append(this.group.getHostAddress());
        sb.append(',');
        sb.append(this.interf.getName());
        if (this.source != null) {
            sb.append(',');
            sb.append(this.source.getHostAddress());
        }
        sb.append((char) Typography.greater);
        return sb.toString();
    }
}
