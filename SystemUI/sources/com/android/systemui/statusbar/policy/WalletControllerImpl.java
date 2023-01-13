package com.android.systemui.statusbar.policy;

import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0007\u0018\u0000 \b2\u00020\u0001:\u0001\bB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000f\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016¢\u0006\u0002\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/WalletControllerImpl;", "Lcom/android/systemui/statusbar/policy/WalletController;", "quickAccessWalletClient", "Landroid/service/quickaccesswallet/QuickAccessWalletClient;", "(Landroid/service/quickaccesswallet/QuickAccessWalletClient;)V", "getWalletPosition", "", "()Ljava/lang/Integer;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: WalletControllerImpl.kt */
public final class WalletControllerImpl implements WalletController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int QS_PRIORITY_POSITION = 3;
    private static final String TAG = "WalletControllerImpl";
    private final QuickAccessWalletClient quickAccessWalletClient;

    @Inject
    public WalletControllerImpl(QuickAccessWalletClient quickAccessWalletClient2) {
        Intrinsics.checkNotNullParameter(quickAccessWalletClient2, "quickAccessWalletClient");
        this.quickAccessWalletClient = quickAccessWalletClient2;
    }

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/WalletControllerImpl$Companion;", "", "()V", "QS_PRIORITY_POSITION", "", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: WalletControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public Integer getWalletPosition() {
        if (this.quickAccessWalletClient.isWalletServiceAvailable()) {
            Log.i(TAG, "Setting WalletTile position: 3");
            return 3;
        }
        Log.i(TAG, "Setting WalletTile position: null");
        Integer num = null;
        return null;
    }
}
