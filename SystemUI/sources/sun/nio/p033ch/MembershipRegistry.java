package sun.nio.p033ch;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.channels.MembershipKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* renamed from: sun.nio.ch.MembershipRegistry */
class MembershipRegistry {
    private Map<InetAddress, List<MembershipKeyImpl>> groups = null;

    MembershipRegistry() {
    }

    /* access modifiers changed from: package-private */
    public MembershipKey checkMembership(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        List<MembershipKeyImpl> list;
        Map<InetAddress, List<MembershipKeyImpl>> map = this.groups;
        if (map == null || (list = map.get(inetAddress)) == null) {
            return null;
        }
        for (MembershipKeyImpl membershipKeyImpl : list) {
            if (membershipKeyImpl.networkInterface().equals(networkInterface)) {
                if (inetAddress2 == null) {
                    if (membershipKeyImpl.sourceAddress() == null) {
                        return membershipKeyImpl;
                    }
                    throw new IllegalStateException("Already a member to receive all packets");
                } else if (membershipKeyImpl.sourceAddress() == null) {
                    throw new IllegalStateException("Already have source-specific membership");
                } else if (inetAddress2.equals(membershipKeyImpl.sourceAddress())) {
                    return membershipKeyImpl;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void add(MembershipKeyImpl membershipKeyImpl) {
        List list;
        InetAddress group = membershipKeyImpl.group();
        Map<InetAddress, List<MembershipKeyImpl>> map = this.groups;
        if (map == null) {
            this.groups = new HashMap();
            list = null;
        } else {
            list = map.get(group);
        }
        if (list == null) {
            list = new LinkedList();
            this.groups.put(group, list);
        }
        list.add(membershipKeyImpl);
    }

    /* access modifiers changed from: package-private */
    public void remove(MembershipKeyImpl membershipKeyImpl) {
        InetAddress group = membershipKeyImpl.group();
        List list = this.groups.get(group);
        if (list != null) {
            Iterator it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next() == membershipKeyImpl) {
                        it.remove();
                        break;
                    }
                } else {
                    break;
                }
            }
            if (list.isEmpty()) {
                this.groups.remove(group);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void invalidateAll() {
        Map<InetAddress, List<MembershipKeyImpl>> map = this.groups;
        if (map != null) {
            for (InetAddress inetAddress : map.keySet()) {
                for (MembershipKeyImpl invalidate : this.groups.get(inetAddress)) {
                    invalidate.invalidate();
                }
            }
        }
    }
}
