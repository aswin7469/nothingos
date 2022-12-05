package com.android.systemui.privacy.television;

import com.android.systemui.privacy.PrivacyItem;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class TvOngoingPrivacyChip$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ TvOngoingPrivacyChip$$ExternalSyntheticLambda2 INSTANCE = new TvOngoingPrivacyChip$$ExternalSyntheticLambda2();

    private /* synthetic */ TvOngoingPrivacyChip$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean lambda$onPrivacyItemsChanged$0;
        lambda$onPrivacyItemsChanged$0 = TvOngoingPrivacyChip.lambda$onPrivacyItemsChanged$0((PrivacyItem) obj);
        return lambda$onPrivacyItemsChanged$0;
    }
}
