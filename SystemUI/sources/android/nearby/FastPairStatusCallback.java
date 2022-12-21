package android.nearby;

public interface FastPairStatusCallback {
    void onPairUpdate(FastPairDevice fastPairDevice, PairStatusMetadata pairStatusMetadata);
}
