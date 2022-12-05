package com.android.systemui.statusbar.policy;

import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.util.Log;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: WalletControllerImpl.kt */
/* loaded from: classes2.dex */
public final class WalletControllerImpl implements WalletController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final QuickAccessWalletClient quickAccessWalletClient;

    public WalletControllerImpl(@NotNull QuickAccessWalletClient quickAccessWalletClient) {
        Intrinsics.checkNotNullParameter(quickAccessWalletClient, "quickAccessWalletClient");
        this.quickAccessWalletClient = quickAccessWalletClient;
    }

    /* compiled from: WalletControllerImpl.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.statusbar.policy.WalletController
    @Nullable
    public Integer getWalletPosition() {
        if (this.quickAccessWalletClient.isWalletServiceAvailable()) {
            Log.i("WalletControllerImpl", "Setting WalletTile position: 3");
            return 3;
        }
        Log.i("WalletControllerImpl", "Setting WalletTile position: null");
        return null;
    }
}
