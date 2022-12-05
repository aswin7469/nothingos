package com.android.systemui.shared.recents.utilities;

import android.graphics.Bitmap;
import android.graphics.ParcelableColorSpace;
import android.hardware.HardwareBuffer;
import android.os.Bundle;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BitmapUtil {
    public static Bitmap bundleToHardwareBitmap(Bundle bundle) {
        if (!bundle.containsKey("bitmap_util_buffer") || !bundle.containsKey("bitmap_util_color_space")) {
            throw new IllegalArgumentException("Bundle does not contain a hardware bitmap");
        }
        HardwareBuffer hardwareBuffer = (HardwareBuffer) bundle.getParcelable("bitmap_util_buffer");
        Objects.requireNonNull(hardwareBuffer);
        return Bitmap.wrapHardwareBuffer(hardwareBuffer, ((ParcelableColorSpace) bundle.getParcelable("bitmap_util_color_space")).getColorSpace());
    }
}
