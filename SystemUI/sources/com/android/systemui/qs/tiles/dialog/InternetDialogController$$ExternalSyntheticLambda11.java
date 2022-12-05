package com.android.systemui.qs.tiles.dialog;

import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class InternetDialogController$$ExternalSyntheticLambda11 implements Predicate {
    public static final /* synthetic */ InternetDialogController$$ExternalSyntheticLambda11 INSTANCE = new InternetDialogController$$ExternalSyntheticLambda11();

    private /* synthetic */ InternetDialogController$$ExternalSyntheticLambda11() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean lambda$onAccessPointsChanged$13;
        lambda$onAccessPointsChanged$13 = InternetDialogController.lambda$onAccessPointsChanged$13((WifiEntry) obj);
        return lambda$onAccessPointsChanged$13;
    }
}
