package com.android.systemui.settings;

import com.android.systemui.settings.UserTracker;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UserTrackerImpl$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ UserTracker.Callback f$0;

    public /* synthetic */ UserTrackerImpl$$ExternalSyntheticLambda0(UserTracker.Callback callback) {
        this.f$0 = callback;
    }

    public final boolean test(Object obj) {
        return UserTrackerImpl.m3025removeCallback$lambda11$lambda10(this.f$0, (DataItem) obj);
    }
}
