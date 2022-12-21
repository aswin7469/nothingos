package com.android.systemui.p012qs.tiles.dialog;

import android.content.Context;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogController;
import java.util.Set;
import java.util.function.Function;

/* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class InternetDialogController$$ExternalSyntheticLambda5 implements Function {
    public final /* synthetic */ Set f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ InternetDialogController$$ExternalSyntheticLambda5(Set set, Context context) {
        this.f$0 = set;
        this.f$1 = context;
    }

    public final Object apply(Object obj) {
        return InternetDialogController.lambda$getUniqueSubscriptionDisplayNames$5(this.f$0, this.f$1, (InternetDialogController.AnonymousClass1DisplayInfo) obj);
    }
}
