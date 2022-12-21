package com.android.systemui.biometrics;

import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UdfpsAnimationViewController$$ExternalSyntheticLambda1 implements PanelExpansionListener {
    public final /* synthetic */ UdfpsAnimationViewController f$0;
    public final /* synthetic */ UdfpsAnimationView f$1;

    public /* synthetic */ UdfpsAnimationViewController$$ExternalSyntheticLambda1(UdfpsAnimationViewController udfpsAnimationViewController, UdfpsAnimationView udfpsAnimationView) {
        this.f$0 = udfpsAnimationViewController;
        this.f$1 = udfpsAnimationView;
    }

    public final void onPanelExpansionChanged(PanelExpansionChangeEvent panelExpansionChangeEvent) {
        UdfpsAnimationViewController.m2580panelExpansionListener$lambda1(this.f$0, this.f$1, panelExpansionChangeEvent);
    }
}
