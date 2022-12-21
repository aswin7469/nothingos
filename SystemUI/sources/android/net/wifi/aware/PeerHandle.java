package android.net.wifi.aware;

public class PeerHandle {
    public int peerId;

    public PeerHandle(int i) {
        this.peerId = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PeerHandle)) {
            return false;
        }
        if (this.peerId == ((PeerHandle) obj).peerId) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.peerId;
    }
}
