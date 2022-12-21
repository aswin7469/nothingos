package com.android.systemui.p012qs;

import android.view.View;
import com.android.systemui.p012qs.VisibilityChangedDispatcher;

/* renamed from: com.android.systemui.qs.FooterActionsController$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FooterActionsController$$ExternalSyntheticLambda3 implements VisibilityChangedDispatcher.OnVisibilityChangedListener {
    public final /* synthetic */ View f$0;
    public final /* synthetic */ View f$1;
    public final /* synthetic */ FooterActionsController f$2;

    public /* synthetic */ FooterActionsController$$ExternalSyntheticLambda3(View view, View view2, FooterActionsController footerActionsController) {
        this.f$0 = view;
        this.f$1 = view2;
        this.f$2 = footerActionsController;
    }

    public final void onVisibilityChanged(int i) {
        FooterActionsController.m2898onViewAttached$lambda5(this.f$0, this.f$1, this.f$2, i);
    }
}
