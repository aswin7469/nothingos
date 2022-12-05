package com.nt.settings.face;

import android.graphics.Rect;
import android.os.Handler;
import android.view.Surface;
import com.nt.facerecognition.manager.FaceMetadata;
import com.nt.facerecognition.manager.NtFaceRecognitionManager;
import java.util.List;
/* loaded from: classes2.dex */
public interface NtIFaceRecognition {
    boolean closeHardwareDevice();

    void deleteFaceMetadatas();

    void endEnroll();

    List<FaceMetadata> getEnrolledFaceMetadatas(int i);

    int getFaceMetadatasCount();

    int[] getIds();

    void removeFaceMetadata(FaceMetadata faceMetadata, NtFaceRecognitionManager.RemovalCallback removalCallback);

    void rename(int i, int i2, String str);

    void startEnroll(Handler handler, byte[] bArr, Rect rect);

    boolean warmUpHardwareDeviceForPreview(Surface surface, Handler handler, int i, int i2);
}
