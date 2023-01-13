package com.android.systemui.unfold.util;

import android.database.ContentObserver;
import android.os.Handler;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/unfold/util/ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1 */
/* compiled from: ScaleAwareTransitionProgressProvider.kt */
public final class C3252x746b6035 extends ContentObserver {
    final /* synthetic */ ScaleAwareTransitionProgressProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C3252x746b6035(ScaleAwareTransitionProgressProvider scaleAwareTransitionProgressProvider) {
        super((Handler) null);
        this.this$0 = scaleAwareTransitionProgressProvider;
    }

    public void onChange(boolean z) {
        this.this$0.onAnimatorScaleChanged();
    }
}
