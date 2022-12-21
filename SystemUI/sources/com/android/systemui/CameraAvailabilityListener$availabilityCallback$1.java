package com.android.systemui;

import android.hardware.camera2.CameraManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/CameraAvailabilityListener$availabilityCallback$1", "Landroid/hardware/camera2/CameraManager$AvailabilityCallback;", "onCameraClosed", "", "cameraId", "", "onCameraOpened", "packageId", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: CameraAvailabilityListener.kt */
public final class CameraAvailabilityListener$availabilityCallback$1 extends CameraManager.AvailabilityCallback {
    final /* synthetic */ CameraAvailabilityListener this$0;

    CameraAvailabilityListener$availabilityCallback$1(CameraAvailabilityListener cameraAvailabilityListener) {
        this.this$0 = cameraAvailabilityListener;
    }

    public void onCameraClosed(String str) {
        Intrinsics.checkNotNullParameter(str, "cameraId");
        if (Intrinsics.areEqual((Object) this.this$0.targetCameraId, (Object) str)) {
            this.this$0.notifyCameraInactive();
        }
    }

    public void onCameraOpened(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "cameraId");
        Intrinsics.checkNotNullParameter(str2, "packageId");
        if (Intrinsics.areEqual((Object) this.this$0.targetCameraId, (Object) str) && !this.this$0.isExcluded(str2)) {
            this.this$0.notifyCameraActive();
        }
    }
}
