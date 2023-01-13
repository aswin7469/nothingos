package com.android.systemui.flags;

import com.android.systemui.flags.FlagListenable;
import com.android.systemui.flags.FlagManager;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FlagManager$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ FlagListenable.Listener f$0;

    public /* synthetic */ FlagManager$$ExternalSyntheticLambda1(FlagListenable.Listener listener) {
        this.f$0 = listener;
    }

    public final boolean test(Object obj) {
        return FlagManager.m2758removeListener$lambda3$lambda2(this.f$0, (FlagManager.PerFlagListener) obj);
    }
}
