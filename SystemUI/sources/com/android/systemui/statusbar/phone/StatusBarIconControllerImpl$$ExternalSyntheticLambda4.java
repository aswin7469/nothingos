package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.StatusBarIconController;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatusBarIconControllerImpl$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ StatusBarIconControllerImpl$$ExternalSyntheticLambda4(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void accept(Object obj) {
        ((StatusBarIconController.IconManager) obj).mGroup.getChildAt(this.f$0).setAccessibilityLiveRegion(this.f$1);
    }
}
