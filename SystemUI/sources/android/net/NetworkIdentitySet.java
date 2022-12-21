package android.net;

import android.util.proto.ProtoOutputStream;
import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class NetworkIdentitySet extends HashSet<NetworkIdentity> {
    private static final int VERSION_ADD_DEFAULT_NETWORK = 5;
    private static final int VERSION_ADD_METERED = 4;
    private static final int VERSION_ADD_NETWORK_ID = 3;
    private static final int VERSION_ADD_OEM_MANAGED_NETWORK = 6;
    private static final int VERSION_ADD_ROAMING = 2;
    private static final int VERSION_ADD_SUB_ID = 7;
    private static final int VERSION_INIT = 1;

    public NetworkIdentitySet() {
    }

    public NetworkIdentitySet(Set<NetworkIdentity> set) {
        super(set);
    }

    public NetworkIdentitySet(DataInput dataInput) throws IOException {
        int readInt = dataInput.readInt();
        int readInt2 = dataInput.readInt();
        for (int i = 0; i < readInt2; i++) {
            boolean z = true;
            if (readInt <= 1) {
                dataInput.readInt();
            }
            int readInt3 = dataInput.readInt();
            add(new NetworkIdentity(readInt3, dataInput.readInt(), readOptionalString(dataInput), readInt >= 3 ? readOptionalString(dataInput) : null, readInt >= 2 ? dataInput.readBoolean() : false, readInt >= 4 ? dataInput.readBoolean() : readInt3 == 0, readInt >= 5 ? dataInput.readBoolean() : z, readInt >= 6 ? dataInput.readInt() : 0, readInt >= 7 ? dataInput.readInt() : -1));
        }
    }

    public void writeToStream(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(7);
        dataOutput.writeInt(size());
        Iterator it = iterator();
        while (it.hasNext()) {
            NetworkIdentity networkIdentity = (NetworkIdentity) it.next();
            dataOutput.writeInt(networkIdentity.getType());
            dataOutput.writeInt(networkIdentity.getRatType());
            writeOptionalString(dataOutput, networkIdentity.getSubscriberId());
            writeOptionalString(dataOutput, networkIdentity.getWifiNetworkKey());
            dataOutput.writeBoolean(networkIdentity.isRoaming());
            dataOutput.writeBoolean(networkIdentity.isMetered());
            dataOutput.writeBoolean(networkIdentity.isDefaultNetwork());
            dataOutput.writeInt(networkIdentity.getOemManaged());
            dataOutput.writeInt(networkIdentity.getSubId());
        }
    }

    public boolean isAnyMemberMetered() {
        if (isEmpty()) {
            return false;
        }
        Iterator it = iterator();
        while (it.hasNext()) {
            if (((NetworkIdentity) it.next()).isMetered()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnyMemberRoaming() {
        if (isEmpty()) {
            return false;
        }
        Iterator it = iterator();
        while (it.hasNext()) {
            if (((NetworkIdentity) it.next()).isRoaming()) {
                return true;
            }
        }
        return false;
    }

    public boolean areAllMembersOnDefaultNetwork() {
        if (isEmpty()) {
            return true;
        }
        Iterator it = iterator();
        while (it.hasNext()) {
            if (!((NetworkIdentity) it.next()).isDefaultNetwork()) {
                return false;
            }
        }
        return true;
    }

    private static void writeOptionalString(DataOutput dataOutput, String str) throws IOException {
        if (str != null) {
            dataOutput.writeByte(1);
            dataOutput.writeUTF(str);
            return;
        }
        dataOutput.writeByte(0);
    }

    private static String readOptionalString(DataInput dataInput) throws IOException {
        if (dataInput.readByte() != 0) {
            return dataInput.readUTF();
        }
        return null;
    }

    public static int compare(NetworkIdentitySet networkIdentitySet, NetworkIdentitySet networkIdentitySet2) {
        Objects.requireNonNull(networkIdentitySet);
        Objects.requireNonNull(networkIdentitySet2);
        if (networkIdentitySet.isEmpty() && networkIdentitySet2.isEmpty()) {
            return 0;
        }
        if (networkIdentitySet.isEmpty()) {
            return -1;
        }
        if (networkIdentitySet2.isEmpty()) {
            return 1;
        }
        return NetworkIdentity.compare((NetworkIdentity) networkIdentitySet.iterator().next(), (NetworkIdentity) networkIdentitySet2.iterator().next());
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        Iterator it = iterator();
        while (it.hasNext()) {
            ((NetworkIdentity) it.next()).dumpDebug(protoOutputStream, 2246267895809L);
        }
        protoOutputStream.end(start);
    }
}
