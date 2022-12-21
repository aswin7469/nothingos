package com.android.systemui.media;

import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/media/MediaResumeListener$setManager$1", "Lcom/android/systemui/tuner/TunerService$Tunable;", "onTuningChanged", "", "key", "", "newValue", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaResumeListener.kt */
public final class MediaResumeListener$setManager$1 implements TunerService.Tunable {
    final /* synthetic */ MediaResumeListener this$0;

    MediaResumeListener$setManager$1(MediaResumeListener mediaResumeListener) {
        this.this$0 = mediaResumeListener;
    }

    public void onTuningChanged(String str, String str2) {
        MediaResumeListener mediaResumeListener = this.this$0;
        mediaResumeListener.useMediaResumption = Utils.useMediaResumption(mediaResumeListener.context);
        MediaDataManager access$getMediaDataManager$p = this.this$0.mediaDataManager;
        if (access$getMediaDataManager$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
            access$getMediaDataManager$p = null;
        }
        access$getMediaDataManager$p.setMediaResumptionEnabled(this.this$0.useMediaResumption);
    }
}
