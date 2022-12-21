package com.android.keyguard;

import android.telephony.PinResult;
import com.android.keyguard.KeyguardSimPukViewController;

/* renamed from: com.android.keyguard.KeyguardSimPukViewController$CheckSimPuk$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C1640x2e140b38 implements Runnable {
    public final /* synthetic */ KeyguardSimPukViewController.CheckSimPuk f$0;
    public final /* synthetic */ PinResult f$1;

    public /* synthetic */ C1640x2e140b38(KeyguardSimPukViewController.CheckSimPuk checkSimPuk, PinResult pinResult) {
        this.f$0 = checkSimPuk;
        this.f$1 = pinResult;
    }

    public final void run() {
        this.f$0.mo26101x9d932adc(this.f$1);
    }
}
