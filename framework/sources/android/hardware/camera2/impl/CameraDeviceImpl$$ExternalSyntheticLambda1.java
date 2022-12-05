package android.hardware.camera2.impl;

import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public final /* synthetic */ class CameraDeviceImpl$$ExternalSyntheticLambda1 implements BiConsumer {
    public static final /* synthetic */ CameraDeviceImpl$$ExternalSyntheticLambda1 INSTANCE = new CameraDeviceImpl$$ExternalSyntheticLambda1();

    private /* synthetic */ CameraDeviceImpl$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((CameraDeviceImpl) obj).notifyError(((Integer) obj2).intValue());
    }
}
