package com.android.systemui.wallet.controller;

import android.service.quickaccesswallet.QuickAccessWalletClient;
import com.android.systemui.wallet.controller.QuickAccessWalletController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QuickAccessWalletController$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ QuickAccessWalletController.C33021 f$0;
    public final /* synthetic */ QuickAccessWalletClient.OnWalletCardsRetrievedCallback f$1;

    public /* synthetic */ QuickAccessWalletController$1$$ExternalSyntheticLambda0(QuickAccessWalletController.C33021 r1, QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback) {
        this.f$0 = r1;
        this.f$1 = onWalletCardsRetrievedCallback;
    }

    public final void run() {
        this.f$0.mo47421x63cc18a2(this.f$1);
    }
}
