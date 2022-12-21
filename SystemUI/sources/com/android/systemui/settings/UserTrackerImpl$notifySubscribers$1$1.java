package com.android.systemui.settings;

import com.android.systemui.settings.UserTracker;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "", "run"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UserTrackerImpl.kt */
public final class UserTrackerImpl$notifySubscribers$1$1 implements Runnable {
    final /* synthetic */ Function1<UserTracker.Callback, Unit> $action;
    final /* synthetic */ DataItem $it;

    public UserTrackerImpl$notifySubscribers$1$1(DataItem dataItem, Function1<? super UserTracker.Callback, Unit> function1) {
        this.$it = dataItem;
        this.$action = function1;
    }

    public final void run() {
        UserTracker.Callback callback = this.$it.getCallback().get();
        if (callback != null) {
            this.$action.invoke(callback);
        }
    }
}
