package com.android.systemui.statusbar;

import android.os.Vibrator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class VibratorHelper$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ Vibrator f$0;

    public /* synthetic */ VibratorHelper$$ExternalSyntheticLambda2(Vibrator vibrator) {
        this.f$0 = vibrator;
    }

    public final void run() {
        this.f$0.cancel();
    }
}
