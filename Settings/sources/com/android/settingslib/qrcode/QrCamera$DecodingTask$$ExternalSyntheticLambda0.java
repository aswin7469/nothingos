package com.android.settingslib.qrcode;

import android.hardware.Camera;
import com.android.settingslib.qrcode.QrCamera;
import java.util.concurrent.Semaphore;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QrCamera$DecodingTask$$ExternalSyntheticLambda0 implements Camera.PreviewCallback {
    public final /* synthetic */ QrCamera.DecodingTask f$0;
    public final /* synthetic */ Semaphore f$1;

    public /* synthetic */ QrCamera$DecodingTask$$ExternalSyntheticLambda0(QrCamera.DecodingTask decodingTask, Semaphore semaphore) {
        this.f$0 = decodingTask;
        this.f$1 = semaphore;
    }

    public final void onPreviewFrame(byte[] bArr, Camera camera) {
        this.f$0.lambda$doInBackground$0(this.f$1, bArr, camera);
    }
}