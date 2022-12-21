package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.flags.BooleanFlag;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\u0006J\u0006\u0010\b\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallFlags;", "", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "(Lcom/android/systemui/flags/FeatureFlags;)V", "isInImmersiveChipTapEnabled", "", "isInImmersiveEnabled", "isStatusBarChipEnabled", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: OngoingCallFlags.kt */
public final class OngoingCallFlags {
    private final FeatureFlags featureFlags;

    @Inject
    public OngoingCallFlags(FeatureFlags featureFlags2) {
        Intrinsics.checkNotNullParameter(featureFlags2, "featureFlags");
        this.featureFlags = featureFlags2;
    }

    public final boolean isStatusBarChipEnabled() {
        FeatureFlags featureFlags2 = this.featureFlags;
        BooleanFlag booleanFlag = Flags.ONGOING_CALL_STATUS_BAR_CHIP;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "ONGOING_CALL_STATUS_BAR_CHIP");
        return featureFlags2.isEnabled(booleanFlag);
    }

    public final boolean isInImmersiveEnabled() {
        if (isStatusBarChipEnabled()) {
            FeatureFlags featureFlags2 = this.featureFlags;
            BooleanFlag booleanFlag = Flags.ONGOING_CALL_IN_IMMERSIVE;
            Intrinsics.checkNotNullExpressionValue(booleanFlag, "ONGOING_CALL_IN_IMMERSIVE");
            if (featureFlags2.isEnabled(booleanFlag)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isInImmersiveChipTapEnabled() {
        if (isInImmersiveEnabled()) {
            FeatureFlags featureFlags2 = this.featureFlags;
            BooleanFlag booleanFlag = Flags.ONGOING_CALL_IN_IMMERSIVE_CHIP_TAP;
            Intrinsics.checkNotNullExpressionValue(booleanFlag, "ONGOING_CALL_IN_IMMERSIVE_CHIP_TAP");
            if (featureFlags2.isEnabled(booleanFlag)) {
                return true;
            }
        }
        return false;
    }
}
