package com.android.systemui.biometrics;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B-\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fR\u0014\u0010\r\u001a\u00020\u000eXD¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/biometrics/UdfpsFpmOtherViewController;", "Lcom/android/systemui/biometrics/UdfpsAnimationViewController;", "Lcom/android/systemui/biometrics/UdfpsFpmOtherView;", "view", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "panelExpansionStateManager", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;", "systemUIDialogManager", "Lcom/android/systemui/statusbar/phone/SystemUIDialogManager;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/biometrics/UdfpsFpmOtherView;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;Lcom/android/systemui/statusbar/phone/SystemUIDialogManager;Lcom/android/systemui/dump/DumpManager;)V", "tag", "", "getTag", "()Ljava/lang/String;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UdfpsFpmOtherViewController.kt */
public final class UdfpsFpmOtherViewController extends UdfpsAnimationViewController<UdfpsFpmOtherView> {
    private final String tag = "UdfpsFpmOtherViewController";

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UdfpsFpmOtherViewController(UdfpsFpmOtherView udfpsFpmOtherView, StatusBarStateController statusBarStateController, PanelExpansionStateManager panelExpansionStateManager, SystemUIDialogManager systemUIDialogManager, DumpManager dumpManager) {
        super(udfpsFpmOtherView, statusBarStateController, panelExpansionStateManager, systemUIDialogManager, dumpManager);
        Intrinsics.checkNotNullParameter(udfpsFpmOtherView, "view");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(panelExpansionStateManager, "panelExpansionStateManager");
        Intrinsics.checkNotNullParameter(systemUIDialogManager, "systemUIDialogManager");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return this.tag;
    }
}
