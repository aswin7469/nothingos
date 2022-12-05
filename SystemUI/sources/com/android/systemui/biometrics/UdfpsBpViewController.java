package com.android.systemui.biometrics;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBar;
/* loaded from: classes.dex */
class UdfpsBpViewController extends UdfpsAnimationViewController<UdfpsBpView> {
    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    String getTag() {
        return "UdfpsBpViewController";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UdfpsBpViewController(UdfpsBpView udfpsBpView, StatusBarStateController statusBarStateController, StatusBar statusBar, DumpManager dumpManager) {
        super(udfpsBpView, statusBarStateController, statusBar, dumpManager);
    }
}
