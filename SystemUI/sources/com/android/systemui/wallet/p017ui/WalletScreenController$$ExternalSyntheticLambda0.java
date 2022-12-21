package com.android.systemui.wallet.p017ui;

import android.service.quickaccesswallet.GetWalletCardsResponse;
import java.util.List;

/* renamed from: com.android.systemui.wallet.ui.WalletScreenController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WalletScreenController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WalletScreenController f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ GetWalletCardsResponse f$2;

    public /* synthetic */ WalletScreenController$$ExternalSyntheticLambda0(WalletScreenController walletScreenController, List list, GetWalletCardsResponse getWalletCardsResponse) {
        this.f$0 = walletScreenController;
        this.f$1 = list;
        this.f$2 = getWalletCardsResponse;
    }

    public final void run() {
        this.f$0.mo47466xe1b09808(this.f$1, this.f$2);
    }
}
