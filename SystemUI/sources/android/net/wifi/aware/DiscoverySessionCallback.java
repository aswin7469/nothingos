package android.net.wifi.aware;

import java.util.List;

public class DiscoverySessionCallback {
    public void onMessageReceived(PeerHandle peerHandle, byte[] bArr) {
    }

    public void onMessageSendFailed(int i) {
    }

    public void onMessageSendSucceeded(int i) {
    }

    public void onPublishStarted(PublishDiscoverySession publishDiscoverySession) {
    }

    public void onServiceDiscovered(PeerHandle peerHandle, byte[] bArr, List<byte[]> list) {
    }

    public void onServiceDiscovered(ServiceDiscoveryInfo serviceDiscoveryInfo) {
    }

    public void onServiceDiscoveredWithinRange(PeerHandle peerHandle, byte[] bArr, List<byte[]> list, int i) {
    }

    public void onServiceDiscoveredWithinRange(ServiceDiscoveryInfo serviceDiscoveryInfo, int i) {
    }

    public void onServiceLost(PeerHandle peerHandle, int i) {
    }

    public void onSessionConfigFailed() {
    }

    public void onSessionConfigUpdated() {
    }

    public void onSessionTerminated() {
    }

    public void onSubscribeStarted(SubscribeDiscoverySession subscribeDiscoverySession) {
    }
}
