package android.net;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$4$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int f$0;

    public /* synthetic */ TetheringManager$4$$ExternalSyntheticLambda2(int i) {
        this.f$0 = i;
    }

    public final void run() {
        TetheringManager.throwIfPermissionFailure(this.f$0);
    }
}
