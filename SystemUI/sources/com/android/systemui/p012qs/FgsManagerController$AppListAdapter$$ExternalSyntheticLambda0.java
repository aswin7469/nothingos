package com.android.systemui.p012qs;

import android.view.View;
import com.android.systemui.p012qs.FgsManagerController;
import kotlin.jvm.internal.Ref;

/* renamed from: com.android.systemui.qs.FgsManagerController$AppListAdapter$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FgsManagerController$AppListAdapter$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ FgsManagerController.AppItemViewHolder f$0;
    public final /* synthetic */ FgsManagerController f$1;
    public final /* synthetic */ Ref.ObjectRef f$2;

    public /* synthetic */ FgsManagerController$AppListAdapter$$ExternalSyntheticLambda0(FgsManagerController.AppItemViewHolder appItemViewHolder, FgsManagerController fgsManagerController, Ref.ObjectRef objectRef) {
        this.f$0 = appItemViewHolder;
        this.f$1 = fgsManagerController;
        this.f$2 = objectRef;
    }

    public final void onClick(View view) {
        FgsManagerController.AppListAdapter.m2893onBindViewHolder$lambda2$lambda1(this.f$0, this.f$1, this.f$2, view);
    }
}
