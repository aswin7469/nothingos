package android.app;

import android.app.ActivityThread;
import android.os.ParcelFileDescriptor;
/* loaded from: classes.dex */
public final /* synthetic */ class ActivityThread$$ExternalSyntheticLambda2 implements ActivityThread.DumpHandler {
    public static final /* synthetic */ ActivityThread$$ExternalSyntheticLambda2 INSTANCE = new ActivityThread$$ExternalSyntheticLambda2();

    private /* synthetic */ ActivityThread$$ExternalSyntheticLambda2() {
    }

    @Override // android.app.ActivityThread.DumpHandler
    public final boolean handle(ParcelFileDescriptor parcelFileDescriptor, String[] strArr) {
        return ActivityThread.lambda$new$7(parcelFileDescriptor, strArr);
    }
}
