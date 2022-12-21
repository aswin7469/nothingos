package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.util.Log;
import com.android.systemui.flags.BooleanFlag;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.flags.ResourceBooleanFlag;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\u000eJ\u0006\u0010\u0010\u001a\u00020\u000eJ\u0006\u0010\u0011\u001a\u00020\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0012"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;", "", "context", "Landroid/content/Context;", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "(Landroid/content/Context;Lcom/android/systemui/flags/FeatureFlags;)V", "getContext", "()Landroid/content/Context;", "getFeatureFlags", "()Lcom/android/systemui/flags/FeatureFlags;", "assertLegacyPipelineEnabled", "", "checkLegacyPipelineEnabled", "", "isDevLoggingEnabled", "isNewPipelineEnabled", "isSmartspaceDedupingEnabled", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifPipelineFlags.kt */
public final class NotifPipelineFlags {
    private final Context context;
    private final FeatureFlags featureFlags;

    @Inject
    public NotifPipelineFlags(Context context2, FeatureFlags featureFlags2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(featureFlags2, "featureFlags");
        this.context = context2;
        this.featureFlags = featureFlags2;
    }

    public final Context getContext() {
        return this.context;
    }

    public final FeatureFlags getFeatureFlags() {
        return this.featureFlags;
    }

    public final boolean checkLegacyPipelineEnabled() {
        if (!isNewPipelineEnabled()) {
            return true;
        }
        FeatureFlags featureFlags2 = this.featureFlags;
        BooleanFlag booleanFlag = Flags.NEW_PIPELINE_CRASH_ON_CALL_TO_OLD_PIPELINE;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "NEW_PIPELINE_CRASH_ON_CALL_TO_OLD_PIPELINE");
        if (!featureFlags2.isEnabled(booleanFlag)) {
            Log.d("NotifPipeline", "Old pipeline code running with new pipeline enabled", new Exception());
            return false;
        }
        throw new RuntimeException("Old pipeline code running with new pipeline enabled");
    }

    public final void assertLegacyPipelineEnabled() {
        if (!(!isNewPipelineEnabled())) {
            throw new IllegalStateException("Old pipeline code running w/ new pipeline enabled".toString());
        }
    }

    public final boolean isNewPipelineEnabled() {
        FeatureFlags featureFlags2 = this.featureFlags;
        BooleanFlag booleanFlag = Flags.NEW_NOTIFICATION_PIPELINE_RENDERING;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "NEW_NOTIFICATION_PIPELINE_RENDERING");
        return featureFlags2.isEnabled(booleanFlag);
    }

    public final boolean isDevLoggingEnabled() {
        FeatureFlags featureFlags2 = this.featureFlags;
        BooleanFlag booleanFlag = Flags.NOTIFICATION_PIPELINE_DEVELOPER_LOGGING;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "NOTIFICATION_PIPELINE_DEVELOPER_LOGGING");
        return featureFlags2.isEnabled(booleanFlag);
    }

    public final boolean isSmartspaceDedupingEnabled() {
        FeatureFlags featureFlags2 = this.featureFlags;
        ResourceBooleanFlag resourceBooleanFlag = Flags.SMARTSPACE;
        Intrinsics.checkNotNullExpressionValue(resourceBooleanFlag, "SMARTSPACE");
        if (featureFlags2.isEnabled(resourceBooleanFlag)) {
            FeatureFlags featureFlags3 = this.featureFlags;
            BooleanFlag booleanFlag = Flags.SMARTSPACE_DEDUPING;
            Intrinsics.checkNotNullExpressionValue(booleanFlag, "SMARTSPACE_DEDUPING");
            if (featureFlags3.isEnabled(booleanFlag)) {
                return true;
            }
        }
        return false;
    }
}
