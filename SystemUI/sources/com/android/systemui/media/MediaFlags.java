package com.android.systemui.media;

import android.app.StatusBarManager;
import android.os.UserHandle;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.flags.BooleanFlag;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\u0006J\u0006\u0010\f\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/media/MediaFlags;", "", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "(Lcom/android/systemui/flags/FeatureFlags;)V", "areMediaSessionActionsEnabled", "", "packageName", "", "user", "Landroid/os/UserHandle;", "areMuteAwaitConnectionsEnabled", "areNearbyMediaDevicesEnabled", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaFlags.kt */
public final class MediaFlags {
    private final FeatureFlags featureFlags;

    @Inject
    public MediaFlags(FeatureFlags featureFlags2) {
        Intrinsics.checkNotNullParameter(featureFlags2, "featureFlags");
        this.featureFlags = featureFlags2;
    }

    public final boolean areMediaSessionActionsEnabled(String str, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(userHandle, "user");
        if (!StatusBarManager.useMediaSessionActionsForApp(str, userHandle)) {
            FeatureFlags featureFlags2 = this.featureFlags;
            BooleanFlag booleanFlag = Flags.MEDIA_SESSION_ACTIONS;
            Intrinsics.checkNotNullExpressionValue(booleanFlag, "MEDIA_SESSION_ACTIONS");
            return featureFlags2.isEnabled(booleanFlag);
        }
    }

    public final boolean areMuteAwaitConnectionsEnabled() {
        FeatureFlags featureFlags2 = this.featureFlags;
        BooleanFlag booleanFlag = Flags.MEDIA_MUTE_AWAIT;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "MEDIA_MUTE_AWAIT");
        return featureFlags2.isEnabled(booleanFlag);
    }

    public final boolean areNearbyMediaDevicesEnabled() {
        FeatureFlags featureFlags2 = this.featureFlags;
        BooleanFlag booleanFlag = Flags.MEDIA_NEARBY_DEVICES;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "MEDIA_NEARBY_DEVICES");
        return featureFlags2.isEnabled(booleanFlag);
    }
}
