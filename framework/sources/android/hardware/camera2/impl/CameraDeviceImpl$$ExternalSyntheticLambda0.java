package android.hardware.camera2.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public final /* synthetic */ class CameraDeviceImpl$$ExternalSyntheticLambda0 implements FileFilter {
    public static final /* synthetic */ CameraDeviceImpl$$ExternalSyntheticLambda0 INSTANCE = new CameraDeviceImpl$$ExternalSyntheticLambda0();

    private /* synthetic */ CameraDeviceImpl$$ExternalSyntheticLambda0() {
    }

    @Override // java.io.FileFilter
    public final boolean accept(File file) {
        boolean matches;
        matches = Pattern.matches("thermal_zone[0-9]+", file.getName());
        return matches;
    }
}
