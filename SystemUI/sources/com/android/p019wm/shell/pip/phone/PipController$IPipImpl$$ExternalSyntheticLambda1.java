package com.android.p019wm.shell.pip.phone;

import android.app.PictureInPictureParams;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import com.android.p019wm.shell.pip.phone.PipController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.pip.phone.PipController$IPipImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipController$IPipImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ Rect[] f$0;
    public final /* synthetic */ ComponentName f$1;
    public final /* synthetic */ ActivityInfo f$2;
    public final /* synthetic */ PictureInPictureParams f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ int f$5;

    public /* synthetic */ PipController$IPipImpl$$ExternalSyntheticLambda1(Rect[] rectArr, ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) {
        this.f$0 = rectArr;
        this.f$1 = componentName;
        this.f$2 = activityInfo;
        this.f$3 = pictureInPictureParams;
        this.f$4 = i;
        this.f$5 = i2;
    }

    public final void accept(Object obj) {
        PipController.IPipImpl.lambda$startSwipePipToHome$2(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, (PipController) obj);
    }
}
