package com.android.systemui.media.taptotransfer;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.flags.BooleanFlag;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/MediaTttFlags;", "", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "(Lcom/android/systemui/flags/FeatureFlags;)V", "isMediaTttEnabled", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaTttFlags.kt */
public final class MediaTttFlags {
    private final FeatureFlags featureFlags;

    @Inject
    public MediaTttFlags(FeatureFlags featureFlags2) {
        Intrinsics.checkNotNullParameter(featureFlags2, "featureFlags");
        this.featureFlags = featureFlags2;
    }

    public final boolean isMediaTttEnabled() {
        FeatureFlags featureFlags2 = this.featureFlags;
        BooleanFlag booleanFlag = Flags.MEDIA_TAP_TO_TRANSFER;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "MEDIA_TAP_TO_TRANSFER");
        return featureFlags2.isEnabled(booleanFlag);
    }
}
