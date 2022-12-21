package com.android.keyguard;

import android.telephony.PinResult;
import com.android.keyguard.KeyguardSimPinViewController;

/* renamed from: com.android.keyguard.KeyguardSimPinViewController$CheckSimPin$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C1636xfea10a16 implements Runnable {
    public final /* synthetic */ KeyguardSimPinViewController.CheckSimPin f$0;
    public final /* synthetic */ PinResult f$1;

    public /* synthetic */ C1636xfea10a16(KeyguardSimPinViewController.CheckSimPin checkSimPin, PinResult pinResult) {
        this.f$0 = checkSimPin;
        this.f$1 = pinResult;
    }

    public final void run() {
        this.f$0.mo26095xffba033a(this.f$1);
    }
}
