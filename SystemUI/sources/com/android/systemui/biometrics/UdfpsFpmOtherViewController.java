package com.android.systemui.biometrics;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBar;
/* loaded from: classes.dex */
class UdfpsFpmOtherViewController extends UdfpsAnimationViewController<UdfpsFpmOtherView> {
    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    String getTag() {
        return "UdfpsFpmOtherViewController";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UdfpsFpmOtherViewController(UdfpsFpmOtherView udfpsFpmOtherView, StatusBarStateController statusBarStateController, StatusBar statusBar, DumpManager dumpManager) {
        super(udfpsFpmOtherView, statusBarStateController, statusBar, dumpManager);
    }
}
